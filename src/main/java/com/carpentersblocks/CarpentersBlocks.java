package com.carpentersblocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import com.carpentersblocks.proxy.CommonProxy;
import com.carpentersblocks.util.CarpentersBlocksTab;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(
        modid = CarpentersBlocks.MODID,
        name = "Carpenter's Blocks",
        version = CarpentersBlocks.VERSION,
        dependencies = "required-after:Forge@[10.13.0.1180,)"
        )
public class CarpentersBlocks {

    public static final String MODID = "CarpentersBlocks";
    public static final String VERSION = "3.3.4.2";
    public static FMLEventChannel channel;
    public static CreativeTabs creativeTab = new CarpentersBlocksTab(MODID);

    @Instance(MODID)
    public static CarpentersBlocks instance;

    @SidedProxy(clientSide = "com.carpentersblocks.proxy.ClientProxy", serverSide = "com.carpentersblocks.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(MODID);
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        proxy.preInit(event, config);

        if (config.hasChanged()) {
            config.save();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

}
