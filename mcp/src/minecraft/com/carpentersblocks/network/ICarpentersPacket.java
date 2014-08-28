package com.carpentersblocks.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;

public interface ICarpentersPacket {

    public void processData(EntityPlayer entityPlayer, DataInputStream bbis) throws IOException;

    public void appendData(DataOutputStream outputStream) throws IOException;

}
