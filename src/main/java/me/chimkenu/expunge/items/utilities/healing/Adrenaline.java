package me.chimkenu.expunge.items.utilities.healing;

import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.enums.Slot;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Adrenaline extends Healing {
    public Adrenaline() {
        super(20, Material.TRIPWIRE_HOOK, "&eAdrenaline", Slot.QUINARY, false);
    }

    @Override
    public void use(LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return;
        }
        if (Healing.usingUtility.contains(player)) {
            return;
        }
        Healing.usingUtility.add(player);
        attemptUse(player, getUtility().getType(), 20, false, "§eUsing adrenaline...", player1 -> {
            player1.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 1, false, false, true));
            double absorption = Math.min(20, player1.getAbsorptionAmount() + 5);
            player1.setAbsorptionAmount(absorption);
            player1.getInventory().getItemInMainHand().setAmount(player1.getInventory().getItemInMainHand().getAmount() - 1);

            // achievement
            if (absorption >= 20) {
                Achievements.OVERDOSE.grant(player1);
            }
        });
    }
}
