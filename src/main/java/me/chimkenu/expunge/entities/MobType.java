package me.chimkenu.expunge.entities;

import me.chimkenu.expunge.entities.goals.MobGoal;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.entities.goals.common.Common;
import me.chimkenu.expunge.entities.goals.special.*;
import me.chimkenu.expunge.entities.goals.special.Witch;
import me.chimkenu.expunge.entities.goals.common.Robot;
import me.chimkenu.expunge.entities.goals.common.Soldier;
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

        @Override
        public Classification classification() {
            return Classification.COMMON;
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

        @Override
        public Classification classification() {
            return Classification.UNCOMMON;
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

        @Override
        public Classification classification() {
            return Classification.UNCOMMON;
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

        @Override
        public Classification classification() {
            return Classification.SPECIAL;
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

        @Override
        public Classification classification() {
            return Classification.SPECIAL;
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

        @Override
        public Classification classification() {
            return Classification.SPECIAL;
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

        @Override
        public Classification classification() {
            return Classification.SPECIAL;
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

        @Override
        public Classification classification() {
            return Classification.SPECIAL;
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

        @Override
        public Classification classification() {
            return Classification.SPECIAL;
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

        @Override
        public Classification classification() {
            return Classification.BOSS;
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

        @Override
        public Classification classification() {
            return Classification.BOSS;
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
    public abstract Classification classification();

    public enum Classification {
        COMMON,
        UNCOMMON,
        SPECIAL,
        BOSS
    }
}
