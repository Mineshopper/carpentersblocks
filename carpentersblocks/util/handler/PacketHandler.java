package carpentersblocks.util.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.block.BlockCarpentersSlope;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class PacketHandler {
    
    public static final byte PACKET_SLOPE_SELECT    = 0;
    public static final byte PACKET_BLOCK_ACTIVATED = 1;
    
    @SubscribeEvent
    public void onServerPacket(ServerCustomPacketEvent event) throws IOException
    {
        ByteBufInputStream bbis = new ByteBufInputStream(event.packet.payload());
        EntityPlayer entityPlayer = ((NetHandlerPlayServer) event.handler).playerEntity;
        
        switch (bbis.readInt()) {
            case PACKET_SLOPE_SELECT:
                slopeSelect(entityPlayer, bbis.readBoolean());
                break;
            case PACKET_BLOCK_ACTIVATED:
            	
            	int x = bbis.readInt();
            	int y = bbis.readInt();
            	int z = bbis.readInt();
            	int side = bbis.readInt();
            	
            	Block block = entityPlayer.worldObj.getBlock(x, y, z);
            	
            	if (block != Blocks.air) {
            		block.onBlockActivated(entityPlayer.worldObj, x, y, z, entityPlayer, side, 1.0F, 1.0F, 1.0F);
            	}
            	
                break;
            default:
                break;
        }

        bbis.close();
    }
    
    public static void sendPacketToServer(int packetId, Object ... obj)
    {
        ByteBuf buffer = Unpooled.buffer();
        
        buffer.writeInt(packetId);
        
        switch (packetId) {
            case PACKET_SLOPE_SELECT:

                buffer.writeBoolean((Boolean) obj[0]);
                CarpentersBlocks.channel.sendToServer(new FMLProxyPacket(new C17PacketCustomPayload(CarpentersBlocks.MODID, buffer)));
                
                break;
            case PACKET_BLOCK_ACTIVATED:

                buffer.writeInt((Integer) obj[0]);
                buffer.writeInt((Integer) obj[1]);
                buffer.writeInt((Integer) obj[2]);
                buffer.writeInt((Integer) obj[3]);
                CarpentersBlocks.channel.sendToServer(new FMLProxyPacket(new C17PacketCustomPayload(CarpentersBlocks.MODID, buffer)));
                
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
