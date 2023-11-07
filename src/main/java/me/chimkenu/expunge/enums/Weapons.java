package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.items.weapons.guns.*;
import me.chimkenu.expunge.items.weapons.melees.*;

public class Weapons {
    public enum Guns {
        COMBAT_RIFLE {
            @Override
            public Gun getGun() {
                return new CombatRifle();
            }
        },
        HUNTING_RIFLE {
            @Override
            public Gun getGun() {
                return new HuntingRifle();
            }
        },
        M16_ASSAULT_RIFLE {
            @Override
            public Gun getGun() {
                return new M16AssaultRifle();
            }
        },
        AK47 {
            @Override
            public Gun getGun() {
                return new AK47();
            }
        },
        PUMP_SHOTGUN {
            @Override
            public Gun getGun() {
                return new PumpShotgun();
            }
        },
        CHROME_SHOTGUN {
            @Override
            public Gun getGun() {
                return new ChromeShotgun();
            }
        },
        AUTO_SHOTGUN {
            @Override
            public Gun getGun() {
                return new AutoShotgun();
            }
        },
        COMBAT_SHOTGUN {
            @Override
            public Gun getGun() {
                return new CombatShotgun();
            }
        },
        SNIPER_RIFLE {
            @Override
            public Gun getGun() {
                return new SniperRifle();
            }
        },
        AWP {
            @Override
            public Gun getGun() {
                return new AWP();
            }
        },
        SCOUT {
            @Override
            public Gun getGun() {
                return new Scout();
            }
        },
        SMG {
            @Override
            public Gun getGun() {
                return new SMG();
            }
        },
        MP5 {
            @Override
            public Gun getGun() {
                return new MP5();
            }
        },
        GRENADE_LAUNCHER {
            @Override
            public Gun getGun() {
                return new GrenadeLauncher();
            }
        },
        M60_MACHINE_GUN {
            @Override
            public Gun getGun() {
                return new M60MachineGun();
            }
        },
        PISTOL {
            @Override
            public Gun getGun() {
                return new Pistol();
            }
        };

        public abstract Gun getGun();
    }

    public enum Melees {
        FIRE_AXE {
            public Melee getMelee() {
                return new FireAxe();
            }
        },
        BASEBALL_BAT {
            @Override
            public Melee getMelee() {
                return new BaseballBat();
            }
        },
        CROWBAR {
            @Override
            public Melee getMelee() {
                return new Crowbar();
            }
        },
        FRYING_PAN {
            @Override
            public Melee getMelee() {
                return new FryingPan();
            }
        },
        MACHETE {
            @Override
            public Melee getMelee() {
                return new Machete();
            }
        },
        NIGHTSTICK {
            @Override
            public Melee getMelee() {
                return new Nightstick();
            }
        },
        PITCHFORK {
            @Override
            public Melee getMelee() {
                return new Pitchfork();
            }
        },
        SHOVEL {
            @Override
            public Melee getMelee() {
                return new Shovel();
            }
        },
        KNIFE {
            @Override
            public Melee getMelee() {
                return new Knife();
            }
        },
        CHAINSAW {
            @Override
            public Melee getMelee() {
                return new Chainsaw();
            }
        },
        MR_COOKIE {
            @Override
            public Melee getMelee() {
                return new MrCookie();
            }
        };

        public abstract Melee getMelee();
    }
}
