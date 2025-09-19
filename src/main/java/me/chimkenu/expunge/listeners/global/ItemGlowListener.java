package me.chimkenu.expunge.listeners.global;

import me.chimkenu.expunge.utils.PacketUtil;
import me.chimkenu.expunge.utils.RayTrace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ItemGlowListener implements Listener {
    final HashMap<Player, Set<Item>> glow = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        var threshold = 0.8;
        var player = e.getPlayer();
        glow.putIfAbsent(player, new HashSet<>());

        var loc = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(3));
        HashSet<Item> set = new HashSet<>();
        for (var entity : player.getWorld().getNearbyEntities(loc, 3, 3, 3)) {
            if (entity instanceof Item item) {
                Vector eye = player.getEyeLocation().toVector();
                Vector toItem = item.getLocation().toVector().add(new Vector(0, 0.25, 0)).subtract(eye);
                var length = toItem.length();
                double dot = player.getEyeLocation().getDirection().dot(toItem) / length;

                var shouldGlow = true;
                for (Vector v : new RayTrace(eye, toItem.normalize()).traverse(length, 0.65)) {
                    var l = v.toLocation(player.getWorld());
                    var block = v.toLocation(player.getWorld()).getBlock();
                    if (!block.isPassable() && block.getBoundingBox().contains(v)) {
                        shouldGlow = false;
                        break;
                    }
                }
                if (!shouldGlow) {
                    continue;
                }

                if (dot >= threshold) {
                    set.add(item);
                }
            }
        }

        var items = glow.get(player);
        items.removeIf(item -> {
            if (!set.contains(item)) {
                PacketUtil.setGlowingForPlayer(item, player, false);
                return true;
            }
            return false;
        });
        set.forEach(item -> {
            if (!items.contains(item)) {
                PacketUtil.setGlowingForPlayer(item, player, true);
                items.add(item);
            }
        });
    }
}
