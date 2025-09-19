package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.events.DeathEvent;
import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.PlayerStats;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.ItemUtil;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;

public class DeathReviveListener extends GameListener {
    public final ArrayList<Player> beingRevived = new ArrayList<>();

    public DeathReviveListener(Expunge plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    private void dead(Player player) {
        DeathEvent event = new DeathEvent(player);
        Bukkit.getPluginManager().callEvent(event);

        gameManager.getWorld().getPlayers().forEach(player1 -> ChatUtil.sendError(player1, player.getName() + " died."));
        PlayerStats playerStats = gameManager.getPlayerStat(player);
        if (playerStats == null) {
            return;
        }

        // spawn in an armor stand which can be revived
        for (ArmorStand armorStand : spawnDeadArmorStand(player)) {
            gameManager.addEntity(armorStand);
        }

        player.setGameMode(GameMode.SPECTATOR);
        // dead people drop their hotbar
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                ItemStack item = inventory.getItem(i + (j * 9));
                if (item != null) {
                    Item dropItem = player.getWorld().dropItem(player.getLocation(), item);
                    gameManager.addEntity(dropItem);
                }
                inventory.setItem(i + (j * 9), new ItemStack(Material.AIR));
            }
        }
        player.getInventory().clear(5); // pistol if they were down
        playerStats.setAlive(false);
        playerStats.setLives(0);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        // cancel death
        player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue());
        e.setDeathMessage("");

        if (!gameManager.getPlayerStat(player).isAlive()) {
            // player died while down
            player.leaveVehicle();
            dead(player);
        } else {
            // update value
            gameManager.getPlayerStat(player).setAlive(false);

            // lives check
            if (gameManager.getPlayerStat(player).getLives() > 1) {
                gameManager.getWorld().getPlayers().forEach(player1 -> ChatUtil.sendError(player1, player.getName() + " is down."));
                Location loc = player.getEyeLocation();
                while (loc.getBlock().getType().equals(Material.AIR)) {
                    loc.subtract(0, 0.25, 0);
                }

                loc.setY(loc.getBlock().getBoundingBox().getMaxY() - 0.95);
                ArmorStand armorStand = player.getWorld().spawn(loc, ArmorStand.class);
                armorStand.setGravity(false);
                armorStand.setInvulnerable(true);
                armorStand.setInvisible(true);
                armorStand.setSmall(true);
                armorStand.addPassenger(player);
                gameManager.addEntity(armorStand);
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 6000, 0, false, false, false));

                // save hotbar in inventory and replace with pistol
                PlayerInventory inventory = player.getInventory();
                for (int i = 0; i < 5; i++) {
                    inventory.setItem(i + 9, inventory.getItem(i));
                    inventory.setItem(i, new ItemStack(Material.AIR));
                }
                inventory.setItem(5, plugin.getItems().toGameItem("PISTOL").toItem());

            } else {
                dead(player);
            }
        }

        // check if all players have died
        for (Player p : gameManager.getPlayers()) {
            if (gameManager.getPlayerStat(p).isAlive()) {
                return;
            }
        }

        // this is reached if all players are dead
        gameManager.getWorld().getPlayers().forEach(player1 -> ChatUtil.sendError(player1, "All players have died, returning to last checkpoint..."));
        for (Player p : gameManager.getPlayers()) {
            p.setGameMode(GameMode.SPECTATOR);
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 4, false, false, false));
        }
        gameManager.getDirector().clearEntities();
        new BukkitRunnable() {
            @Override
            public void run() {
                ((CampaignGameManager) gameManager).restartMap();
            }
        }.runTaskLater(plugin, 20 * 3);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDismount(EntityDismountEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }
        if (gameManager.getPlayerStat(player).isAlive() || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if (gameManager.getPlayerStat(player).getLives() < 1) {
            return;
        }
        if (e.getDismounted().getScoreboardTags().contains("RESPAWN_ARMOR_STAND")) {
            e.setCancelled(true);
        }
    }

    private void revive(Player target, Player savior) {
        if (gameManager.getPlayers().contains(target)) {
            PlayerStats targetStats = gameManager.getPlayerStat(target);
            targetStats.setAlive(true);
            targetStats.setLives(targetStats.getLives() - 1);

            target.setHealth(1d);
            Objects.requireNonNull(target.getAttribute(Attribute.MAX_ABSORPTION)).setBaseValue(20);
            target.setAbsorptionAmount(5d);
            target.removePotionEffect(PotionEffectType.GLOWING);

            // get hotbar back
            PlayerInventory inventory = target.getInventory();
            for (int i = 0; i < 5; i++) {
                inventory.setItem(i, inventory.getItem(i + 9));
                inventory.setItem(i + 9, new ItemStack(Material.AIR));
            }

            inventory.clear(5); // clear pistol

            if (targetStats.getLives() == 1) {
                new BukkitRunnable() {
                    int i = 200;
                    @Override
                    public void run() {
                        if (i <= 0 ||
                                !gameManager.isRunning() ||
                                !gameManager.getPlayers().contains(target) ||
                                target.getGameMode() != GameMode.ADVENTURE ||
                                targetStats.getLives() > 1
                        ) this.cancel();
                        if (i % 20 == 0 || i % 20 == 6) {
                            target.playSound(target, Sound.BLOCK_STONE_PLACE, SoundCategory.PLAYERS, 1, 0);
                        }
                        i--;
                    }
                }.runTaskTimer(plugin, 1, 1);
            }

            if (target.getVehicle() != null) target.getVehicle().remove();
            target.teleport(savior);

            // achievement
            Achievements.WERE_IN_THIS_TOGETHER.grant(savior);
        }
    }

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent e) {
        if (!gameManager.isRunning()) {
            return;
        }
        Player player = e.getPlayer();
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }
        if (player.isSneaking()) {
            return;
        }
        if (!gameManager.getPlayerStat(player).isAlive()) {
            return;
        }

        for (Entity entity : player.getNearbyEntities(1, 1, 1)) {
            if (!(entity instanceof Player target)) {
                continue;
            }
            if (target == player) {
                continue;
            }
            if (!gameManager.getPlayers().contains(target)) {
                continue;
            }
            if (beingRevived.contains(target)) {
                ChatUtil.sendActionBar(player, "&b" + target.getName() + "&e is already being revived.");
                continue;
            }
            if (gameManager.getPlayerStat(target).isAlive()) {
                continue;
            }
            if (gameManager.getPlayerStat(target).getLives() <= 1) {
                continue;
            }

            Vector loc = player.getLocation().toVector();
            beingRevived.add(target);
            new BukkitRunnable() {
                final int time = 20 * 5;
                int i = time;

                @Override
                public void run() {
                    i--;
                    String progressBar = ChatUtil.progressBar(time, i);
                    String prefix = "&eReviving &b" +  target.getName() + "&e...";
                    ChatUtil.sendActionBar(player, prefix + " " + progressBar);
                    prefix = "&eBeing revived by &b" + player.getName() + "&e...";
                    ChatUtil.sendActionBar(target, prefix + " " + progressBar);

                    if (loc.distanceSquared(player.getLocation().toVector()) > 1) {
                        ChatUtil.sendActionBar(player, "&cStopped. &8(You moved too far)");
                        ChatUtil.sendActionBar(target, "&cStopped.");
                        beingRevived.remove(target);
                        this.cancel();
                    }
                    if (!player.isSneaking()) {
                        ChatUtil.sendActionBar(player, "&cStopped.");
                        ChatUtil.sendActionBar(target, "&cStopped.");
                        beingRevived.remove(target);
                        this.cancel();
                    }
                    if (i <= 0) {
                        // revived player
                        ChatUtil.sendActionBar(player, "&aSuccessful.");
                        ChatUtil.sendActionBar(target, "&aSuccessful.");
                        revive(target, player);
                        beingRevived.remove(target);
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 1, 1);
        }
    }

    private ArmorStand[] spawnDeadArmorStand(Player player) {
        Location loc = player.getEyeLocation();
        while (loc.getBlock().getType().equals(Material.AIR)) {
            loc.subtract(0, 0.25, 0);
        }

        loc.setY(loc.getBlock().getBoundingBox().getMaxY() - 1.2);
        loc.setPitch(0);

        ArmorStand upper = player.getWorld().spawn(loc, ArmorStand.class);
        ArmorStand lower = player.getWorld().spawn(loc.add(0, 0.65, 0).add(loc.getDirection().multiply(0.7)), ArmorStand.class);
        ArmorStand[] armorStand = new ArmorStand[]{upper, lower};
        for (ArmorStand a : armorStand) {
            a.setInvulnerable(true);
            a.setGravity(false);
            // a.setBodyYaw(loc.getYaw());
            ItemUtil.putOnRandomClothes(a.getEquipment());
        }

        upper.getEquipment().setHelmet(ItemUtil.getSkull(player));
        upper.getEquipment().setLeggings(new ItemStack(Material.AIR));
        upper.getEquipment().setBoots(new ItemStack(Material.AIR));
        upper.setLeftArmPose(new EulerAngle(-1.22, -0.524, -0.175));
        upper.setRightArmPose(new EulerAngle(-1.31, 0.524, 0.175));
        upper.setHeadPose(new EulerAngle(-1.571, -0.07, 0));
        upper.setBodyPose(new EulerAngle(-1.518, -0.017, 0));
        upper.setArms(true);

        upper.addScoreboardTag(player.getUniqueId().toString());
        upper.addScoreboardTag(upper.getUniqueId().toString());
        upper.addScoreboardTag(lower.getUniqueId().toString());

        lower.getEquipment().setChestplate(new ItemStack(Material.AIR));
        lower.setLeftLegPose(new EulerAngle(-1.588, -0.524, -0.0175));
        lower.setRightLegPose(new EulerAngle(-1.5533, 0.5236, 0.0175));
        lower.setInvisible(true);

        return armorStand;
    }
}
