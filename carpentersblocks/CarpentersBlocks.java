package carpentersblocks;

import net.minecraft.creativetab.CreativeTabs;
import carpentersblocks.proxy.CommonProxy;
import carpentersblocks.util.CarpentersBlocksTab;
import carpentersblocks.util.ModLogger;
import carpentersblocks.util.handler.BlockHandler;
import carpentersblocks.util.handler.FeatureHandler;
import carpentersblocks.util.handler.ItemHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(
        modid = "CarpentersBlocks",
        name = "Carpenter's Blocks",
        version = "v1.9",
        dependencies = "required-after:Forge@[7.7.2.682,)"
	)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false
	)
public class CarpentersBlocks
{
	
    @Instance("CarpentersBlocks")
    public static CarpentersBlocks instance;
    
    @SidedProxy(clientSide = "carpentersblocks.proxy.ClientProxy", serverSide = "carpentersblocks.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    public static CreativeTabs tabCarpentersBlocks = new CarpentersBlocksTab("carpentersBlocks");

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
    	FeatureHandler.initProps(event);
        BlockHandler.initBlocks(event);
        ItemHandler.initItems(event);
        
        ModLogger.init();
        
        proxy.registerHandlers(event);
    	proxy.registerRenderInformation(event);
    }

    @Init
    public void init(FMLInitializationEvent event)
    {
    	FeatureHandler.registerTileEntities();
    	BlockHandler.registerBlocks();
    	ItemHandler.registerItems();
    }

}
