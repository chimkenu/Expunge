package me.chimkenu.expunge.mobs;

import me.chimkenu.expunge.Expunge;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class GameMob {
    public static Player getRandomPlayer() {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.ADVENTURE) players.add(player);
        }
        if (players.size() < 1) return null;
        return players.get(ThreadLocalRandom.current().nextInt(players.size()));
    }

    public static void putOnRandomClothes(Mob mob) {

    }

    private final Mob mob;
    private final BukkitTask runnable;

    public <T extends Mob> GameMob(World world, Location locationToSpawn, Class<T> mobToSpawn, MobBehavior behavior) {
        mob = world.spawn(locationToSpawn, mobToSpawn);
        mob.addScoreboardTag("MOB");
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                behavior.run(mob);
                if (mob.isDead()) this.cancel();
            }
        }.runTaskTimer(Expunge.instance, 1, 20);
    }

    public Mob getMob() {
        return mob;
    }

    public void remove() {
         mob.remove();
         if (runnable != null && !runnable.isCancelled()) runnable.cancel();
    }
}
