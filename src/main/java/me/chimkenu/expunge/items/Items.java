package me.chimkenu.expunge.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.chimkenu.expunge.utils.ResourceCopy;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;

public final class Items {
    private final JavaPlugin plugin;
    private final String itemConfigFile;
    private final HashMap<NamespacedKey, GameItem> items;

    public Items(JavaPlugin plugin, String configFileName) {
        this.plugin = plugin;
        this.itemConfigFile = configFileName;
        items = new HashMap<>();
        reload();
    }

    public void reload() {
        plugin.getLogger().log(Level.INFO, "LOADING ITEMS...");
        var itemConfigFile = new File(plugin.getDataFolder(), this.itemConfigFile);
        if (!itemConfigFile.exists()) {
            itemConfigFile.mkdirs();
            try {
                ResourceCopy.copyFromJar(plugin.getClass(), this.itemConfigFile, itemConfigFile.toPath());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            List<GameItem> itemList = mapper.readValue(
                    itemConfigFile,
                    mapper.getTypeFactory().constructCollectionType(List.class, GameItem.class)
            );
            itemList.forEach(item -> {
                if (items.containsKey(item.namespacedKey())) {
                    items.put(item.namespacedKey(), item);
                    plugin.getLogger().log(
                            Level.WARNING,
                            "Tried to load item " + item.name() + " with id " + item.id() + ", but conflicts with " + items.get(item.namespacedKey()).name(), " assigned to the same id."
                    );
                    return;
                }

                items.put(item.namespacedKey(), item);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<GameItem> toGameItem(String id) {
        return items.values().stream().filter(gi -> Objects.equals(gi.id(), id)).findFirst();
    }

    public Optional<GameItem> toGameItem(ItemStack itemStack) {
        var meta = itemStack.getItemMeta();
        if (meta != null) {
            for (var key : meta.getPersistentDataContainer().getKeys()) {
                if (items.containsKey(key)) {
                    return Optional.of(items.get(key));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Throwable> toThrowable(Projectile projectile) {
        for (var key : projectile.getPersistentDataContainer().getKeys()) {
            var item = items.get(key);
            if (item instanceof Throwable throwable) {
                return Optional.of(throwable);
            }
        }
        return Optional.empty();
    }

    public Optional<Interactable> toInteractable(Entity entity) {
        for (var key : entity.getPersistentDataContainer().getKeys()) {
            var item = items.get(key);
            if (item instanceof Interactable interactable) {
                return Optional.of(interactable);
            }
        }
        return Optional.empty();
    }

    public Collection<GameItem> list() {
        return items.values();
    }

    public List<GameItem> list(Predicate<? super GameItem> filter) {
        return items.values().stream().filter(filter).toList();
    }
}
