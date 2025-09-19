package me.chimkenu.expunge.mobs;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.mobs.common.Common;
import me.chimkenu.expunge.mobs.special.*;
import me.chimkenu.expunge.mobs.special.Witch;
import me.chimkenu.expunge.mobs.common.Robot;
import me.chimkenu.expunge.mobs.common.Soldier;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public enum MobType {
    COMMON {
        @Override
        public Class<? extends Mob> defaultMob() {
            return Zombie.class;
        }

        @Override
        public Class<? extends MobGoal> goal() {
            return Common.class;
        }
    },
    ROBOT {
        @Override
        public Class<? extends Mob> defaultMob() {
            return Zombie.class;
        }

        @Override
        public Class<? extends MobGoal> goal() {
            return Robot.class;
        }
    },
    SOLDIER {
        @Override
        public Class<? extends Mob> defaultMob() {
            return Zombie.class;
        }

        @Override
        public Class<? extends MobGoal> goal() {
            return Soldier.class;
        }
    },
    CHARGER {
        @Override
        public Class<? extends Mob> defaultMob() {
            return Zoglin.class;
        }

        @Override
        public Class<? extends MobGoal> goal() {
            return Charger.class;
        }
    },
    CHOKER {
        @Override
        public Class<? extends Mob> defaultMob() {
            return Husk.class;
        }

        @Override
        public Class<? extends MobGoal> goal() {
            return Choker.class;
        }
    },
    POUNCER {
        @Override
        public Class<? extends Mob> defaultMob() {
            return Stray.class;
        }

        @Override
        public Class<? extends MobGoal> goal() {
            return Pouncer.class;
        }
    },
    RIDER {
        @Override
        public Class<? extends Mob> defaultMob() {
            return Spider.class;
        }

        @Override
        public Class<? extends MobGoal> goal() {
            return Rider.class;
        }
    },
    SPEWER {
        @Override
        public Class<? extends Mob> defaultMob() {
            return Creeper.class;
        }

        @Override
        public Class<? extends MobGoal> goal() {
            return Spewer.class;
        }
    },
    SPITTER {
        @Override
        public Class<? extends Mob> defaultMob() {
            return ZombieVillager.class;
        }

        @Override
        public Class<? extends MobGoal> goal() {
            return Spitter.class;
        }
    },
    TANK {
        @Override
        public Class<? extends Mob> defaultMob() {
            return IronGolem.class;
        }

        @Override
        public Class<? extends MobGoal> goal() {
            return Tank.class;
        }
    },
    WITCH {
        @Override
        public Class<? extends Mob> defaultMob() {
            return Enderman.class;
        }

        @Override
        public Class<? extends MobGoal> goal() {
            return Witch.class;
        }
    };

    public Mob spawn(GameManager manager, Vector locationToSpawn, Class<? extends Mob> mobToSpawn, MobSettings settings) {
        var mob = manager.getWorld().spawn(locationToSpawn.toLocation(manager.getWorld()), mobToSpawn);
        mob.setCanPickupItems(false);
        mob.setRemoveWhenFarAway(false);
        try {
            goal().getDeclaredConstructor(GameManager.class, Mob.class, MobSettings.class).newInstance(manager, mob, settings);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return mob;
    }

    public Mob spawn(GameManager manager, Vector locationToSpawn, MobSettings settings) {
        return spawn(manager, locationToSpawn, this.defaultMob(), settings);
    }

    public abstract Class<? extends Mob> defaultMob();
    public abstract Class<? extends MobGoal> goal();
}
