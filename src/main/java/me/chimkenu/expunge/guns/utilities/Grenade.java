package me.chimkenu.expunge.guns.utilities;

import me.chimkenu.expunge.guns.shoot.ShootGrenade;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class Grenade extends Utility {

    public Grenade() {
        super(20, Material.COAL, "&8Grenade", true);
    }

    @Override
    public void use(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);
        ShootGrenade.shoot(player);
    }
}
