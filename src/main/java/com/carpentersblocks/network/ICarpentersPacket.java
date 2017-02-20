package com.carpentersblocks.network;

import java.io.IOException;

import io.netty.buffer.ByteBufInputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public interface ICarpentersPacket {

    public void processData(EntityPlayer entityPlayer, ByteBufInputStream bbis) throws IOException;

    public void appendData(PacketBuffer buffer) throws IOException;

}
