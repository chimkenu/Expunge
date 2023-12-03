package me.chimkenu.expunge.items.interactables;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Explosive implements Interactable {
    private final String textureURL;
    private final Component name;

    public Explosive(String textureURL, Component name) {
        this.textureURL = textureURL;
        this.name = name;
    }

    @Override
    public Entity spawn(Location locationToSpawn) {
        ArmorStand armorStand = locationToSpawn.getWorld().spawn(locationToSpawn.subtract(0, 1.4, 0), ArmorStand.class);
        armorStand.addScoreboardTag(getTag());
        armorStand.getEquipment().setHelmet(Utils.getSkull(textureURL));
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        return armorStand;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public Slot getSlot() {
        return Slot.SENARY;
    }

    @Override
    public ItemStack get() {
        ItemStack item = Utils.getSkull(textureURL);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(getName().decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);
        return item;
    }
}
