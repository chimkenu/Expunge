package me.chimkenu.expunge.guns.listeners;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.Utils;
import me.chimkenu.expunge.guns.RayTrace;
import me.chimkenu.expunge.guns.shoot.*;
import me.chimkenu.expunge.guns.utilities.Utility;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class UtilityListener implements Listener {

    private void useUtil(Player player, Utility utility) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.isSimilar(utility.getUtility()) && player.getCooldown(utility.getMaterial()) < 1) {
            utility.use(player);
            player.setCooldown(utility.getMaterial(), utility.getCooldown());
            if (utility.isInstantUse()) {
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onClickBlock(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            Player player = e.getPlayer();
            Utility util = Utils.getPlayerHeldUtility(player.getInventory().getItemInMainHand());

            if (util != null) {
                e.setCancelled(true);
                useUtil(player, util);
            }
        }
    }

    @EventHandler
    public void onClickEntity(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Utility util = Utils.getPlayerHeldUtility(player.getInventory().getItemInMainHand());

        if (util != null) {
            e.setCancelled(true);
            useUtil(player, util);
        }
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        if (projectile.getCustomName() == null) return;

        int bounceNum;
        try {
            bounceNum = Integer.parseInt(projectile.getCustomName());
        } catch (NumberFormatException exception) { return; }
        BlockFace blockFace = e.getHitBlockFace();
        if (bounceNum > 0 && blockFace != null) {
            e.setCancelled(true);
            Vector bounceVelocity = projectile.getVelocity();

            // find which coordinate to invert
            if (blockFace.equals(BlockFace.EAST) || blockFace.equals(BlockFace.WEST)) {
                bounceVelocity.setX(-bounceVelocity.getX());
            } else if (blockFace.equals(BlockFace.NORTH) || blockFace.equals(BlockFace.SOUTH)) {
                bounceVelocity.setZ(-bounceVelocity.getZ());
            } else {
                bounceVelocity.setY(-bounceVelocity.getY());
            }

            bounceVelocity.multiply(0.6);
            bounceNum--;

            projectile.getWorld().playSound(projectile.getLocation(), Sound.ENTITY_ARMOR_STAND_HIT, SoundCategory.PLAYERS, 0.5f, 0);
            projectile.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, projectile.getLocation(), 10, 0.1, 0.1, 0.1, 0);
            Entity entity;
            if (projectile.getScoreboardTags().contains("GUNS-PLUGIN_GRENADE")) entity = ShootGrenade.shoot(projectile.getLocation(), bounceVelocity, projectile.getWorld(), bounceNum);
            else if (projectile.getScoreboardTags().contains("GUNS-PLUGIN_SMOKE")) entity = ShootSmoke.shoot(projectile.getLocation(), bounceVelocity, projectile.getWorld(), bounceNum);
            else if (projectile.getScoreboardTags().contains("GUNS-PLUGIN_FLASH")) entity = ShootFlash.shoot(projectile.getLocation(), bounceVelocity, projectile.getWorld(), bounceNum);
            else return;
            projectile.remove();
            ((Projectile)entity).setShooter(projectile.getShooter());
            entity.setVelocity(bounceVelocity);
            entity.setCustomName("" + bounceNum);

        } else {
            if (projectile.getScoreboardTags().contains("GUNS-PLUGIN_GRENADE")) {
                projectile.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, projectile.getLocation(), 1);
                projectile.getWorld().playSound(projectile.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1, 1);
                for (Entity entity : projectile.getWorld().getNearbyEntities(projectile.getLocation(), 4, 4, 4)) {
                    if (!(entity instanceof LivingEntity livingEntity)) {
                        continue;
                    }
                    if (livingEntity instanceof ArmorStand) {
                        continue;
                    }
                    if (livingEntity instanceof Player player) {
                        if (player.getGameMode() == GameMode.ADVENTURE)
                            livingEntity.damage(1, (Entity) projectile.getShooter());
                        continue;
                    }
                    livingEntity.getWorld().spawnParticle(Particle.BLOCK_CRACK, livingEntity.getLocation().add(0, .5, 0), 50, 0.2, 0.2, 0.2, Material.NETHER_WART_BLOCK.createBlockData());
                    livingEntity.damage(80, (Entity) projectile.getShooter());
                }
            }
            else if (projectile.getScoreboardTags().contains("GUNS-PLUGIN_SMOKE")) {
                Location loc = projectile.getLocation();
                World world = projectile.getWorld();
                world.playSound(loc, Sound.ENTITY_CREEPER_PRIMED, 1, 2);
                new BukkitRunnable() {
                    int t = 0;
                    @Override
                    public void run() {
                        world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 25, 2, 2, 2, 0, null, true);
                        t++;
                        if (t > 20*20) this.cancel();
                    }
                }.runTaskTimer(Expunge.instance, 0, 1);
            }
            else if (projectile.getScoreboardTags().contains("GUNS-PLUGIN_MOLOTOV")) {
                Location loc = projectile.getLocation();
                World world = projectile.getWorld();
                world.playSound(loc, Sound.ITEM_FIRECHARGE_USE, 1, 0);
                new BukkitRunnable() {
                    int t = 0;
                    @Override
                    public void run() {
                        world.spawnParticle(Particle.FLAME, loc, 20, 0.8, 0.8, 0.8, 0, null, true);
                        t++;
                        if (t > 7*20) this.cancel();

                        // damage nearby
                        if (t % 4 == 0) {
                            for (Entity entity : world.getNearbyEntities(loc, 2, 2, 2)) {
                                if (entity instanceof LivingEntity livingEntity) {
                                    Vector vec = livingEntity.getVelocity();
                                    livingEntity.damage(1);
                                    livingEntity.setVelocity(vec);
                                    livingEntity.setNoDamageTicks(0);
                                    if (livingEntity instanceof Player) {
                                        livingEntity.setFireTicks(40);
                                    } else {
                                        livingEntity.setFireTicks(20 * 60);
                                    }
                                }
                            }
                        }
                    }
                }.runTaskTimer(Expunge.instance, 0, 1);
            }
            else if (projectile.getScoreboardTags().contains("GUNS-PLUGIN_FLASH")) {
                Location loc = projectile.getLocation();
                World world = projectile.getWorld();
                world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR, 1, 1);
                world.spawnParticle(Particle.FLASH, loc, 1);
                for (Entity entity : world.getNearbyEntities(loc, 75, 30, 75)) {
                    LivingEntity livingEntity;
                    try { livingEntity = (LivingEntity) entity; }
                    catch (ClassCastException exception) { continue; }
                    RayTrace ray = new RayTrace(loc.toVector(), livingEntity.getEyeLocation().toVector().subtract(loc.toVector()).normalize());
                    boolean flashEntity = true;
                    for (Vector v : ray.traverse(loc.distance(livingEntity.getEyeLocation()), 0.3)) {
                        if (v.toLocation(livingEntity.getWorld()).getBlock().getType().isSolid()) {
                            flashEntity = false;
                            break;
                        }
                    }

                    if (flashEntity) livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 5, false, false, false));
                }
            }
            projectile.remove();
        }
    }
}
