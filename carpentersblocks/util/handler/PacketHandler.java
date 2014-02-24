package carpentersblocks.util.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.block.BlockCarpentersSlope;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class PacketHandler {
    
    public static final byte PACKET_SLOPE_SELECT = 0;
    
    @SubscribeEvent
    public void onServerPacket(ServerCustomPacketEvent event) throws IOException
    {
        ByteBufInputStream bbis = new ByteBufInputStream(event.packet.payload());
        EntityPlayer entityPlayer = ((NetHandlerPlayServer) event.handler).playerEntity;
        
        switch (bbis.readInt()) {
            case PACKET_SLOPE_SELECT:
                slopeSelect(entityPlayer, bbis.readBoolean());
                break;
            default:
                break;
        }

        bbis.close();
    }
    
    public static void sendPacketToServer(int packetId, Object ... obj)
    {
        ByteBuf buffer = Unpooled.buffer();
        
        switch (packetId) {
            case PACKET_SLOPE_SELECT:
                
                buffer.writeInt(PACKET_SLOPE_SELECT);
                buffer.writeBoolean((Boolean) obj[0]);
                FMLProxyPacket packet = new FMLProxyPacket(new C17PacketCustomPayload(CarpentersBlocks.MODID, buffer));
                CarpentersBlocks.channel.sendToServer(packet);
                
                break;
            default:
                break;
        }
    }
    
    private void slopeSelect(EntityPlayer entityPlayer, boolean incDamage)
    {
        ItemStack itemStack = entityPlayer.getHeldItem();

        if (itemStack != null) {

            if (incDamage) {

                if (itemStack.getItemDamage() >= BlockCarpentersSlope.slopeType.length - 1) {
                    itemStack.setItemDamage(0);
                } else {
                    itemStack.setItemDamage(itemStack.getItemDamage() + 1);
                }

            } else {

                if (itemStack.getItemDamage() <= 0) {
                    itemStack.setItemDamage(BlockCarpentersSlope.slopeType.length - 1);
                } else {
                    itemStack.setItemDamage(itemStack.getItemDamage() - 1);
                }

            }

        }
    }
    
}
