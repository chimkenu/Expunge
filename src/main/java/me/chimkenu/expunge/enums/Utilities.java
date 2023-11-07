package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.items.utilities.healing.*;
import me.chimkenu.expunge.items.utilities.throwable.Bile;
import me.chimkenu.expunge.items.utilities.throwable.FreshAir;
import me.chimkenu.expunge.items.utilities.throwable.Molotov;
import me.chimkenu.expunge.items.utilities.throwable.Throwable;

public class Utilities {
    public enum Throwables {
        MOLOTOV {
            public Throwable getUtility() {
                return new Molotov();
            }
        },
        FRESH_AIR {
            public Throwable getUtility() {
                return new FreshAir();
            }
        },
        BILE {
            public Throwable getUtility() {
                return new Bile();
            }
        };

        public abstract Throwable getUtility();
    }

    public enum Healings {
        DEFIBRILLATOR {
            public Healing getUtility() {
                return new Defibrillator();
            }
        },
        MEDKIT {
            public Healing getUtility() {
                return new Medkit();
            }
        },

        PILLS {
            public Healing getUtility() {
                return new Pills();
            }
        },
        ADRENALINE {
            public Healing getUtility() {
                return new Adrenaline();
            }
        };

        public abstract Healing getUtility();
    }
}
