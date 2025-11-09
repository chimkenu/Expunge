package me.chimkenu.expunge.items;

import me.chimkenu.expunge.entities.GameEntity;
import me.chimkenu.expunge.entities.survivor.Survivor;
import me.chimkenu.expunge.events.HealEvent;
import me.chimkenu.expunge.items.data.HealData;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.tasks.SurvivorTask;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Healing(
        String id,
        String name,
        Material material,
        Slot slot,
        Tier tier,
        int swapCooldown,

        HealData.Type healType,
        HealData.Modifier healModifier,
        double healthAmount,
        double absorptionAmount,
        int useTime,
        boolean hasToStayStill
) implements GameItem, Utility {
    public Healing {
        if (slot == null) slot = Slot.QUATERNARY;
    }
    
    private void heal(GameManager manager, Survivor survivor) {
        final double max = Survivor.MAX_HEALTH;
        var event = createHealEvent(survivor);
        Bukkit.getPluginManager().callEvent(event);

        var hp = Math.min(max, survivor.getHealth() + event.getHealth());
        var abs = Math.min(max, survivor.getAbsorption() + event.getAbsorption());

        survivor.setHealth(hp);
        survivor.setAbsorption(abs);

        switch (healType()) {
            case PERMANENT -> survivor.resetLives();
            case BOOST -> survivor.addEffect(PotionEffectType.SPEED, 20 * 15, 1);
        }
    }

    @Override
    public boolean use(GameManager manager, GameEntity entity) {
        if (!(entity instanceof Survivor survivor)) {
            return false;
        }

        if (healType() == HealData.Type.REVIVE) {
            useRevive(manager, survivor);
            return false;
        }

        if (useTime <= 0) {
            heal(manager, survivor);
            return true;
        }

        attemptUse(manager, survivor);
        return false;
    }

    public void attemptUse(GameManager manager, Survivor survivor) {
        var opt = survivor.getActiveItem();
        if (opt.isEmpty()) return;
        var itemstack = opt.get();
        survivor.setCooldown(itemstack.item(), useTime() + 1);
        manager.addTask(
                new SurvivorTask(survivor) {
                    final Location finalLoc = survivor.getLocation();
                    int progressTime = 0;
                    
                    @Override
                    public void run() {
                        String prefix = "&eUsing " + name() + "&e... ";
                        ChatUtil.sendActionBar(survivor, prefix + ChatUtil.progressBar(useTime, progressTime));

                        if (hasToStayStill() && finalLoc.distanceSquared(survivor.getLocation()) > 1) {
                            ChatUtil.sendActionBar(survivor, "&cStopped. &8(You moved)");
                            cancel();
                        }
                        var opt = survivor.getActiveItem();
                        if (opt.isEmpty() || !opt.get().stack().equals(itemstack.stack())) {
                            ChatUtil.sendActionBar(survivor, "&cStopped. ");
                            cancel();
                        }
                        if (progressTime >= useTime) {
                            ChatUtil.sendActionBar(survivor, "&aSuccessful.");
                            heal(manager, survivor);
                            itemstack.stack().setAmount(itemstack.stack().getAmount() - 1);
                            cancel();
                        }

                        progressTime++;
                    }
                    
                    @Override
                    public void cancel() {
                        super.cancel();
                        survivor.setCooldown(itemstack.item(), 0);
                    }

                }.runTaskTimer(manager.getPlugin(), 1, 1)
        );
    }

    @NotNull
    private HealEvent createHealEvent(Survivor survivor) {
        int health = 0;
        int absorption = 0;

        switch (healModifier()) {
            case ABSOLUTE -> {
                health = (int) healthAmount();
                absorption = (int) absorptionAmount();
            }
            case RELATIVE -> {
                health = (int) ((Survivor.MAX_HEALTH - survivor.getHealth()) * healthAmount());
                absorption = (int) ((Survivor.MAX_HEALTH - survivor.getAbsorption()) * absorptionAmount());
            }
        }

        return new HealEvent(survivor, health, absorption);
    }

    private void useRevive(GameManager manager, Survivor survivor) {
        var opt = survivor.getActiveItem();
        if (opt.isEmpty()) return;
        var itemStack = opt.get();

        if (this.material.equals(itemStack.item().material()) || survivor.getCooldown(itemStack.item()) > 0) {
            return;
        }

        if (!manager.getSurvivors().contains(survivor)) {
            return;
        }
        if (!survivor.isAlive()) {
            return;
        }

        for (Entity e : survivor.getHandle().getNearbyEntities(1, 1, 1)) {
            if (!(e instanceof ArmorStand armorStand)) {
                continue;
            }
            Player target = null;
            for (String string : armorStand.getScoreboardTags()) {
                try {
                    UUID uuid = UUID.fromString(string);
                    target = Bukkit.getPlayer(uuid);
                    if (target != null) break;
                } catch (IllegalArgumentException ignored) {}
            }
            if (target == null || !target.isOnline()) {
                continue;
            }
            var targetSurvivor = manager.getSurvivor(target).orElse(null);
            if (targetSurvivor == null) {
                continue;
            }
            if (!targetSurvivor.isAlive()) {
                continue;
            }
            if (targetSurvivor.getLives() > 1) {
                continue;
            }
            if (!target.hasPotionEffect(PotionEffectType.GLOWING)) {
                ChatUtil.sendActionBar(survivor, "&b" + target.getDisplayName() + "&e is already being revived.");
                continue;
            }

            target.removePotionEffect(PotionEffectType.GLOWING);

            manager.addTask(
                    new SurvivorTask(survivor) {
                        final Location finalLoc = survivor.getLocation();
                        final ItemStack item = survivor.getActiveItem().orElse(null);
                        int progressTime = 0;

                        @Override
                        public void run() {
                            String prefix = "&eReviving" + targetSurvivor.getHandle().getName() + "&e... ";
                            ChatUtil.sendActionBar(survivor, prefix + ChatUtil.progressBar(useTime, progressTime));

                            if (hasToStayStill() && finalLoc.distanceSquared(survivor.getLocation()) > 1) {
                                ChatUtil.sendActionBar(survivor, "&cStopped. &8(You moved)");
                                cancel();
                            }
                            var opt = survivor.getActiveItem();
                            if (opt.isEmpty() || !opt.get().stack().equals(item.stack())) {
                                ChatUtil.sendActionBar(survivor, "&cStopped. ");
                                cancel();
                            }
                            if (progressTime >= useTime) {
                                ChatUtil.sendActionBar(survivor, "&aSuccessful.");
                                success();
                                cancel();
                            }

                            progressTime++;
                        }

                        @Override
                        public void cancel() {
                            super.cancel();
                            survivor.setCooldown(item.item(), 0);
                            targetSurvivor.addEffect(PotionEffectType.GLOWING, 10000000, 0);
                        }

                        private void success() {
                            var opt = survivor.getActiveItem();
                            if (opt.isEmpty()) return;
                            var item = opt.get().stack();

                            item.setAmount(item.getAmount() - 1);
                            targetSurvivor.revive();
                            targetSurvivor.setLocation(survivor.getLocation());
                            for (String string : armorStand.getScoreboardTags()) {
                                try {
                                    UUID uuid = UUID.fromString(string);
                                    if (Bukkit.getEntity(uuid) instanceof ArmorStand a) a.remove();
                                } catch (IllegalArgumentException ignored) {}
                            }
                        }

                    }.runTaskTimer(manager.getPlugin(), 1, 1)
            );

            return;
        }

        ChatUtil.sendActionBar(survivor, "&cNo dead player nearby.");
    }
}
