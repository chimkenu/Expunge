package me.chimkenu.expunge.entities;

public interface RestorableState<T extends Restorable> {
    void restore(T entity);
}
