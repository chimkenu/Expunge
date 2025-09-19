package me.chimkenu.expunge.utils;

import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class PacketUtil {
    public static void toggleRedBorderEffect(Player bukkitPlayer, boolean enabled) {
        try {
            ServerPlayer nmsPlayer = ((CraftPlayer) bukkitPlayer).getHandle();
            ServerLevel level = nmsPlayer.level();

            // Create new fake border
            var border = new WorldBorder();

            Field worldField = WorldBorder.class.getDeclaredField("world");
            worldField.setAccessible(true);
            worldField.set(border, level);

            border.setWarningBlocks(10);
            border.setWarningTime(5);


            var big = 600000000;
            if (enabled) {
                border.setCenter(nmsPlayer.getX() + big, nmsPlayer.getZ() + big);
                border.setSize(-1);
                nmsPlayer.connection.send(new ClientboundInitializeBorderPacket(border));
            } else {
                border.setCenter(nmsPlayer.getX(), nmsPlayer.getZ());
                border.setSize(big);
                nmsPlayer.connection.send(new ClientboundInitializeBorderPacket(border));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static EntityDataAccessor<Byte> FLAGS_ACCESSOR;

    static {
        try {
            Field field = Entity.class.getDeclaredField("DATA_SHARED_FLAGS_ID");
            field.setAccessible(true);
            FLAGS_ACCESSOR = (EntityDataAccessor<Byte>) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setGlowingForPlayer(org.bukkit.entity.Entity entity, Player target, boolean glowing) {
        try {
            ServerPlayer nmsTarget = ((CraftPlayer) target).getHandle();
            Entity nmsEntity = ((CraftEntity) entity).getHandle();

            SynchedEntityData data = nmsEntity.getEntityData();

            // Read current flags via accessor
            byte flags = data.get(FLAGS_ACCESSOR);

            // Modify glowing bit
            if (glowing) flags |= 0x40;
            else flags &= ~0x40;

            // Build DataValue for packet
            SynchedEntityData.DataValue<Byte> packetValue = new SynchedEntityData.DataValue<>(0, EntityDataSerializers.BYTE, flags);

            // Send packet to specific player
            ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(
                    nmsEntity.getId(),
                    Collections.singletonList(packetValue)
            );

            nmsTarget.connection.send(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setGlowingForPlayer(List<org.bukkit.entity.Entity> entities, Player target, boolean glowing) {
        for (org.bukkit.entity.Entity entity : entities) {
            setGlowingForPlayer(entity, target, glowing);
        }
    }
}
