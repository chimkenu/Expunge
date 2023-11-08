package me.chimkenu.expunge.items.utilities.throwable;

import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.game.LocalGameManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Grenade implements Throwable {
    @Override
    public void use(JavaPlugin plugin, LocalGameManager localGameManager, LivingEntity entity) {
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);
        Projectile ball = entity.launchProjectile(Snowball.class);
        ball.addScoreboardTag(getTag());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=THROWABLE_GRENADE] run data merge entity @s {Item:{id:\"minecraft:coal_block\",Count:1b}}");
    }

    @Override
    public void onLand(JavaPlugin plugin, World world, Location loc, Entity shooter) {
        world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 1);
        world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1, 1);
        int numOfMobs = 0;
        for (Entity entity : world.getNearbyEntities(loc, 4, 4, 4)) {
            if (!(entity instanceof LivingEntity livingEntity)) {
                continue;
            }
            if (livingEntity instanceof ArmorStand) {
                continue;
            }
            if (livingEntity instanceof Player player) {
                if (player.getGameMode() == GameMode.ADVENTURE)
                    livingEntity.damage(1, shooter);
                continue;
            }
            numOfMobs++;
            livingEntity.getWorld().spawnParticle(Particle.BLOCK_CRACK, livingEntity.getLocation().add(0, .5, 0), 50, 0.2, 0.2, 0.2, Material.NETHER_WART_BLOCK.createBlockData());
            livingEntity.damage(80, shooter);
        }

        // achievement
        if (numOfMobs >= 20 && shooter instanceof Player player) {
            Achievements.WHEN_GUTS_FLY.grant(player);
        }
    }

    @Override
    public Material getMaterial() {
        return Material.COAL;
    }

    @Override
    public String getName() {
        return "&8Grenade";
    }

    @Override
    public String getTag() {
        return "THROWABLE_GRENADE";
    }
}
