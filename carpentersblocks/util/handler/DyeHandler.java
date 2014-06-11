package carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.oredict.OreDictionary;

public class DyeHandler {

    public static Map dyeColor = new HashMap();

    /**
     * Initializes dye colors.
     */
    public static void init()
    {
        for (int idx = 0; idx < ItemDye.field_150923_a.length; ++idx) {
            dyeColor.put("dye" + WordUtils.capitalize(ItemDye.field_150923_a[idx]), ItemDye.field_150922_c[idx]);
        }
    }

    /**
     * Returns definition for ItemStack from OreDictionary.
     */
    public static String getOreDictName(ItemStack itemStack)
    {
        if (ForgeVersion.getBuildVersion() > 1112) {
            return OreDictionary.getOreName(OreDictionary.getOreIDs(itemStack)[0]);
        } else {
            return OreDictionary.getOreName(OreDictionary.getOreID(itemStack));
        }
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
