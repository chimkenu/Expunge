package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.enums.Difficulty;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zoglin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Charger extends Special {
    public Charger(JavaPlugin plugin, World world, Vector locationToSpawn, Difficulty difficulty) {
        super(plugin, world, locationToSpawn, Zoglin.class, mob -> {
            double distance;
            if (mob.getTarget() == null) mob.setTarget(getRandomPlayer(world));
            if (!mob.getTarget().getPassengers().isEmpty()) {
                mob.setTarget(getRandomPlayer(world));
                return;
            }
            else distance = mob.getLocation().distanceSquared(mob.getTarget().getLocation());
            if (mob.getVehicle() != null) {
                ((LivingEntity) mob.getVehicle()).damage(2, mob);
                return;
            }
            if (!mob.hasPotionEffect(PotionEffectType.CONFUSION) && distance > 5 * 5 && distance < 10 * 10) {
                // charge at player
                mob.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 0, true, true, false));
                new BukkitRunnable() {
                    final LivingEntity target = mob.getTarget();
                    int time = 0;
                    final Vector direction = mob.getLocation().getDirection().setY(0).normalize().multiply(1.5);
                    @Override
                    public void run() {
                        if (mob.isDead()) {
                            this.cancel();
                            return;
                        }
                        if (target == null) {
                            this.cancel();
                            return;
                        }
                        if (mob.getLocation().distanceSquared(target.getLocation()) < 2 * 2) {
                            this.cancel();
                            return;
                        }
                        if (time >= 60) {
                            this.cancel();
                            return;
                        } else if (!mob.hasPotionEffect(PotionEffectType.CONFUSION)) {
                            this.cancel();
                            return;
                        }

                        mob.setVelocity(mob.getVelocity().add(direction));
                        time++;
                    }
                }.runTaskTimer(plugin, 0, 1);

                // stun
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!mob.isDead() && mob.getVehicle() == null) {
                            mob.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 200, false, false, false));
                            mob.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, 200, false, false, false));
                        }
                    }
                }.runTaskLater(plugin, 61);
            }
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1, false, false));
        });
        Objects.requireNonNull(getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(4);
        Objects.requireNonNull(getMob().getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(400 + ((difficulty.ordinal() * 200)));
        getMob().setHealth(400 + (difficulty.ordinal() * 200));
        getMob().addScoreboardTag("CHARGER");
    }

    @Override
    protected void playJingle() {
        final float[] pitches = new float[]{0.594604f, 0.561231f, 0.594604f, 0.629961f, 0.529732f, 0.561231f};
        for (int i = 0; i < pitches.length; i++) {
            final int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.HOSTILE, 2, pitches[finalI]);
                }
            }.runTaskLater(plugin, 3 * i);
        }
    }
}
