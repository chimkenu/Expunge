package me.chimkenu.expunge.guns.listeners;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.Utils;
import me.chimkenu.expunge.guns.weapons.melees.Melee;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Swing implements Listener {
    private void swing(Player player, Melee melee, boolean playSound) {
        ArrayList<LivingEntity> entities = new ArrayList<>();
        World world = player.getWorld();
        Location loc = player.getEyeLocation();
        for (int i = 0; i < melee.getRange(); i++) {
            loc.add(loc.getDirection());
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
        if (entities.size() < 1 && playSound) {
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
                    case ZOGLIN -> livingEntity.damage((400 + (Expunge.currentDifficulty.ordinal() * 200)) * 0.5, player);
                    case IRON_GOLEM -> livingEntity.damage(2500 + (Expunge.currentDifficulty.ordinal() * 1000) * 0.05, player);
                    case ENDERMAN -> livingEntity.damage(1000 * 0.25, player);
                    default -> livingEntity.damage(livingEntity.getHealth() + livingEntity.getAbsorptionAmount() + 1, player);
                }
            else // fixed damage
                livingEntity.damage(melee.getDamage(), player);
        }
    }

    @EventHandler
    public void onSwing(PlayerInteractEvent e) {
        if (e.getHand() == null || e.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (!(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
            return;
        }

        Player player = e.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (player.getCooldown(mainHand.getType()) > 1) {
            return;
        }

        Melee weapon = Utils.getPlayerHeldMelee(mainHand);
        if (weapon == null) {
            return;
        }
        player.setCooldown(mainHand.getType(), weapon.getCooldown());
        if (!(weapon instanceof Chainsaw)) {
            swing(player, weapon, true);
            return;
        }
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (player.itemInMainHand != weapon.getWeapon())
                    this.cancel();
                if (i % 5 == 0) {
                    Location loc = player.getEyeLocation().add(attacker.getLocation().getDirection().multiply(1.5));
                    player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 1);
                    swing(player, weapon, false);
                }
                i = (i + 1) % 20;
            }
        }.runTaskTimer(Expunge.instance, 1, 1);
    }
}
