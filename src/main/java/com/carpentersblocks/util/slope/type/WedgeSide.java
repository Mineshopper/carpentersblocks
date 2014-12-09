package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class WedgeSide implements SlopeType {

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
