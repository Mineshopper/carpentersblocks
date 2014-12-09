package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class ObliqueInt implements SlopeType {

	@Override
	public int onHammerLeftClick(Slope slope, int slopeID) {
		if (slope.isPositive) {
            if (++slopeID > Slope.ID_OBL_INT_POS_SW) {
                slopeID = Slope.ID_OBL_INT_POS_SE;
            }
        } else {
            if (++slopeID > Slope.ID_OBL_INT_NEG_SW) {
                slopeID = Slope.ID_OBL_INT_NEG_SE;
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
