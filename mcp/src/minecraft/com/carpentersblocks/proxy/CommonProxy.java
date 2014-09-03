package com.carpentersblocks.proxy;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.entity.item.EntityCarpentersTile;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.tileentity.TECarpentersDaylightSensor;
import com.carpentersblocks.tileentity.TECarpentersFlowerPot;
import com.carpentersblocks.tileentity.TECarpentersSafe;
import com.carpentersblocks.tileentity.TECarpentersTorch;
import com.carpentersblocks.util.handler.DesignHandler;
import com.carpentersblocks.util.handler.EventHandler;
import com.carpentersblocks.util.handler.OverlayHandler;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.FeatureRegistry;
import com.carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

    public final static int ID_TILE = 0;

    public void preInit(FMLPreInitializationEvent event, Configuration config)
    {
        FeatureRegistry.preInit(event, config); // Do before block and item registration
        BlockRegistry.preInit(event, config);
        ItemRegistry.preInit(event, config);
        DesignHandler.preInit(event);
    }

    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        /* Initialize Components */

        BlockRegistry.init(event);
        ItemRegistry.init(event);

        if (FeatureRegistry.enableOverlays) {
            OverlayHandler.init();
        }

        /* Register Tile Entities */

        GameRegistry.registerTileEntity(                    TEBase.class,          "TileEntityCarpentersSlope"); // Compatibility mapping
        GameRegistry.registerTileEntity(                    TEBase.class,            "TileEntityCarpentersBed"); // Compatibility mapping
        GameRegistry.registerTileEntity(                    TEBase.class,          "TileEntityCarpentersBlock");
        GameRegistry.registerTileEntity(TECarpentersDaylightSensor.class, "TileEntityCarpentersDaylightSensor");
        GameRegistry.registerTileEntity(TECarpentersDaylightSensor.class,            "TileEntityCarpentersExt"); // Compatibility mapping
        GameRegistry.registerTileEntity(     TECarpentersFlowerPot.class,      "TileEntityCarpentersFlowerPot");
        GameRegistry.registerTileEntity(          TECarpentersSafe.class,           "TileEntityCarpentersSafe");
        GameRegistry.registerTileEntity(         TECarpentersTorch.class,          "TileEntityCarpentersTorch");

        /* Register Entities */

        if (ItemRegistry.enableTile) {
            EntityRegistry.registerModEntity(EntityCarpentersTile.class, "CarpentersTile", ID_TILE, CarpentersBlocks.instance, 64, 999, false);
        }
    }

}
