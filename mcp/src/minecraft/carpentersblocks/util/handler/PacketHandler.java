package carpentersblocks.util.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.block.BlockCarpentersSlope;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

    public static final byte PACKET_SLOPE_SELECT    = 0;
    public static final byte PACKET_BLOCK_ACTIVATED = 1;

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        if (packet.channel.equals(CarpentersBlocks.MODID)) {

            EntityPlayer entityPlayer = (EntityPlayer) player;
            ItemStack itemStack = entityPlayer.getHeldItem();
            DataInputStream bbis = new DataInputStream(new ByteArrayInputStream(packet.data));

            switch (getEventId(bbis)) {
                case PACKET_SLOPE_SELECT:

                    if (itemStack != null) {
                        try {
                        	int maxDmg = BlockCarpentersSlope.slopeType.length - 1;
                        	int itemDmg = itemStack.getItemDamage();
                        	itemDmg += bbis.readBoolean() ? 1 : -1;

                        	if (itemDmg > maxDmg) {
                        		itemDmg = 0;
                        	} else if (itemDmg < 0) {
                        		itemDmg = maxDmg;
                        	}

                        	itemStack.setItemDamage(itemDmg);
                        } catch (IOException e) { }
                    }

                    break;
                case PACKET_BLOCK_ACTIVATED:

                    try {

                        int x = bbis.readInt();
                        int y = bbis.readInt();
                        int z = bbis.readInt();
                        int side = bbis.readInt();

                        Block block = Block.blocksList[entityPlayer.worldObj.getBlockId(x, y, z)];

                        if (block != null) {

                            boolean result = block.onBlockActivated(entityPlayer.worldObj, x, y, z, entityPlayer, side, 1.0F, 1.0F, 1.0F);

                            if (!result) {

                                if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {

                                    itemStack.tryPlaceItemIntoWorld(entityPlayer, entityPlayer.worldObj, x, y, z, side, 1.0F, 1.0F, 1.0F);

                                    if (!entityPlayer.capabilities.isCreativeMode && --itemStack.stackSize <= 0) {
                                        entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, (ItemStack)null);
                                    }

                                }

                            }

                        }

                    } catch (IOException e) { }

                    break;
                default: {}
            }

        }
    }

    /**
     * Returns mod-specific event ID value.
     * If fails, returns class packet ID.
     */
    private int getEventId(DataInputStream inputStream)
    {
        int packetId = 0;

        try {
            packetId = inputStream.readInt();
        } catch (Exception e) {}

        return packetId;
    }

    public static void sendPacketToServer(int packetId, Object ... obj)
    {
        try {

            Packet250CustomPayload packet = new Packet250CustomPayload();
            ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
            DataOutputStream outputStream = new DataOutputStream(bos);
            outputStream.writeInt(packetId);
            switch (packetId) {
                case PACKET_SLOPE_SELECT:
                    outputStream.writeBoolean((Boolean) obj[0]);
                    break;
                case PACKET_BLOCK_ACTIVATED:
                    outputStream.writeInt((Integer) obj[0]);
                    outputStream.writeInt((Integer) obj[1]);
                    outputStream.writeInt((Integer) obj[2]);
                    outputStream.writeInt((Integer) obj[3]);
                    break;
                default:
                    break;
            }

            packet.channel = CarpentersBlocks.MODID;
            packet.data = bos.toByteArray();
            packet.length = bos.size();

            Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);

        } catch (IOException e) { }
    }

}
