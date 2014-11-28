package com.carpentersblocks.data.slope;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.data.Slope.Type;
import com.carpentersblocks.data.SlopeType;

public class WedgeSide extends SlopeType {

	@Override
	public int onHammerLeftClick(Slope slope, int slopeID) {
		if (++slopeID > Slope.ID_WEDGE_SW) {
            slopeID = Slope.ID_WEDGE_SE;
        }
		return slopeID;
	}

	@Override
	public int onHammerRightClick(Slope slope, int slopeID) {
		slopeID += 8;
		return slopeID;
	}
}
