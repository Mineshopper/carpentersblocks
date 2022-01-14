package com.carpentersblocks.network;

import com.carpentersblocks.CarpentersBlocks;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@EventBusSubscriber(modid = CarpentersBlocks.MOD_ID, bus = Bus.MOD)
public class CarpentersBlocksPacketHandler {

	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
	    new ResourceLocation(CarpentersBlocks.MOD_ID, "main"),
	    () -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);
	
	private CarpentersBlocksPacketHandler() { }
	
	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {
		int id = 0;
		INSTANCE.registerMessage(id++, PacketAttackBlock.class, PacketAttackBlock::encode, PacketAttackBlock::new, PacketAttackBlock::handle);
		INSTANCE.registerMessage(id++, PacketUseItemOnBlock.class, PacketUseItemOnBlock::encode, PacketUseItemOnBlock::new, PacketUseItemOnBlock::handle);
		INSTANCE.registerMessage(id++, PacketSetPlacedBy.class, PacketSetPlacedBy::encode, PacketSetPlacedBy::new, PacketSetPlacedBy::handle);
		INSTANCE.registerMessage(id++, PacketEnrichPlant.class, PacketEnrichPlant::encode, PacketEnrichPlant::new, PacketEnrichPlant::handle);
		INSTANCE.registerMessage(id++, PacketSlopeSelect.class, PacketSlopeSelect::encode, PacketSlopeSelect::new, PacketSlopeSelect::handle);
	}
	
	public static void sendToServer(ICarpentersBlocksPacket pkt) {
		INSTANCE.send(PacketDistributor.SERVER.noArg(), pkt);
	}
	
	public static void sentToClient(ICarpentersBlocksPacket pkt, ServerPlayerEntity sender) {
		INSTANCE.sendTo(pkt, sender.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}
	
}