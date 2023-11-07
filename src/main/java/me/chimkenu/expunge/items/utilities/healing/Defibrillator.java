package me.chimkenu.expunge.items.utilities.healing;

import me.chimkenu.expunge.enums.Slot;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Defibrillator extends Healing {
    public Defibrillator() {
        super(20, Material.NETHERITE_SCRAP, "&9Defibrillator", Slot.QUATERNARY, false);
    }

    @Override
    public void use(LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return;
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cThis doesn't work yet."));
    }
}
