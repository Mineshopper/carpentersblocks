package com.carpentersblocks.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EntityLivingUtil {

    /**
     * Decrements the player's currently active ItemStack.
     *
     * @param entityPlayer the player
     */
    public static void decrementCurrentSlot(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        if (!itemStack.isEmpty()) {
            if (!entityPlayer.capabilities.isCreativeMode) {
            	itemStack.shrink(1);
            	if (itemStack.isEmpty()) {
            		entityPlayer.inventory.removeStackFromSlot(entityPlayer.inventory.currentItem);
            	}
            }
        }
    }

}
