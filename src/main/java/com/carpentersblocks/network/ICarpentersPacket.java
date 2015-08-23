package com.carpentersblocks.network;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

public interface ICarpentersPacket {

    public void processData(EntityPlayer entityPlayer, ByteBufInputStream bbis) throws IOException;

    public void appendData(ByteBuf buffer) throws IOException;

}
