package carpentersblocks.util.handler;

import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersDaylightSensor;
import carpentersblocks.tileentity.TECarpentersTorch;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntityHandler {

    /**
     * Registers tile entities.
     */
    public static void registerTileEntities()
    {
    	/* For compatibility, string descriptions may not match. */
    	GameRegistry.registerTileEntity(TEBase.class, "TileEntityCarpentersSlope");
    	GameRegistry.registerTileEntity(TECarpentersDaylightSensor.class, "TileEntityCarpentersExt");
    	GameRegistry.registerTileEntity(TECarpentersTorch.class, "TileEntityCarpentersTorch");
    }
	
}
