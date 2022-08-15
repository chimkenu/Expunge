package me.chimkenu.expunge.guns.utilities.healing;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.game.listeners.DeathRevive;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Medkit extends Healing {
    public Medkit() {
        super(20, Material.BRICK, "&cMedkit", Slot.QUATERNARY,false);
    }

    @Override
    public void use(LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return;
        }
        if (player.getHealth() >= 19) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou still have some health left..."));
            return;
        }
        if (Healing.usingUtility.contains(player)) {
            return;
        }

        Healing.usingUtility.add(player);
        attemptUse(player, getUtility().getType(), 20 * 5, true, "§eUsing medkit...",
                player1 -> {
                    player1.setHealth(player1.getHealth() + ((20 - player1.getHealth()) * 0.8));
                    player1.getInventory().getItemInMainHand().setAmount(player1.getInventory().getItemInMainHand().getAmount() - 1);

                    DeathRevive.currentLives.put(player1, 3);
                });
    }
}
