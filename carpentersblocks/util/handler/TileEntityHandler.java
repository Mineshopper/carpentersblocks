package carpentersblocks.util.handler;

import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersDaylightSensor;
import carpentersblocks.tileentity.TECarpentersSafe;
import carpentersblocks.tileentity.TECarpentersTorch;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntityHandler {

    /**
     * Registers tile entities.
     */
    public static void registerTileEntities()
    {
    	/*
    	 * For compatibility, string descriptions may not match.
    	 */
    	
    	// Change this to "TileEntityCarpentersBase" for 1.7+
    	GameRegistry.registerTileEntity(TEBase.class, "TileEntityCarpentersSlope");
    	
    	// Change this to "TileEntityCarpentersDaylightSensor" for 1.7+
    	GameRegistry.registerTileEntity(TECarpentersDaylightSensor.class, "TileEntityCarpentersExt");
    	
    	GameRegistry.registerTileEntity(TECarpentersTorch.class, "TileEntityCarpentersTorch");
    	GameRegistry.registerTileEntity(TECarpentersSafe.class, "TileEntityCarpentersSafe");
    	
    	// Add in 1.7+
    	//GameRegistry.registerTileEntity(TECarpentersBed.class, "TileEntityCarpentersBed");
    }
	
}
