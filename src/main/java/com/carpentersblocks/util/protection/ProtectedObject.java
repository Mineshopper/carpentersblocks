package com.carpentersblocks.util.protection;

import net.minecraft.entity.player.PlayerEntity;

public class ProtectedObject {

    private PlayerEntity playerEntity;

	public ProtectedObject(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }
	
	public PlayerEntity getPlayerEntity() {
		return playerEntity;
	}

    @Override
    public String toString() {
        if (!playerEntity.level.isClientSide()) {
            if (playerEntity.getServer().usesAuthentication()) {
                return playerEntity.getUUID().toString();
            }
        }
        return playerEntity.getDisplayName().getString();
    }

}
