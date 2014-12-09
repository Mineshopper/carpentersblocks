package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Wedge implements SlopeType {

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
