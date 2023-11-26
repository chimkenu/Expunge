package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.items.interactables.FuelCan;
import me.chimkenu.expunge.items.interactables.Interactable;
import me.chimkenu.expunge.items.interactables.OxygenTank;
import me.chimkenu.expunge.items.interactables.PropaneTank;

public enum Interactables {
    FUEL_CAN {
        @Override
        public Interactable get() {
            return new FuelCan();
        }
    },
    OXYGEN_TANK {
        @Override
        public Interactable get() {
            return new OxygenTank();
        }
    },
    PROPANE_TANK {
        @Override
        public Interactable get() {
            return new PropaneTank();
        }
    };

    public abstract Interactable get();
}
