package com.carpentersblocks.data.slope;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.data.SlopeType;

public class Wedge extends SlopeType {

	@Override
	public int onHammerLeftClick(Slope slope, int slopeID) {
		if (slope.isPositive) {
            if (++slopeID > Slope.ID_WEDGE_POS_E) {
                slopeID = Slope.ID_WEDGE_POS_N;
            }
        } else {
            if (++slopeID > Slope.ID_WEDGE_NEG_E) {
                slopeID = Slope.ID_WEDGE_NEG_N;
            }
        }
	    return slopeID;
	}

	@Override
	public int onHammerRightClick(Slope slope, int slopeID) {
		if (slope.isPositive) {
            slopeID -= 4;
        } else {
            slopeID += 12;
        }
		return slopeID;
	}

}
