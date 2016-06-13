package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class WedgeSide implements SlopeType {

	@Override
	public Slope onHammerLeftClick(Slope slope) {
	    int slopeID = slope.slopeID;
		if (++slopeID > Slope.ID_WEDGE_SW) {
            slopeID = Slope.ID_WEDGE_SE;
        }
		return Slope.getSlopeById(slopeID);
	}

	@Override
	public Slope onHammerRightClick(Slope slope) {
	    int slopeID = slope.slopeID;
		slopeID += 8;
		return Slope.getSlopeById(slopeID);
	}
}
