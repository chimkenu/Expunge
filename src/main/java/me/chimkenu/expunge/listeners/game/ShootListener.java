package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import me.chimkenu.expunge.items.ShootParticle;
import me.chimkenu.expunge.items.utilities.throwable.Grenade;
import me.chimkenu.expunge.items.weapons.guns.GrenadeLauncher;
import me.chimkenu.expunge.items.weapons.guns.Gun;
import me.chimkenu.expunge.items.weapons.guns.MP5;
import me.chimkenu.expunge.items.weapons.guns.SMG;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ShootListener extends GameListener {
    public ShootListener(JavaPlugin plugin, LocalGameManager localGameManager) {
        super(plugin, localGameManager);
    }

    public static int getAmmo(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return 0;
        List<String> lore = meta.getLore();
        if (lore == null) return 0;
        int ammo;
        try {
            ammo = Integer.parseInt(meta.getLore().get(0));
        } catch (NumberFormatException ignored) {
            return 0;
        }
        return ammo;
    }
    public static void setAmmo(ItemStack item, int newAmmo) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        List<String> lore = new ArrayList<>();
        lore.add(String.valueOf(newAmmo));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private void fireGun(Player player, Gun gun) {
        ItemStack item = player.getInventory().getItemInMainHand();

        int currentAmmo = getAmmo(item);
        if (currentAmmo < 1) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§c§lOut of Ammo!"));
            return;
        }
        if (player.getCooldown(gun.getMaterial()) > 0) {
            return;
        }

        currentAmmo--;
        setAmmo(item, currentAmmo);
        player.setLevel(currentAmmo);

        // OFFSET MODIFICATION : Offset is increased based on certain actions/conditions
        double offset = 0.001;
        Location loc = player.getLocation();

        // is on ladder
        if (loc.subtract(0, 0.1, 0).getBlock().getType() == Material.LADDER) offset += 0.005;

        // is not on solid ground
        if (!(loc.subtract(.31, 0.1, .31).getBlock().getType().isSolid() ||
                loc.subtract(-.31, 0.1, -.31).getBlock().getType().isSolid() ||
                loc.subtract(.31, 0.1, -.31).getBlock().getType().isSolid() ||
                loc.subtract(-.31, 0.1, .31).getBlock().getType().isSolid() ||
                loc.subtract(0, 0.1, 0).getBlock().getType().isSolid()
                ))
            offset += 0.01;

        // is moving a lot
        if (player.getVelocity().getX() != 0) offset += 0.005;
        if (player.getVelocity().getZ() != 0) offset += 0.005;
        if (player.isSprinting()) offset += 0.005;

        // is using a smg
        if (gun instanceof SMG) offset += 0.005;
        else if (gun instanceof MP5) offset += 0.005;

        if (gun instanceof GrenadeLauncher) new Grenade().use(player);
        else {
            if (gun.getPellets() > 1) {
                offset += ThreadLocalRandom.current().nextDouble(0.05, 0.1);
            }

            for (int i = 0; i < gun.getPellets(); i++) {
                ShootParticle.shoot(gun.getParticle(), gun.getRange(), gun.getDamage(), player, gun.getEntitiesToHit(), offset, gun.getPellets() > 1);
            }
        }

        player.getWorld().playSound(player.getLocation(), gun.getSound(), SoundCategory.PLAYERS, 1, gun.getPitch());
        player.setCooldown(gun.getMaterial(), gun.getCooldown());

        if (item.getAmount() == 1) {
            reload(player);
        } else {
            item.setAmount(item.getAmount() - 1);
        }
    }

    public void reload(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        Gun gun = Utils.getPlayerHeldGun(item);

        if (gun == null) return;
        if (item.getAmount() == gun.getClipSize()) return;

        if (ShootListener.getAmmo(item) < 1) return;
        if (ShootListener.getAmmo(item) == item.getAmount()) return;
        else if (ShootListener.getAmmo(item) + 1 < item.getAmount()) {
            item.setAmount(ShootListener.getAmmo(item) + 1);
            return;
        }

        new BukkitRunnable() {
            int t = 1;
            final short maxDurability = item.getType().getMaxDurability();
            final int reloadTime = gun.getReload();
            final Damageable damageable = (Damageable) item.getItemMeta();

            @Override
            public void run() {
                if (t >= reloadTime) this.cancel();
                if (t % 5 == 0) player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 0.1f, 0);

                double percentComplete = (double) t / reloadTime;
                int dmg = (int) Math.ceil(maxDurability - (percentComplete * maxDurability));
                if (damageable != null) damageable.setDamage(dmg);
                item.setItemMeta(damageable);
                player.updateInventory();

                t++;
            }
        }.runTaskTimer(plugin, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                item.setAmount(Math.min(gun.getClipSize(), ShootListener.getAmmo(item)));
                Damageable damageable = (Damageable) item.getItemMeta();
                if (damageable != null) damageable.setDamage(0);
                item.setItemMeta(damageable);
                player.updateInventory();
            }
        }.runTaskLater(plugin, gun.getReload());
    }

    @EventHandler
    public void onClickBlock(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

            Player player = e.getPlayer();
            Gun gun = Utils.getPlayerHeldGun(player.getInventory().getItemInMainHand());

            if (gun != null) {
                e.setCancelled(true);
                if (!player.isSneaking())
                    fireGun(player, gun);
                else
                    reload(player);
            }
        }
    }
}
