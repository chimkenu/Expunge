package me.chimkenu.expunge.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public enum Difficulty {
    EASY {
        @Override
        public Component component() {
            return Component.text("Easy", NamedTextColor.GREEN);
        }

        @Override
        public Material material() {
            return Material.GREEN_STAINED_GLASS_PANE;
        }
    },
    NORMAL {
        @Override
        public Component component() {
            return Component.text("Normal", NamedTextColor.YELLOW);
        }

        @Override
        public Material material() {
            return Material.YELLOW_STAINED_GLASS_PANE;
        }
    },
    HARD {
        @Override
        public Component component() {
            return Component.text("Hard", NamedTextColor.RED);
        }

        @Override
        public Material material() {
            return Material.ORANGE_STAINED_GLASS_PANE;
        }
    },
    SUFFERING {
        @Override
        public Component component() {
            return Component.text("Suffering", NamedTextColor.DARK_RED);
        }

        @Override
        public Material material() {
            return Material.RED_STAINED_GLASS_PANE;
        }
    };

    public abstract Component component();
    public abstract Material material();
}
