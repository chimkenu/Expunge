package me.chimkenu.expunge.game;

import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.mobs.MobType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.stream.Stream;

public interface Director {
    /** Called every game tick to update internal state and spawn/despawn entities. */
    void update(GameState gameState);

    void spawnMob(Class<? extends Mob> mob, MobType type, int numberToSpawn, Vector position, boolean isAggressive);

    default void spawnMob(MobType type, int numberToSpawn, Vector position, boolean isAggressive) {
        spawnMob(type.defaultMob(), type, numberToSpawn, position, isAggressive);
    }

    void spawnCommon(int numberToSpawn, boolean isWanderer, boolean force);

    void bile(LivingEntity target, double radius);

    void setPhase(Phase phase);

    Phase getPhase();

    void resetState();

    Stream<Entity> getActiveMobs();

    Stream<Player> getAlivePlayers();

    void spawnItem(GameItem item, Vector pos, boolean isInvulnerable);

    void spawnStartingItems();

    void spawnMapItems();

    void spawnAmmoPiles();

    void clearEntities();

    enum Phase {
        DISABLED,
        BUILD,
        PEAK,
        RELAX
    }
}
