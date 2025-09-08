package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.ItemUtils;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.items.Gun;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AmmoPileListener extends GameListener {
    public AmmoPileListener(Expunge plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    @EventHandler
    public void onAmmoRefill(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof FallingBlock fallingBlock)) {
            return;
        }
        if (!fallingBlock.getScoreboardTags().contains("AMMO_PILE")) {
            return;
        }
        e.setCancelled(true);

        // player clicked an ammo pile
        Player player = e.getPlayer();
        // update ammo if holding gun
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        var gameItem = plugin.getItems().toGameItem(itemStack);
        if (!(gameItem instanceof Gun gun)) {
            return;
        }

        if (gun.tier() == Tier.SPECIAL) {
            ChatUtil.sendActionBar(player, "&cThere's no ammo for this weapon...");
            return;
        }

        player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 1, 1);
        ChatUtil.sendActionBar(player, "&9+Ammo");
        gun.setAmmo(itemStack, gun.maxAmmo());
        // itemStack.setAmount(gun.clipSize()); auto reload??
        player.setLevel(gun.maxAmmo());
    }
}
