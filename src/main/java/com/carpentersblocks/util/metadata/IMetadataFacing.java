package com.carpentersblocks.util.metadata;

import com.carpentersblocks.nbt.CbTileEntity;

import net.minecraft.util.Direction;

public interface IMetadataFacing {

    /**
     * Sets block facing.
     *
     * @param  cbTileEntity the tile entity
     * @param  facing the block facing
     * @return <code>true</code> if facing changed
     */
    public boolean setFacing(CbTileEntity cbTileEntity, Direction facing);

    /**
     * Gets block facing.
     * 
     * @return the block facing
     */
    public Direction getFacing();

}
