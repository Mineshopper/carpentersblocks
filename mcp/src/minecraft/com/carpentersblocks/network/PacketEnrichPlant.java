package com.carpentersblocks.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.carpentersblocks.data.FlowerPot;
import com.carpentersblocks.tileentity.TECarpentersFlowerPot;

public class PacketEnrichPlant extends TilePacket {

    private int hexColor;

    public PacketEnrichPlant() {}

    /**
     * For the server to examine plant color, since it's a client-side only property.
     */
    public PacketEnrichPlant(int x, int y, int z, int hexColor)
    {
        super(x, y, z);
        this.hexColor = hexColor;
    }

    @Override
    public void processData(EntityPlayer entityPlayer, DataInputStream bbis) throws IOException
    {
        super.processData(entityPlayer, bbis);
        World world = entityPlayer.worldObj;
        hexColor = bbis.readInt();

        TECarpentersFlowerPot TE = (TECarpentersFlowerPot) world.getBlockTileEntity(x, y, z);

        if (TE != null) {
            if (hexColor != 16777215 && !FlowerPot.isEnriched(TE)) {
                FlowerPot.setEnrichment(TE, true);
            }
        }
    }

    @Override
    public void appendData(DataOutputStream outputStream) throws IOException
    {
        super.appendData(outputStream);
        outputStream.writeInt(hexColor);
    }

}
