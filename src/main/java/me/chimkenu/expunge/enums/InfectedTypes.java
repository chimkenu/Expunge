package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.mobs.GameMob;
import me.chimkenu.expunge.mobs.common.Horde;
import me.chimkenu.expunge.mobs.common.Wanderer;
import me.chimkenu.expunge.mobs.special.*;
import me.chimkenu.expunge.mobs.uncommon.Robot;
import me.chimkenu.expunge.mobs.uncommon.Soldier;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public enum InfectedTypes {
    HORDE {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Horde(plugin, gameManager.getWorld(), locationToSpawn, difficulty);
        }
    },
    WANDERER {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Wanderer(plugin, gameManager.getWorld(), locationToSpawn, difficulty);
        }
    },
    ROBOT {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Robot(plugin, gameManager.getWorld(), locationToSpawn, difficulty);
        }
    },
    SOLDIER {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Soldier(plugin, gameManager.getWorld(), locationToSpawn, difficulty);
        }
    },
    CHARGER {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Charger(plugin, gameManager.getWorld(), locationToSpawn, difficulty);
        }
    },
    CHOKER {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Choker(plugin, gameManager.getWorld(), locationToSpawn);
        }
    },
    POUNCER {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Pouncer(plugin, gameManager.getWorld(), locationToSpawn, gameManager.getDirector().getItemHandler());
        }
    },
    RIDER {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Rider(plugin, gameManager.getWorld(), locationToSpawn);
        }
    },
    SPEWER {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Spewer(plugin, gameManager.getWorld(), locationToSpawn);
        }
    },
    SPITTER {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Spitter(plugin, gameManager, gameManager.getWorld(), locationToSpawn);
        }
    },
    TANK {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Tank(plugin, gameManager.getWorld(), locationToSpawn, difficulty);
        }
    },
    WITCH {
        @Override
        public GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty) {
            return new Witch(plugin, gameManager.getWorld(), locationToSpawn);
        }
    };

    public abstract GameMob spawn(JavaPlugin plugin, GameManager gameManager, Vector locationToSpawn, Difficulty difficulty);
}
