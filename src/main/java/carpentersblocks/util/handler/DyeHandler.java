package carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.oredict.OreDictionary;

public class DyeHandler {

    private final static Map<String, Integer> dyeMap;
    static {
        dyeMap = new HashMap<String, Integer>();
        dyeMap.put("dyeBlack", ItemDye.field_150922_c[0]);
        dyeMap.put("dyeRed", ItemDye.field_150922_c[1]);
        dyeMap.put("dyeGreen", ItemDye.field_150922_c[2]);
        dyeMap.put("dyeBrown", ItemDye.field_150922_c[3]);
        dyeMap.put("dyeBlue", ItemDye.field_150922_c[4]);
        dyeMap.put("dyePurple", ItemDye.field_150922_c[5]);
        dyeMap.put("dyeCyan", ItemDye.field_150922_c[6]);
        dyeMap.put("dyeLightGray", ItemDye.field_150922_c[7]);
        dyeMap.put("dyeGray", ItemDye.field_150922_c[8]);
        dyeMap.put("dyePink", ItemDye.field_150922_c[9]);
        dyeMap.put("dyeLime", ItemDye.field_150922_c[10]);
        dyeMap.put("dyeYellow", ItemDye.field_150922_c[11]);
        dyeMap.put("dyeLightBlue", ItemDye.field_150922_c[12]);
        dyeMap.put("dyeMagenta", ItemDye.field_150922_c[13]);
        dyeMap.put("dyeOrange", ItemDye.field_150922_c[14]);
        dyeMap.put("dyeWhite", ItemDye.field_150922_c[15]);
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

        for (int idx = 0; idx < ItemDye.field_150922_c.length; ++idx) {
            if (color == ItemDye.field_150922_c[idx]) {
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
        if (ForgeVersion.getBuildVersion() > 1112) {
            for (int id : OreDictionary.getOreIDs(itemStack)) {
                String result = OreDictionary.getOreName(id);
                if (result.startsWith("dye") && result.length() > 3) {
                    return result;
                }
            }
            return "Unknown";
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
        return dyeMap.containsKey(name) ? (name.equals("dyeWhite") && !allowWhite ? false : true) : false;
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
