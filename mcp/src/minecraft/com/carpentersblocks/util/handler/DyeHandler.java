package com.carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DyeHandler {

    private final static Map<String, Integer> dyeMap;
    static {
        dyeMap = new HashMap<String, Integer>();
        dyeMap.put("dyeBlack", ItemDye.dyeColors[0]);
        dyeMap.put("dyeRed", ItemDye.dyeColors[1]);
        dyeMap.put("dyeGreen", ItemDye.dyeColors[2]);
        dyeMap.put("dyeBrown", ItemDye.dyeColors[3]);
        dyeMap.put("dyeBlue", ItemDye.dyeColors[4]);
        dyeMap.put("dyePurple", ItemDye.dyeColors[5]);
        dyeMap.put("dyeCyan", ItemDye.dyeColors[6]);
        dyeMap.put("dyeLightGray", ItemDye.dyeColors[7]);
        dyeMap.put("dyeGray", ItemDye.dyeColors[8]);
        dyeMap.put("dyePink", ItemDye.dyeColors[9]);
        dyeMap.put("dyeLime", ItemDye.dyeColors[10]);
        dyeMap.put("dyeYellow", ItemDye.dyeColors[11]);
        dyeMap.put("dyeLightBlue", ItemDye.dyeColors[12]);
        dyeMap.put("dyeMagenta", ItemDye.dyeColors[13]);
        dyeMap.put("dyeOrange", ItemDye.dyeColors[14]);
        dyeMap.put("dyeWhite", ItemDye.dyeColors[15]);
    }

    /**
     * Returns vanilla dye metadata value for OreDictionary dye ItemStack.
     *
     * @param itemStack
     * @return
     */
    public static int getVanillaDmgValue(ItemStack itemStack)
    {
        int color = getColor(itemStack);

        for (int idx = 0; idx < ItemDye.dyeColors.length; ++idx) {
            if (color == ItemDye.dyeColors[idx]) {
                return 15 - idx;
            }
        }

        return 15;
    }

    /**
     * Returns definition for ItemStack from OreDictionary.
     */
    public static String getOreDictName(ItemStack itemStack)
    {
        return OreDictionary.getOreName(OreDictionary.getOreID(itemStack));
    }

    /**
     * Returns true if ItemStack is a dye.
     */
    public static boolean isDye(ItemStack itemStack, boolean allowWhite)
    {
        String name = getOreDictName(itemStack);
        return dyeMap.containsKey(name) && (name.equals("dyeWhite") && !allowWhite ? false : true);
    }

    /**
     * Returns a integer with hex for 0xrrggbb based on ItemStack.
     */
    public static int getColor(ItemStack itemStack)
    {
        return getColor(getOreDictName(itemStack));
    }

    /**
     * Returns a integer with hex for 0xrrggbb based on ore dictionary name.
     */
    public static int getColor(String dye)
    {
        int color = 0xffffff;

        Object dyeLookup = dyeMap.get(dye);

        if (dyeLookup != null) {
            color = (Integer) dyeLookup;
        }

        return color;
    }

}
