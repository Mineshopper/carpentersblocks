package com.carpentersblocks.util.handler;

import org.apache.logging.log4j.Level;

import com.carpentersblocks.renderer.helper.LightingHelper;
import com.carpentersblocks.util.ModLogger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShadersHandler {

    private static Class<?> ShadersClass;
    public static boolean enableShaderModCoreIntegration = false;

    public static void init()
    {
        try {
            ShadersClass = Class.forName("shadersmodcore.client.Shaders");
            ModLogger.log(Level.INFO, "ShadersModCore integration successful.");
            enableShaderModCoreIntegration = true;
        } catch (ClassNotFoundException e) {}
    }

    /**
     * Prepares lighting properties based on whether.
     */
    public static void updateLightness()
    {
        try {
            float[] lightness = LightingHelper.LIGHTNESS;
            lightness[0] = ShadersClass.getDeclaredField("blockLightLevel05").getFloat(null);
            lightness[2] = lightness[3] = ShadersClass.getDeclaredField("blockLightLevel08").getFloat(null);
            lightness[4] = lightness[5] = ShadersClass.getDeclaredField("blockLightLevel06").getFloat(null);
        } catch (Exception e) {
            ModLogger.log(Level.WARN, "ShadersModCore integration failed.");
            enableShaderModCoreIntegration = false;
        }
    }

}
