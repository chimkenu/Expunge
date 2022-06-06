package me.chimkenu.expunge.guns.listeners;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.Utils;
import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.guns.guns.Gun;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;

public class Reload implements Listener {
    public static boolean reload(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        Gun gun = Utils.getPlayerHeldGun(item);

        if (gun == null) return false;
        if (item.getAmount() == gun.getClipSize()) return false;

        Weapons.Guns weapon = Utils.getEnumFromGun(gun.getClass());
        if (Shoot.getAmmo(player, weapon) < 1) return false;
        if (Shoot.getAmmo(player, weapon) <= item.getAmount()) return false;

        new BukkitRunnable() {
            int t = 1;
            final short maxDurability = item.getType().getMaxDurability();
            final int reloadTime = gun.getReload();

            @Override
            public void run() {
                if (t >= reloadTime) this.cancel();
                if (t % 5 == 0) player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 0.1f, 0);

                double percentComplete = (double) t / reloadTime;
                int dmg = (int) Math.ceil(maxDurability - (percentComplete * maxDurability));
                Damageable damageable = (Damageable) item.getItemMeta();
                if (damageable != null) damageable.setDamage(dmg);
                item.setItemMeta(damageable);
                player.updateInventory();

                t++;
            }
        }.runTaskTimer(Expunge.instance, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                item.setAmount(Math.min(gun.getClipSize(), Shoot.getAmmo(player, weapon)));
                Damageable damageable = (Damageable) item.getItemMeta();
                if (damageable != null) damageable.setDamage(0);
                item.setItemMeta(damageable);
                player.updateInventory();
            }
        }.runTaskLater(Expunge.instance, gun.getReload());
        return true;
    }

    private void refill(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 1, 1);
        for (Weapons.Guns gun : Weapons.Guns.values()) {
            Shoot.setAmmo(player, gun, gun.getGun().getMaxAmmo());
        }
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
        refill(e.getPlayer());
        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§9+Ammo"));
        // update ammo if holding gun
        Gun gun = Utils.getPlayerHeldGun(e.getPlayer().getInventory().getItemInMainHand());
        if (gun == null) {
            return;
        }
        e.getPlayer().setLevel(Shoot.getAmmo(e.getPlayer(), Utils.getEnumFromGun(gun.getClass())));
    }
}
