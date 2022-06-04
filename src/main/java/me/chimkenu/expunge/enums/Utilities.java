package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.guns.utilities.*;

public class Utilities {
    public enum Throwable {
        MOLOTOV {
            public Utility getUtility() {
                return new Molotov();
            }
        };

        public abstract Utility getUtility();
    }

    public enum Healing {
        DEFIBRILLATOR {
            public Utility getUtility() {
                return new Defibrillator();
            }
            public boolean isMain() {
                return true;
            }
        },
        MEDKIT {
            public Utility getUtility() {
                return new Medkit();
            }
            public boolean isMain() {
                return true;
            }
        },

        PILLS {
            public Utility getUtility() {
                return new Pills();
            }
            public boolean isMain() {
                return false;
            }
        },
        ADRENALINE {
            public Utility getUtility() {
                return new Adrenaline();
            }
            public boolean isMain() {
                return false;
            }
        };

        public abstract Utility getUtility();
        public abstract boolean isMain();
    }
}
