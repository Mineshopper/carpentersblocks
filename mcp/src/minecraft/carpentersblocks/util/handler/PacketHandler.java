package carpentersblocks.util.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.block.BlockCarpentersSlope;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketHandler implements IPacketHandler {

    public final static int PACKET_DAMAGE_SLOPE_IN_SLOT = 0;

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        if (packet.channel.equals(CarpentersBlocks.MODID)) {

            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

            switch (getEventId(inputStream)) {
                case PACKET_DAMAGE_SLOPE_IN_SLOT:
                    damageSlopeInSlot(inputStream, (EntityPlayer) player);
                    break;
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

    /**
     * Updates player inventory slot with new ItemStack.
     * Used to dynamically adjust slope type with mouse event.
     */
    public static void damageSlopeInSlot(DataInputStream inputStream, EntityPlayer entityPlayer)
    {
        try {

            inputStream.readInt();
            boolean damage = inputStream.readBoolean();

            ItemStack itemStack = entityPlayer.getHeldItem();

            if (itemStack != null) {

                if (damage) {

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    /**
     * Tells server to set slope damage in given slot.
     * When damage is true, increases damage; decreases damage for false.
     * Used to dynamically adjust slope type with mouse event.
     */
    public static void damageSlopeInSlot(int slot, boolean damage)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);

        Packet250CustomPayload packet = new Packet250CustomPayload();

        try {
            outputStream.writeInt(PACKET_DAMAGE_SLOPE_IN_SLOT);
            outputStream.writeInt(slot);
            outputStream.writeBoolean(damage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        packet.channel = CarpentersBlocks.MODID;
        packet.data = bos.toByteArray();
        packet.length = bos.size();

        Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
    }

}
