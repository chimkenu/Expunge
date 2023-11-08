package me.chimkenu.expunge.items.utilities.healing;

import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.enums.Slot;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Pills implements Healing {
    @Override
    public void use(LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!getUtility().equals(item) || player.getCooldown(getUtility().getType()) > 0) {
            return;
        }

        player.setCooldown(getUtility().getType(), getCooldown() + 1);

        attemptUse(player, item, 20, false, "§eUsing pills...", player1 -> {
            double absorption = Math.min(20, player1.getAbsorptionAmount() + 10);
            player1.setAbsorptionAmount(absorption);
            player1.getInventory().getItemInMainHand().setAmount(player1.getInventory().getItemInMainHand().getAmount() - 1);

            // achievement
            if (absorption >= 20) {
                Achievements.OVERDOSE.grant(player1);
            }
        });
    }

    @Override
    public int getCooldown() {
        return 20;
    }

    @Override
    public Material getMaterial() {
        return Material.WHITE_CANDLE;
    }

    @Override
    public String getName() {
        return "&fPills";
    }

    @Override
    public Slot getSlot() {
        return Slot.QUINARY;
    }
}
