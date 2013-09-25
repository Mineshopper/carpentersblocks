package carpentersblocks.proxy;

import net.minecraftforge.common.MinecraftForge;
import carpentersblocks.util.handler.BedDesignHandler;
import carpentersblocks.util.handler.DyeColorHandler;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.handler.FeatureHandler;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.handler.PatternHandler;
import carpentersblocks.util.handler.PlantHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


public class CommonProxy
{
	
	public void registerHandlers(FMLPreInitializationEvent event)
	{
        FeatureHandler.enablePlantSupport = PlantHandler.init();
        OverlayHandler.init();
        DyeColorHandler.init();
        PatternHandler.init(event);
        BedDesignHandler.init(event);
    	MinecraftForge.EVENT_BUS.register(new EventHandler());    	
	}
	
    public void registerRenderInformation(FMLPreInitializationEvent event)
    {

    }
}
