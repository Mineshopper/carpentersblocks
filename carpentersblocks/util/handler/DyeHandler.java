package carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

public class DyeHandler {

    /* Grab colors here for easy reuse. */
    
    private int DYE_BLACK      = ItemDye.field_150922_c[0];
    private int DYE_RED        = ItemDye.field_150922_c[1];
    private int DYE_GREEN      = ItemDye.field_150922_c[2];
    private int DYE_BROWN      = ItemDye.field_150922_c[3];
    private int DYE_BLUE       = ItemDye.field_150922_c[4];
    private int DYE_PURPLE     = ItemDye.field_150922_c[5];
    private int DYE_CYAN       = ItemDye.field_150922_c[6];
    private int DYE_SILVER     = ItemDye.field_150922_c[7];
    private int DYE_GRAY       = ItemDye.field_150922_c[8];
    private int DYE_PINK       = ItemDye.field_150922_c[9];
    private int DYE_LIME       = ItemDye.field_150922_c[10];
    private int DYE_YELLOW     = ItemDye.field_150922_c[11];
    private int DYE_LIGHT_BLUE = ItemDye.field_150922_c[12];
    private int DYE_MAGENTA    = ItemDye.field_150922_c[13];
    private int DYE_ORANGE     = ItemDye.field_150922_c[14];
    
    public static Map dyeMap = new HashMap();
    
    /**
     * Initializes dye colors.
     */
    public static void init()
    {
        /* Vanilla dyes. */
        
        for (int dye = 0; dye < 15; ++dye) {
            dyeMap.put("item.dyePowder." + ItemDye.field_150923_a[dye], ItemDye.field_150922_c[dye]);
        }

        /* Mod dyes. */
        
    }
        
    /**
     * Returns RGB from dye ItemStack, or no color if item does
     * not represent a dye.
     */
    public static int getColor(ItemStack itemStack)
    {
        if (itemStack != null) {
            return (Integer) dyeMap.get(itemStack.getUnlocalizedName());
        }
        
        return 0xffffff;
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
