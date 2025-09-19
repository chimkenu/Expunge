package me.chimkenu.expunge.campaigns;

import org.bukkit.Material;
import org.bukkit.util.Vector;

public record Barrier(Vector position, Material type, boolean isInit) {
    @Override
    public boolean equals(Object o) {
        if (o instanceof Barrier b) {
            return this.position.equals(b.position);
        } else if (o instanceof Vector v) {
            return this.position.equals(v);
        }
        return false;
    }
}
