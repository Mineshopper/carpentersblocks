package com.carpentersblocks.util.block;

import com.carpentersblocks.tileentity.CbTileEntity;

import net.minecraft.util.EnumFacing;

public interface IDataFacing {

    /**
     * Sets block facing.
     *
     * @param  cbTileEntity the tile entity
     * @param  facing the block facing
     * @return <code>true</code> if facing changed
     */
    public boolean setFacing(CbTileEntity cbTileEntity, EnumFacing facing);

    /**
     * Gets block facing.
     * 
     * @return the block facing
     */
    public EnumFacing getFacing();

}
