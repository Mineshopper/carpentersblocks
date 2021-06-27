package com.carpentersblocks.network;

import java.util.function.Supplier;

import com.carpentersblocks.block.AbstractCoverableBlock;
import com.carpentersblocks.item.CbItems;
import com.carpentersblocks.util.BlockUtil;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketSlopeSelect implements ICarpentersBlocksPacket {

    private int slot = 0;
    private boolean next;

    public PacketSlopeSelect() {}

    public PacketSlopeSelect(int slot, boolean next) {
        this.slot = slot;
        this.next = next;
    }
    
    public PacketSlopeSelect(PacketBuffer packetBuffer) {
    	this.slot = packetBuffer.readByte();
    	this.next = packetBuffer.readBoolean();
    }
    
	public static void encode(PacketSlopeSelect msg, PacketBuffer buffer) {
		buffer.writeByte(msg.slot);
		buffer.writeBoolean(msg.next);
	}
	
	public static void handle(final PacketSlopeSelect msg, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity player = ctx.get().getSender();
			ItemStack itemStack = player.inventory.getItem(msg.slot);
	        if (itemStack != null && BlockUtil.toBlock(itemStack) instanceof AbstractCoverableBlock) {
	        	if (msg.next) {
	        		player.inventory.setItem(msg.slot,
	        				new ItemStack(CbItems.getNextSlopeSubType(itemStack.getItem().getRegistryName().toString()), itemStack.getCount()));
	        	} else {
	        		player.inventory.setItem(msg.slot,
	        				new ItemStack(CbItems.getPreviousSlopeSubType(itemStack.getItem().getRegistryName().toString()), itemStack.getCount()));
	        	}
	        }
		});
	}
	
}