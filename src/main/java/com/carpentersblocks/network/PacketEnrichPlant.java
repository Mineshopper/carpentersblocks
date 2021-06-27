package com.carpentersblocks.network;

import java.io.IOException;
import java.util.function.Supplier;

import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.nbt.attribute.EnumAttributeType;
import com.carpentersblocks.util.EntityLivingUtil;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketEnrichPlant implements ICarpentersBlocksPacket {

	private BlockPos blockPos;
    private int hexColor;

    public PacketEnrichPlant() {}

    /**
     * For the server to examine plant color, since it's a client-side only property.
     * 
     * @param blockPos the block position
     * @param hexColor the integer color
     */
    public PacketEnrichPlant(BlockPos blockPos, int hexColor) {
        this.blockPos = blockPos;
        this.hexColor = hexColor;
    }
    
    public PacketEnrichPlant(PacketBuffer packetBuffer) {
        blockPos = packetBuffer.readBlockPos();
        hexColor = packetBuffer.readInt();
    }
    
	public static void encode(PacketEnrichPlant msg, PacketBuffer buffer) {
		buffer.writeBlockPos(msg.blockPos);
		buffer.writeInt(msg.hexColor);
	}
	
	public static void handle(final PacketEnrichPlant msg, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity player = ctx.get().getSender();
			World world = player.level;
	        TileEntity tileEntity = world.getBlockEntity(msg.blockPos);
	        if (tileEntity != null && tileEntity instanceof CbTileEntity) {
	        	CbTileEntity cbTileEntity = (CbTileEntity) tileEntity;
	            if (msg.hexColor != 16777215 && !cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.FERTILIZER)) {
	            	cbTileEntity.addAttribute(EnumAttributeLocation.HOST, EnumAttributeType.FERTILIZER, new ItemStack(Items.BONE_MEAL));
	                EntityLivingUtil.decrementCurrentSlot(player);
	            }
	        }
		});
	}
	
}