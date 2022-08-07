package me.chimkenu.expunge.enums;

import org.bukkit.ChatColor;

public enum Difficulty {
    EASY {
        @Override
        public String string() {
            return ChatColor.GREEN + "Easy";
        }
    },
    NORMAL {
        @Override
        public String string() {
            return ChatColor.YELLOW + "Normal";
        }
    },
    HARD {
        @Override
        public String string() {
            return ChatColor.RED + "Hard";
        }
    },
    SUFFERING {
        @Override
        public String string() {
            return ChatColor.DARK_RED + "Suffering";
        }
    };
    public abstract String string();
}
