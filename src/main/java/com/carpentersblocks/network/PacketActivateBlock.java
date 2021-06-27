package com.carpentersblocks.network;

import java.util.function.Supplier;

import com.carpentersblocks.util.EntityLivingUtil;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketActivateBlock implements ICarpentersBlocksPacket {

	private BlockPos blockPos;
    private Direction direction;

    public PacketActivateBlock() {}

    public PacketActivateBlock(BlockPos blockPos, Direction direction) {
    	this.blockPos = blockPos;
    	this.direction = direction;
    }
    
    public PacketActivateBlock(PacketBuffer packetBuffer) {
    	blockPos = packetBuffer.readBlockPos();
		direction = Direction.from3DDataValue(packetBuffer.readByte());
    }
    
	public static void encode(PacketActivateBlock msg, PacketBuffer buffer) {
		buffer.writeBlockPos(msg.blockPos);
		buffer.writeByte(msg.direction.ordinal());
	}
	
	public static void handle(final PacketActivateBlock msg, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity player = ctx.get().getSender();
			double blockReach = Minecraft.getInstance().gameMode.getPickRange();
        	RayTraceResult rayTraceResult = player.pick(blockReach, 0.0f, false);
			ItemStack itemStack = player.getMainHandItem();
	        BlockState blockState = player.level.getBlockState(msg.blockPos);
	        ActionResultType result = blockState.getBlock().use(blockState, player.level, msg.blockPos, player, Hand.MAIN_HAND, (BlockRayTraceResult) rayTraceResult);
	        if (!ActionResultType.SUCCESS.equals(result)) {
	            if (itemStack != null && itemStack.getItem() instanceof BlockItem) {
	            	ItemUseContext itemUseContext = new ItemUseContext(player, player.getUsedItemHand(), (BlockRayTraceResult) rayTraceResult);
	                itemStack.onItemUseFirst(itemUseContext);
	                EntityLivingUtil.decrementCurrentSlot(player);
	            }
	        }
		});
	}
	
}