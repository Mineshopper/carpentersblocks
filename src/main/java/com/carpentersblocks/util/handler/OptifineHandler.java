package com.carpentersblocks.util.handler;

import java.lang.reflect.Method;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import org.apache.logging.log4j.Level;
import com.carpentersblocks.util.ModLogger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OptifineHandler {

    public static boolean enableOptifineIntegration = false;
    private static Method getColorMultiplier;

    /**
     * Initializes Optifine integration.
     * If reflection fails, will return false.
     */
    public static void init()
    {
        try {
            Class<?> CustomColorizer = Class.forName("CustomColorizer");
            getColorMultiplier = CustomColorizer.getMethod("getColorMultiplier", Block.class, IBlockAccess.class, int.class, int.class, int.class);
            ModLogger.log(Level.INFO, "Optifine integration successful.");
            enableOptifineIntegration = true;
        } catch (Exception e) {
            ModLogger.log(Level.WARN, "Optifine integration failed: " + e.getMessage());
        }
    }

    public static int getColorMultiplier(Block block, IBlockAccess blockAccess, int x, int y, int z)
    {
        int colorMultiplier = block.colorMultiplier(blockAccess, x, y, z);
        try {
            colorMultiplier = (Integer) getColorMultiplier.invoke(null, block, blockAccess, x, y, z);
        } catch (Exception e) {
            ModLogger.log(Level.WARN, "Block custom coloring failed, disabling Optifine integration: " + e.getMessage());
            enableOptifineIntegration = false;
        }

        return colorMultiplier;
    }

}
