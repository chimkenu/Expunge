package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.game.director.ItemHandler;
import me.chimkenu.expunge.mobs.GameMob;
import me.chimkenu.expunge.listeners.game.MobListener;
import org.bukkit.Material;
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

public class Pouncer extends GameMob {
    public Pouncer(JavaPlugin plugin, World world, Vector locationToSpawn, ItemHandler itemHandler) {
        super(plugin, world, locationToSpawn, Stray.class, mob -> {
            if (mob.getVehicle() instanceof Player target) {
                mob.setTarget(target);
                target.getInventory().setHeldItemSlot(5);
                target.setVelocity(target.getVelocity().add(mob.getEyeLocation().getDirection()));
                target.damage(0.5, mob);
            } else {
                if (mob.getTarget() instanceof Player player) {
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
}
