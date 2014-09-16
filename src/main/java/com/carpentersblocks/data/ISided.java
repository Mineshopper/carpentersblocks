package com.carpentersblocks.data;

import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;

public interface ISided {

    /**
     * Sets block direction.
     *
     * @param  TE the {@link TEBase}
     * @param  side the side
     * @return nothing
     */
    public void setDirection(TEBase TE, ForgeDirection dir);

    /**
     * Gets block direction.
     *
     * @param  TE the {@link TEBase}
     * @return the {@link ForgeDirection}
     */
    public ForgeDirection getDirection(TEBase TE);

}
