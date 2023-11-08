package me.chimkenu.expunge.items.utilities.healing;

import me.chimkenu.expunge.game.LocalGameManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Defibrillator implements Healing {
    @Override
    public void use(JavaPlugin plugin, LocalGameManager localGameManager, LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return;
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cThis doesn't work yet."));
    }

    @Override
    public int getCooldown() {
        return 20;
    }

    @Override
    public Material getMaterial() {
        return Material.NETHERITE_SCRAP;
    }

    @Override
    public String getName() {
        return "&eDefibrillator";
    }
}
