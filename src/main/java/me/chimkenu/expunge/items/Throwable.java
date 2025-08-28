package me.chimkenu.expunge.items;

import me.chimkenu.expunge.enums.EffectType;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.data.BouncyProjectile;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public record Throwable(
        String id,
        String name,
        Material material,
        Tier tier,
        Slot slot,
        int swapCooldown,

        Material thrownItem,
        EffectType.Flight flightType,
        double throwVelocity,
        double bounceVelocity,
        int flightDuration,

        EffectType.Land landType,
        int effectDuration,
        Sound landSound,
        float landSoundPitch
) implements GameItem, Utility {
    public Throwable {
        if (slot == null) slot = Slot.TERTIARY;
    }

    @Override
    public boolean use(GameManager manager, LivingEntity thrower) {
        manager.getWorld().playSound(
                thrower, Sound.ENTITY_PLAYER_ATTACK_SWEEP,
                thrower instanceof Player ? SoundCategory.PLAYERS : SoundCategory.HOSTILE,
                0.5f, 0
        );
        var projectile = thrower.launchProjectile(Snowball.class);
        projectile.setItem(new ItemStack(thrownItem()));
        projectile.getPersistentDataContainer().set(namespacedKey(), PersistentDataType.BOOLEAN, bounceVelocity() == 0);
        flightType().trigger(manager, this, thrower, projectile);

        if (flightType != EffectType.Flight.ATTRACT && bounceVelocity() > 0) {
            manager.addTask(new BouncyProjectile(manager, this, thrower, projectile).runTaskTimer(manager.getPlugin(), 0, 1));
        }

        return true;
    }

    public void land(GameManager manager, Location loc, LivingEntity shooter) {
        landType().trigger(manager, loc, shooter, effectDuration());
    }
}
