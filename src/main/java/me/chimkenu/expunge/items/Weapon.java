package me.chimkenu.expunge.items;

public sealed interface Weapon extends GameItem
        permits Gun, Melee {
    double damage();
    int pierceNumber();
    int range();
}
