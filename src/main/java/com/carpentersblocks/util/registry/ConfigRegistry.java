package com.carpentersblocks.util.registry;

import java.util.ArrayList;

import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigRegistry {

	// Block properties
    public static boolean enableBarrier          = false;
    public static boolean enableBed              = false;
    public static boolean enableButton           = false;
    public static boolean enableCollapsibleBlock = true;
    public static boolean enableDaylightSensor   = false;
    public static boolean enableDoor             = false;
    public static boolean enableFlowerPot        = false;
    public static boolean enableGarageDoor       = false;
    public static boolean enableGate             = false;
    public static boolean enableHatch            = false;
    public static boolean enableLadder           = false;
    public static boolean enableLever            = false;
    public static boolean enablePressurePlate    = true;
    public static boolean enableSafe             = false;
    public static boolean enableSlope            = false;
    public static boolean enableStairs           = false;
    public static boolean enableTorch            = false;
	
    // Item properties
    public static boolean enableHammer = true;
    public static boolean enableChisel = true;
    public static boolean enableTile   = true;
    public static int     itemCarpentersToolsUses       = 400;
    public static boolean itemCarpentersToolsDamageable = true;
    public static double  itemHammerDamageChanceFromSlopes      = 0.75D;
    public static double  itemHammerDamageChanceFromStairs      = 1.0D;
    public static double  itemHammerDamageChanceFromCollapsible = 0.2D;
    
    // Features
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
    public static int slopeSmoothness     = 2;
    public static int multiBlockSizeLimit = 500;
    public static ArrayList<String> overlayItems    = new ArrayList<String>();
    public static ArrayList<String> coverExceptions = new ArrayList<String>();
	
    public static void loadConfiguration(FMLPreInitializationEvent event) {
    	net.minecraftforge.common.config.Configuration config = new net.minecraftforge.common.config.Configuration(event.getSuggestedConfigurationFile());
    	config.load();
    	
    	// Blocks
    	//enableBarrier          = config.get("blocks",           "Enable Barrier",          enableBarrier).getBoolean(enableBarrier);
        //enableBed              = config.get("blocks",               "Enable Bed",              enableBed).getBoolean(enableBed);
        //enableButton           = config.get("blocks",            "Enable Button",           enableButton).getBoolean(enableButton);
        enableCollapsibleBlock = config.get("blocks", "Enable Collapsible Block", enableCollapsibleBlock).getBoolean(enableCollapsibleBlock);
        //enableDaylightSensor   = config.get("blocks",   "Enable Daylight Sensor",   enableDaylightSensor).getBoolean(enableDaylightSensor);
        //enableDoor             = config.get("blocks",              "Enable Door",             enableDoor).getBoolean(enableDoor);
        //enableFlowerPot        = config.get("blocks",        "Enable Flower Pot",        enableFlowerPot).getBoolean(enableFlowerPot);
        //enableGarageDoor       = config.get("blocks",       "Enable Garage Door",       enableGarageDoor).getBoolean(enableGarageDoor);
        //enableGate             = config.get("blocks",              "Enable Gate",             enableGate).getBoolean(enableGate);
        //enableHatch            = config.get("blocks",             "Enable Hatch",            enableHatch).getBoolean(enableHatch);
        //enableLadder           = config.get("blocks",            "Enable Ladder",           enableLadder).getBoolean(enableLadder);
        //enableLever            = config.get("blocks",             "Enable Lever",            enableLever).getBoolean(enableLever);
        enablePressurePlate    = config.get("blocks",    "Enable Pressure Plate",    enablePressurePlate).getBoolean(enablePressurePlate);
        //enableSafe             = config.get("blocks",              "Enable Safe",             enableSafe).getBoolean(enableSafe);
        //enableSlope            = config.get("blocks",             "Enable Slope",            enableSlope).getBoolean(enableSlope);
        //enableStairs           = config.get("blocks",            "Enable Stairs",           enableStairs).getBoolean(enableStairs);
        //enableTorch            = config.get("blocks",             "Enable Torch",            enableTorch).getBoolean(enableTorch);
        
    	// Items
    	enableHammer = config.get("items", "Enable Hammer", enableHammer).getBoolean(enableHammer);
        enableChisel = config.get("items", "Enable Chisel", enableChisel).getBoolean(enableChisel);
        enableTile = config.get("items", "Enable Tile", enableTile).getBoolean(enableTile);
        itemCarpentersToolsUses = config.get("items", "Vanilla Tool Uses", itemCarpentersToolsUses).getInt(itemCarpentersToolsUses);
        itemCarpentersToolsDamageable = config.get("items", "Vanilla Tools Damageable", itemCarpentersToolsDamageable).getBoolean(itemCarpentersToolsDamageable);
        itemHammerDamageChanceFromSlopes = config.get("items", "itemHammerDamageChanceFromSlopes", itemHammerDamageChanceFromSlopes).getDouble(itemHammerDamageChanceFromSlopes);
        itemHammerDamageChanceFromStairs = config.get("items", "itemHammerDamageChanceFromStairs", itemHammerDamageChanceFromStairs).getDouble(itemHammerDamageChanceFromStairs);
        itemHammerDamageChanceFromCollapsible = config.get("items", "itemHammerDamageChanceFromCollapsible", itemHammerDamageChanceFromCollapsible).getDouble(itemHammerDamageChanceFromCollapsible);
        
        // Features
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
        
	    if (config.hasChanged()) {
	        config.save();
	    }
    }
	
}
