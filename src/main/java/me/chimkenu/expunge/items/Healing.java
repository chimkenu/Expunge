package me.chimkenu.expunge.items;

import me.chimkenu.expunge.events.HealEvent;
import me.chimkenu.expunge.items.data.HealData;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.tasks.PlayerTask;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
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
    
    private void heal(GameManager manager, Player player) {
        final double max = Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).getValue();
        var event = createHealEvent(player, max);
        Bukkit.getPluginManager().callEvent(event);

        var hp = Math.min(max, player.getHealth() + event.getHealth());
        var abs = Math.min(max, player.getAbsorptionAmount() + event.getAbsorption());

        player.setHealth(hp);
        Objects.requireNonNull(player.getAttribute(Attribute.MAX_ABSORPTION)).setBaseValue(20);
        player.setAbsorptionAmount(abs);

        switch (healType()) {
            case PERMANENT -> manager.getPlayerStat(player).resetLives();
            case BOOST -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 1, false, true, true));
        }
    }

    @Override
    public boolean use(GameManager manager, LivingEntity entity) {
        if (!(entity instanceof  Player player)) {
            return false;
        }

        if (healType() == HealData.Type.REVIVE) {
            useRevive(manager, player);
            return false;
        }

        if (useTime <= 0) {
            heal(manager, player);
            return true;
        }

        attemptUse(manager, player);
        return false;
    }

    public void attemptUse(GameManager manager, Player player) {
        final ItemStack item = player.getInventory().getItemInMainHand();
        player.setCooldown(item, useTime() + 1);
        manager.addTask(
                new PlayerTask(player) {
                    final Location finalLoc = player.getLocation();
                    int progressTime = 0;
                    
                    @Override
                    public void run() {
                        String prefix = "&eUsing " + name() + "&e... ";
                        ChatUtil.sendActionBar(player, prefix + ChatUtil.progressBar(useTime, progressTime));

                        if (hasToStayStill() && finalLoc.distanceSquared(player.getLocation()) > 1) {
                            ChatUtil.sendActionBar(player, "&cStopped. &8(You moved)");
                            cancel();
                        }
                        if (!player.getInventory().getItemInMainHand().equals(item)) {
                            ChatUtil.sendActionBar(player, "&cStopped. ");
                            cancel();
                        }
                        if (progressTime >= useTime) {
                            ChatUtil.sendActionBar(player, "&aSuccessful.");
                            heal(manager, player);
                            item.setAmount(item.getAmount() - 1);
                            cancel();
                        }

                        progressTime++;
                    }
                    
                    @Override
                    public void cancel() {
                        super.cancel();
                        player.setCooldown(item, 0);
                    }

                }.runTaskTimer(manager.getPlugin(), 1, 1)
        );
    }

    @NotNull
    private HealEvent createHealEvent(Player player, double max) {
        int health = 0;
        int absorption = 0;

        switch (healModifier()) {
            case ABSOLUTE -> {
                health = (int) healthAmount();
                absorption = (int) absorptionAmount();
            }
            case RELATIVE -> {
                health = (int) ((max - player.getHealth()) * healthAmount());
                absorption = (int) ((max - player.getAbsorptionAmount()) * absorptionAmount());
            }
        }

        return new HealEvent(player, health, absorption);
    }

    private void useRevive(GameManager manager, Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!toItem().getType().equals(item.getType()) || player.getCooldown(toItem().getType()) > 0) {
            return;
        }

        if (!manager.getPlayers().contains(player)) {
            return;
        }
        if (!manager.getPlayerStat(player).isAlive()) {
            return;
        }

        for (Entity e : player.getNearbyEntities(1, 1, 1)) {
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
            if (!manager.getPlayers().contains(target)) {
                continue;
            }
            if (manager.getPlayerStat(target).isAlive()) {
                continue;
            }
            if (manager.getPlayerStat(target).getLives() > 1) {
                continue;
            }
            if (!target.hasPotionEffect(PotionEffectType.GLOWING)) {
                ChatUtil.sendActionBar(player, "&b" + target.getDisplayName() + "&e is already being revived.");
                continue;
            }

            target.removePotionEffect(PotionEffectType.GLOWING);
            Player finalTarget = target;

            manager.addTask(
                    new PlayerTask(player) {
                        final Location finalLoc = player.getLocation();
                        final ItemStack item = player.getInventory().getItemInMainHand();
                        int progressTime = 0;

                        @Override
                        public void run() {
                            String prefix = "&eReviving" + finalTarget.getName() + "&e... ";
                            ChatUtil.sendActionBar(player, prefix + ChatUtil.progressBar(useTime, progressTime));

                            if (hasToStayStill() && finalLoc.distanceSquared(player.getLocation()) > 1) {
                                ChatUtil.sendActionBar(player, "&cStopped. &8(You moved)");
                                cancel();
                            }
                            if (!player.getInventory().getItemInMainHand().equals(item)) {
                                ChatUtil.sendActionBar(player, "&cStopped. ");
                                cancel();
                            }
                            if (progressTime >= useTime) {
                                ChatUtil.sendActionBar(player, "&aSuccessful.");
                                success();
                                cancel();
                            }

                            progressTime++;
                        }

                        @Override
                        public void cancel() {
                            super.cancel();
                            player.setCooldown(item, 0);
                            finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10000000, 0, false, false, false));
                        }

                        private void success() {
                            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                            manager.getPlayerStat(finalTarget).revive();
                            finalTarget.teleport(player);
                            finalTarget.setGameMode(GameMode.ADVENTURE);
                            finalTarget.setHealth(10d);
                            finalTarget.setAbsorptionAmount(0);
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

        ChatUtil.sendActionBar(player, "&cNo dead player nearby.");
    }
}
