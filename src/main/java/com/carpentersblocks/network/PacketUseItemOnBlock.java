package com.carpentersblocks.network;

import java.util.function.Supplier;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.util.BlockUtil;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.NetworkEvent.Context;

@EventBusSubscriber(modid = CarpentersBlocks.MOD_ID)
public class PacketUseItemOnBlock implements ICarpentersBlocksPacket {

	private BlockRayTraceResult blockRayTraceResult;
	private Hand hand;
	
	public PacketUseItemOnBlock() {}

    public PacketUseItemOnBlock(BlockRayTraceResult blockRayTraceResult, Hand hand) {
    	this.hand = hand;
    	this.blockRayTraceResult = blockRayTraceResult;
    }
    
    public PacketUseItemOnBlock(PacketBuffer packetBuffer) {
    	hand = packetBuffer.readEnum(Hand.class);
    	blockRayTraceResult = packetBuffer.readBlockHitResult();
    }
    
	public static void encode(PacketUseItemOnBlock msg, PacketBuffer buffer) {
		buffer.writeEnum(msg.hand);
		buffer.writeBlockHitResult(msg.blockRayTraceResult);
	}
	
	public static void handle(final PacketUseItemOnBlock msg, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (!BlockUtil.isAreaLoaded(ctx.get().getSender().getLevel(), msg.blockRayTraceResult.getBlockPos())) {
				return;
			}
			ServerPlayerEntity player = ctx.get().getSender();
			ItemStack itemStack = player.getMainHandItem();
	        BlockState blockState = player.level.getBlockState(msg.blockRayTraceResult.getBlockPos());
        	ActionResultType result = blockState.getBlock().use(blockState, player.level, msg.blockRayTraceResult.getBlockPos(), player, msg.hand, msg.blockRayTraceResult);
	        if (!ActionResultType.SUCCESS.equals(result) && itemStack != null && itemStack.getItem() instanceof BlockItem) {
	        	itemStack.onItemUseFirst(new ItemUseContext(player, player.getUsedItemHand(), (BlockRayTraceResult) msg.blockRayTraceResult));
	        	// TODO: may need to consume item
	        }
		});
	}
	
    /**
     * Intercepts block right click action and sends packet to
     * server with {@link BlockRayTraceResult} if conditions
     * are met.
     * <p>
     * Provides ability to right-click a Carpenter's Block while
     * the player is crouching.
     * 
     * @param event the event
     */
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
    	ItemStack itemStack;
    	if ((itemStack = event.getPlayer().getItemInHand(event.getHand())) == null) {
    		return;
    	}
    	return;
    	// handle placedBy()
    	//if (itemStack.getItem()..is(AbstractCoverableBlock) instanceof AbstractCoverableBlock) {
    	/*
    	if (isValidCarpentersBlock(event.getWorld(), event.getPos())) {
            if (event.getPlayer().isCrouching()
            		&& !(itemStack != null && itemStack.getItem() instanceof BlockItem && !BlockUtil.isOverlay(itemStack))) {
        		// prevent event from propagating to server
	        	if (event.getWorld().isClientSide()) {
	        		event.setCanceled(true);
	        	}
	        	// no need to call default attack() block method
            	event.setUseBlock(Result.DENY);
            	// call our own method and also send packet to server
            	CarpentersBlocksPacketHandler.sendToServer(new PacketBlockInteraction.UseItem(event.getHitVec(), event.getHand()));
            	BlockState blockState = event.getPlayer().level.getBlockState(event.getPos());
            	ActionResultType result = blockState.getBlock().use(blockState, event.getWorld(), event.getPos(), event.getPlayer(), event.getHand(), event.getHitVec());
		        if (!ActionResultType.SUCCESS.equals(result) && itemStack != null && itemStack.getItem() instanceof BlockItem) {
		        	itemStack.onItemUseFirst(new ItemUseContext(event.getPlayer(), event.getHand(), event.getHitVec()));
		        	// TODO: may need to consume item
		        }
            }
    	}
    	*/
    }
	
}