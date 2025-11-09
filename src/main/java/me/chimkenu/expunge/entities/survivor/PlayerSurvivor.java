package me.chimkenu.expunge.entities.survivor;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.entities.item.ItemEntity;
import me.chimkenu.expunge.entities.item.MiscEntity;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.PlayerStats;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.items.ItemStack;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.ItemUtil;
import me.chimkenu.expunge.utils.PacketUtil;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class PlayerSurvivor extends Survivor {
    private static final int FULL_HUNGER = 20;
    private static final int WALK_HUNGER = 20;

    private final GameManager manager;
    private final Player player;
    private ArmorStand incapStand = null;

    @Override
    public void up() {
        super.up();
        if (getLives() == 1) {
            manager.addTask(
                    new BukkitRunnable() {
                        int i = 200;
                        @Override
                        public void run() {
                            if (i <= 0 || getLives() != 1) this.cancel();
                            if (i % 20 == 0 || i % 20 == 6) {
                                player.playSound(player, Sound.BLOCK_STONE_PLACE, SoundCategory.PLAYERS, 1, 0);
                            }
                            i--;
                        }
                    }.runTaskTimer(manager.getPlugin(), 1, 1)
            );
            PacketUtil.toggleRedBorderEffect(player, true);
        }
    }

    public PlayerSurvivor(GameManager manager, Player player) {
        super();
        this.manager = manager;
        this.player = player;

        var absorptionAttribute = player.getAttribute(Attribute.MAX_ABSORPTION);
        if (absorptionAttribute != null) {
            absorptionAttribute.setBaseValue(MAX_HEALTH);
        }
    }

    @Override
    public double getHealth() {
        return player.getHealth();
    }

    @Override
    public void setHealth(double health) {
        player.setHealth(health);
    }

    @Override
    public double getAbsorption() {
        return player.getAbsorptionAmount();
    }

    @Override
    public void setAbsorption(double absorption) {
        player.setAbsorptionAmount(absorption);
    }

    @Override
    public Location getEyeLocation() {
        return player.getEyeLocation();
    }

    @Override
    public Vector getEyeDirection() {
        return player.getEyeLocation().getDirection();
    }

    @Override
    protected void setAlive(boolean isAlive) {
        if (isAlive() == isAlive) return;

        super.setAlive(isAlive);

        if (isAlive) {
            player.setGameMode(GameMode.ADVENTURE);
            PacketUtil.toggleRedBorderEffect(player, false);
        } else {
            // spawn in an armor stand which can be revived
            spawnDeadArmorStand();
            player.setGameMode(GameMode.SPECTATOR);

            // dead people drop their hotbar
            PlayerInventory inventory = player.getInventory();
            for (var slot : Slot.values()) {
                var v = slot.ordinal();
                for (int i = 0; i < 2; i++) {
                    org.bukkit.inventory.ItemStack item = inventory.getItem(v + (i * 9));
                    if (item != null) {
                        Expunge.getItems().toGameItem(item).ifPresent(gameItem -> {
                            Item dropItem = player.getWorld().dropItem(player.getLocation(), item);
                            manager.addEntity(new ItemEntity(gameItem, dropItem));
                        });
                    }
                    inventory.setItem(v + (i * 9), new org.bukkit.inventory.ItemStack(Material.AIR));
                }
            }
            // TODO: player.getInventory().clear(5); // pistol if they were down
        }
    }

    @Override
    protected void setIncapacitated(boolean isIncapacitated) {
        if (isIncapacitated() == isIncapacitated) return;

        super.setIncapacitated(isIncapacitated);

        if (isIncapacitated) {
            // incapacitate: force player to sit down and shoot
            var loc = getEyeLocation();
            while (loc.getBlock().getType().equals(Material.AIR)) {
                loc.subtract(0, 0.25, 0);
            }
            loc.setY(loc.getBlock().getBoundingBox().getMaxY() - 0.95);

            if (incapStand != null) incapStand.remove();
            incapStand = player.getWorld().spawn(loc, ArmorStand.class);
            incapStand.setGravity(false);
            incapStand.setInvulnerable(true);
            incapStand.setInvisible(true);
            incapStand.setSmall(true);
            incapStand.addPassenger(player);

            addEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0);

            // save hotbar in inventory and replace with a pistol
            PlayerInventory inventory = player.getInventory();
            for (var slot : Slot.values()) {
                if (slot.equals(Slot.SECONDARY)) continue;
                var v = slot.ordinal();
                inventory.setItem(v + 9, inventory.getItem(v));
                inventory.setItem(v, new org.bukkit.inventory.ItemStack(Material.AIR));
            }
            /* TODO: put pistols in secondary slot if down?
                     i think forcing players to use what they have is better.
            Expunge.getItems().toGameItem("PISTOL").ifPresent(pistol ->
                    inventory.setItem(Slot.NINE.ordinal(), pistol.toItem()));
             */

        } else {
            // get up
            removeEffect(PotionEffectType.GLOWING);

            // get hotbar back
            PlayerInventory inventory = player.getInventory();
            for (var slot : Slot.values()) {
                var v = slot.ordinal();
                inventory.setItem(v, inventory.getItem(v + 9));
                inventory.setItem(v + 9, new org.bukkit.inventory.ItemStack(Material.AIR));
            }

            if (incapStand != null) {
                incapStand.remove();
                incapStand = null;
            }
        }
    }

    @Override
    public void setCanSprint(boolean canSprint) {
        player.setFoodLevel(canSprint ? FULL_HUNGER : WALK_HUNGER);
    }

    @Override
    public Slot getActiveSlot() {
        return Slot.values()[player.getInventory().getHeldItemSlot()];
    }

    @Override
    public Optional<ItemStack> getItemSlot(Slot slot) {
        var stack = player.getInventory().getItem(slot.ordinal());
        if (stack == null) {
            return Optional.empty();
        }

        var item = Expunge.getItems().toGameItem(stack);
        return item.map(gameItem -> new ItemStack(gameItem, stack));
    }

    @Override
    public void setItemSlot(Slot slot, @Nullable ItemStack item) {
        player.getInventory().setItem(slot.ordinal(), item == null ? null : item.stack());
    }

    @Override
    public int getCooldown(GameItem item) {
        return player.getCooldown(item.material());
    }

    @Override
    public void setCooldown(GameItem item, int cooldown) {
        player.setCooldown(item.material(), cooldown);
    }

    @Override
    public void addEffect(PotionEffectType potionEffectType, int duration, int amplifier) {
        player.addPotionEffect(new PotionEffect(potionEffectType, duration, amplifier, false, false, false));
    }

    @Override
    public void removeEffect(PotionEffectType potionEffectType) {
        player.removePotionEffect(potionEffectType);
    }

    @Override
    public boolean hasEffect(PotionEffectType potionEffectType) {
        return player.hasPotionEffect(potionEffectType);
    }

    @Override
    public void clearEffects() {
        player.getActivePotionEffects().forEach(e -> player.removePotionEffect(e.getType()));
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public void setLocation(Location l) {
        if (player.getVehicle() != null) player.getVehicle().remove();
        player.teleport(l);
        player.setGameMode(GameMode.ADVENTURE);
    }

    @Override
    public Vector getVelocity() {
        return player.getVelocity();
    }

    @Override
    public void setVelocity(Vector v) {
        player.setVelocity(v);
    }

    @Override
    public void remove() {
        player.damage(Double.MAX_VALUE);
    }

    @Override
    public boolean isDead() {
        return player.isDead();
    }

    @Override
    public String getIdentifier() {
        return player.getDisplayName();
    }

    @Override
    public Player getHandle() {
        return player;
    }

    private void spawnDeadArmorStand() {
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
            manager.addEntity(new MiscEntity(a));
        }

        upper.getEquipment().setHelmet(ItemUtil.getSkull(player));
        upper.getEquipment().setLeggings(new org.bukkit.inventory.ItemStack(Material.AIR));
        upper.getEquipment().setBoots(new org.bukkit.inventory.ItemStack(Material.AIR));
        upper.setLeftArmPose(new EulerAngle(-1.22, -0.524, -0.175));
        upper.setRightArmPose(new EulerAngle(-1.31, 0.524, 0.175));
        upper.setHeadPose(new EulerAngle(-1.571, -0.07, 0));
        upper.setBodyPose(new EulerAngle(-1.518, -0.017, 0));
        upper.setArms(true);

        upper.addScoreboardTag(player.getUniqueId().toString());
        upper.addScoreboardTag(upper.getUniqueId().toString());
        upper.addScoreboardTag(lower.getUniqueId().toString());

        lower.getEquipment().setChestplate(new org.bukkit.inventory.ItemStack(Material.AIR));
        lower.setLeftLegPose(new EulerAngle(-1.588, -0.524, -0.0175));
        lower.setRightLegPose(new EulerAngle(-1.5533, 0.5236, 0.0175));
        lower.setInvisible(true);
    }
}
