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

/**
 * CarpentersBlocks - The main mod file that handles the events of the MineCraft server and client loading, as well
 * as registering everything else that is to be executed later.
 *
 * @author Mineshopper
 */
@Mod(
        modid = CarpentersBlocks.MODID,
        name = "Carpenter's Blocks",
        version = CarpentersBlocks.VERSION,
        dependencies = "required-after:Forge@[10.13.0.1180,)"
        )
public class CarpentersBlocks {

    /**
     * The ID of this mod
     */
    public static final String MODID = "CarpentersBlocks";

    /**
     * The version of this mod
     */
    public static final String VERSION = "3.2.5";

    /**
     * The {@link cpw.mods.fml.common.network.FMLEventChannel} that this mod uses for communication between the
     * server and the client
     */
    public static FMLEventChannel channel;

    /**
     * The {@link net.minecraft.creativetab.CreativeTabs} implementation that this mod registers
     */
    public static CreativeTabs creativeTab = new CarpentersBlocksTab(MODID);

    /**
     * The instance of the mod
     */
    @Instance(MODID)
    public static CarpentersBlocks instance;

    /**
     * The proxy class that defines differences between the client (using {@link com.carpentersblocks.proxy.ClientProxy})
     * and the server (using {@link com.carpentersblocks.proxy.CommonProxy}
     */
    @SidedProxy(clientSide = "com.carpentersblocks.proxy.ClientProxy", serverSide = "com.carpentersblocks.proxy.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Called when the Forge Mod Loader is getting ready to initialize mods
     *
     * @param event The {@link cpw.mods.fml.common.event.FMLPreInitializationEvent} that is being referenced
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        //First, create a new event-driven channel for networking
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(MODID);

        //Now load the configuration
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        //Call the proxy-specifig pre-initialization code
        proxy.preInit(event, config);

        //Check to see if the configuration has been changed
        if (config.hasChanged()) {
            //I came to eat apples and save config. And I'm all out of apples
            config.save();
        }
    }

    /**
     * Called when the Forge Mod Loader is initializing mods
     *
     * @param event The {@link cpw.mods.fml.common.event.FMLInitializationEvent} being referenced
     */
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

}
