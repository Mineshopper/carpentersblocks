package com.carpentersblocks.util.registry;

import java.util.ArrayList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class FeatureRegistry {

    public static boolean enableCovers              = true;
    public static boolean enableOverlays            = true;
    public static boolean enableSideCovers          = true;
    public static boolean enableDyeColors           = true;
    public static boolean enableChiselDesigns       = true;
    public static boolean enableTorchWeatherEffects = true;
    public static boolean enableOwnership           = true;
    public static boolean enableIllumination        = true;
    public static boolean enableRoutableFluids      = false;
    public static boolean enableAlphaPanes          = true;

    public static ArrayList<String> overlayItems    = new ArrayList<String>();
    public static ArrayList<String> coverExceptions = new ArrayList<String>();

    public static int slopeSmoothness = 2;

    /**
     * Initializes configuration properties.
     */
    public static void preInit(FMLPreInitializationEvent event, Configuration config)
    {
        enableCovers        = config.get("features",         "Enable Covers",        enableCovers).getBoolean(enableCovers);
        enableOverlays      = config.get("features",       "Enable Overlays",      enableOverlays).getBoolean(enableOverlays);
        enableSideCovers    = config.get("features",    "Enable Side Covers",    enableSideCovers).getBoolean(enableSideCovers);
        enableDyeColors     = config.get("features",     "Enable Dye Colors",     enableDyeColors).getBoolean(enableDyeColors);
        enableChiselDesigns = config.get("features", "Enable Chisel Designs", enableChiselDesigns).getBoolean(enableChiselDesigns);

        Property routableFluidsProp = config.get("features", "Routable Fluids", enableRoutableFluids);
        routableFluidsProp.comment = "When enabled, unobstructed stationary fluid adjacent to block will render in the block space.\nNote: when enabled, you may experience noticeable chunk update lag spikes.";
        enableRoutableFluids = routableFluidsProp.getBoolean(enableRoutableFluids);

        Property illuminationProp = config.get("features", "Enable Illumination", enableIllumination);
        illuminationProp.comment = "This will enable players to cover blocks with glowstone dust to make them illuminate.";
        enableIllumination = illuminationProp.getBoolean(enableIllumination);

        Property ownershipProp = config.get("features", "Enable Ownership", enableOwnership);
        ownershipProp.comment = "This will prevent players besides you and server operators from editing your objects.\nNote: this does not protect objects against destruction (intentional), and may allow activation if appropriate. Also, the Carpenter's Safe is not affected by this.";
        enableOwnership = ownershipProp.getBoolean(enableOwnership);

        Property slopeSmoothnessProp = config.get("features", "Smoothness", slopeSmoothness);
        slopeSmoothnessProp.comment = "This controls the smoothness of the slope faces.\nNote: smoothness of 2 is similar to stairs, while a value above 25 is generally fluid.";
        slopeSmoothness = slopeSmoothnessProp.getInt(slopeSmoothness);

        Property torchWeatherEffectsProp = config.get("features", "Enable Torch Weather Effects", enableTorchWeatherEffects);
        torchWeatherEffectsProp.comment = "This controls whether torches extinguish themselves when exposed to rain or snow.";
        enableTorchWeatherEffects = torchWeatherEffectsProp.getBoolean(enableTorchWeatherEffects);

        Property alphaPaneProp = config.get("features", "Enable Pane Alpha Rendering", enableAlphaPanes);
        alphaPaneProp.comment = "This controls whether panes in doors and hatches should render on alpha pass.\nThis is needed to allow translucent window glass, for instance.";
        enableAlphaPanes = alphaPaneProp.getBoolean(enableAlphaPanes);

        Property overlayList = config.get("features", "Overlay Definitions", new String[] { "Seeds:grass", "Snowball:snow", "String:web", "Vines:vine", "Wheat:hay", "Mushroom:mycelium" });
        overlayList.comment = "This maps items to overlays.\nItems are prefixed with display names (en_US only).\nOverlay suffixes are :grass, :snow, :web, :vine, :hay, :mycelium";
        for (String item : overlayList.getStringList()) {
            overlayItems.add(item);
        }

        Property coverExceptionList = config.get(
                "features",
                "Cover Exceptions",
                new String[] {
                        "Silverwood Planks", // Thaumcraft
                        "Greatwood Planks",  // Thaumcraft
                        "Thatch"  // TerraFirmaCraft
                });
        coverExceptionList.comment = "This allows restricted blocks to be used as covers.\nAdd your own by supplying the display name for the block (en_US only).";
        for (String item : coverExceptionList.getStringList()) {
            coverExceptions.add(item);
        }
    }

}
