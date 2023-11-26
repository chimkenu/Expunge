package me.chimkenu.expunge.items.interactables;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.utilities.throwable.Molotov;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class FuelCan extends Explosive {
    public FuelCan() {
        super("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTRiZmMwYWEwY2QyYTFhNDQ2N2FhODUwZmIxZjBhNjNjMGExYTZmY2I5Y2E3NTkxYTAyMjUyNTFhODgzYTY3NiJ9fX0=", Component.text("Fuel Can", NamedTextColor.RED));
    }

    @Override
    public void onInteract(JavaPlugin plugin, GameManager gameManager, Entity entity, Entity actor) {
        Molotov molotov = new Molotov();
        Location location = entity.getLocation().add(0, 1.4, 0);
        molotov.fire(plugin, gameManager.getWorld(), location, actor, ThreadLocalRandom.current().nextInt(15 * 20));
        entity.remove();
        for (int i = 0; i < 6; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!gameManager.isRunning()) {
                        this.cancel();
                    }
                    molotov.fire(plugin, gameManager.getWorld(), location.clone().add(ThreadLocalRandom.current().nextDouble(-3, 3), 0, ThreadLocalRandom.current().nextDouble(-3, 3)), actor, ThreadLocalRandom.current().nextInt(15 * 20));
                }
            }.runTaskLater(plugin, i * 6 + 5 * ThreadLocalRandom.current().nextInt(4));
        }
    }

    @Override
    public String getTag() {
        return "INTERACTABLE_FUEL_CAN";
    }
}
