package carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DyeHandler {

    public static Map dyeColor = new HashMap();

    /**
     * Initializes dye colors.
     */
    public static void init()
    {
        dyeColor.put(    "dyeBlack", ItemDye.field_150922_c[0] );
        dyeColor.put(      "dyeRed", ItemDye.field_150922_c[1] );
        dyeColor.put(    "dyeGreen", ItemDye.field_150922_c[2] );
        dyeColor.put(    "dyeBrown", ItemDye.field_150922_c[3] );
        dyeColor.put(     "dyeBlue", ItemDye.field_150922_c[4] );
        dyeColor.put(   "dyePurple", ItemDye.field_150922_c[5] );
        dyeColor.put(     "dyeCyan", ItemDye.field_150922_c[6] );
        dyeColor.put("dyeLightGray", ItemDye.field_150922_c[7] );
        dyeColor.put(     "dyeGray", ItemDye.field_150922_c[8] );
        dyeColor.put(     "dyePink", ItemDye.field_150922_c[9] );
        dyeColor.put(     "dyeLime", ItemDye.field_150922_c[10]);
        dyeColor.put(   "dyeYellow", ItemDye.field_150922_c[11]);
        dyeColor.put("dyeLightBlue", ItemDye.field_150922_c[12]);
        dyeColor.put(  "dyeMagenta", ItemDye.field_150922_c[13]);
        dyeColor.put(   "dyeOrange", ItemDye.field_150922_c[14]);
        dyeColor.put(    "dyeWhite", ItemDye.field_150922_c[15]);

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
        return dyeColor.containsKey(name) ? (name.equals("dyeWhite") && !allowWhite ? false : true) : false;
    }

    /**
     * Returns color value based on ItemStack.
     */
    public static int getColor(ItemStack itemStack)
    {
        return getColor(getOreDictName(itemStack));
    }

    /**
     * Returns color value based on ore dictionary name.
     */
    public static int getColor(String dye)
    {
        int color = 0xffffff;

        Object dyeLookup = dyeColor.get(dye);

        if (dyeLookup != null) {
            color = (Integer) dyeLookup;
        }

        return color;
    }

}
