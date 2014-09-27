package com.carpentersblocks.util.handler;

import org.apache.logging.log4j.Level;
import com.carpentersblocks.util.ModLogger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShadersHandler {

    private static Class<?> ShadersClass;
    public static boolean enableShadersModCoreIntegration = false;
    public static boolean oldLighting = true;

    public static void init()
    {
        try {
            ShadersClass = Class.forName("shadersmodcore.client.Shaders");
            ModLogger.log(Level.INFO, "ShadersModCore integration successful.");
            enableShadersModCoreIntegration = true;
        } catch (ClassNotFoundException e) {}
    }

    /**
     * Updates fields based on ShadersModCore configuration.
     */
    public static void update()
    {
        try {
            oldLighting = ShadersClass.getDeclaredField("configOldLighting").getBoolean(null);
        } catch (Exception e) {
            ModLogger.log(Level.WARN, "ShadersModCore integration failed: " + e.getMessage());
            enableShadersModCoreIntegration = false;
            oldLighting = true;
        }
    }

}
