package com.carpentersblocks.tileentity;

import net.minecraft.item.ItemDye;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import com.carpentersblocks.data.FlowerPot;

public class TECarpentersFlowerPot extends TEBase {

    public static final byte ID_PLANT = 22;
    public static final byte ID_SOIL  = 23;

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

            boolean wasEnriched = FlowerPot.isEnriched(this);

            super.onDataPacket(net, pkt);

            /*
             * The server doesn't send particle spawn packets, so it
             * has to be handled client-side.
             *
             * This spawns the fertilization effect as seen when growing
             * saplings or crops with bonemeal.
             */

            if (!wasEnriched && FlowerPot.isEnriched(this)) {
                ItemDye.func_150918_a(world, xCoord, yCoord, zCoord, 15);
            }

        }
    }

}
