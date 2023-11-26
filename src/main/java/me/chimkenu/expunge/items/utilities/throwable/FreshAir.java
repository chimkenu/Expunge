package me.chimkenu.expunge.items.utilities.throwable;

import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.GameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FreshAir implements Throwable {
    @Override
    public void use(JavaPlugin plugin, GameManager gameManager, LivingEntity entity) {
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);
        Projectile ball = entity.launchProjectile(Snowball.class);
        ball.addScoreboardTag(getTag());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=THROWABLE_FRESH_AIR] run data merge entity @s {Item:{id:\"minecraft:glass_bottle\",Count:1b}}");
    }

    @Override
    public void onLand(JavaPlugin plugin, World world, Location loc, Entity shooter) {
        world.spawnParticle(Particle.SPELL, loc, 200, 2, 0.5, 2, 0);
        world.playSound(loc, Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1, 0);
        world.playSound(loc, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.PLAYERS, 1, 0);
        int numOfMobs = 0;
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
            numOfMobs++;
            livingEntity.getWorld().spawnParticle(Particle.SPELL, livingEntity.getLocation().add(0, .5, 0), 25, 0.2, 0.2, 0.2, 0);
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 10, 0, false, true, false));
        }

        // achievement
        if (numOfMobs >= 20 && shooter instanceof Player player) {
            Achievements.ZERO_GRAVITY.grant(player);
        }
    }

    @Override
    public Material getMaterial() {
        return Material.GLASS_BOTTLE;
    }

    @Override
    public Component getName() {
        return Component.text("Fresh Air", NamedTextColor.AQUA);
    }

    @Override
    public String getTag() {
        return "THROWABLE_FRESH_AIR";
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }
}
