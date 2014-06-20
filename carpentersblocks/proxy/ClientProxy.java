package carpentersblocks.proxy;

import net.minecraftforge.common.MinecraftForge;
import carpentersblocks.renderer.BlockHandlerCarpentersBarrier;
import carpentersblocks.renderer.BlockHandlerCarpentersBed;
import carpentersblocks.renderer.BlockHandlerCarpentersBlock;
import carpentersblocks.renderer.BlockHandlerCarpentersButton;
import carpentersblocks.renderer.BlockHandlerCarpentersCollapsibleBlock;
import carpentersblocks.renderer.BlockHandlerCarpentersDaylightSensor;
import carpentersblocks.renderer.BlockHandlerCarpentersDoor;
import carpentersblocks.renderer.BlockHandlerCarpentersFlowerPot;
import carpentersblocks.renderer.BlockHandlerCarpentersGate;
import carpentersblocks.renderer.BlockHandlerCarpentersHatch;
import carpentersblocks.renderer.BlockHandlerCarpentersLadder;
import carpentersblocks.renderer.BlockHandlerCarpentersLever;
import carpentersblocks.renderer.BlockHandlerCarpentersPressurePlate;
import carpentersblocks.renderer.BlockHandlerCarpentersSafe;
import carpentersblocks.renderer.BlockHandlerCarpentersSlope;
import carpentersblocks.renderer.BlockHandlerCarpentersStairs;
import carpentersblocks.renderer.BlockHandlerCarpentersTorch;
import carpentersblocks.util.handler.EntityHandler;
import carpentersblocks.util.handler.OptifineHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderInformation(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new IconRegistry());
        EntityHandler.registerEntityRenderers();

        if (BlockRegistry.enableBarrier) {
            BlockRegistry.carpentersBarrierRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersBarrierRenderID, new BlockHandlerCarpentersBarrier());
        }

        if (BlockRegistry.enableButton) {
            BlockRegistry.carpentersButtonRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersButtonRenderID, new BlockHandlerCarpentersButton());
        }

        if (BlockRegistry.enableDaylightSensor) {
            BlockRegistry.carpentersDaylightSensorRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersDaylightSensorRenderID, new BlockHandlerCarpentersDaylightSensor());
        }

        if (BlockRegistry.enableGate) {
            BlockRegistry.carpentersGateRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersGateRenderID, new BlockHandlerCarpentersGate());
        }

        if (BlockRegistry.enableLever) {
            BlockRegistry.carpentersLeverRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersLeverRenderID, new BlockHandlerCarpentersLever());
        }

        if (BlockRegistry.enablePressurePlate) {
            BlockRegistry.carpentersPressurePlateRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersPressurePlateRenderID, new BlockHandlerCarpentersPressurePlate());
        }

        if (BlockRegistry.enableSlope) {
            BlockRegistry.carpentersSlopeRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersSlopeRenderID, new BlockHandlerCarpentersSlope());
        }

        if (BlockRegistry.enableStairs) {
            BlockRegistry.carpentersStairsRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersStairsRenderID, new BlockHandlerCarpentersStairs());
        }

        if (BlockRegistry.enableHatch) {
            BlockRegistry.carpentersHatchRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersHatchRenderID, new BlockHandlerCarpentersHatch());
        }

        if (BlockRegistry.enableDoor) {
            BlockRegistry.carpentersDoorRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersDoorRenderID, new BlockHandlerCarpentersDoor());
        }

        if (BlockRegistry.enableBed) {
            BlockRegistry.carpentersBedRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersBedRenderID, new BlockHandlerCarpentersBed());
        }

        if (BlockRegistry.enableLadder) {
            BlockRegistry.carpentersLadderRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersLadderRenderID, new BlockHandlerCarpentersLadder());
        }

        if (BlockRegistry.enableCollapsibleBlock) {
            BlockRegistry.carpentersCollapsibleBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersCollapsibleBlockRenderID, new BlockHandlerCarpentersCollapsibleBlock());
        }

        if (BlockRegistry.enableTorch) {
            BlockRegistry.carpentersTorchRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersTorchRenderID, new BlockHandlerCarpentersTorch());
        }

        if (BlockRegistry.enableSafe) {
            BlockRegistry.carpentersSafeRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersSafeRenderID, new BlockHandlerCarpentersSafe());
        }

        if (BlockRegistry.enableBlock) {
            BlockRegistry.carpentersBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersBlockRenderID, new BlockHandlerCarpentersBlock());
        }

        if (BlockRegistry.enableFlowerPot) {
            BlockRegistry.carpentersFlowerPotRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(BlockRegistry.carpentersFlowerPotRenderID, new BlockHandlerCarpentersFlowerPot());
        }

        if (FMLClientHandler.instance().hasOptifine()) {
            OptifineHandler.init();
        }
    }

}
