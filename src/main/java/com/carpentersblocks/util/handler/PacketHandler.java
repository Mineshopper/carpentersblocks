package com.carpentersblocks.util.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.network.ICarpentersPacket;
import com.carpentersblocks.network.PacketActivateBlock;
import com.carpentersblocks.network.PacketEnrichPlant;
import com.carpentersblocks.network.PacketSlopeSelect;
import com.carpentersblocks.util.ModLogger;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class PacketHandler {

    private final static List<Class> packetCarrier;
    static {
        packetCarrier = new ArrayList<Class>();
        packetCarrier.add(PacketActivateBlock.class);
        packetCarrier.add(PacketEnrichPlant.class);
        packetCarrier.add(PacketSlopeSelect.class);
    }

    @SubscribeEvent
    public void onServerPacket(ServerCustomPacketEvent event) throws IOException {
        ByteBufInputStream bbis = new ByteBufInputStream(event.getPacket().payload());
        EntityPlayer entityPlayer = ((NetHandlerPlayServer) event.getHandler()).playerEntity;
        int packetId = bbis.readInt();
        if (packetId < packetCarrier.size()) {
            try {
                ICarpentersPacket packetClass = (ICarpentersPacket) packetCarrier.get(packetId).newInstance();
                packetClass.processData(entityPlayer, bbis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ModLogger.log(Level.WARN, "Encountered out of range packet Id: " + packetId);
        }
        bbis.close();
    }

    public static void sendPacketToServer(ICarpentersPacket packet) {
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        buffer.writeInt(packetCarrier.indexOf(packet.getClass()));
        try {
            packet.appendData(buffer);
        } catch (IOException e) { }
        CarpentersBlocks.channel.sendToServer(new FMLProxyPacket(new CPacketCustomPayload(CarpentersBlocks.MOD_ID, buffer)));
    }

}
