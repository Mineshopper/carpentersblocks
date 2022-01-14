package com.carpentersblocks.network;

import java.util.function.Supplier;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.block.AbstractCoverableBlock;
import com.carpentersblocks.util.BlockUtil;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.NetworkEvent.Context;

@EventBusSubscriber(modid = CarpentersBlocks.MOD_ID)
public class PacketSetPlacedBy implements ICarpentersBlocksPacket {

	private BlockRayTraceResult blockRayTraceResult;
	
	public PacketSetPlacedBy() {}

    public PacketSetPlacedBy(BlockRayTraceResult blockRayTraceResult, Hand hand) {
    	this.blockRayTraceResult = blockRayTraceResult;
    }
    
    public PacketSetPlacedBy(PacketBuffer packetBuffer) {
    	blockRayTraceResult = packetBuffer.readBlockHitResult();
    }
    
	public static void encode(PacketSetPlacedBy msg, PacketBuffer buffer) {
		buffer.writeBlockHitResult(msg.blockRayTraceResult);
	}
	
	public static void handle(final PacketSetPlacedBy msg, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (!BlockUtil.isAreaLoaded(ctx.get().getSender().getLevel(), msg.blockRayTraceResult.getBlockPos())) {
				return;
			}
			ServerPlayerEntity player = ctx.get().getSender();
			ItemStack itemStack = player.getMainHandItem();
	        BlockState blockState = BlockUtil.getBlockState(itemStack);
	        BlockPos blockPos = msg.blockRayTraceResult.getBlockPos().relative(msg.blockRayTraceResult.getDirection());
	        if (player.getLevel().setBlock(blockPos, blockState, 3)) {
	        	((AbstractCoverableBlock)blockState.getBlock()).setPlacedBy(
		        		player.getLevel(),
		        		blockPos,
	    				blockState,
	    				player,
	    				itemStack,
	    				msg.blockRayTraceResult);
	       }
		});
	}
	
    /**
     * Intercepts block right click action and sends packet to
     * server with {@link BlockRayTraceResult}.
     * <p>
     * Invokes {@link AbstractCoverableBlock#setPlacedBy(net.minecraft.world.World,
     *     net.minecraft.util.math.BlockPos, BlockState, net.minecraft.entity.LivingEntity,
     *     ItemStack, Hand, BlockRayTraceResult) setPlacedBy} if conditions are met.
     * 
     * @param event the event
     */
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
    	// skip if not holding a Carpenter's block
    	ItemStack itemStack;
    	if (!((itemStack = event.getPlayer().getItemInHand(event.getHand())) != null
    			&& itemStack.getItem() instanceof BlockItem
    			&& BlockUtil.getBlockState(itemStack).getBlock() instanceof AbstractCoverableBlock)) {
    		return;
    	}
    	
    	// send packet if we're client side
    	if (event.getWorld().isClientSide()) {
    		CarpentersBlocksPacketHandler.sendToServer(new PacketSetPlacedBy(event.getHitVec(), event.getHand()));
    	}
    }
	
}