package me.chimkenu.expunge.items.utilities.healing;

import me.chimkenu.expunge.game.GameManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Medkit implements Healing {
    @Override
    public void use(JavaPlugin plugin, GameManager gameManager, LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return;
        }
        if (player.getHealth() >= 19) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou still have some health left..."));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!getUtility().getType().equals(item.getType()) || player.getCooldown(getUtility().getType()) > 0) {
            return;
        }


        attemptUse(plugin, gameManager, player, item, getCooldown(), true, "§eUsing medkit...",
                (plugin1, gameManager1, player1) -> {
                    player1.setHealth(player1.getHealth() + ((20 - player1.getHealth()) * 0.8));
                    player1.getInventory().getItemInMainHand().setAmount(player1.getInventory().getItemInMainHand().getAmount() - 1);

                    gameManager1.getPlayerStat(player1).setLives(3);
                });
    }

    @Override
    public int getCooldown() {
        return 100;
    }

    @Override
    public Material getMaterial() {
        return Material.BRICK;
    }

    @Override
    public String getName() {
        return "&cMedkit";
    }
}
