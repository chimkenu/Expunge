package me.chimkenu.expunge.listeners;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.enums.Weapons;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class DeathRevive implements Listener {
    public static final HashMap<Player, Integer> currentLives = new HashMap<>();
    public static final ArrayList<Player> beingRevived = new ArrayList<>();

    private static void dead(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        // dead people drop their hotbar
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                ItemStack item = inventory.getItem(i + (j * 9));
                if (item != null) {
                    Item dropItem = player.getWorld().dropItem(player.getLocation(), item);
                    dropItem.addScoreboardTag("ITEM");
                }
                inventory.setItem(i + (j * 9), new ItemStack(Material.AIR));
            }
        }
        player.getInventory().clear(5); // pistol if they were down
        if (Expunge.playing.getKeys().contains(player)) {
            Expunge.playing.setIsAlive(player, false);
            currentLives.put(player, 0);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if (!Expunge.playing.getKeys().contains(player)) {
            return;
        }
        if (!Expunge.playing.isAlive(player)) {
            // player died while down
            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " died.");
            player.leaveVehicle();
            dead(player);
        }
        else {
            // update value
            Expunge.playing.setIsAlive(player, false);

            // lives check
            if (currentLives.get(player) > 1) {
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " is down.");
                Location loc = player.getLocation();
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
                armorStand.addScoreboardTag("RESPAWN_ARMOR_STAND");
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 6000, 0, false, false, false));

                // save hotbar in inventory and replace with pistol
                PlayerInventory inventory = player.getInventory();
                for (int i = 0; i < 5; i++) {
                    inventory.setItem(i + 9, inventory.getItem(i));
                    inventory.setItem(i, new ItemStack(Material.AIR));
                }
                inventory.setItem(5, Weapons.Guns.PISTOL.getGun().getWeapon());

            } else {
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " died.");
                dead(player);
            }
        }

        // check if all players have died
        for (Player p : Expunge.playing.getKeys()) {
            if (Expunge.playing.isAlive(p)) {
                return;
            }
        }

        // this is reached if all players are dead
        Bukkit.broadcastMessage(ChatColor.RED + "All players have died, returning to last checkpoint...");
        for (Player p : Expunge.playing.getKeys()) {
            p.setGameMode(GameMode.SPECTATOR);
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 4, false, false, false));
        }
        Expunge.runningDirector.clearEntities();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Expunge.isGameRunning) Expunge.restartScene(Expunge.currentMap.getMaps().get(Expunge.currentSceneIndex));
            }
        }.runTaskLater(Expunge.instance, 20 * 3);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (Expunge.spectators.contains(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }
        if (!Expunge.playing.getKeys().contains(player)) {
            return;
        }
        if (Expunge.playing.isAlive(player) || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if (currentLives.get(player) < 1) {
            return;
        }
        if (e.getDismounted().getScoreboardTags().contains("RESPAWN_ARMOR_STAND")) {
            e.setCancelled(true);
        }
    }

    private void revive(Player target, Player savior) {
        if (Expunge.playing.getKeys().contains(target)) {
            Expunge.playing.setIsAlive(target, true);
            currentLives.put(target, currentLives.get(target) - 1);

            target.setHealth(1d);
            target.setAbsorptionAmount(5d);
            target.removePotionEffect(PotionEffectType.GLOWING);

            // get hotbar back
            PlayerInventory inventory = target.getInventory();
            for (int i = 0; i < 5; i++) {
                inventory.setItem(i, inventory.getItem(i + 9));
                inventory.setItem(i + 9, new ItemStack(Material.AIR));
            }

            inventory.clear(5); // clear pistol

            if (currentLives.get(target) == 1) {
                new BukkitRunnable() {
                    int i = 200;
                    @Override
                    public void run() {
                        if (
                                i <= 0 ||
                                !Expunge.isGameRunning ||
                                !Expunge.playing.getKeys().contains(target) ||
                                target.getGameMode() != GameMode.ADVENTURE ||
                                Expunge.playing.getLives(target) > 1
                        ) this.cancel();
                        if (i % 20 == 0 || i % 20 == 6) {
                            target.playSound(target, Sound.BLOCK_STONE_PLACE, SoundCategory.PLAYERS, 1, 0);
                        }
                        i--;
                    }
                }.runTaskTimer(Expunge.instance, 1, 1);
            }

            if (target.getVehicle() != null) target.getVehicle().remove();
            target.teleport(savior);

            // achievement
            Achievements.WERE_IN_THIS_TOGETHER.grant(savior);
        }
    }

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent e) {
        if (!Expunge.isGameRunning) {
            return;
        }
        Player player = e.getPlayer();
        if (!Expunge.playing.getKeys().contains(player)) {
            return;
        }
        if (player.isSneaking()) {
            return;
        }
        if (!Expunge.playing.isAlive(player)) {
            return;
        }

        for (Entity entity : player.getNearbyEntities(1, 1, 1)) {
            if (entity instanceof Player target && target != player) {
                if (beingRevived.contains(target)) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§b" + target.getDisplayName() + " §eis already being revived."));
                    return;
                }
                if (Expunge.playing.getKeys().contains(target) && !Expunge.playing.isAlive(target) && currentLives.get(target) > 1) {
                    Vector loc = e.getPlayer().getLocation().toVector();
                    beingRevived.add(target);
                    new BukkitRunnable() {
                        final int time = 20 * 5;
                        int i = time;

                        @Override
                        public void run() {
                            i--;
                            // display status as action bar
                            // percentage completion
                            double percentage = (double) (time - i) / time;
                            int progress = (int) (percentage * 10);
                            percentage = (int) (percentage * 100);

                            // create progress bar
                            String progress_bar_complete = "|".repeat(Math.max(0, progress));
                            String progress_bar_incomplete = "|".repeat(Math.max(0, 10 - progress));
                            String progress_bar = "§e" + "Progress: " + "§7" + "[" + "§a" + progress_bar_complete + "§7" + progress_bar_incomplete + "§7" + "]" + "§8" + " [" + percentage + "%]";
                            String prefix = "§eReviving " + "§b" + target.getDisplayName() + "§e...";
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(prefix + " " + progress_bar));
                            prefix = "§eBeing revived by " + "§b" + player.getDisplayName() + "§e...";
                            target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(prefix + " " + progress_bar));

                            if (loc.distanceSquared(player.getLocation().toVector()) > 1) {
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cStopped. §8(You moved too far)"));
                                target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cStopped."));
                                beingRevived.remove(target);
                                this.cancel();
                            }
                            if (!player.isSneaking()) {
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cStopped."));
                                target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cStopped."));
                                beingRevived.remove(target);
                                this.cancel();
                            }
                            if (i <= 0) {
                                // revived player
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§aSuccessful."));
                                target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§aSuccessful."));
                                revive(target, player);
                                beingRevived.remove(target);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Expunge.instance, 1, 1);
                }
            }
        }
    }
}
