package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.game.DifficultySettings;
import me.chimkenu.expunge.mobs.MobSettings;
import me.chimkenu.expunge.mobs.MobType;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Material;

import java.util.HashMap;

public enum CampaignDifficulty {
    EASY {
        @Override
        public String displayName() {
            return ChatUtil.format("&3Easy");
        }

        @Override
        public Material material() {
            return Material.GREEN_STAINED_GLASS_PANE;
        }

        @Override
        public DifficultySettings settings() {
            var map = new HashMap<MobType, MobSettings>();
            for (var type : MobType.values()) {
                map.put(type, new MobSettings(20, 2, 100));
            }
            return new DifficultySettings(
                    map,
                    15,
                    20 * 3,
                    20 * 10,
                    10,
                    4,
                    2
            );
        }
    },
    NORMAL {
        @Override
        public String displayName() {
            return ChatUtil.format("&eNormal");
        }

        @Override
        public Material material() {
            return Material.YELLOW_STAINED_GLASS_PANE;
        }

        @Override
        public DifficultySettings settings() {
            var map = new HashMap<MobType, MobSettings>();
            for (var type : MobType.values()) {
                map.put(type, new MobSettings(20, 2, 100));
            }
            return new DifficultySettings(
                    map,
                    20,
                    20 * 3,
                    20 * 10,
                    18,
                    3,
                    2
            );
        }
    },
    HARD {
        @Override
        public String displayName() {
            return ChatUtil.format("&aHard");
        }

        @Override
        public Material material() {
            return Material.ORANGE_STAINED_GLASS_PANE;
        }

        @Override
        public DifficultySettings settings() {
            var map = new HashMap<MobType, MobSettings>();
            for (var type : MobType.values()) {
                map.put(type, new MobSettings(20, 2, 100));
            }
            return new DifficultySettings(
                    map,
                    25,
                    20 * 2,
                    20 * 8,
                    25,
                    3,
                    2
            );
        }
    },
    SUFFERING {
        @Override
        public String displayName() {
            return ChatUtil.format("&4Suffering");
        }

        @Override
        public Material material() {
            return Material.RED_STAINED_GLASS_PANE;
        }

        @Override
        public DifficultySettings settings() {
            var map = new HashMap<MobType, MobSettings>();
            for (var type : MobType.values()) {
                map.put(type, new MobSettings(20, 2, 100));
            }
            return new DifficultySettings(
                    map,
                    30,
                    20,
                    20 * 6,
                    30,
                    3,
                    2
            );
        }
    };

    public abstract String displayName();
    public abstract Material material();
    public abstract DifficultySettings settings();
}
