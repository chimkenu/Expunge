package me.chimkenu.expunge.game;

import me.chimkenu.expunge.mobs.MobSettings;
import me.chimkenu.expunge.mobs.MobType;

import java.util.Map;

public record DifficultySettings(
        Map<MobType, MobSettings> mobs,
        int startingMobCount,
        int commonMobQueueFrequency,
        int specialMobQueueFrequency,
        int maxHordeSpawnSize,
        int peakNodeBuffer,
        int relaxNodeBuffer
) {
}
