package com.carpentersblocks.tileentity;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import com.carpentersblocks.data.GarageDoor;

public class TECarpentersGarageDoor extends TEBase {

    @Override
    /**
     * Garage door state change sounds are handled strictly
     * client-side so that only the nearest state change is
     * audible.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        if (getWorldObj().isRemote) {
            GarageDoor data = GarageDoor.INSTANCE;
            int oldState = data.getState(this);
            super.onDataPacket(net, pkt);
            if (data.getState(this) != oldState) {
                data.playStateChangeSound(this);
            }
        }
    }

}
