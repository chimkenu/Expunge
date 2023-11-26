package me.chimkenu.expunge.items.interactables;

import me.chimkenu.expunge.game.GameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OxygenTank extends Explosive {
    public OxygenTank() {
        super("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTVkYjM2YmFlOWExNTFlZGI4YmY4ZjhiMDY3NDEyMjFlYTE2M2ZmOTExYWFhZTNhNDJlMTgxMTgzZDdjYmRmNSJ9fX0=", Component.text("Oxygen Tank", NamedTextColor.GREEN));
    }

    @Override
    public void onInteract(JavaPlugin plugin, GameManager gameManager, Entity entity, Entity actor) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            new PropaneTank().onInteract(plugin, gameManager, entity, actor);
            entity.remove();
            return;
        }

        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_CREEPER_PRIMED, SoundCategory.BLOCKS, 0.5f, 0f);
        new BukkitRunnable() {
            int t = 20 * 3;
            @Override
            public void run() {
                if (!gameManager.isRunning() || entity.isDead()) {
                    this.cancel();
                }
                for (Entity e : entity.getNearbyEntities(5, 5, 5)) {
                    if (e instanceof Zombie zombie) {
                        zombie.setTarget(livingEntity);
                    }
                }
                if (t <= 0) {
                    new PropaneTank().onInteract(plugin, gameManager, entity, actor);
                    this.cancel();
                }
                t--;
            }
        } .runTaskTimer(plugin, 0, 1);
    }

    @Override
    public String getTag() {
        return "INTERACTABLE_OXYGEN_TANK";
    }
}