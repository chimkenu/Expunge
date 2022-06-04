package me.chimkenu.expunge.guns.shoot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public class ShootFlash {

    public static void shoot(Player player) {
        Projectile ball = player.launchProjectile(Snowball.class);
        ball.setVelocity(ball.getVelocity().multiply(0.7));
        ball.setCustomName("0");
        ball.addScoreboardTag("GUNS-PLUGIN_FLASH");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=GUNS-PLUGIN_FLASH] run data merge entity @s {Item:{id:\"minecraft:light_gray_candle\",Count:1b}}");
    }

    public static Entity shoot(Location loc, Vector vec, World world, int bounceNum) {
        Entity ball = world.spawnEntity(loc, EntityType.SNOWBALL);
        ball.setCustomName(bounceNum + "");
        ball.addScoreboardTag("GUNS-PLUGIN_FLASH");
        ball.setVelocity(vec);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=GUNS-PLUGIN_FLASH] run data merge entity @s {Item:{id:\"minecraft:light_gray_candle\",Count:1b}}");
        return ball;
    }
}
