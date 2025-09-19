package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.MeleeType;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.Gun;
import me.chimkenu.expunge.items.Melee;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SwingListener extends GameListener {
    private final Set<Player> playersUsingChainsaw;
    private final Map<Player, Long> playersLastSwing;

    public SwingListener(Expunge plugin, GameManager gameManager) {
        super(plugin, gameManager);
        playersUsingChainsaw = new HashSet<>();
        playersLastSwing = new HashMap<>();
    }

    private void swing(Player player, Entity target, Melee melee) {
        playersLastSwing.putIfAbsent(player, 0L);
        var debounce = System.currentTimeMillis() - playersLastSwing.get(player);
        if (debounce < 100 || player.getAttackCooldown() != 1) {
            return;
        }
        playersLastSwing.put(player, System.currentTimeMillis());

        HashSet<LivingEntity> entities = new HashSet<>();
        for (Entity e : player.getWorld().getNearbyEntities(target.getBoundingBox().expand(1, 0.25, 1))) {
            if (e == player) {
                continue;
            }
            if (!(e instanceof LivingEntity living)) {
                continue;
            }
            if (e instanceof ArmorStand) {
                continue;
            }
            if (e instanceof Player p && p.getGameMode() != GameMode.ADVENTURE) {
                continue;
            }
            entities.add(living);
            if (entities.size() >= melee.pierceNumber()) break;
        }

        player.getWorld().playSound(target, Sound.ENTITY_IRON_GOLEM_DAMAGE, SoundCategory.PLAYERS, 1, 1);
        player.getWorld().playSound(target, Sound.BLOCK_HONEY_BLOCK_STEP, SoundCategory.PLAYERS, 1, 1);
        player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(1.5)), 1);
        for (LivingEntity livingEntity : entities) {
            livingEntity.getWorld().spawnParticle(Particle.BLOCK, livingEntity.getLocation().add(0, .5, 0), 50, 0.2, 0.2, 0.2, Material.NETHER_WART_BLOCK.createBlockData());

            /* most melee weapons do % damage rather than fixed
             * 5% tank
             * 25% witch
             * 50% charger
             * 100% everything else
             */
            var damage = melee.damage();
            var attribute = livingEntity.getAttribute(Attribute.MAX_HEALTH);
            if (melee.damageType() == MeleeType.Damage.RELATIVE && attribute != null) {
                var maxHealth = attribute.getValue();
                switch (livingEntity.getType()) {
                    case ZOGLIN ->
                            damage = maxHealth * 0.5;
                    case IRON_GOLEM ->
                            damage = maxHealth * 0.05;
                    case ENDERMAN ->
                            damage = maxHealth * 0.25;
                    default ->
                            damage = maxHealth;
                }

                // avoid armor shenanigans. anyway, its better that it does more damage
                damage += 5;
            }

            livingEntity.damage(damage, player);
        }
    }

    private void handleSwing(Player player, Entity target, Melee melee) {
        if (!Objects.equals(melee.id(), "CHAINSAW")) {
            swing(player, target, melee);
            return;
        }

        if (playersUsingChainsaw.contains(player)) {
            return;
        }
        playersUsingChainsaw.add(player);

        gameManager.addTask(
                new BukkitRunnable() {
                    int i = 0;
                    @Override
                    public void run() {
                        if (player.getInventory().getItemInMainHand().getType() != melee.material()) {
                            playersUsingChainsaw.remove(player);
                            this.cancel();
                        }
                        if (i % 5 == 0) {
                            Location loc = player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.5));
                            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 1);
                            for (Entity e : player.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                                if (e == player) {
                                    continue;
                                }
                                if (!(e instanceof LivingEntity living)) {
                                    continue;
                                }
                                if (e instanceof ArmorStand) {
                                    continue;
                                }
                                if (e instanceof Player p && p.getGameMode() != GameMode.ADVENTURE) {
                                    continue;
                                }
                                swing(player, living, melee);
                                break;
                            }
                        }
                        i++;
                    }
                }.runTaskTimer(plugin, 1, 1)
        );
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }

        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        var heldItem = player.getInventory().getItemInMainHand();
        var item = plugin.getItems().toGameItem(heldItem);
        if (!(item instanceof Melee melee)) {
            return;
        }

        // shove threshold (or spam attack)
        if (e.getDamage() < 10) {
            return;
        }

        handleSwing(player, e.getEntity(), melee);
    }

    @EventHandler
    public void stopWeaponDamage(PlayerItemDamageEvent e) {
        if (gameManager.getPlayers().contains(e.getPlayer()))
            e.setCancelled(true);
    }
}
