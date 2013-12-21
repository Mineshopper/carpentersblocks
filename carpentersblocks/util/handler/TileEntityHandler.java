package carpentersblocks.util.handler;

import carpentersblocks.renderer.tileentity.TERendererCarpentersBed;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersBed;
import carpentersblocks.tileentity.TECarpentersDaylightSensor;
import carpentersblocks.tileentity.TECarpentersSafe;
import carpentersblocks.tileentity.TECarpentersTorch;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityHandler {

	/**
	 * Registers tile entities.
	 */
	public static void registerTileEntities()
	{
		// Change this to "TileEntityCarpentersBase" for 1.7+
		GameRegistry.registerTileEntity(TEBase.class, "TileEntityCarpentersSlope");

		// Change this to "TileEntityCarpentersDaylightSensor" for 1.7+
		GameRegistry.registerTileEntity(TECarpentersDaylightSensor.class, "TileEntityCarpentersExt");

		GameRegistry.registerTileEntity(TECarpentersTorch.class, "TileEntityCarpentersTorch");
		GameRegistry.registerTileEntity(TECarpentersSafe.class, "TileEntityCarpentersSafe");
		GameRegistry.registerTileEntity(TECarpentersBed.class, "TileEntityCarpentersBed");
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Initializes tile entity renderers.
	 */
	public static void registerTileEntityRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TECarpentersBed.class, new TERendererCarpentersBed());
	}

}
