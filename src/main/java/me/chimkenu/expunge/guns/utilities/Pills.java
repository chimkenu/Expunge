package me.chimkenu.expunge.guns.utilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Pills extends Healing {
    public Pills() {
        super(20, Material.WHITE_CANDLE, "&fPills", false);
    }

    @Override
    public void use(Player player) {
        if (Healing.usingUtility.contains(player)) {
            return;
        }

        Healing.usingUtility.add(player);
        attemptUse(player, getUtility().getType(), 20, false, "§eUsing pills...", player1 -> {
            player1.setAbsorptionAmount(Math.min(20, player1.getAbsorptionAmount() + 10));
            player1.getInventory().getItemInMainHand().setAmount(player1.getInventory().getItemInMainHand().getAmount() - 1);
        });
    }
}
