package me.chimkenu.expunge.entities.survivor;

import me.chimkenu.expunge.entities.Damageable;
import me.chimkenu.expunge.entities.RestorableState;
import me.chimkenu.expunge.entities.GameEntity;
import me.chimkenu.expunge.entities.Restorable;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.events.DeathEvent;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.items.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Survivor implements GameEntity, Damageable, Restorable {
    private final static int INCAP_DECAY = 20 * 10;
    private final static int ABSORPTION_DECAY = 20 * 15;
    private final static int SPRINT_THRESHOLD = 6;
    private final static int SLOW_THRESHOLD = 1;
    protected final static int MAX_LIVES = 3;
    public final static int MAX_HEALTH = 20;
    protected final static int REVIVE_HEALTH = 10;
    protected final static int TEMP_HEALTH = 6;

    private int lives;
    private boolean isAlive;
    private boolean isIncapacitated;

    protected Survivor(int lives, boolean isAlive, boolean isIncapacitated) {
        this.lives = lives;
        this.isAlive = isAlive;
        this.isIncapacitated = isIncapacitated;
    }

    protected Survivor() {
        this(MAX_LIVES, true, false);
    }

    public abstract Location getEyeLocation();
    public abstract Vector getEyeDirection();

    public int getLives() {
        return lives;
    }

    public void resetLives() {
        lives = MAX_LIVES;
    };

    public boolean isAlive() {
        return isAlive;
    }

    protected void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isIncapacitated() {
        return isIncapacitated;
    }

    protected void setIncapacitated(boolean isIncapacitated) {
        this.isIncapacitated = isIncapacitated;
    }

    public abstract void setCanSprint(boolean canSprint);

    public abstract Slot getActiveSlot();
    public abstract Optional<ItemStack> getItemSlot(Slot slot);
    public abstract void setItemSlot(Slot slot, @Nullable ItemStack item);
    public abstract int getCooldown(GameItem item);
    public abstract void setCooldown(GameItem item, int cooldown);
    public boolean hasCooldown(GameItem item) {
        return getCooldown(item) > 0;
    }
    public Optional<ItemStack> getActiveItem() {
        return getItemSlot(getActiveSlot());
    }

    public void down() {
        if (!isAlive()) {
            throw new RuntimeException(getIdentifier() + ": Invalid state for down(): Not alive");
        }

        if (isIncapacitated() || getLives() <= 1) {
            die();
            return;
        }

        setIncapacitated(true);
        setHealth(MAX_HEALTH);
        setAbsorption(0);
    }

    public void up() {
        if (!isAlive()) {
            throw new RuntimeException(getIdentifier() + ": Invalid state for up(): Not dead");
        }

        if (!isIncapacitated()) {
            throw new RuntimeException(getIdentifier() + ": Invalid state for up(): Not incapacitated");
        }

        setHealth(1);
        setAbsorption(TEMP_HEALTH);
        setIncapacitated(false);
        lives -= 1;
    }

    public void die() {
        if (!isAlive()) {
            throw new RuntimeException(getIdentifier() + ": Invalid state for die(): Not alive");
        }

        DeathEvent event = new DeathEvent(this);
        Bukkit.getPluginManager().callEvent(event);

        setHealth(MAX_HEALTH);
        setAbsorption(0);
        setAlive(false);
        setIncapacitated(false);
        lives = 0;
    }

    public void revive() {
        if (isAlive()) {
            throw new RuntimeException(getIdentifier() + ": Invalid state for revive(): Not dead");
        }

        setHealth(REVIVE_HEALTH);
        setAbsorption(0);
        Arrays.stream(Slot.values()).forEach(s -> setItemSlot(s, null));
        setAlive(true);
        setIncapacitated(false);
        resetLives();
    }

    public abstract void addEffect(PotionEffectType potionEffectType, int duration, int amplifier);
    public abstract void removeEffect(PotionEffectType potionEffectType);
    public abstract boolean hasEffect(PotionEffectType potionEffectType);
    public abstract void clearEffects();

    public void update(long time) {
        // slowly decay temporary hit points (absorption)
        if (getAbsorption() > 0) {
            if (time % (ABSORPTION_DECAY) == 0) setAbsorption(Math.max(0, getAbsorption() - 1));
        }
        if (isAlive() && isIncapacitated()) {
            if (time % (INCAP_DECAY) == 0) {
                setHealth(getHealth() - 1);
            }
        }

        // give slow when low on hp
        var totalHp = getHealth() + getAbsorption();
        if (totalHp <= SPRINT_THRESHOLD) {
            setCanSprint(false);
            if (totalHp <= SLOW_THRESHOLD) addEffect(PotionEffectType.SLOWNESS, 2, 0);
        } else {
            setCanSprint(true);
        }
    }

    @Override
    public RestorableState<Survivor> getState() {
        Map<Slot, ItemStack> hotbar = new HashMap<>();
        Arrays.stream(Slot.values()).forEach(s -> hotbar.put(s, getItemSlot(s).orElse(null)));

        return new State(
                getLocation().clone(),
                getHealth(),
                getAbsorption(),
                lives,
                isAlive,
                isIncapacitated,
                hotbar
        );
    }

    private record State(
            Location location,
            double health,
            double absorption,
            int lives,
            boolean isAlive,
            boolean isIncapacitated,
            Map<Slot, ItemStack> hotbar
    ) implements RestorableState<Survivor> {
        @Override
        public void restore(Survivor entity) {
            entity.setLocation(location);
            entity.setHealth(health);
            entity.setAbsorption(absorption);
            entity.lives = this.lives;
            entity.isAlive = this.isAlive;
            entity.setAlive(this.isAlive);
            entity.setIncapacitated(this.isIncapacitated);
            Arrays.stream(Slot.values()).forEach(slot -> entity.setItemSlot(slot, this.hotbar.get(slot)));
        }
    }
}
