package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.events.DeathEvent;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.PlayerStats;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class RescueClosetListener extends GameListener {
    private final BreakDoorListener breakDoorListener;
    private final Set<ArmorStand> armorStands;

    public RescueClosetListener(JavaPlugin plugin, GameManager gameManager, BreakDoorListener breakDoorListener) {
        super(plugin, gameManager);
        this.breakDoorListener = breakDoorListener;
        armorStands = new HashSet<>();
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent e) {
        Player savior = e.getPlayer();
        if (!gameManager.getPlayers().contains(savior)) {
            return;
        }
        if (!gameManager.getPlayerStat(savior).isAlive()) {
            return;
        }
        if (e.getClickedBlock() == null) {
            return;
        }
        if (gameManager.getWorld() != e.getClickedBlock().getWorld()) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.useInteractedBlock() != Event.Result.ALLOW) {
            return;
        }

        Block block = e.getClickedBlock();
        if (!block.getType().toString().contains("_DOOR") || block.getType() == Material.IRON_DOOR) {
            return;
        }
        if (block.getRelative(0, 1, 0).getType() == block.getType()) {
            block = block.getRelative(0, 1, 0);
        }
        if (!doesMapContainRescueCloset(gameManager.getMap().rescueClosetLocations(), block.getLocation().toVector())) {
            return;
        }
        if (breakDoorListener.hasBeenInteractedWith(block)) {
            return; // Someone already touched the rescue closet and thus cannot respawn any players here
        }

        for (ArmorStand armorStand : armorStands) {
            armorStand.remove();
        }
        armorStands.clear();

        // Respawn dead players
        for (Player p : gameManager.getPlayers()) {
            PlayerStats s = gameManager.getPlayerStat(p);
            if (s.isAlive()) {
                continue;
            }

            s.revive();
            p.teleport(block.getLocation().add(0.5, -1, 0.5));
            p.removePotionEffect(PotionEffectType.GLOWING);
            p.setGameMode(GameMode.ADVENTURE);
            // TODO: broadcast message
        }
    }

    @EventHandler
    public void onPlayerDeath(DeathEvent e) {
        if (!gameManager.getPlayers().contains(e.getPlayer())) {
            return;
        }

        World world = gameManager.getWorld();
        for (Vector v : gameManager.getMap().rescueClosetLocations()) {
            Location loc = v.toLocation(world);
            if (loc.getBlock().getState() instanceof Door door) {
                ArmorStand armorStand = world.spawn(loc, ArmorStand.class);
                armorStand.setInvulnerable(true);
                armorStand.setGravity(false);
                armorStand.setArms(true);
                armorStand.setBasePlate(false);
                armorStand.setGlowing(true);
                armorStand.getLocation().setDirection(door.getFacing().getDirection());

                armorStand.setHeadPose(new EulerAngle(6.17847f,0,0));
                armorStand.setLeftArmPose(new EulerAngle(3.66519f,0,0));
                armorStand.setRightArmPose(new EulerAngle(3.66519f, 0, 0));
                armorStand.setLeftLegPose(new EulerAngle(0.122173f,0,6.14356f));
                armorStand.setRightLegPose(new EulerAngle(6.17847f,0,0.10472f));

                armorStands.add(armorStand);
                gameManager.getDirector().getItemHandler().addEntity(armorStand);

                Utils.putOnRandomClothes(armorStand.getEquipment());
                armorStand.getEquipment().setHelmet(Utils.getSkull(e.getPlayer()));

                new BukkitRunnable() {
                    final float headXMin = 0.174533f;
                    final float headXMax = 6.17847f;
                    final float rightArmXMin = 3.66519f;
                    final float rightArmXMax = 4.45059f;
                    final float dx = 0.0872665f;

                    boolean isHeadIncreasing = true;
                    boolean isRightArmIncreasing = true;

                    @Override
                    public void run() {
                        if (armorStand.isDead()) {
                            this.cancel();
                            return;
                        }

                        EulerAngle headPose = armorStand.getHeadPose();
                        if (isHeadIncreasing && headPose.getX() > headXMax) {
                            isHeadIncreasing = false;
                        } else if (!isHeadIncreasing && headPose.getX() < headXMin) {
                            isHeadIncreasing = true;
                        }
                        headPose.setX(headPose.getX() + (isHeadIncreasing ? dx : -dx));

                        EulerAngle rightArmPose = armorStand.getRightArmPose();
                        if (isRightArmIncreasing && rightArmPose.getX() > rightArmXMax) {
                            isRightArmIncreasing = false;
                        } else if (!isRightArmIncreasing && rightArmPose.getX() < rightArmXMin) {
                            isRightArmIncreasing = true;
                        }
                        rightArmPose.setX(rightArmPose.getX() + (isRightArmIncreasing ? dx : -dx));
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
        }
    }

    private boolean doesMapContainRescueCloset(Vector[] rescueClosetLocations, Vector rescueClosetInQuestion) {
        for (Vector v : rescueClosetLocations) {
            if (v == rescueClosetInQuestion) return true;
        }
        return false;
    }
}
