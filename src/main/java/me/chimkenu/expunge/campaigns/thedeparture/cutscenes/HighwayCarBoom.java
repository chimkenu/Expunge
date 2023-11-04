package me.chimkenu.expunge.campaigns.thedeparture.cutscenes;

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

public class HighwayCarBoom extends Cutscene {
    public HighwayCarBoom(List<Player> viewers, List<GameMob> mobs) {
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

        // freeze mobs for the duration of the cutscene (6 seconds)
        // also teleport viewers to face cutscene
        new BukkitRunnable() {
            int i = 20 * 6;
            @Override
            public void run() {
                for (Mob m : mobs.keySet()) {
                    m.teleport(mobs.get(m));
                }
                if (i <= 0) this.cancel();
                i--;
            }
        }.runTaskTimer(Expunge.instance, 0, 1);
        new BukkitRunnable() {
            int i = 20 * 2;
            final Location loc = new Location(Expunge.currentMap.getWorld(), 1009.50, 36.00, 1248.50);
            @Override
            public void run() {
                for (Player p : viewers.keySet()) {
                    p.teleport(loc);
                }
                i--;
                if (i <= 0) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setblock 1011 31 1242 minecraft:redstone_block");
                    this.cancel();
                }
            }
        }.runTaskTimer(Expunge.instance, 0, 1);

        // apply invisibility
        for (Player p : viewers.keySet()) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 5, 0, false, false, false));
            p.addScoreboardTag("HIGHWAY_SCENE");
        }

        // wait 4 (+2) seconds after animation then bring em back
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : viewers.keySet()) {
                    p.teleport(viewers.get(p));
                    p.removeScoreboardTag("HIGHWAY_SCENE");
                }
            }
        }.runTaskLater(Expunge.instance, (20 * 6) + 1);
    }
}
