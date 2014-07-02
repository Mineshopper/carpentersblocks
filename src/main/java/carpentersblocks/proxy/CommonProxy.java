package carpentersblocks.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.util.flowerpot.FlowerPotHandler;
import carpentersblocks.util.handler.DesignHandler;
import carpentersblocks.util.handler.DyeHandler;
import carpentersblocks.util.handler.EntityHandler;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.handler.PacketHandler;
import carpentersblocks.util.handler.TileEntityHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.FeatureRegistry;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event, Configuration config)
    {
        FeatureRegistry.preInit(event, config);
        BlockRegistry.preInit(event, config);
        ItemRegistry.preInit(event, config);
        DesignHandler.preInit(event);
        registerRenderInformation();
    }

    public void init(FMLInitializationEvent event)
    {
        BlockRegistry.init(event);
        ItemRegistry.init(event);

        if (BlockRegistry.enableFlowerPot) {
            FlowerPotHandler.init();
        }

        if (FeatureRegistry.enableDye) {
            DyeHandler.init();
        }

        if (FeatureRegistry.enableOverlay) {
            OverlayHandler.init();
        }

        if (FeatureRegistry.enableTile) {
            EntityHandler.init();
        }

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        CarpentersBlocks.channel.register(new PacketHandler());
        TileEntityHandler.init();
    }

    public void registerRenderInformation() { }

}
