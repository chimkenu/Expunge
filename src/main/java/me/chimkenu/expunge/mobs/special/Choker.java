package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.enums.Modifiers;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.mobs.MobGoal;
import me.chimkenu.expunge.mobs.MobSettings;
import me.chimkenu.expunge.utils.RayTrace;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Choker extends MobGoal {
    private Player victim;

    public Choker(GameManager manager, Mob mob, MobSettings settings) {
        super(manager, mob, settings);
        victim = null;
    }

    @Override
    public boolean canUse() {
        if (victim != null) {
            victim = null;
            return false;
        }

        if (!(mob.getTarget() instanceof Player target)) {
            return false;
        }

        if (!target.getPassengers().isEmpty()) {
            mob.setTarget(null);
            return false;
        }

        victim = target;
        return super.canUse() && raytrace();
    }

    private boolean raytrace() {
        var targetBoundingBox = victim.getBoundingBox();
        RayTrace rayTrace = new RayTrace(mob.getEyeLocation().toVector(), mob.getLocation().getDirection());
        for (Vector v : rayTrace.traverse(20, 0.2)) {
            Location l = v.toLocation(mob.getWorld());
            if (!l.getBlock().isEmpty() && l.getBlock().getBoundingBox().contains(v)) {
                return false;
            }
            if (targetBoundingBox.contains(v)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        attackFrameTicks = 20;
        playTellAnimation("");
    }

    @Override
    public void tick() {
        if (attackFrameTicks > 0) {
            attackFrameTicks--;
            return;
        }

        if (attackFrameTicks == 0) {
            victim.getInventory().setHeldItemSlot(6);
            Modifiers.NULLIFY.apply(victim, Attribute.MOVEMENT_SPEED);
            Modifiers.NULLIFY.apply(victim, Attribute.JUMP_STRENGTH);

            // add a small armorStand as a check
            var armorStand = victim.getWorld().spawn(victim.getLocation(), ArmorStand.class);
            armorStand.setGravity(false);
            armorStand.setInvulnerable(true);
            armorStand.setInvisible(true);
            armorStand.setSmall(true);
            armorStand.addScoreboardTag("KNOCKED");
            victim.addPassenger(armorStand);
            manager.addEntity(armorStand);

            attackFrameTicks--;
        }

        Vector direction = victim.getEyeLocation().toVector().subtract(mob.getEyeLocation().toVector()).normalize();
        double distance = mob.getEyeLocation().distance(victim.getEyeLocation());
        var world = manager.getWorld();
        for (Vector v : new RayTrace(mob.getEyeLocation().toVector(), direction).traverse(distance, 0.1)) {
            world.spawnParticle(Particle.DUST, v.getX(), v.getY(), v.getZ(), 1, new Particle.DustOptions(Color.RED, 2));
        }
        if (distance > 2) {
            victim.setVelocity(direction.multiply(-0.1));
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !mob.isDead() && (attackFrameTicks > 0 || !victim.getPassengers().isEmpty());
    }

    @Override
    public void stop() {
        super.stop();

        if (victim == null) {
            return;
        }
        Modifiers.NULLIFY.remove(victim, Attribute.MOVEMENT_SPEED);
        Modifiers.NULLIFY.remove(victim, Attribute.JUMP_STRENGTH);
        victim = null;
    }

    @Override
    protected void playTellAnimation(String type) {
        mob.getWorld().playSound(mob, Objects.requireNonNull(mob.getHurtSound()), 2, 2);
    }

    @Override
    protected void playJingle() {
        mob.getWorld().playSound(mob, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 2, 0.561231f);
        mob.getWorld().playSound(mob, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 2, 0.594604f);
        manager.addTask(
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        mob.getWorld().playSound(mob, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 2, 0.5f);
                    }
                }.runTaskLater(manager.getPlugin(), 15)
        );
    }
}
