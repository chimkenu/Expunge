package me.chimkenu.expunge.mobs;

import me.chimkenu.expunge.game.GameManager;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftEntity;
import org.bukkit.entity.Mob;

import java.util.EnumSet;
import java.util.Objects;

public abstract class MobGoal extends Goal {
    protected final GameManager manager;
    protected final Mob mob;
    protected final MobSettings settings;
    protected int cooldown;
    protected int attackFrameTicks;
    protected int executionTick;
    protected Runnable pendingAttack;

    public MobGoal(GameManager manager, Mob mob, MobSettings settings) {
        this.manager = manager;
        this.mob = mob;
        this.settings = settings;
        this.setFlags(EnumSet.of(Flag.MOVE));

        if (settings != null) {
            Objects.requireNonNull(mob.getAttribute(Attribute.ATTACK_DAMAGE)).setBaseValue(settings.damage());
            Objects.requireNonNull(mob.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(settings.health());
            mob.setHealth(settings.health());
        }

        var entity = ((CraftEntity) mob).getHandle();
        if (!(entity instanceof PathfinderMob pathfinderMob)) {
            throw new RuntimeException("not a pathfinder mob");
        }

        pathfinderMob.goalSelector.addGoal(0, this);
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        return mob.getTarget() != null;
    }

    @Override
    public void start() {
        /*  SAMPLE CODE
            LivingEntity target = mob.getTarget();
            if (target == null) return;

            if (distance > 10) {
                attackFrameTicks = 10;
                executionTick = 10;
                pendingAttack = () -> rockThrow(mob, target);
                playTellAnimation("rock_throw");
            }
         */
    }

    @Override
    public boolean canContinueToUse() {
        return attackFrameTicks > 0;
    }

    @Override
    public void tick() {
        if (attackFrameTicks > 0) {
            if (attackFrameTicks == executionTick && pendingAttack != null) {
                pendingAttack.run();
                pendingAttack = null;
            }
            attackFrameTicks--;
        }
    }

    @Override
    public void stop() {
        cooldown = settings.cooldown();
    }

    protected void playTellAnimation(String type) {
        /* SAMPLE CODE
           mob.playEffect(EntityEffect.IRON_GOLEN_ATTACK);
           Bukkit.broadcastMessage("ambatu " + type);
        */
    }

    protected void playJingle() {}
}
