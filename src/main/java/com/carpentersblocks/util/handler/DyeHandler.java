package com.carpentersblocks.util.handler;

import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;

public class DyeHandler {

    /**
     * Returns true if ItemStack is a dye.
     */
    public static boolean isDye(ItemStack itemStack, boolean allowWhite) {
    	boolean isDye = itemStack.getItem() instanceof DyeItem;
    	if (!allowWhite && DyeColor.WHITE.equals(((DyeItem)itemStack.getItem()).getDyeColor())) {
    		isDye = false;
    	}
        return isDye;
    }

    /**
     * Returns a integer with hex for 0xrrggbb based on ItemStack.
     */
    public static int getColor(ItemStack itemStack) {
    	if (!(itemStack.getItem() instanceof DyeItem)) {
    		return 0xffffff;
    	}
    	return ((DyeItem)itemStack.getItem()).getDyeColor().getColorValue();
    }

}