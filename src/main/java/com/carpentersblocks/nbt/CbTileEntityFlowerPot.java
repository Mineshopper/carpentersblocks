package com.carpentersblocks.nbt;

import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.nbt.attribute.EnumAttributeType;

import net.minecraft.item.BoneMealItem;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.world.World;

public class CbTileEntityFlowerPot extends IDesignable {

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
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        World world = getLevel();
        if (world.isClientSide()) {
            boolean wasEnriched = this.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.FERTILIZER);
            super.onDataPacket(net, pkt);

            /*
             * The server doesn't send particle spawn packets, so it
             * has to be handled client-side.
             *
             * This spawns the fertilization effect as seen when growing
             * saplings or crops with bonemeal.
             */
            if (!wasEnriched && this.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.FERTILIZER)) {
            	BoneMealItem.addGrowthParticles(world, getBlockPos(), 15);
            }
        }
    }

}
