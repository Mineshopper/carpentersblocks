package com.carpentersblocks.util.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.network.ICarpentersPacket;
import com.carpentersblocks.network.PacketActivateBlock;
import com.carpentersblocks.network.PacketEnrichPlant;
import com.carpentersblocks.network.PacketSlopeSelect;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

    private final static List<Class> packetCarrier;
    static {
        packetCarrier = new ArrayList<Class>();
        packetCarrier.add(PacketEnrichPlant.class);
        packetCarrier.add(PacketSlopeSelect.class);
        packetCarrier.add(PacketActivateBlock.class);
    }

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        if (packet.channel.equals(CarpentersBlocks.MODID)) {

            DataInputStream bbis = new DataInputStream(new ByteArrayInputStream(packet.data));
            EntityPlayer entityPlayer = (EntityPlayer) player;

            try {
                int packetId = bbis.readInt();
                ICarpentersPacket packetClass = (ICarpentersPacket) packetCarrier.get(packetId).newInstance();
                packetClass.processData(entityPlayer, bbis);
            } catch (Exception e) { }
        }
    }

    public static void sendPacketToServer(ICarpentersPacket inPacket)
    {
        Packet250CustomPayload packet = new Packet250CustomPayload();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);

        try {
            outputStream.writeInt(packetCarrier.indexOf(inPacket.getClass()));
            inPacket.appendData(outputStream);
        } catch (IOException e) { }

        packet.channel = CarpentersBlocks.MODID;
        packet.data = bos.toByteArray();
        packet.length = bos.size();

        Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
    }

}
