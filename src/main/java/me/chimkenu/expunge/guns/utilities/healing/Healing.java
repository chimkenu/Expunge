package me.chimkenu.expunge.guns.utilities.healing;

import me.chimkenu.expunge.Action;
import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.guns.utilities.Utility;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public abstract class Healing extends Utility {

    public static final ArrayList<Player> usingUtility = new ArrayList<>();

    private final boolean isInstantUse;

    public Healing(int cooldown, Material material, String name, Slot slot, boolean isInstantUse) {
        super(cooldown, material, name, slot);
        this.isInstantUse = isInstantUse;
    }

    public boolean isInstantUse() {
        return isInstantUse;
    }

    public static void attemptUse(Player player, Material material, int useTime, boolean hasToStayStill, String prefix, Action actionWhenSuccessful) {
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
                double percentage = (double) (useTime - i) / useTime;
                int progress = (int) (percentage * 10);
                percentage = (int) (percentage * 100);

                // create progress bar
                String progress_bar_complete = "|".repeat(Math.max(0, progress));
                String progress_bar_incomplete = "|".repeat(Math.max(0, 10 - progress));
                String progress_bar = "§e" + "Progress: " + "§7" + "[" + "§a" + progress_bar_complete + "§7" + progress_bar_incomplete + "§7" + "]" + "§8" + " [" + percentage + "%]";
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(prefix + " " + progress_bar));

                if (finalLoc != null) {
                    Vector diff = finalLoc.clone().subtract(player.getLocation().toVector());
                    double sum = diff.getX() + diff.getY() + diff.getZ();
                    if (sum > 1) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cStopped. §8(You moved)"));
                        usingUtility.remove(player);
                        this.cancel();
                    }
                }
                if (!player.getInventory().getItemInMainHand().getType().equals(material)) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cStopped."));
                    usingUtility.remove(player);
                    this.cancel();
                }
                if (i <= 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§aSuccessful."));
                    actionWhenSuccessful.run(player);
                    usingUtility.remove(player);
                    this.cancel();
                }
            }
        }.runTaskTimer(Expunge.instance, 1, 1);
    }
}
