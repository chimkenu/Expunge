package me.chimkenu.expunge.entities;

public interface Regenerable {
    RegenerableState<? extends Regenerable> getState();
}
