package com.carpentersblocks.tileentity;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;

import com.carpentersblocks.data.Torch;
import com.carpentersblocks.data.Torch.State;
import com.carpentersblocks.renderer.helper.ParticleHelper;

public class TECarpentersTorch extends TEBase {

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
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        if (getWorldObj().isRemote) {

            State existing_state = Torch.getState(this);

            super.onDataPacket(net, pkt);

            /*
             * The server doesn't send particle spawn packets, so it
             * has to be handled client-side.
             */

            if (Torch.getState(this).ordinal() > existing_state.ordinal()) {
                ParticleHelper.spawnTorchBigSmoke(this);
            }

        }
    }

}
