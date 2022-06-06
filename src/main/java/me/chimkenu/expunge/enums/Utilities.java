package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.guns.utilities.throwable.Molotov;
import me.chimkenu.expunge.guns.utilities.throwable.Throwable;
import me.chimkenu.expunge.guns.utilities.healing.*;

public class Utilities {
    public enum Throwables {
        MOLOTOV {
            public Throwable getUtility() {
                return new Molotov();
            }
        };

        public abstract Throwable getUtility();
    }

    public enum Healings {
        DEFIBRILLATOR {
            public Healing getUtility() {
                return new Defibrillator();
            }
            public boolean isMain() {
                return true;
            }
        },
        MEDKIT {
            public Healing getUtility() {
                return new Medkit();
            }
            public boolean isMain() {
                return true;
            }
        },

        PILLS {
            public Healing getUtility() {
                return new Pills();
            }
            public boolean isMain() {
                return false;
            }
        },
        ADRENALINE {
            public Healing getUtility() {
                return new Adrenaline();
            }
            public boolean isMain() {
                return false;
            }
        };

        public abstract Healing getUtility();
        public abstract boolean isMain();
    }
}
