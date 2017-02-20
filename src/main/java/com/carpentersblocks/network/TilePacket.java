package com.carpentersblocks.network;

import java.io.IOException;

import io.netty.buffer.ByteBufInputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class TilePacket implements ICarpentersPacket {

    protected BlockPos _blockPos;

    public TilePacket() {}

    /**
     * Creates a packet that holds block information.
     * 
     * @param blockPos the block position
     */
    public TilePacket(BlockPos blockPos) {
        _blockPos = blockPos;
    }

    @Override
    public void processData(EntityPlayer entityPlayer, ByteBufInputStream bbis) throws IOException {
    	_blockPos = new BlockPos(bbis.readInt(), bbis.readInt(), bbis.readInt());
    }

    @Override
    public void appendData(PacketBuffer buffer) throws IOException {
        buffer.writeInt(_blockPos.getX());
        buffer.writeInt(_blockPos.getY());
        buffer.writeInt(_blockPos.getZ());
    }

}
