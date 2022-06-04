package me.chimkenu.expunge.guns.utilities;

import me.chimkenu.expunge.guns.shoot.ShootFlash;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class Flash extends Utility {

    public Flash() {
        super(20, Material.LIGHT_GRAY_CANDLE, "&7Flash", true);
    }

    @Override
    public void use(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);
        ShootFlash.shoot(player);
    }
}
