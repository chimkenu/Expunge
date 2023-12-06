package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.utils.RayTrace;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class Choker extends Special {
    private Player victim = null;
    private ArmorStand topArmorStand = null;
    private ArmorStand bottomArmorStand = null;

    public Choker(JavaPlugin plugin, World world, GameManager gameManager, Vector locationToSpawn) {
        super(plugin, gameManager.getWorld(), locationToSpawn, Husk.class, mob -> {});
        setBehavior(mob -> {
            if (victim != null) {
                if (victim.getVehicle() == null || !victim.getPassengers().contains(topArmorStand) || victim.getVehicle() != bottomArmorStand || mob.isDead()) {
                    victim = null;
                    if (topArmorStand != null) topArmorStand.remove();
                    topArmorStand = null;
                    if (bottomArmorStand != null) bottomArmorStand.remove();
                    bottomArmorStand = null;
                    return;
                }

                mob.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 250, 1, false, true, false));
                mob.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 100, false, true, false));

                Vector direction = victim.getEyeLocation().toVector().subtract(mob.getEyeLocation().toVector()).normalize();
                double distance = mob.getEyeLocation().distance(victim.getEyeLocation());
                for (Vector v : new RayTrace(mob.getEyeLocation().toVector(), direction).traverse(distance, 0.1)) {
                    world.spawnParticle(Particle.REDSTONE, v.getX(), v.getY(), v.getZ(), 1, new Particle.DustOptions(Color.RED, 2));
                }
                if (distance > 2) bottomArmorStand.setVelocity(direction.multiply(-1));
                return;
            }
            if (mob.getTarget() == null) {
                mob.setTarget(getRandomPlayer(gameManager.getWorld()));
                return;
            }
            if (!mob.getTarget().getPassengers().isEmpty()) {
                mob.setTarget(getRandomPlayer(world));
                return;
            }
            if (!(mob.getTarget() instanceof Player player)) {
                return;
            }

            mob.removePotionEffect(PotionEffectType.SLOW);
            if (mob.hasPotionEffect(PotionEffectType.CONFUSION)) {
                return;
            }

            BoundingBox b = player.getBoundingBox();
            RayTrace rayTrace = new RayTrace(mob.getEyeLocation().toVector(), mob.getLocation().getDirection());
            for (Vector v : rayTrace.traverse(30, 0.2)) {
                Location l = v.toLocation(world);
                if (!l.getBlock().isEmpty() && l.getBlock().getBoundingBox().contains(v)) {
                    return;
                }
                if (b.contains(v)) {
                    victim = player;
                    victim.getInventory().setHeldItemSlot(6);

                    bottomArmorStand = world.spawn(victim.getLocation(), ArmorStand.class);
                    bottomArmorStand.setInvisible(true);
                    bottomArmorStand.setInvulnerable(true);
                    bottomArmorStand.setSmall(true);
                    bottomArmorStand.addScoreboardTag("KNOCKED");
                    bottomArmorStand.addScoreboardTag("SMOKER_KNOCKED");
                    gameManager.getDirector().getItemHandler().addEntity(bottomArmorStand);
                    bottomArmorStand.addPassenger(victim);

                    topArmorStand = world.spawn(victim.getLocation(), ArmorStand.class);
                    topArmorStand.setInvisible(true);
                    topArmorStand.setInvulnerable(true);
                    gameManager.getDirector().getItemHandler().addEntity(topArmorStand);
                    victim.addPassenger(topArmorStand);

                    return;
                }
            }
        });
        getMob().addScoreboardTag("SMOKER");
    }

    @Override
    protected void playJingle() {
        getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 2, 0.561231f);
        getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 2, 0.594604f);
        new BukkitRunnable() {
            @Override
            public void run() {
                getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 2, 0.5f);
            }
        }.runTaskLater(plugin, 15);
    }
}
