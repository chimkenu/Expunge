package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.ShootParticle;
import me.chimkenu.expunge.items.utilities.throwable.Grenade;
import me.chimkenu.expunge.items.weapons.guns.GrenadeLauncher;
import me.chimkenu.expunge.items.weapons.guns.Gun;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ShootListener extends GameListener {
    private final BreakGlassListener breakGlassListener;

    public ShootListener(JavaPlugin plugin, GameManager gameManager, BreakGlassListener breakGlassListener) {
        super(plugin, gameManager);
        this.breakGlassListener = breakGlassListener;
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
            player.sendActionBar(Component.text("Out of Ammo!", NamedTextColor.RED, TextDecoration.BOLD));
            return;
        }
        if (player.getCooldown(gun.getMaterial()) > 0) {
            return;
        }

        currentAmmo--;
        setAmmo(item, currentAmmo);
        player.setLevel(currentAmmo);

        // OFFSET MODIFICATION : Offset is increased based on certain actions/conditions
        double offset = gun.getOffset();
        Location loc = player.getLocation();

        // is on ladder
        if (loc.subtract(0, 0.1, 0).getBlock().getType() == Material.LADDER) offset += plugin.getConfig().getDouble("gun.ladder-offset");

        // is not on solid ground
        if (!(loc.subtract(.31, 0.1, .31).getBlock().getType().isSolid() ||
                loc.subtract(-.31, 0.1, -.31).getBlock().getType().isSolid() ||
                loc.subtract(.31, 0.1, -.31).getBlock().getType().isSolid() ||
                loc.subtract(-.31, 0.1, .31).getBlock().getType().isSolid() ||
                loc.subtract(0, 0.1, 0).getBlock().getType().isSolid()
                ))
            offset += plugin.getConfig().getDouble("gun.jumping-offset");

        // is moving a lot
        double movingOffset = plugin.getConfig().getDouble("gun.moving-offset");
        if (player.getVelocity().getX() != 0) offset += movingOffset;
        if (player.getVelocity().getZ() != 0) offset += movingOffset;
        if (player.isSprinting()) offset += movingOffset;

        // is down
        if (player.getVehicle() != null) offset += plugin.getConfig().getDouble("gun.downed-offset");

        if (gun instanceof GrenadeLauncher) new Grenade().use(plugin, gameManager, player);
        else {
            if (gun.getPellets() > 1) {
                offset += ThreadLocalRandom.current().nextDouble(0.05, 0.1);
            }

            for (int i = 0; i < gun.getPellets(); i++) {
                Set<Block> blocks =  ShootParticle.shoot(gun.getParticle(), gun.getRange(), gun.getDamage(), player, gun.getEntitiesToHit(), offset, gun.getPellets() > 1);
                for (Block b : blocks) {
                    breakGlassListener.breakGlass(b);
                    // if (!b.isEmpty()) player.sendBlockDamage(b.getLocation(), (float) Math.random()); // Visual damage (needs to use BukkitRunnable(), maybe add block particles as well)
                }
            }
        }

        // Play flash effect
        Location light = player.getEyeLocation();
        if (light.getBlock().isEmpty()) {
            player.sendBlockChange(light, Material.LIGHT.createBlockData("[level=" + (int) (10 + Math.random() * 5) + "]"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendBlockChange(light, Material.AIR.createBlockData());
                }
            }.runTaskLater(plugin, 2);
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

                if (player.getGameMode() != GameMode.ADVENTURE)
                    return;
                if (!player.isSneaking())
                    fireGun(player, gun);
                else
                    reload(player);
            }
        }
    }

    @EventHandler
    public void onClickEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }
        if (e.getDamage() > 2) {
            return;
        }

        Gun gun = Utils.getPlayerHeldGun(player.getInventory().getItemInMainHand());
        if (gun != null) {
            e.setCancelled(true);
            if (player.getGameMode() != GameMode.ADVENTURE)
                return;
            if (!player.isSneaking())
                fireGun(player, gun);
            else
                reload(player);
        }
    }
}
