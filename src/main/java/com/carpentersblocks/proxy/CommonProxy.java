package com.carpentersblocks.proxy;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.handler.DesignHandler;
import com.carpentersblocks.util.handler.EventHandler;
import com.carpentersblocks.util.handler.OverlayHandler;
import com.carpentersblocks.util.handler.PacketHandler;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.FeatureRegistry;
import com.carpentersblocks.util.registry.ItemRegistry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event, Configuration config) {
        FeatureRegistry.preInit(event, config); // Do before block and item registration
        BlockRegistry.preInit(event, config); // Do before item registration
        ItemRegistry.preInit(event, config);
        DesignHandler.preInit(event);        
    }
    
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        CarpentersBlocks.channel.register(new PacketHandler());

        // Initialize blocks and items
        BlockRegistry.init(event);
        ItemRegistry.init(event);

        // Initialize overlays
        if (FeatureRegistry.enableOverlays) {
            OverlayHandler.init();
        }

        // Register tile entities
        GameRegistry.registerTileEntity(              CbTileEntity.class,            "TileEntityCarpentersSlope"); // Compatibility mapping
        GameRegistry.registerTileEntity(              CbTileEntity.class,              "TileEntityCarpentersBed"); // Compatibility mapping
        GameRegistry.registerTileEntity(              CbTileEntity.class,            "TileEntityCarpentersBlock");
        // TODO: Implement these
        //GameRegistry.registerTileEntity(CbDaylightSensorTileEntity.class,              "TileEntityCarpentersExt"); // Compatibility mapping
        //GameRegistry.registerTileEntity(CbDaylightSensorTileEntity.class,   "TileEntityCarpentersDaylightSensor");
        //GameRegistry.registerTileEntity(     CbFlowerPotTileEntity.class,        "TileEntityCarpentersFlowerPot");
        //GameRegistry.registerTileEntity(          CbSafeTileEntity.class,             "TileEntityCarpentersSafe");
        //GameRegistry.registerTileEntity(         CbTorchTileEntity.class,            "TileEntityCarpentersTorch");
        //GameRegistry.registerTileEntity(    CbGarageDoorTileEntity.class,       "TileEntityCarpentersGarageDoor");

        // Register entities
        if (ItemRegistry.enableTile) {
            //EntityRegistry.registerModEntity(EntityCarpentersTile.class, "CarpentersTile", ENTITY_ID_TILE, CarpentersBlocks.instance, 64, 999, false);
        }
    }
	
}
