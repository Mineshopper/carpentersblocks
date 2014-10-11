package com.carpentersblocks.tileentity;

import net.minecraft.item.ItemDye;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;

public class TECarpentersFlowerPot extends TEBase {

    @Override
    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        World world = getWorldObj();

        if (world.isRemote) {

            boolean wasEnriched = hasAttribute(ATTR_FERTILIZER);

            super.onDataPacket(net, pkt);

            /*
             * The server doesn't send particle spawn packets, so it
             * has to be handled client-side.
             *
             * This spawns the fertilization effect as seen when growing
             * saplings or crops with bonemeal.
             */

            if (!wasEnriched && hasAttribute(ATTR_FERTILIZER)) {
                ItemDye.func_150918_a(world, xCoord, yCoord, zCoord, 15);
            }

        }
    }

}
