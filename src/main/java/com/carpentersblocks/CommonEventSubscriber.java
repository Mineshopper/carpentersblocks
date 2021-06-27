package com.carpentersblocks;

import java.util.List;

import com.carpentersblocks.block.CbBlocks;
import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.util.ReflectionHelper;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = CarpentersBlocks.MOD_ID, bus = Bus.MOD)
public class CommonEventSubscriber {
	
	public static TileEntityType<CbTileEntity> tileEntityType;
	
	@SubscribeEvent
    public static void onTileEntityTypeRegisterEvent(RegistryEvent.Register<TileEntityType<?>> event) {
		List<Block> blocks = ReflectionHelper.getStaticValues(Block.class, CbBlocks.class);
		tileEntityType = TileEntityType.Builder.of(CbTileEntity::new, blocks.toArray(new Block[blocks.size()])).build(null);
		tileEntityType.setRegistryName(CarpentersBlocks.MOD_ID, "tile_entity");
		event.getRegistry().register(tileEntityType);
    }
	
}
