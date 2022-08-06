package me.chimkenu.expunge.mobs;

import me.chimkenu.expunge.Expunge;
import org.bukkit.*;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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

    private static ItemStack getDyedArmor(Material material) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (meta != null) {
            meta.setColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255)));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static void putOnRandomClothes(Mob mob) {
        EntityEquipment equipment = mob.getEquipment();
        if (equipment != null) {
            equipment.setChestplate(getDyedArmor(Material.LEATHER_CHESTPLATE));
            equipment.setLeggings(getDyedArmor(Material.LEATHER_LEGGINGS));
            equipment.setBoots(getDyedArmor(Material.LEATHER_BOOTS));
        }
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
