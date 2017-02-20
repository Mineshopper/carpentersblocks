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
        dyeMap.put("dyeBlack", ItemDye.DYE_COLORS[0]);
        dyeMap.put("dyeRed", ItemDye.DYE_COLORS[1]);
        dyeMap.put("dyeGreen", ItemDye.DYE_COLORS[2]);
        dyeMap.put("dyeBrown", ItemDye.DYE_COLORS[3]);
        dyeMap.put("dyeBlue", ItemDye.DYE_COLORS[4]);
        dyeMap.put("dyePurple", ItemDye.DYE_COLORS[5]);
        dyeMap.put("dyeCyan", ItemDye.DYE_COLORS[6]);
        dyeMap.put("dyeLightGray", ItemDye.DYE_COLORS[7]);
        dyeMap.put("dyeGray", ItemDye.DYE_COLORS[8]);
        dyeMap.put("dyePink", ItemDye.DYE_COLORS[9]);
        dyeMap.put("dyeLime", ItemDye.DYE_COLORS[10]);
        dyeMap.put("dyeYellow", ItemDye.DYE_COLORS[11]);
        dyeMap.put("dyeLightBlue", ItemDye.DYE_COLORS[12]);
        dyeMap.put("dyeMagenta", ItemDye.DYE_COLORS[13]);
        dyeMap.put("dyeOrange", ItemDye.DYE_COLORS[14]);
        dyeMap.put("dyeWhite", ItemDye.DYE_COLORS[15]);
    }

    /**
     * Returns vanilla dye metadata value for OreDictionary dye ItemStack.
     *
     * @param itemStack
     * @return
     */
    public static int getVanillaDmgValue(ItemStack itemStack) {
        int color = getColor(itemStack);
        for (int idx = 0; idx < ItemDye.DYE_COLORS.length; ++idx) {
            if (color == ItemDye.DYE_COLORS[idx]) {
                return 15 - idx;
            }
        }
        return 15;
    }

    /**
     * Returns definition for ItemStack from OreDictionary.
     */
    public static String getOreDictName(ItemStack itemStack) {
        for (int id : OreDictionary.getOreIDs(itemStack)) {
            String result = OreDictionary.getOreName(id);
            if (result.startsWith("dye") && result.length() > 3) {
                return result;
            }
        }
        return "Unknown";
    }

    /**
     * Returns true if ItemStack is a dye.
     */
    public static boolean isDye(ItemStack itemStack, boolean allowWhite) {
        String name = getOreDictName(itemStack);
        return dyeMap.containsKey(name) && (name.equals("dyeWhite") && !allowWhite ? false : true);
    }

    /**
     * Returns a integer with hex for 0xrrggbb based on ItemStack.
     */
    public static int getColor(ItemStack itemStack) {
        return getColor(getOreDictName(itemStack));
    }

    /**
     * Returns a integer with hex for 0xrrggbb based on ore dictionary name.
     */
    public static int getColor(String dye) {
        int color = 0xffffff;
        Object dyeLookup = dyeMap.get(dye);
        if (dyeLookup != null) {
            color = (Integer) dyeLookup;
        }
        return color;
    }

}