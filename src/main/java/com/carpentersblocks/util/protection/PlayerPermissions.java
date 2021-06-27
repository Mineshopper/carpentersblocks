package com.carpentersblocks.util.protection;

import java.util.UUID;

import com.carpentersblocks.config.Configuration;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerPermissions {

    /**
     * Returns true if player is operator.
     * Can only return true if called server-side.
     */
    public static boolean isOp(PlayerEntity playerEntity) {
        if (!playerEntity.level.isClientSide()) {
            return playerEntity.getServer().getPlayerList().getOps().get(playerEntity.getGameProfile()) != null;
        } else {
            return false;
        }
    }

    /**
     * Whether player is an operator, owner or is in a singleplayer server.
     *
     * @param  object the {@link IProtected} block or entity
     * @param  playerEntity the {@link PlayerEntity}
     * @param  enforceOwnership whether ownership is required, bypassing configuration settings
     * @return <code>true</code> if player has elevated permission
     */
    public static boolean hasElevatedPermission(IProtected object, PlayerEntity playerEntity, boolean enforceOwnership) {
        if (playerEntity.level.isClientSide() && Minecraft.getInstance().hasSingleplayerServer()) { // Check if client is playing singleplayer
            return true;
        } else if (!playerEntity.level.isClientSide() && playerEntity.getServer().isSingleplayer()) { // Check if server is integrated (singleplayer)
            return true;
        } else if (isOp(playerEntity)) {
            return true;
        } else if (!enforceOwnership && !Configuration.isOwnershipEnabled()) {
            return true;
        } else {
            return isOwner(object, playerEntity);
        }
    }

    /**
     * Whether the player is the owner of the object.
     *
     * @param object
     * @param playerEntity
     * @return <code>true</code> if player is owner
     */
    private static boolean isOwner(IProtected object, PlayerEntity playerEntity) {
        try {
            UUID.fromString(object.getOwner());
            return object.getOwner().equals(playerEntity.getUUID().toString());
        } catch (IllegalArgumentException e) {
            return object.getOwner().equals(playerEntity.getDisplayName().getString());
        }
    }

}
