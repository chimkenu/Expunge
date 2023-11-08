package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.BreakGlass;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.items.weapons.melees.Chainsaw;
import me.chimkenu.expunge.items.weapons.melees.Melee;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SwingListener extends GameListener {
    public SwingListener(JavaPlugin plugin, LocalGameManager localGameManager) {
        super(plugin, localGameManager);
    }

    private void swing(Player player, Melee melee) {
        ArrayList<LivingEntity> entities = new ArrayList<>();
        World world = player.getWorld();
        Location loc = player.getEyeLocation();
        for (int i = 0; i < melee.getRange(); i++) {
            loc.add(loc.getDirection());

            // check for glass to break
            BreakGlass.breakGlass(loc.getBlock());

            for (Entity e : world.getNearbyEntities(loc, 0.5, 1, 0.5)) {
                if (e == player) {
                    continue;
                }
                if (!(e instanceof LivingEntity living)) {
                    continue;
                }
                if (e instanceof ArmorStand) {
                    continue;
                }
                if (e instanceof Pig) {
                    continue;
                }
                if (e instanceof Player p && p.getGameMode() != GameMode.ADVENTURE) {
                    continue;
                }
                if (entities.contains(living)) {
                    continue;
                }
                entities.add(living);
                if (entities.size() >= melee.getEntitiesToHit()) break;
            }
            if (entities.size() >= melee.getEntitiesToHit()) break;
        }

        // play sound depending on whether an entity was hit (only if playSound is true)
        if (entities.isEmpty()) {
            if (melee.getRange() <= 2) player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1, 1);
            else player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1, 1);
            return;
        }

        player.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_DAMAGE, SoundCategory.PLAYERS, 1, 1);
        player.getWorld().playSound(loc, Sound.BLOCK_HONEY_BLOCK_STEP, SoundCategory.PLAYERS, 1, 1);
        for (LivingEntity livingEntity : entities) {
            livingEntity.getWorld().spawnParticle(Particle.BLOCK_CRACK, livingEntity.getLocation().add(0, .5, 0), 50, 0.2, 0.2, 0.2, Material.NETHER_WART_BLOCK.createBlockData());

            /* most melee weapons do % damage rather than fixed
             * 5% tank
             * 25% witch
             * 50% charger
             * 100% everything else
             */
            if (melee.getDamage() == 0)
                switch (livingEntity.getType()) {
                    case ZOGLIN -> livingEntity.damage((400 + (localGameManager.getDifficulty().ordinal() * 200)) * 0.5, player);
                    case IRON_GOLEM -> livingEntity.damage(2500 + (localGameManager.getDifficulty().ordinal() * 1000) * 0.05, player);
                    case ENDERMAN -> livingEntity.damage(1000 * 0.25, player);
                    default -> livingEntity.damage(livingEntity.getHealth() + livingEntity.getAbsorptionAmount() * 2, player);
                }
            else // fixed damage
                livingEntity.damage(melee.getDamage(), player);
        }
    }

    @EventHandler
    public void onSwing(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!localGameManager.getPlayers().contains(player)) {
            return;
        }
        if (e.getHand() == null || e.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        if (!(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
            return;
        }

        ItemStack mainHand = player.getInventory().getItemInMainHand();

        Melee weapon = Utils.getPlayerHeldMelee(mainHand);
        if (weapon == null) {
            return;
        }

        e.setCancelled(true);
        if (player.getCooldown(mainHand.getType()) > 1) {
            return;
        }

        player.setCooldown(mainHand.getType(), weapon.getCooldown());
        if (!(weapon instanceof Chainsaw)) {
            swing(player, weapon);
            return;
        }
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (player.getInventory().getItemInMainHand().getType() != weapon.getMaterial())
                    this.cancel();
                if (i % 5 == 0) {
                    Location loc = player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.5));
                    player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 1);
                    swing(player, weapon);
                }
                i++;
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    @EventHandler
    public void stopWeaponDamage(PlayerItemDamageEvent e) {
        if (localGameManager.getPlayers().contains(e.getPlayer()))
            e.setCancelled(true);
    }
}
