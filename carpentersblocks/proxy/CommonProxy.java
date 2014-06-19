package carpentersblocks.proxy;

import net.minecraftforge.common.MinecraftForge;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.util.flowerpot.FlowerPotHandler;
import carpentersblocks.util.handler.DesignHandler;
import carpentersblocks.util.handler.DyeHandler;
import carpentersblocks.util.handler.EntityHandler;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.handler.PacketHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.FeatureRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void registerHandlers(FMLPreInitializationEvent event)
    {
        if (BlockRegistry.enableFlowerPot) {
            FlowerPotHandler.initPlants();
        }

        if (FeatureRegistry.enableDyeColors) {
            DyeHandler.init();
        }

        if (FeatureRegistry.enableOverlays) {
            OverlayHandler.init();
        }

        if (FeatureRegistry.enableTile) {
            EntityHandler.registerEntities();
        }

        DesignHandler.init(event);

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        CarpentersBlocks.channel.register(new PacketHandler());
    }

    public void registerRenderInformation(FMLPreInitializationEvent event) { }

}
