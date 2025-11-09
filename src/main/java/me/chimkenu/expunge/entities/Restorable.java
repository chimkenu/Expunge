package me.chimkenu.expunge.entities;

public interface Restorable {
    RestorableState<? extends Restorable> getState();
}
