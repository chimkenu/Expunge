package me.chimkenu.expunge.guns.shoot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public class ShootMolotov {

    public static void shoot(Player player) {
        Projectile ball = player.launchProjectile(Snowball.class);
        ball.setCustomName("0");
        ball.addScoreboardTag("GUNS-PLUGIN_MOLOTOV");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=GUNS-PLUGIN_MOLOTOV] run data merge entity @s {Item:{id:\"minecraft:lantern\",Count:1b}}");
    }

    public static Entity shoot(Location loc, Vector vec, World world, int bounceNum) {
        Entity ball = world.spawnEntity(loc, EntityType.SNOWBALL);
        ball.setCustomName(bounceNum + "");
        ball.addScoreboardTag("GUNS-PLUGIN_MOLOTOV");
        ball.setVelocity(vec);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=GUNS-PLUGIN_MOLOTOV] run data merge entity @s {Item:{id:\"minecraft:lantern\",Count:1b}}");
        return ball;
    }
}
