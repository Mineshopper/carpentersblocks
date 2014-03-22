package carpentersblocks.proxy;

import net.minecraftforge.common.MinecraftForge;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.util.bed.BedDesignHandler;
import carpentersblocks.util.flowerpot.FlowerPotDesignHandler;
import carpentersblocks.util.flowerpot.FlowerPotHandler;
import carpentersblocks.util.handler.DyeHandler;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.handler.PacketHandler;
import carpentersblocks.util.handler.PatternHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.FeatureRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    
    public void registerHandlers(FMLPreInitializationEvent event)
    {
        if (BlockRegistry.enableFlowerPot) {
            FlowerPotHandler.initPlants();
            FlowerPotDesignHandler.init(event);
        }
        
        if (FeatureRegistry.enableDyeColors) {
            DyeHandler.init();
        }
        
        if (FeatureRegistry.enableOverlays) {
            OverlayHandler.init();
        }
        
        if (FeatureRegistry.enablePatterns) {
            PatternHandler.init(event);
        }
        
        if (BlockRegistry.enableBed) {
            BedDesignHandler.init(event);
        }
        
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        CarpentersBlocks.channel.register(new PacketHandler());
    }
    
    public void registerRenderInformation(FMLPreInitializationEvent event) { }
    
}
