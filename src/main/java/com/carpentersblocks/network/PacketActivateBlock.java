package com.carpentersblocks.network;

import java.io.IOException;

import com.carpentersblocks.util.EntityLivingUtil;

import io.netty.buffer.ByteBufInputStream;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class PacketActivateBlock extends TilePacket {

    private EnumFacing _facing;

    public PacketActivateBlock() {}

    public PacketActivateBlock(BlockPos blockPos, EnumFacing facing) {
        super(blockPos);
        _facing = facing;
    }

    @Override
    public void processData(EntityPlayer entityPlayer, ByteBufInputStream bbis) throws IOException {
        super.processData(entityPlayer, bbis);
        ItemStack itemStack = entityPlayer.getHeldItemMainhand();
        _facing = EnumFacing.getFront(bbis.readInt());
        IBlockState blockState = entityPlayer.worldObj.getBlockState(_blockPos);
        boolean result = blockState.getBlock().onBlockActivated(entityPlayer.worldObj, _blockPos, blockState, entityPlayer, EnumHand.MAIN_HAND, entityPlayer.getHeldItemMainhand(), _facing, 1.0F, 1.0F, 1.0F);
        if (!result) {
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                itemStack.onItemUse(entityPlayer, entityPlayer.worldObj, _blockPos, EnumHand.MAIN_HAND, _facing, 1.0F, 1.0F, 1.0F);
                EntityLivingUtil.decrementCurrentSlot(entityPlayer);
            }
        }
    }

    @Override
    public void appendData(PacketBuffer buffer) throws IOException {
        super.appendData(buffer);
        buffer.writeInt(_facing.ordinal());
    }

}
