package me.chimkenu.expunge.items;

import me.chimkenu.expunge.enums.ShotType;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.tasks.PlayerTask;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public record Gun(
        String id,
        String name,
        Material material,
        Tier tier,
        Slot slot,
        int swapCooldown,

        double damage,
        int pierceNumber,
        int range,

        ShotType shotType,
        int shotCooldown,
        double offset,
        int reloadTime,
        int clipSize,
        int maxAmmo,
        Particle particle,
        Sound sound,
        float pitch
) implements Weapon {
    public Gun {
        if (slot == null) slot = Slot.PRIMARY;
    }

    @Override
    public ItemStack toItem() {
        var gun = new ItemStack(material(), clipSize());
        var meta = gun.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatUtil.format(name()));
            List<String> lore = new ArrayList<>();
            lore.add(String.valueOf(maxAmmo()));
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(namespacedKey(), PersistentDataType.INTEGER, maxAmmo());
            gun.setItemMeta(meta);
        }
        return gun;
    }

    public int getAmmo(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return 0;
        Integer ammo = meta.getPersistentDataContainer().get(namespacedKey(), PersistentDataType.INTEGER);
        if (ammo == null)
            return 0;
        return ammo;
    }

    public void setAmmo(ItemStack item, int newAmmo) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;
        meta.getPersistentDataContainer().set(namespacedKey(), PersistentDataType.INTEGER, newAmmo);
        item.setItemMeta(meta);
    }

    public Set<Block> fire(GameManager manager, Player player, double extraOffset) {
        ItemStack item = player.getInventory().getItemInMainHand();
        var meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().getKeys().contains(namespacedKey())) {
            return null;
        }

        int currentAmmo = getAmmo(item);
        if (currentAmmo < 1) {
            ChatUtil.sendActionBar(player, "&a&lOut of Ammo!");
            return null;
        }
        if (player.getCooldown(material()) > 0) {
            return null;
        }
        if (((Damageable) item.getItemMeta()).hasDamage()) {
            return null;
        }

        currentAmmo--;
        setAmmo(item, currentAmmo);
        player.setLevel(currentAmmo);

        Set<Block> toBreak = new HashSet<>();
        switch (shotType()) {
            case SINGLE ->
                    toBreak.addAll(ShootParticle.shoot(
                            particle(), range(), damage(),
                            player, pierceNumber(), offset() + extraOffset, false
                    ));
            case SPREAD -> {
                extraOffset += ThreadLocalRandom.current().nextDouble(0.05, 0.1);
                for (int i = 0; i < offset(); i++) {
                    toBreak.addAll(ShootParticle.shoot(
                            particle(), range(), damage(),
                            player, pierceNumber(), extraOffset, true
                    ));
                }
            }
            case GRENADE -> ((Throwable) manager.getPlugin().getItems()
                    .toGameItem("GRENADE")).use(manager, player);
        }

        player.getWorld().playSound(player.getLocation(), sound(), SoundCategory.PLAYERS, 1, pitch());
        player.setCooldown(material(), shotCooldown());

        if (item.getAmount() == 1) {
            reload(manager, player);
        } else {
            item.setAmount(item.getAmount() - 1);
        }

        return toBreak;
    }

    public void reload(GameManager manager, Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        var meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().getKeys().contains(namespacedKey())) {
            return;
        }

        int currentAmmo = getAmmo(item);
        if (item.getAmount() == clipSize()) {
            return;
        }
        if (currentAmmo <= 0) {
            return;
        }
        if (currentAmmo == item.getAmount()) {
            return;
        }
        if (currentAmmo + 1 < item.getAmount()) {
            item.setAmount(currentAmmo + 1);
            return;
        }

        manager.addTask(
                new PlayerTask(player) {
                    int t = 1;

                    @Override
                    public void run() {
                        super.run();

                        if (t >= reloadTime) {
                            item.setAmount(Math.min(clipSize(), getAmmo(item)));
                            setDamage(0);
                            // TODO: check if this fixes/breaks reloads: player.updateInventory();
                            cancel();
                        }

                        if (t % 5 == 0) {
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 0.1f, 0);
                        }

                        double percentComplete = (double) t / reloadTime;
                        short maxDurability = item.getType().getMaxDurability();
                        setDamage((int) Math.ceil(maxDurability - (percentComplete * maxDurability)));
                        // TODO: check if this fixes/breaks reloads: player.updateInventory();

                        t++;
                    }

                    void setDamage(int dmg) {
                        var damageable = (Damageable) item.getItemMeta();
                        if (damageable != null) damageable.setDamage(dmg);
                        item.setItemMeta(damageable);
                    }
                }.runTaskTimer(manager.getPlugin(), 1, 1)
        );
    }
}
