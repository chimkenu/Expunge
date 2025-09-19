package me.chimkenu.expunge.game;

import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.items.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemRandomizer {
    public static final int MATCH_PLAYER_COUNT = -1;

    private final Vector pos;
    private final double chance;
    private final int count;
    private final boolean isInvulnerable;

    private final List<String> possibilities;
    private final Preset preset;

    private GameItem choice = null;

    public ItemRandomizer(double x, double y, double z, double chance, int count, boolean isInvulnerable, List<String> possibilities, Preset preset) {
        this.pos = new Vector(x, y, z);
        this.chance = chance;
        this.count = count;
        this.isInvulnerable = isInvulnerable;
        this.possibilities = possibilities;
        this.preset = preset;

        // at least one must not be null, possibilities take priority over preset
        assert(possibilities != null || preset != null);
    }

    public ItemRandomizer(double x, double y, double z, double chance, int count, boolean isInvulnerable, List<String> possibilities) {
        this(x, y, z, chance, count, isInvulnerable, possibilities, null);
    }

    public ItemRandomizer(double x, double y, double z, double chance, int count, boolean isInvulnerable, Preset preset) {
        this(x, y, z, chance, count, isInvulnerable, null, preset);
    }

    public ItemRandomizer(double x, double y, double z, double chance, int count, List<String> possibilities) {
        this(x, y, z, chance, count, false, possibilities);
    }

    public ItemRandomizer(double x, double y, double z, double chance, int count, Preset preset) {
        this(x, y, z, chance, count, false, preset);
    }

    public void randomize(GameManager manager, Items items, int attempts) {
        if (choice == null || attempts == 0) {
            if (ThreadLocalRandom.current().nextDouble() >= chance) {
                return;
            }

            var choices = resolve(items);
            if (choices == null) {
                assert(preset != null);
                choices = preset.gameItems(items);
            }
            choice = choices.get((ThreadLocalRandom.current().nextInt(choices.size())));
        }

        var n = count;
        if (count == MATCH_PLAYER_COUNT) {
            n = manager.getPlayers().size();
        }
        for (int i = 0; i < n; i++) {
            spawn(manager);
        }
    }

    private void spawn(GameManager manager) {
        Entity entity;
        if (choice instanceof Interactable interactable) {
            entity = interactable.spawn(pos.toLocation(manager.getWorld()));
        } else {
            entity = manager.getWorld().dropItemNaturally(pos.toLocation(manager.getWorld()), choice.toItem());
            entity.setInvulnerable(isInvulnerable);
        }
        manager.addEntity(entity);
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
