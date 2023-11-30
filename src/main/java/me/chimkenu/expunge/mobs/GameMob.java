package me.chimkenu.expunge.mobs;

import org.bukkit.*;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class GameMob {
    private final Mob mob;
    private final BukkitTask runnable;
    private MobBehavior behavior;

    public <T extends Mob> GameMob(JavaPlugin plugin, World world, Vector locationToSpawn, Class<T> mobToSpawn, MobBehavior behavior) {
        mob = world.spawn(new Location(world, locationToSpawn.getX(), locationToSpawn.getY(), locationToSpawn.getZ()), mobToSpawn);
        mob.addScoreboardTag("MOB");
        mob.setCanPickupItems(false);
        mob.setRemoveWhenFarAway(false);
        this.behavior = behavior;
        runnable = this.behavior == null ? null : new BukkitRunnable() {
            @Override
            public void run() {
                getBehavior().run(mob);
                if (mob.isDead()) this.cancel();
            }
        }.runTaskTimer(plugin, 1, 20);
    }

    private MobBehavior getBehavior() {
        return behavior;
    }

    protected void setBehavior(MobBehavior behavior) {
        this.behavior = behavior;
    }

    public Mob getMob() {
        return mob;
    }

    public void remove() {
        if (runnable != null && !runnable.isCancelled()) runnable.cancel();
        mob.remove();
    }

    public static Player getRandomPlayer(World world) {
        List<Player> players = world.getPlayers();
        players.removeIf(player -> player.getGameMode() != GameMode.ADVENTURE);
        if (players.isEmpty()) return null;
        int item = ThreadLocalRandom.current().nextInt(players.size());
        int i = 0;
        for (Player p : players) {
            if (i == item) return p;
            i++;
        }
        return null;
    }
}
