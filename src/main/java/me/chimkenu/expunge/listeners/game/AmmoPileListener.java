package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.items.weapons.guns.Gun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AmmoPileListener extends GameListener {
    public AmmoPileListener(JavaPlugin plugin, GameManager gameManager) {
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
        Gun gun = Utils.getPlayerHeldGun(itemStack);
        if (gun == null) {
            return;
        } else if (gun.getTier() == Tier.SPECIAL) {
            player.sendActionBar(Component.text("There's no ammo for this weapon...", NamedTextColor.RED));
            return;
        }
        player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 1, 1);
        player.sendActionBar(Component.text("+Ammo", NamedTextColor.BLUE));
        ShootListener.setAmmo(itemStack, gun.getMaxAmmo());
        itemStack.setAmount(gun.getClipSize());
        player.setLevel(ShootListener.getAmmo(itemStack));
    }
}
