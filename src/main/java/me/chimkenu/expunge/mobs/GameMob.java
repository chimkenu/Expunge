package me.chimkenu.expunge.mobs;

import org.bukkit.*;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class GameMob {
    private final Mob mob;
    private final BukkitTask runnable;

    public <T extends Mob> GameMob(JavaPlugin plugin, World world, Vector locationToSpawn, Class<T> mobToSpawn, MobBehavior behavior) {
        mob = world.spawn(new Location(world, locationToSpawn.getX(), locationToSpawn.getY(), locationToSpawn.getZ()), mobToSpawn);
        mob.addScoreboardTag("MOB");
        runnable = behavior == null ? null : new BukkitRunnable() {
            @Override
            public void run() {
                behavior.run(mob);
                if (mob.isDead()) this.cancel();
            }
        }.runTaskTimer(plugin, 1, 20);
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
        if (players.size() == 0) return null;
        int item = ThreadLocalRandom.current().nextInt(players.size());
        int i = 0;
        for (Player p : players) {
            if (i == item) return p;
            i++;
        }
        return null;
    }

    public void putOnRandomClothes(Mob mob) {
        EntityEquipment equipment = mob.getEquipment();
        equipment.setChestplate(getDyedArmor(Material.LEATHER_CHESTPLATE));
        equipment.setLeggings(getDyedArmor(Material.LEATHER_LEGGINGS));
        equipment.setBoots(getDyedArmor(Material.LEATHER_BOOTS));
    }

    private ItemStack getDyedArmor(Material material) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (meta != null) {
            meta.setColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255)));
            item.setItemMeta(meta);
        }
        return item;
    }
}
