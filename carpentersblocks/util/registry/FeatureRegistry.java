package carpentersblocks.util.registry;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersDaylightSensor;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class FeatureRegistry {

    public static boolean enableCovers = true;
    public static boolean enableOverlays = true;
    public static boolean enableSideCovers = true;
    public static boolean enableDyeColors = true;
    public static boolean enableFancyFluids = true;
    public static boolean enableTorchWeatherEffects = true;
    public static boolean enableBlockOwnership = true;

    public static int slopeSmoothness = 2;

    public static boolean enableZFightingFix = false;
    public static boolean enableOptifineIntegration = true;
    public static boolean enablePlantSupport = true;
	
    /**
     * Initializes configuration properties.
     */
    public static void initFeatures(FMLPreInitializationEvent event, Configuration config)
    {
        enableCovers = config.get("features", "Enable Covers", enableCovers).getBoolean(enableCovers);
        enableOverlays = config.get("features", "Enable Overlays", enableOverlays).getBoolean(enableOverlays);
        enableSideCovers = config.get("features", "Enable Side Covers", enableSideCovers).getBoolean(enableSideCovers);
        enableDyeColors = config.get("features", "Enable Dye Colors", enableDyeColors).getBoolean(enableDyeColors);
        enableFancyFluids = config.get("features", "Enable Fancy Fluids", enableFancyFluids).getBoolean(enableFancyFluids);

        Property enableBlockOwnershipProp = config.get("features", "Enable Block Ownership", enableBlockOwnership);
        enableBlockOwnershipProp.comment = "This will prevent players besides you and server operators from editing your blocks.\nNote: this does not protect blocks against destruction (intentional), and may allow activation if appropriate. Also, the Carpenter's Safe is not affected by this.";
        enableBlockOwnership = enableBlockOwnershipProp.getBoolean(enableBlockOwnership);
        
        Property enableZFightingFixProp = config.get("rendering", "Enable Z-Fighting Fix", enableZFightingFix);
        enableZFightingFixProp.comment = "This resolves z-fighting with chiseled patterns that may occur with Optifine or other client-side performance mods.\nNote: this will likely cause all Carpenter's Blocks to be invisible behind ice or water.";
        enableZFightingFix = enableZFightingFixProp.getBoolean(enableZFightingFix);
        
        Property enableOptifineIntegrationProp = config.get("rendering", "Enable Optifine Integration", enableOptifineIntegration);
        enableOptifineIntegrationProp.comment = "Provides integration with Optifine's block coloring methods.\nNote: this is needed to support Custom Colors.";
        enableOptifineIntegration = enableOptifineIntegrationProp.getBoolean(enableOptifineIntegration);
        
        Property slopeSmoothnessProp = config.get("slope", "Smoothness", slopeSmoothness);
        slopeSmoothnessProp.comment = "This controls the smoothness of the slope faces.\nNote: smoothness of 2 is similar to stairs, while a value above 25 is generally fluid.";
        slopeSmoothness = slopeSmoothnessProp.getInt(slopeSmoothness);
        
        Property enableTorchWeatherEffectsProp = config.get("torch", "Enable Torch Weather Effects", enableTorchWeatherEffects);
        enableTorchWeatherEffectsProp.comment = "This controls whether torches extinguish themselves when exposed to rain or snow.";
        enableTorchWeatherEffects = enableTorchWeatherEffectsProp.getBoolean(enableTorchWeatherEffects);
    }
    	
}
