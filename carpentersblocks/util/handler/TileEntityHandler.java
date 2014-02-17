package carpentersblocks.util.handler;

import carpentersblocks.renderer.tileentity.TERendererCarpentersBed;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersBed;
import carpentersblocks.tileentity.TECarpentersDaylightSensor;
import carpentersblocks.tileentity.TECarpentersFlowerPot;
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
        GameRegistry.registerTileEntity(                    TEBase.class,          "TileEntityCarpentersBlock");
        GameRegistry.registerTileEntity(TECarpentersDaylightSensor.class, "TileEntityCarpentersDaylightSensor");
        GameRegistry.registerTileEntity(         TECarpentersTorch.class,          "TileEntityCarpentersTorch");
        GameRegistry.registerTileEntity(          TECarpentersSafe.class,           "TileEntityCarpentersSafe");
        GameRegistry.registerTileEntity(           TECarpentersBed.class,            "TileEntityCarpentersBed");
        GameRegistry.registerTileEntity(     TECarpentersFlowerPot.class,      "TileEntityCarpentersFlowerPot");
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
