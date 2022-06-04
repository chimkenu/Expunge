package me.chimkenu.expunge.guns.utilities;

import me.chimkenu.expunge.guns.shoot.ShootSmoke;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class Smoke extends Utility {

    public Smoke() {
        super(20, Material.SEA_PICKLE, "&bSmoke", true);
    }

    @Override
    public void use(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);
        ShootSmoke.shoot(player);
    }
}