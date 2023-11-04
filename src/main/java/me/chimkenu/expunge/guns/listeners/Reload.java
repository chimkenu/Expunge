package me.chimkenu.expunge.guns.listeners;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.utils.Utils;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.guns.weapons.guns.Gun;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;

public class Reload implements Listener {
    public static void reload(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        Gun gun = Utils.getPlayerHeldGun(item);

        if (gun == null) return;
        if (item.getAmount() == gun.getClipSize()) return;

        if (Shoot.getAmmo(item) < 1) return;
        if (Shoot.getAmmo(item) == item.getAmount()) return;
        else if (Shoot.getAmmo(item) + 1 < item.getAmount()) {
            item.setAmount(Shoot.getAmmo(item) + 1);
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
        }.runTaskTimer(Expunge.instance, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                item.setAmount(Math.min(gun.getClipSize(), Shoot.getAmmo(item)));
                Damageable damageable = (Damageable) item.getItemMeta();
                if (damageable != null) damageable.setDamage(0);
                item.setItemMeta(damageable);
                player.updateInventory();
            }
        }.runTaskLater(Expunge.instance, gun.getReload());
    }

    @EventHandler
    public void onAmmoRefill(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof FallingBlock fallingBlock)) {
            return;
        }
        if (!fallingBlock.getScoreboardTags().contains("AMMO_PILE")) {
            return;
        }
        e.setCancelled(true);

        // player clicked an ammo refill thing
        Player player = e.getPlayer();
        // update ammo if holding gun
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Gun gun = Utils.getPlayerHeldGun(itemStack);
        if (gun == null) {
            return;
        } else if (gun.getTier() == Tier.SPECIAL) {
            e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cThere's no ammo for this weapon..."));
            return;
        }
        player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 1, 1);
        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§9+Ammo"));
        Shoot.setAmmo(itemStack, gun.getMaxAmmo());
        itemStack.setAmount(gun.getClipSize());
        e.getPlayer().setLevel(Shoot.getAmmo(itemStack));
    }
}
