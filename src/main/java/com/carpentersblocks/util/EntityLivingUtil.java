package com.carpentersblocks.util;

import com.carpentersblocks.nbt.CbTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;

public class EntityLivingUtil {

    /**
     * Decrements the player's currently active ItemStack.
     *
     * @param playerEntity the player
     */
    public static void decrementCurrentSlot(PlayerEntity playerEntity) {
        ItemStack itemStack = playerEntity.inventory.getSelected();
        if (!itemStack.isEmpty()) {
            if (!playerEntity.isCreative()) {
            	playerEntity.inventory.removeItem(playerEntity.inventory.selected, 1);
            }
        }
    }
    
    /**
     * Gets the {@link CbTileEntity} object at player's feet, if one exists.
     * <p>
     * It is safer to gather the tile entity reference than a block reference.
     *
     * @param entity the entity
     * @return the tile entity, or null if it does not exist
     */
    public static CbTileEntity getTileEntityAtFeet(Entity entity) {
        int x = MathHelper.floor(entity.getX());
        int y = MathHelper.floor(entity.getY() - 0.20000000298023224D - entity.getMyRidingOffset());
        int z = MathHelper.floor(entity.getZ());
        TileEntity tileEntity = entity.level.getBlockEntity(new BlockPos(x, y, z));
        if (tileEntity != null && tileEntity instanceof CbTileEntity) {
            return (CbTileEntity) tileEntity;
        } else {
            return null;
        }
    }

    /**
     * Determines if the entity is moving in the x, z directions on
     * solid ground.
     *
     * @param livingEntity the living entity
     * @return <code>true</code> if entity is moving on ground
     */
    public static boolean isMovingOnGround(LivingEntity livingEntity) {
        return !livingEntity.isFallFlying() &&
        		(livingEntity.getDeltaMovement().x() != 0 || livingEntity.getDeltaMovement().z() != 0);
    }
    
    /**
     * Get block ray trace result.
     * 
     * @param playerEntity the event player
     * @return a block ray trace result
     */
    public static BlockRayTraceResult calculateBlockRayTraceResult(LivingEntity livingEntity) {
    	double blockReach = Minecraft.getInstance().gameMode.getPickRange();
    	return (BlockRayTraceResult) livingEntity.pick(blockReach, 0.0f, false);
    }

}
