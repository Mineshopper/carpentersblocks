package com.carpentersblocks.util.registry;

import java.util.ArrayList;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
    public static boolean enableRailSlopes          = true;
    public static boolean enableGarageDoorVoidFill  = true;
    public static boolean enableFreeStandingLadders = false;

    public static ArrayList<String> overlayItems    = new ArrayList<String>();
    public static ArrayList<String> coverExceptions = new ArrayList<String>();

    public static int     slopeSmoothness           = 2;
    public static int     multiBlockSizeLimit       = 500;

    /**
     * Initializes configuration properties.
     */
    public static void preInit(FMLPreInitializationEvent event, Configuration config) {
        enableCovers              = config.get("features",               "Enable Covers",              enableCovers).getBoolean(enableCovers);
        enableOverlays            = config.get("features",             "Enable Overlays",            enableOverlays).getBoolean(enableOverlays);
        enableSideCovers          = config.get("features",          "Enable Side Covers",          enableSideCovers).getBoolean(enableSideCovers);
        enableDyeColors           = config.get("features",           "Enable Dye Colors",           enableDyeColors).getBoolean(enableDyeColors);
        enableChiselDesigns       = config.get("features",       "Enable Chisel Designs",       enableChiselDesigns).getBoolean(enableChiselDesigns);
        enableFreeStandingLadders = config.get("features", "Enable Freestanding Ladders", enableFreeStandingLadders).getBoolean(enableFreeStandingLadders);

        Property routableFluidsProp = config.get("features", "Routable Fluids", enableRoutableFluids);
        routableFluidsProp.setComment("When enabled, unobstructed stationary fluid adjacent to block will render in the block space.\nNote: when enabled, you may experience noticeable chunk update lag spikes.");
        enableRoutableFluids = routableFluidsProp.getBoolean(enableRoutableFluids);

        Property illuminationProp = config.get("features", "Enable Illumination", enableIllumination);
        illuminationProp.setComment("This will enable players to cover blocks with glowstone dust to make them illuminate.");
        enableIllumination = illuminationProp.getBoolean(enableIllumination);

        Property ownershipProp = config.get("features", "Enable Ownership", enableOwnership);
        ownershipProp.setComment("This will prevent players besides you and server operators from editing your objects.\nNote: this does not protect objects against destruction (intentional), and may allow activation if appropriate. Also, the Carpenter's Safe is not affected by this.");
        enableOwnership = ownershipProp.getBoolean(enableOwnership);

        Property slopeSmoothnessProp = config.get("features", "Smoothness", slopeSmoothness);
        slopeSmoothnessProp.setComment("This controls the smoothness of the slope faces.\nNote: smoothness of 2 is similar to stairs, while a value above 25 is generally fluid.");
        slopeSmoothness = slopeSmoothnessProp.getInt(slopeSmoothness);

        Property multiBlockSizeLimitProp = config.get("features", "MultiBlock Size Limit", multiBlockSizeLimit);
        multiBlockSizeLimitProp.setComment("This controls how many blocks can be connected as a single entity.\nNote: only applies to Garage Doors.");
        multiBlockSizeLimit = multiBlockSizeLimitProp.getInt(multiBlockSizeLimit);

        Property torchWeatherEffectsProp = config.get("features", "Enable Torch Weather Effects", enableTorchWeatherEffects);
        torchWeatherEffectsProp.setComment("This controls whether torches extinguish themselves when exposed to rain or snow.");
        enableTorchWeatherEffects = torchWeatherEffectsProp.getBoolean(enableTorchWeatherEffects);

        Property alphaPaneProp = config.get("features", "Enable Pane Alpha Rendering", enableAlphaPanes);
        alphaPaneProp.setComment("This controls whether panes (used in doors, hatches, and other blocks) should render on alpha pass.\nThis is needed to allow translucent window glass, for instance.");
        enableAlphaPanes = alphaPaneProp.getBoolean(enableAlphaPanes);

        Property railSlopesProp = config.get("features", "Enable Rail Slope Fill", enableRailSlopes);
        railSlopesProp.setComment("This allows Carpenter's Blocks with solid top faces to create slopes above them when a sloping rail is above the block.");
        enableRailSlopes = railSlopesProp.getBoolean(enableRailSlopes);

        Property garageDoorVoidFillProp = config.get("features", "Enable Garage Door Void Autofill", enableGarageDoorVoidFill);
        garageDoorVoidFillProp.setComment("This allows garage doors to automatically fill in gaps when barriers beneath doors are destroyed.");
        enableGarageDoorVoidFill = garageDoorVoidFillProp.getBoolean(enableGarageDoorVoidFill);

        Property overlayList = config.get("features", "Overlay Definitions", new String[] { "Seeds:grass", "Snowball:snow", "String:web", "Vines:vine", "Wheat:hay", "Mushroom:mycelium" });
        overlayList.setComment("This maps items to overlays.\nItems are prefixed with display names (en_US only).\nOverlay suffixes are :grass, :snow, :web, :vine, :hay, :mycelium");
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
        coverExceptionList.setComment("This allows restricted blocks to be used as covers.\nAdd your own by supplying the display name for the block (en_US only).");
        for (String item : coverExceptionList.getStringList()) {
            coverExceptions.add(item);
        }
    }

}
