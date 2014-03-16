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
        //dyeColor.put(    "dyeWhite", ItemDye.field_150922_c[15]);
  
    }
    
    /**
     * Grabs dye object from dyeColor map, or null if not found.
     */
    public static Object getDyeObject(ItemStack itemStack)
    {
        return dyeColor.get(OreDictionary.getOreName(OreDictionary.getOreID(itemStack)));
    }
        
    /**
     * Looks up ItemStack in OreDictionary to identify whether it is a dye.
     * Returns representative dye color as integer.
     */
    public static int getColor(ItemStack itemStack)
    {
        int color = 0xffffff;
        
        if (itemStack != null) {

            Object dyeLookup = getDyeObject(itemStack);
            
            if (dyeLookup != null) {
                color = (Integer) dyeLookup;
            }
            
        }
        
        return color;
    }
    
    /**
     * Returns RGB from integer color.
     */
    public static float[] getRGB(int color)
    {
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        return new float[] { red, green, blue };
    }
    
}
