package me.chimkenu.expunge.items.utilities.healing;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.game.director.Director;
import me.chimkenu.expunge.items.utilities.Utility;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface Healing extends Utility {
    default void attemptUse(JavaPlugin plugin, Director director, Player player, ItemStack itemStack, int useTime, boolean hasToStayStill, String prefix, GameAction gameActionWhenSuccessful) {
        Vector loc = null;
        if (hasToStayStill) {
            loc = player.getLocation().toVector();
        }

        Vector finalLoc = loc;
        new BukkitRunnable() {
            int i = useTime;

            @Override
            public void run() {
                i--;
                // display status as action bar
                // percentage completion
                String progress_bar = getProgressBar();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(prefix + " " + progress_bar));

                if (finalLoc != null && finalLoc.distanceSquared(player.getLocation().toVector()) > 1) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cStopped. §8(You moved)"));
                    player.setCooldown(itemStack.getType(), 0);
                    this.cancel();
                }
                if (!player.getInventory().getItemInMainHand().equals(itemStack)) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cStopped."));
                    player.setCooldown(itemStack.getType(), 0);
                    this.cancel();
                }
                if (i <= 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§aSuccessful."));
                    gameActionWhenSuccessful.run(plugin, director.getGameManager(), player);
                    player.setCooldown(itemStack.getType(), 0);
                    this.cancel();
                }
            }

            @NotNull
            private String getProgressBar() {
                double percentage = (double) (useTime - i) / useTime;
                int progress = (int) (percentage * 10);
                percentage = (int) (percentage * 100);

                // create progress bar
                String progress_bar_complete = "|".repeat(Math.max(0, progress));
                String progress_bar_incomplete = "|".repeat(Math.max(0, 10 - progress));
                String progress_bar = "§e" + "Progress: " + "§7" + "[" + "§a" + progress_bar_complete + "§7" + progress_bar_incomplete + "§7" + "]" + "§8" + " [" + percentage + "%]";
                return progress_bar;
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    @Override
    default Slot getSlot() {
        return Slot.QUATERNARY;
    }
}
