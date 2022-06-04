package me.chimkenu.expunge.guns.utilities;

import me.chimkenu.expunge.guns.shoot.ShootMolotov;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class Molotov extends Utility {

    public Molotov() {
        super(20, Material.LANTERN, "&6Molotov", true);
    }

    @Override
    public void use(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);
        ShootMolotov.shoot(player);
    }
}
