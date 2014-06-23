package carpentersblocks.util.handler;

import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersDaylightSensor;
import carpentersblocks.tileentity.TECarpentersFlowerPot;
import carpentersblocks.tileentity.TECarpentersSafe;
import carpentersblocks.tileentity.TECarpentersTorch;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntityHandler {

    /**
     * Registers tile entities.
     */
    public static void init()
    {
        GameRegistry.registerTileEntity(                    TEBase.class,          "TileEntityCarpentersBlock");
        GameRegistry.registerTileEntity(TECarpentersDaylightSensor.class, "TileEntityCarpentersDaylightSensor");
        GameRegistry.registerTileEntity(         TECarpentersTorch.class,          "TileEntityCarpentersTorch");
        GameRegistry.registerTileEntity(          TECarpentersSafe.class,           "TileEntityCarpentersSafe");
        GameRegistry.registerTileEntity(     TECarpentersFlowerPot.class,      "TileEntityCarpentersFlowerPot");

        registerCompatibilityMappings();
    }

    /**
     * Add tile entity class mappings needed to retain compatibility
     * with older versions of this mod.
     */
    private static void registerCompatibilityMappings()
    {
        GameRegistry.registerTileEntity(                    TEBase.class, "TileEntityCarpentersSlope"); // Mapping prior to MC 1.7+ migration
        GameRegistry.registerTileEntity(TECarpentersDaylightSensor.class,   "TileEntityCarpentersExt"); // Mapping prior to MC 1.7+ migration
    }

}
