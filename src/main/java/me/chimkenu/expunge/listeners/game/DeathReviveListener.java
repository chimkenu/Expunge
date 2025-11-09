package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.entities.survivor.Survivor;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class DeathReviveListener extends GameListener {
    public final ArrayList<Survivor> beingRevived = new ArrayList<>();

    public DeathReviveListener(JavaPlugin plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    @EventHandler
    public void onDeath(EntityDamageEvent e) {
        var opt = gameManager.getEntity(e.getEntity());
        if (opt.isEmpty()) return;
        var entity = opt.get();
        if (!(entity instanceof Survivor survivor)) return;
        if (survivor.getTotalHealth() - e.getFinalDamage() > 0) return;

        // cancel (actual) death and do the programmed death
        e.setCancelled(true);
        // .down() handles already incapacitated survivors (hopefully)
        survivor.down();

        if (!survivor.isAlive()) {
            broadcast(survivor.getHandle().getName() + " died.");
        } else if (survivor.isIncapacitated()) {
            broadcast(survivor.getHandle().getName() + " is down.");
        }

        // check if all players have died
        for (var s : gameManager.getSurvivors()) {
            if (s.isAlive() && !s.isIncapacitated()) {
                return;
            }
        }

        // this is reached if all players are dead
        gameManager.getWorld().getPlayers().forEach(player1 -> ChatUtil.sendError(player1, "All players have died, returning to last checkpoint..."));
        for (Player p : gameManager.getPlayers()) {
            p.setGameMode(GameMode.SPECTATOR);
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 4, false, false, false));
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                gameManager.restart();
            }
        }.runTaskLater(plugin, 20 * 3);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDismount(EntityDismountEvent e) {
        var opt = gameManager.getEntity(e.getEntity());
        if (opt.isEmpty()) return;
        var entity = opt.get();
        if (!(entity instanceof Survivor survivor)) return;

        if (survivor.isAlive() && survivor.isIncapacitated()) {
            e.setCancelled(true);
        }
    }

    private void revive(Survivor target, Player savior) {
        target.up();
        target.setLocation(savior.getLocation());
        Achievements.WERE_IN_THIS_TOGETHER.grant(savior);
    }

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent e) {
        var player = e.getPlayer();
        if (player.isSneaking()) {
            return;
        }

        var opt = gameManager.getSurvivor(player);
        if (opt.isEmpty()) return;
        var survivor = opt.get();

        if (!survivor.isAlive()) {
            return;
        }

        for (Entity entity : player.getNearbyEntities(1, 1, 1)) {
            var targetOpt = gameManager.getEntity(entity);
            if (targetOpt.isEmpty()) continue;
            var targetEntity = targetOpt.get();
            if (!(targetEntity instanceof Survivor target)) continue;
            if (entity == player) continue;
            if (!target.isAlive() || !target.isIncapacitated()) continue;
            if (beingRevived.contains(target)) {
                ChatUtil.sendActionBar(player, "&b" + target.getHandle().getName() + "&e is already being revived.");
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
                    String prefix = "&eReviving &b" +  target.getHandle().getName() + "&e...";
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

    private void broadcast(String message) {
        gameManager.getPlayers().forEach(p -> ChatUtil.sendError(p, message));
    }
}
