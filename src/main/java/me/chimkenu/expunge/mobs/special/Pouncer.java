package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.game.director.ItemHandler;
import me.chimkenu.expunge.listeners.game.MobListener;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Stray;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class Pouncer extends Special {
    public Pouncer(JavaPlugin plugin, World world, Vector locationToSpawn, ItemHandler itemHandler) {
        super(plugin, world, locationToSpawn, Stray.class, mob -> {
            if (mob.getVehicle() instanceof Player target) {
                mob.setTarget(target);
                target.getInventory().setHeldItemSlot(5);
                target.setVelocity(target.getVelocity().add(mob.getEyeLocation().getDirection()));
                target.damage(0.5, mob);
            } else {
                if (mob.getTarget() instanceof Player player) {
                    if (!player.getPassengers().isEmpty()) {
                        mob.setTarget(getRandomPlayer(world));
                        return;
                    }

                    double distance = mob.getLocation().distanceSquared(mob.getTarget().getLocation());
                    int speed = 1;
                    if (distance > 10 * 10) speed += 2;
                    mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, speed, false, false));
                    if (distance < 5 * 5 && ThreadLocalRandom.current().nextDouble() < 0.5) {
                        mob.setVelocity(mob.getVelocity().add(mob.getEyeLocation().getDirection().normalize().multiply(2.5)).add(new Vector(0, 0.2, 0)));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (mob.getTarget() != null && mob.getLocation().distanceSquared(mob.getTarget().getLocation()) < 3 * 3) {
                                    player.getInventory().setHeldItemSlot(5);
                                    player.addPassenger(mob);
                                    MobListener.disable(player, itemHandler);
                                }
                            }
                        }.runTaskLater(plugin, 5);
                    }
                } else mob.setTarget(getRandomPlayer(world));
            }
        });
        getMob().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    }

    @Override
    protected void playJingle() {
        final float[] pitches = new float[]{1.781797f, 1.887749f, 1.414214f, 1.587401f, 1.498307f, 1.587401f};
        for (int i = 0; i < pitches.length / 2; i++) {
            final int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.HOSTILE, 2, pitches[2 * finalI]);
                }
            }.runTaskLater(plugin, i * 10);
            new BukkitRunnable() {
                @Override
                public void run() {
                    getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.HOSTILE, 2, pitches[2 * finalI + 1]);
                }
            }.runTaskLater(plugin, i * 10 + 2);
        }
    }
}
