package com.carpentersblocks.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.IBlockAccess;
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
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        if (getWorldObj().isRemote) {

            Torch data = new Torch();
            State existing_state = data.getState(this);

            super.onDataPacket(net, pkt);

            /*
             * The server doesn't send particle spawn packets, so it
             * has to be handled client-side.
             */

            if (data.getState(this).ordinal() > existing_state.ordinal()) {
                ParticleHelper.spawnTorchBigSmoke(this);
            }

        }
    }

    /**
     * Returns the current block light value. This is the only method
     * that will grab the tile entity to calculate lighting, which
     * is a very expensive operation to call while rendering, as it is
     * called often.
     *
     * @param  blockAccess the {@link IBlockAccess} object
     * @param  x the x coordinate
     * @param  y the y coordinate
     * @param  z the z coordinate
     * @return a light value from 0 to 15
     */
    @Override
    protected int getDynamicLightValue()
    {
        int value = 0;

        // Grab current torch state light value
        Torch data = new Torch();
        switch (data.getState(this)) {
            case LIT:
                value = Blocks.torch.getLightValue();
                break;
            case SMOLDERING:
                value = (int) (Blocks.torch.getLightValue() * 0.66F);
            default: {
                value = Math.max(value, super.getDynamicLightValue());
            }
        }

        return value;
    }

}
