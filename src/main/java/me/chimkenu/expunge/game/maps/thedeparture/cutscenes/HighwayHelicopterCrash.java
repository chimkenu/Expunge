package me.chimkenu.expunge.game.maps.thedeparture.cutscenes;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.maps.Cutscene;
import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class HighwayHelicopterCrash extends Cutscene {
    public HighwayHelicopterCrash(List<Player> viewers, List<GameMob> mobs) {
        super(viewers, mobs);
    }

    @Override
    public void play() {
        // filter viewers then stop if viewers < 0
        for (Player p : viewers.keySet()) {
            if (p.getPassengers().size() > 0 || p.getVehicle() != null) viewers.remove(p);
        }
        if (viewers.size() < 1) return;

        // filter mobs that target the viewers
        for (Mob m : mobs.keySet()) {
            try {
                if (!viewers.containsKey((Player) m.getTarget())) {
                    mobs.remove(m);
                }
            } catch (ClassCastException ignored) {
                mobs.remove(m);
            }
        }

        // freeze mobs for the duration of the cutscene (5 seconds)
        // also teleport viewers to face cutscene
        new BukkitRunnable() {
            int i = 20 * 5;
            @Override
            public void run() {
                for (Mob m : mobs.keySet()) {
                    m.teleport(mobs.get(m));
                }
                for (Player p : viewers.keySet()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + p.getName() + " at @s anchored eyes rotated as @e[tag=HIGHWAY_HELICOPTER_CRASH_ROTATION] positioned ^ ^ ^5 rotated as @s positioned ^ ^ ^40 facing entity @s eyes facing ^ ^ ^-1 positioned as @s run tp @s 1004.5 36.0 1280.5 ~ ~");
                }
                if (i <= 0) this.cancel();
                i--;
            }
        }.runTaskTimer(Expunge.instance, 0, 1);

        // apply invisibility
        for (Player p : viewers.keySet()) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 5, 0, false, false, false));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setblock 978 26 1286 minecraft:redstone_block");
            }
        }.runTaskLater(Expunge.instance, 20 * 2);

        // wait 3 (+2) seconds after animation then bring em back
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : viewers.keySet()) {
                    p.teleport(viewers.get(p));
                }
            }
        }.runTaskLater(Expunge.instance, (20 * 5) + 1);
    }
}
