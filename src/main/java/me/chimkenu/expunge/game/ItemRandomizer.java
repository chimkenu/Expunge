package me.chimkenu.expunge.game;

import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.items.*;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemRandomizer {
    private final double x;
    private final double y;
    private final double z;
    private final double chance;
    private final int count;
    private final boolean isInvulnerable;

    private final List<String> possibilities;
    private final Preset preset;

    public ItemRandomizer(double x, double y, double z, double chance, int count, List<String> possibilities) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chance = chance;
        this.count = count;
        this.isInvulnerable = false;
        this.possibilities = possibilities;
        this.preset = null;
    }

    public ItemRandomizer(double x, double y, double z, double chance, int count, Preset preset) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chance = chance;
        this.count = count;
        this.isInvulnerable = false;
        this.possibilities = null;
        this.preset = preset;
    }

    public ItemRandomizer(double x, double y, double z, double chance, int count, boolean isInvulnerable, Preset preset) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chance = chance;
        this.count = count;
        this.isInvulnerable = isInvulnerable;
        this.possibilities = null;
        this.preset = preset;
    }

    public ItemRandomizer(double x, double y, double z, double chance, int count, boolean isInvulnerable, List<String> possibilities) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chance = chance;
        this.count = count;
        this.isInvulnerable = isInvulnerable;
        this.possibilities = possibilities;
        this.preset = null;
    }

    public void randomize(GameManager manager, Items items) {
        for (int i = 0; i < count; i++) {
            if (ThreadLocalRandom.current().nextDouble() < chance) {
                var choices = resolve(items);
                if (choices == null) {
                    assert(preset != null);
                    choices = preset.gameItems(items);
                }

                var item = choices.get((ThreadLocalRandom.current().nextInt(choices.size())));
                Entity entity;
                if (item instanceof Interactable interactable) {
                    entity = interactable.spawn(new Location(manager.getWorld(), x, y, z));
                } else {
                    entity = manager.getWorld().dropItem(new Location(manager.getWorld(), x, y, z), item.toItem());
                    entity.setInvulnerable(isInvulnerable);
                }
                manager.getDirector().getItemHandler().addEntity(entity);
            }
        }
    }

    private List<GameItem> resolve(Items items) {
        if (possibilities == null) {
            return null;
        }

        return items.list(item -> possibilities.contains(item.id()));
    }

    public enum Preset {
        TIER1_GUNS {
            @Override
            public List<GameItem> gameItems(Items items) {
                return items.list(item -> item instanceof Gun && item.tier() == Tier.COMMON);
            }
        },
        TIER2_GUNS {
            @Override
            public List<GameItem> gameItems(Items items) {
                return items.list(item -> item instanceof Gun && item.tier() == Tier.RARE);
            }
        },
        MELEE {
            @Override
            public List<GameItem> gameItems(Items items) {
                return items.list(item -> item instanceof Melee && item.tier() != Tier.SPECIAL);
            }
        },
        TIER1_UTILITY {
            @Override
            public List<GameItem> gameItems(Items items) {
                return items.list(item -> item instanceof Utility && item.tier() == Tier.COMMON);
            }
        },
        TIER2_UTILITY {
            @Override
            public List<GameItem> gameItems(Items items) {
                return items.list(item -> item instanceof Utility && item.tier() == Tier.RARE);
            }
        };

        public abstract List<GameItem> gameItems(Items items);
    }
}
