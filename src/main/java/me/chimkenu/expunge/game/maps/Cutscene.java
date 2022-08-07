package me.chimkenu.expunge.game.maps;

import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public abstract class Cutscene {
    protected final HashMap<Player, Location> viewers;
    protected final HashMap<Mob, Location> mobs;
    public Cutscene(List<Player> viewers, List<GameMob> mobs) {
        this.viewers = new HashMap<>();
        for (Player p : viewers) {
            this.viewers.put(p, p.getLocation());
        }
        this.mobs = new HashMap<>();
        for (GameMob m : mobs) {
            this.mobs.put(m.getMob(), m.getMob().getLocation());
        }
    }
    public abstract void play();
}
