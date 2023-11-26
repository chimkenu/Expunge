package me.chimkenu.expunge.items.interactables;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.utilities.throwable.Grenade;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public class PropaneTank extends Explosive {
    public PropaneTank() {
        super("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZmY2ViYzVjZDM0N2UzMDNlNGUzYTU5NDhkMGIyNDdiMGRiY2M0MTBhYmFmODlkMzY3OTM4ZmYxMTM0Njk1In19fQ==", Component.text("Propane Tank", NamedTextColor.WHITE));
    }

    @Override
    public void onInteract(JavaPlugin plugin, GameManager gameManager, Entity entity, Entity actor) {
        Location location = entity.getLocation().add(0, 1.4, 0);
        new Grenade().onLand(plugin, entity.getWorld(), location, actor);
        entity.getWorld().spawnParticle(Particle.LAVA, location, 30, 0.5, 0.5, 0.5, 1);
        entity.remove();
    }

    @Override
    public String getTag() {
        return "INTERACTABLE_PROPANE_TANK";
    }
}
