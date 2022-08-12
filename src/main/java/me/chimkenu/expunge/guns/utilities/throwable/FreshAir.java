package me.chimkenu.expunge.guns.utilities.throwable;

import me.chimkenu.expunge.enums.Slot;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FreshAir extends Throwable {
    public FreshAir() {
        super(20, Material.GLASS_BOTTLE, "&3Fresh Air™", Slot.TERTIARY, "THROWABLE_FRESH_AIR");
    }

    @Override
    public void use(LivingEntity entity) {
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);
        Projectile ball = entity.launchProjectile(Snowball.class);
        ball.addScoreboardTag(getTag());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=THROWABLE_FRESH_AIR] run data merge entity @s {Item:{id:\"minecraft:glass_bottle\",Count:1b}}");
    }

    @Override
    public void onLand(World world, Location loc, Entity shooter) {
        world.spawnParticle(Particle.SPELL, loc, 200, 2, 0.5, 2, 0);
        world.playSound(loc, Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1, 0);
        world.playSound(loc, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.PLAYERS, 1, 0);
        for (Entity entity : world.getNearbyEntities(loc, 5, 5, 5)) {
            if (!(entity instanceof LivingEntity livingEntity)) {
                continue;
            }
            if (livingEntity instanceof ArmorStand) {
                continue;
            }
            if (livingEntity instanceof Player) {
                continue;
            }
            livingEntity.getWorld().spawnParticle(Particle.SPELL, livingEntity.getLocation().add(0, .5, 0), 25, 0.2, 0.2, 0.2, 0);
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 10, 0, false, true, false));
        }
    }
}
