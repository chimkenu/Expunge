package me.chimkenu.expunge.game;

import me.chimkenu.expunge.entities.MobSettings;
import me.chimkenu.expunge.entities.MobType;

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
