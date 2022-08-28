package me.chimkenu.expunge.enums;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum Achievements {
    WELCOME_TO_EXPUNGE {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/welcome");
        }
    },
    SAFE_FOR_NOW {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/safe_for_now");
        }
    },
    WERE_IN_THIS_TOGETHER {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/were_in_this_together");
        }
    },
    SURVIVOR {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/survivor");
        }
    },
    ZERO_GRAVITY {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/zero_gravity");
        }
    },
    HACK_N_SLASH {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/hack_n_slash");
        }
    },
    WHEN_GUTS_FLY {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/when_guts_fly");
        }
    },
    UNDEAD_EXTERMINATOR_I {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/undead_exterminator_i");
        }
    },
    UNDEAD_EXTERMINATOR_II {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/undead_exterminator_ii");
        }
    },
    UNDEAD_EXTERMINATOR_III {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/undead_exterminator_iii");
        }
    },
    PEST_CONTROL {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/pest_control");
        }
    },
    HOWD_THAT_GET_THERE {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/howd_that_get_there");
        }
    },
    OVERDOSE {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/overdose");
        }
    },
    NO_MERCY {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:general/no_mercy");
        }
    },
    THE_DEPARTURE {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:the_departure/the_departure");
        }
    },
    FORGED_IN_FIRE {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:the_departure/forged_in_fire");
        }
    },
    COOKIE_MONSTER {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:the_departure/cookie_monster");
        }
    },
    THE_DEVELOPERS_ROOM {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:the_departure/the_developers_room");
        }
    },
    THE_BIG_LEAGUES {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:the_departure/the_big_leagues");
        }
    },
    SUBWAY_LURKERS {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:the_departure/subway_lurkers");
        }
    },
    A_BITE_TO_EAT {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:the_departure/a_bite_to_eat");
        }
    },
    HEY_DONT_TOUCH_THAT {
        @Override
        public void grant(Player player) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only expunge:the_departure/hey_dont_touch_that");
        }
    };
    public abstract void grant(Player player);
}
