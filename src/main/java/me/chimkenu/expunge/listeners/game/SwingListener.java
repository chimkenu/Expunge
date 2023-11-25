package me.chimkenu.expunge.listeners.game;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.weapons.melees.Chainsaw;
import me.chimkenu.expunge.items.weapons.melees.Melee;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;

public class SwingListener extends GameListener {
    private final BreakGlassListener breakGlassListener;
    private final HashSet<Player> playersUsingChainsaw;

    public SwingListener(JavaPlugin plugin, GameManager gameManager, BreakGlassListener breakGlassListener) {
        super(plugin, gameManager);
        this.breakGlassListener = breakGlassListener;
        playersUsingChainsaw = new HashSet<>();
    }

    private void swing(Player player, Melee melee) {
        ArrayList<LivingEntity> entities = new ArrayList<>();
        World world = player.getWorld();
        Location loc = player.getEyeLocation();
        for (int i = 0; i < melee.getRange(); i++) {
            loc.add(loc.getDirection());

            // check for glass to break
            breakGlassListener.breakGlass(loc.getBlock());

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
                    case ZOGLIN -> livingEntity.damage((400 + (gameManager.getDifficulty().ordinal() * 200)) * 0.5, player);
                    case IRON_GOLEM -> livingEntity.damage(2500 + (gameManager.getDifficulty().ordinal() * 1000) * 0.05, player);
                    case ENDERMAN -> livingEntity.damage(1000 * 0.25, player);
                    default -> livingEntity.damage((livingEntity.getHealth() + livingEntity.getAbsorptionAmount()) * 2, player);
                }
            else // fixed damage
                livingEntity.damage(melee.getDamage(), player);
        }
    }

    private void handleSwing(Player player, ItemStack mainHand, Melee melee) {
        if (player.getCooldown(mainHand.getType()) > 1) {
            return;
        }

        player.setCooldown(mainHand.getType(), melee.getCooldown());
        if (!(melee instanceof Chainsaw)) {
            swing(player, melee);
            return;
        }

        if (playersUsingChainsaw.contains(player)) {
            return;
        }

        playersUsingChainsaw.add(player);
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (player.getInventory().getItemInMainHand().getType() != melee.getMaterial()) {
                    playersUsingChainsaw.remove(player);
                    this.cancel();
                }
                if (i % 5 == 0) {
                    Location loc = player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.5));
                    player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 1);
                    swing(player, melee);
                }
                i++;
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    @EventHandler
    public void onClick(PlayerArmSwingEvent e) {
        Player player = e.getPlayer();
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (!(Utils.getGameItemFromItemStack(mainHand) instanceof Melee melee)) {
            return;
        }

        e.setCancelled(true);
        handleSwing(player, mainHand, melee);
    }

    @EventHandler
    public void stopWeaponDamage(PlayerItemDamageEvent e) {
        if (gameManager.getPlayers().contains(e.getPlayer()))
            e.setCancelled(true);
    }
}
