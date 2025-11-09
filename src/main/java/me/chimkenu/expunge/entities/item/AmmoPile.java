package me.chimkenu.expunge.entities.item;

import me.chimkenu.expunge.entities.Regenerable;
import me.chimkenu.expunge.entities.GameEntity;
import me.chimkenu.expunge.entities.RegenerableState;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

public class AmmoPile implements GameEntity, Regenerable {
    private final FallingBlock fallingBlock;

    private static FallingBlock spawn(World world, Location location) {
        FallingBlock ammoPile = world.spawnFallingBlock(
                location,
                Material.GRAY_CANDLE.createBlockData("[candles=4,lit=false,waterlogged=false]")
        );
        ammoPile.setGravity(false);
        ammoPile.setDropItem(false);
        ammoPile.setCancelDrop(true);
        ammoPile.setInvulnerable(true);
        ammoPile.setCustomName(ChatUtil.format("&7Ammo Pile (Right Click)"));
        ammoPile.setCustomNameVisible(true);
        ammoPile.addScoreboardTag("AMMO_PILE");
        return ammoPile;
    }

    public AmmoPile(World world, Location location) {
        this.fallingBlock = spawn(world, location);
    }

    @Override
    public Location getLocation() {
        return fallingBlock.getLocation();
    }

    @Override
    public void setLocation(Location l) {
        fallingBlock.teleport(l);
    }

    @Override
    public Vector getVelocity() {
        return fallingBlock.getVelocity();
    }

    @Override
    public void setVelocity(Vector v) {
        fallingBlock.setVelocity(v);
    }

    @Override
    public void remove() {
        fallingBlock.remove();
    }

    @Override
    public boolean isDead() {
        return fallingBlock.isDead();
    }

    @Override
    public String getIdentifier() {
        return "AMMO PILE - " + fallingBlock.getUniqueId();
    }

    @Override
    public FallingBlock getHandle() {
        return fallingBlock;
    }

    @Override
    public RegenerableState<AmmoPile> getState() {
        return new State(getLocation());
    }

    private record State(
            Location location
    ) implements RegenerableState<AmmoPile> {
        @Override
        public AmmoPile regenerate(World world) {
            return new AmmoPile(world, location);
        }
    }
}
