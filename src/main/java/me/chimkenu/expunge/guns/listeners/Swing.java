package me.chimkenu.expunge.guns.listeners;

import me.chimkenu.expunge.Utils;
import me.chimkenu.expunge.guns.weapons.melees.Melee;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Swing implements Listener {
    private void swing(Player player, Melee melee) {
        ArrayList<LivingEntity> entities = new ArrayList<>();
        World world = player.getWorld();
        Location loc = player.getEyeLocation();
        for (int i = 0; i < melee.getRange(); i++) {
            loc.add(loc.getDirection());
            for (Entity e : world.getNearbyEntities(loc, 1.5, 1.5, 1.5)) {
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
                if (entities.contains(living)) {
                    continue;
                }
                entities.add(living);
                if (entities.size() >= melee.getEntitiesToHit()) break;
            }
            if (entities.size() >= melee.getEntitiesToHit()) break;
        }

        // play sound depending on whether an entity was hit
        if (entities.size() < 1) {
            if (melee.getRange() <= 2) player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1, 1);
            else player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1, 1);
            return;
        }

        player.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_DAMAGE, SoundCategory.PLAYERS, 1, 1);
        player.getWorld().playSound(loc, Sound.BLOCK_HONEY_BLOCK_STEP, SoundCategory.PLAYERS, 1, 1);
        for (LivingEntity livingEntity : entities) {
            livingEntity.getWorld().spawnParticle(Particle.BLOCK_CRACK, livingEntity.getLocation().add(0, .5, 0), 50, 0.2, 0.2, 0.2, Material.NETHER_WART_BLOCK.createBlockData());
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

        ItemStack mainHand = e.getPlayer().getInventory().getItemInMainHand();
        if (e.getPlayer().getCooldown(mainHand.getType()) > 1) {
            return;
        }

        Melee weapon = Utils.getPlayerHeldMelee(mainHand);
        if (weapon == null) {
            return;
        }
        e.getPlayer().setCooldown(mainHand.getType(), weapon.getCooldown());
        swing(e.getPlayer(), weapon);
    }
}
