package carpentersblocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import carpentersblocks.proxy.CommonProxy;
import carpentersblocks.util.CarpentersBlocksTab;
import carpentersblocks.util.handler.TileEntityHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.FeatureRegistry;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = CarpentersBlocks.MODID, name = "Carpenter's Blocks", version = CarpentersBlocks.VERSION)
public class CarpentersBlocks {
    
    public static final String MODID   = "CarpentersBlocks";
    public static final String VERSION = "3.0.2";
    public static FMLEventChannel channel;
    public static CreativeTabs creativeTab = new CarpentersBlocksTab(MODID);
    
    @Instance(MODID)
    public static CarpentersBlocks instance;
    
    @SidedProxy(clientSide = "carpentersblocks.proxy.ClientProxy", serverSide = "carpentersblocks.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(MODID);
        
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        
        config.load();
        
        FeatureRegistry.initFeatures(event, config);
        BlockRegistry.initBlocks(event, config);
        ItemRegistry.initItems(event, config);
        
        if (config.hasChanged()) {
            config.save();
        }
        
        BlockRegistry.registerBlocks();
        ItemRegistry.registerItems();
        
        proxy.registerHandlers(event);
        proxy.registerRenderInformation(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        TileEntityHandler.registerTileEntities();
    }

}
