package me.chimkenu.expunge.game;

import me.chimkenu.expunge.enums.GameItems;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.items.interactables.Interactable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemRandomizer {
    private final double x;
    private final double y;
    private final double z;
    private final double chance;
    private final int count;
    private final boolean isInvulnerable;
    private final List<GameItems> possibilities;

    public ItemRandomizer(double x, double y, double z, double chance, int count, List<GameItems> possibilities) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chance = chance;
        this.count = count;
        this.isInvulnerable = false;
        this.possibilities = possibilities;
    }

    public ItemRandomizer(double x, double y, double z, double chance, int count, Preset preset) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chance = chance;
        this.count = count;
        this.isInvulnerable = false;
        this.possibilities = preset.gameItems();
    }

    public ItemRandomizer(double x, double y, double z, double chance, int count, boolean isInvulnerable, Preset preset) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chance = chance;
        this.count = count;
        this.isInvulnerable = isInvulnerable;
        this.possibilities = preset.gameItems();
    }

    public ItemRandomizer(double x, double y, double z, double chance, int count, boolean isInvulnerable, List<GameItems> possibilities) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chance = chance;
        this.count = count;
        this.isInvulnerable = isInvulnerable;
        this.possibilities = possibilities;
    }

    public void randomize(GameManager gameManager) {
        for (int i = 0; i < count; i++) {
            if (ThreadLocalRandom.current().nextDouble() < chance) {
                GameItem gameItems = possibilities.get((ThreadLocalRandom.current().nextInt(possibilities.size()))).getGameItem();
                Entity entity;
                if (gameItems instanceof Interactable interactable) {
                    entity = interactable.spawn(new Location(gameManager.getWorld(), x, y, z));
                } else {
                    entity = gameManager.getWorld().dropItem(new Location(gameManager.getWorld(), x, y, z), possibilities.get((ThreadLocalRandom.current().nextInt(possibilities.size()))).getGameItem().get());
                    entity.setInvulnerable(isInvulnerable);
                }
                gameManager.getDirector().getItemHandler().addEntity(entity);
            }
        }
    }

    public enum Preset {
        TIER1_GUNS {
            @Override
            public List<GameItems> gameItems() {
                List<GameItems> gameItems = new ArrayList<>(GameItems.getGuns());
                gameItems.removeIf(gameItem -> gameItem.getGameItem().getTier() != Tier.TIER1);
                return gameItems;
            }
        },
        TIER2_GUNS {
            @Override
            public List<GameItems> gameItems() {
                List<GameItems> gameItems = new ArrayList<>(GameItems.getGuns());
                gameItems.removeIf(gameItem -> gameItem.getGameItem().getTier() != Tier.TIER2);
                return gameItems;
            }
        },
        MELEE {
            @Override
            public List<GameItems> gameItems() {
                List<GameItems> gameItems = new ArrayList<>(GameItems.getMelees());
                gameItems.removeIf(gameItem -> gameItem.getGameItem().getTier() == Tier.SPECIAL);
                return gameItems;
            }
        },
        TIER1_UTILITY {
            @Override
            public List<GameItems> gameItems() {
                List<GameItems> gameItems = new ArrayList<>(GameItems.getUtilities());
                gameItems.removeIf(gameItem -> gameItem.getGameItem().getTier() != Tier.TIER1);
                return gameItems;
            }
        },
        TIER2_UTILITY {
            @Override
            public List<GameItems> gameItems() {
                List<GameItems> gameItems = new ArrayList<>(GameItems.getUtilities());
                gameItems.removeIf(gameItem -> gameItem.getGameItem().getTier() != Tier.TIER2);
                return gameItems;
            }
        };

        public abstract List<GameItems> gameItems();
    }
}
