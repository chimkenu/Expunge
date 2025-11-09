package me.chimkenu.expunge.entities;

public interface Damageable {
    double getHealth();
    void setHealth(double health);
    double getAbsorption();
    void setAbsorption(double absorption);
    default double getTotalHealth() {
        return getHealth() + getAbsorption();
    }
}
