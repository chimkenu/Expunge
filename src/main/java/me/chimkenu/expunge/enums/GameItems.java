package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.items.utilities.healing.*;
import me.chimkenu.expunge.items.utilities.throwable.Bile;
import me.chimkenu.expunge.items.utilities.throwable.FreshAir;
import me.chimkenu.expunge.items.utilities.throwable.Grenade;
import me.chimkenu.expunge.items.utilities.throwable.Molotov;
import me.chimkenu.expunge.items.weapons.guns.*;
import me.chimkenu.expunge.items.weapons.melees.*;

import java.util.Arrays;
import java.util.List;

public enum GameItems {
    COMBAT_RIFLE {
        @Override
        public GameItem getGameItem() {
            return new CombatRifle();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    HUNTING_RIFLE {
        @Override
        public GameItem getGameItem() {
            return new HuntingRifle();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    M16_ASSAULT_RIFLE {
        @Override
        public GameItem getGameItem() {
            return new M16AssaultRifle();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    AK47 {
        @Override
        public GameItem getGameItem() {
            return new AK47();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    PUMP_SHOTGUN {
        @Override
        public GameItem getGameItem() {
            return new PumpShotgun();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    CHROME_SHOTGUN {
        @Override
        public GameItem getGameItem() {
            return new ChromeShotgun();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    AUTO_SHOTGUN {
        @Override
        public GameItem getGameItem() {
            return new AutoShotgun();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    COMBAT_SHOTGUN {
        @Override
        public GameItem getGameItem() {
            return new CombatShotgun();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    SNIPER_RIFLE {
        @Override
        public GameItem getGameItem() {
            return new SniperRifle();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    AWP {
        @Override
        public GameItem getGameItem() {
            return new AWP();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    SCOUT {
        @Override
        public GameItem getGameItem() {
            return new Scout();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    SMG {
        @Override
        public GameItem getGameItem() {
            return new SMG();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    MP5 {
        @Override
        public GameItem getGameItem() {
            return new MP5();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    GRENADE_LAUNCHER {
        @Override
        public GameItem getGameItem() {
            return new GrenadeLauncher();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    M60_MACHINE_GUN {
        @Override
        public GameItem getGameItem() {
            return new M60MachineGun();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    PISTOL {
        @Override
        public GameItem getGameItem() {
            return new Pistol();
        }

        @Override
        public Type getType() {
            return Type.GUN;
        }
    },
    FIRE_AXE {
        public GameItem getGameItem() {
            return new FireAxe();
        }

        @Override
        public Type getType() {
            return Type.MELEE;
        }
    },
    BASEBALL_BAT {
        @Override
        public GameItem getGameItem() {
            return new BaseballBat();
        }

        @Override
        public Type getType() {
            return Type.MELEE;
        }
    },
    CROWBAR {
        @Override
        public GameItem getGameItem() {
            return new Crowbar();
        }

        @Override
        public Type getType() {
            return Type.MELEE;
        }
    },
    FRYING_PAN {
        @Override
        public GameItem getGameItem() {
            return new FryingPan();
        }

        @Override
        public Type getType() {
            return Type.MELEE;
        }
    },
    MACHETE {
        @Override
        public GameItem getGameItem() {
            return new Machete();
        }

        @Override
        public Type getType() {
            return Type.MELEE;
        }
    },
    NIGHTSTICK {
        @Override
        public GameItem getGameItem() {
            return new Nightstick();
        }

        @Override
        public Type getType() {
            return Type.MELEE;
        }
    },
    PITCHFORK {
        @Override
        public GameItem getGameItem() {
            return new Pitchfork();
        }

        @Override
        public Type getType() {
            return Type.MELEE;
        }
    },
    SHOVEL {
        @Override
        public GameItem getGameItem() {
            return new Shovel();
        }

        @Override
        public Type getType() {
            return Type.MELEE;
        }
    },
    KNIFE {
        @Override
        public GameItem getGameItem() {
            return new Knife();
        }

        @Override
        public Type getType() {
            return Type.MELEE;
        }
    },
    CHAINSAW {
        @Override
        public GameItem getGameItem() {
            return new Chainsaw();
        }

        @Override
        public Type getType() {
            return Type.MELEE;
        }
    },
    MR_COOKIE {
        @Override
        public GameItem getGameItem() {
            return new MrCookie();
        }

        @Override
        public Type getType() {
            return Type.MELEE;
        }
    },
    GRENADE {
        @Override
        public GameItem getGameItem() {
            return new Grenade();
        }

        @Override
        public Type getType() {
            return Type.THROWABLE;
        }
    },
    MOLOTOV {
        @Override
        public GameItem getGameItem() {
            return new Molotov();
        }

        @Override
        public Type getType() {
            return Type.THROWABLE;
        }
    },
    FRESH_AIR {
        @Override
        public GameItem getGameItem() {
            return new FreshAir();
        }

        @Override
        public Type getType() {
            return Type.THROWABLE;
        }
    },
    BILE {
        @Override
        public GameItem getGameItem() {
            return new Bile();
        }

        @Override
        public Type getType() {
            return Type.THROWABLE;
        }
    },
    DEFIBRILLATOR {
        @Override
        public GameItem getGameItem() {
            return new Defibrillator();
        }

        @Override
        public Type getType() {
            return Type.HEALING;
        }
    },
    MEDKIT {
        @Override
        public GameItem getGameItem() {
            return new Medkit();
        }

        @Override
        public Type getType() {
            return Type.HEALING;
        }
    },

    PILLS {
        @Override
        public GameItem getGameItem() {
            return new Pills();
        }

        @Override
        public Type getType() {
            return Type.HEALING;
        }
    },
    ADRENALINE {
        @Override
        public GameItem getGameItem() {
            return new Adrenaline();
        }

        @Override
        public Type getType() {
            return Type.HEALING;
        }
    };

    public abstract Type getType();
    public abstract GameItem getGameItem();

    public static List<GameItems> getWeapons() {
        return Arrays.stream(values()).filter(gameItems -> gameItems.getType() == Type.GUN || gameItems.getType() == Type.MELEE).toList();
    }

    public static List<GameItems> getGuns() {
        return Arrays.stream(values()).filter(gameItems -> gameItems.getType() == Type.GUN).toList();
    }

    public static List<GameItems> getMelees() {
        return Arrays.stream(values()).filter(gameItems -> gameItems.getType() == Type.MELEE).toList();
    }

    public static List<GameItems> getUtilities() {
        return Arrays.stream(values()).filter(gameItems -> gameItems.getType() == Type.THROWABLE || gameItems.getType() == Type.HEALING).toList();
    }

    public static List<GameItems> getThrowables() {
        return Arrays.stream(values()).filter(gameItems -> gameItems.getType() == Type.THROWABLE).toList();
    }

    public static List<GameItems> getHealings() {
        return Arrays.stream(values()).filter(gameItems -> gameItems.getType() == Type.HEALING).toList();
    }

    public enum Type {
        GUN,
        MELEE,
        THROWABLE,
        HEALING
    }
}
