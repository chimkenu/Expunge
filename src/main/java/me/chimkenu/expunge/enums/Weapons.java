package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.guns.guns.*;
import me.chimkenu.expunge.guns.melees.*;

public class Weapons {
    public enum Guns {
        COMBAT_RIFLE {
            @Override
            public Gun getGun() {
                return new CombatRifle();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER2;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        HUNTING_RIFLE {
            @Override
            public Gun getGun() {
                return new HuntingRifle();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER2;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        M16_ASSAULT_RIFLE {
            @Override
            public Gun getGun() {
                return new M16AssaultRifle();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER2;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        AK47 {
            @Override
            public Gun getGun() {
                return new AK47();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER2;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        PUMP_SHOTGUN {
            @Override
            public Gun getGun() {
                return new PumpShotgun();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER1;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        CHROME_SHOTGUN {
            @Override
            public Gun getGun() {
                return new ChromeShotgun();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER1;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        AUTO_SHOTGUN {
            @Override
            public Gun getGun() {
                return new AutoShotgun();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER2;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        COMBAT_SHOTGUN {
            @Override
            public Gun getGun() {
                return new CombatShotgun();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER2;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        SNIPER_RIFLE {
            @Override
            public Gun getGun() {
                return new SniperRifle();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER2;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        AWP {
            @Override
            public Gun getGun() {
                return new AWP();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER2;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        SCOUT {
            @Override
            public Gun getGun() {
                return new Scout();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER2;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        SMG {
            @Override
            public Gun getGun() {
                return new SMG();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER1;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        MP5 {
            @Override
            public Gun getGun() {
                return new MP5();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER1;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        GRENADE_LAUNCHER {
            @Override
            public Gun getGun() {
                return new GrenadeLauncher();
            }

            @Override
            public Tier getTier() {
                return Tier.SPECIAL;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        M60_MACHINE_GUN {
            @Override
            public Gun getGun() {
                return new M60MachineGun();
            }

            @Override
            public Tier getTier() {
                return Tier.SPECIAL;
            }

            @Override
            public Slot getSlot() {
                return Slot.PRIMARY;
            }
        },
        PISTOL {
            @Override
            public Gun getGun() {
                return new Pistol();
            }

            @Override
            public Tier getTier() {
                return Tier.TIER1;
            }

            @Override
            public Slot getSlot() {
                return Slot.SECONDARY;
            }
        };

        public abstract Gun getGun();
        public abstract Tier getTier();
        public abstract Slot getSlot();
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
        };

        public abstract Melee getMelee();
    }

    public enum Tier {
        TIER1,
        TIER2,
        SPECIAL
    }

    public enum Slot {
        PRIMARY,
        SECONDARY
    }
}
