package carpentersblocks.util.registry;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class FeatureRegistry {
    
    public static boolean enableCovers                 = true;
    public static boolean enableOverlays               = true;
    public static boolean enableSideCovers             = true;
    public static boolean enableDyeColors              = true;
    public static boolean enablePatterns               = true;
    public static boolean enableFancyFluids            = true;
    public static boolean enableTorchWeatherEffects    = true;
    public static boolean enableOwnership              = true;
    public static boolean enableTile                   = true;

    public static String[] overlay_list;
    
    public static int slopeSmoothness = 2;
    
    /**
     * Initializes configuration properties.
     */
    public static void initFeatures(FMLPreInitializationEvent event, Configuration config)
    {
        enableCovers     = config.get("features",      "Enable Covers",     enableCovers).getBoolean(enableCovers);
        enableOverlays   = config.get("overlays",    "Enable Overlays",   enableOverlays).getBoolean(enableOverlays);
        enableSideCovers = config.get("features", "Enable Side Covers", enableSideCovers).getBoolean(enableSideCovers);
        enableDyeColors  = config.get("features",  "Enable Dye Colors",  enableDyeColors).getBoolean(enableDyeColors);
        enablePatterns   = config.get("features",    "Enable Patterns",   enablePatterns).getBoolean(enablePatterns);
        enableTile       = config.get("features",        "Enable Tile",       enableTile).getBoolean(enableTile);
        
        Property fancyFluidsProp = config.get("features", "Enable Fancy Fluids", enableFancyFluids);
        fancyFluidsProp.comment = "When enabled, unobstructed stationary fluid adjacent to block will render in the same space.\nNote: this only takes effect when Fancy Graphics are enabled.";
        enableFancyFluids = fancyFluidsProp.getBoolean(enableFancyFluids);
        
        Property ownershipProp = config.get("features", "Enable Ownership", enableOwnership);
        ownershipProp.comment = "This will prevent players besides you and server operators from editing your objects.\nNote: this does not protect objects against destruction (intentional), and may allow activation if appropriate. Also, the Carpenter's Safe is not affected by this.";
        enableOwnership = ownershipProp.getBoolean(enableOwnership);
        
        Property slopeSmoothnessProp = config.get("slope", "Smoothness", slopeSmoothness);
        slopeSmoothnessProp.comment = "This controls the smoothness of the slope faces.\nNote: smoothness of 2 is similar to stairs, while a value above 25 is generally fluid.";
        slopeSmoothness = slopeSmoothnessProp.getInt(slopeSmoothness);
        
        Property torchWeatherEffectsProp = config.get("torch", "Enable Torch Weather Effects", enableTorchWeatherEffects);
        torchWeatherEffectsProp.comment = "This controls whether torches extinguish themselves when exposed to rain or snow.";
        enableTorchWeatherEffects = torchWeatherEffectsProp.getBoolean(enableTorchWeatherEffects);

        Property overlayList = config.get("overlays", "Overlay Items", new String[] { "item.seeds:grass", "item.snowball:snow", "item.string:web", "tile.vine:vine", "item.wheat:hay", "tile.mushroom:mycelium" });
        overlayList.comment = "This maps items that can be used as overlays.\nItems are prefixed with unlocalized names (get these from en_US.lang from resource jar)\nOverlay suffixes are :grass, :snow, :web, :vine, :hay, :mycelium";
        overlay_list = overlayList.getStringList();
    }
    
}
