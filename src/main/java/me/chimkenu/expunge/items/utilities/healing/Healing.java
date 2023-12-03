package me.chimkenu.expunge.items.utilities.healing;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.utilities.Utility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public interface Healing extends Utility {
    default void attemptUse(JavaPlugin plugin, GameManager gameManager, Player player, ItemStack itemStack, int useTime, boolean hasToStayStill, Component prefix, GameAction gameActionWhenSuccessful) {
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
                player.sendActionBar(prefix.append(Component.space()).append(getProgressBar()));

                if (finalLoc != null && finalLoc.distanceSquared(player.getLocation().toVector()) > 1) {
                    player.sendActionBar(Component.text("Stopped. ", NamedTextColor.RED).append(Component.text("(You moved)", NamedTextColor.DARK_GRAY)));
                    player.setCooldown(itemStack.getType(), 0);
                    this.cancel();
                }
                if (!player.getInventory().getItemInMainHand().equals(itemStack)) {
                    player.sendActionBar(Component.text("Stopped. ", NamedTextColor.RED));
                    player.setCooldown(itemStack.getType(), 0);
                    this.cancel();
                }
                if (i <= 0) {
                    player.sendActionBar(Component.text("Successful.", NamedTextColor.GREEN));
                    gameActionWhenSuccessful.run(plugin, gameManager, player);
                    player.setCooldown(itemStack.getType(), 0);
                    this.cancel();
                }
            }

            @NotNull
            private Component getProgressBar() {
                double percentage = (double) (useTime - i) / useTime;
                int progress = (int) (percentage * 10);
                percentage = (int) (percentage * 100);

                // create progress bar
                Component progressBarComplete = Component.text("|".repeat(Math.max(0, progress)), NamedTextColor.GREEN);
                Component progressBarIncomplete = Component.text("|".repeat(Math.max(0, 10 - progress)), NamedTextColor.GRAY);
                return Component.text("Progress: ", NamedTextColor.YELLOW)
                        .append(Component.text("[", NamedTextColor.GRAY))
                        .append(progressBarComplete)
                        .append(progressBarIncomplete)
                        .append(Component.text("]", NamedTextColor.GRAY))
                        .append(Component.text(" [" + percentage + "%]", NamedTextColor.DARK_GRAY));
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    @Override
    default Slot getSlot() {
        return Slot.QUATERNARY;
    }
}
