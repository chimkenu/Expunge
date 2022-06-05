package me.chimkenu.expunge.guns.utilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Adrenaline extends Healing {
    public Adrenaline() {
        super(20, Material.TRIPWIRE_HOOK, "&eAdrenaline", false);
    }

    @Override
    public void use(Player player) {
        if (Healing.usingUtility.contains(player)) {
            return;
        }
        Healing.usingUtility.add(player);
        attemptUse(player, getUtility().getType(), 20, false, "§eUsing adrenaline...", player1 -> {
            player1.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 0, false, false, true));
            player1.setAbsorptionAmount(Math.min(20, player1.getAbsorptionAmount() + 5));
            player1.getInventory().getItemInMainHand().setAmount(player1.getInventory().getItemInMainHand().getAmount() - 1);
        });
    }
}
