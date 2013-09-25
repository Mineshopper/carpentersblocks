package carpentersblocks.util.handler;

import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.tileentity.TECarpentersBlockExt;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class FeatureHandler
{

    public static boolean enableCovers = true;
    public static boolean enableOverlays = true;
    public static boolean enableSideCovers = true;
    public static boolean enableDyeColors = true;
    public static boolean enableFancyFluids = true;

    public static int hitboxPrecision = 2;

    public static boolean enableZFightingFix = false;
    public static boolean enableOptifineIntegration = true;
    public static boolean enablePlantSupport = true;
	
    /**
     * Initializes configuration properties.
     */
    public static void initProps(FMLPreInitializationEvent event)
    {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        enableCovers = config.get("features", "Enable Covers", enableCovers).getBoolean(enableCovers);
        enableOverlays = config.get("features", "Enable Overlays", enableOverlays).getBoolean(enableOverlays);
        enableSideCovers = config.get("features", "Enable Side Covers", enableSideCovers).getBoolean(enableSideCovers);
        enableDyeColors = config.get("features", "Enable Dye Colors", enableDyeColors).getBoolean(enableDyeColors);
        enableFancyFluids = config.get("features", "Enable Fancy Fluids", enableFancyFluids).getBoolean(enableFancyFluids);

        Property enableZFightingFixProp = config.get("rendering", "enableZFightingFix", enableZFightingFix);
        enableZFightingFixProp.comment = "Setting this to true will resolve z-fighting with chiseled patterns\nthat may occur with Optifine or other client-side performance mods.\nWill cause all Carpenter's Blocks to be invisible behind ice or water.";
        enableZFightingFix = enableZFightingFixProp.getBoolean(enableZFightingFix);
        
        Property enableOptifineIntegrationProp = config.get("rendering", "enableOptifineIntegration", enableOptifineIntegration);
        enableOptifineIntegrationProp.comment = "Provides integration with Optifine's block coloring methods.\nNeeded to support Custom Colors.";
        enableOptifineIntegration = enableOptifineIntegrationProp.getBoolean(enableOptifineIntegration);
        
        Property hitboxPrecisionProp = config.get("slope", "hitboxPrecision", hitboxPrecision);
        hitboxPrecisionProp.comment = "This controls the smoothness of the slope faces (excluding oblique interior corners).\nSmoothness of 2 is similar to stairs.\nA value of 50 is recommended for fluidity, but higher values under certain configurations will create collision bugs.";
        hitboxPrecision = hitboxPrecisionProp.getInt(hitboxPrecision);
        
        config.save();
    }
    
    /**
     * Registers tile entities.
     */
    public static void registerTileEntities()
    {
    	// String ID reflects old class name for compatibility with versions prior to v1.6
    	GameRegistry.registerTileEntity(TECarpentersBlock.class, "TileEntityCarpentersSlope");
    	GameRegistry.registerTileEntity(TECarpentersBlockExt.class, "TileEntityCarpentersExt");
    }
	
}
