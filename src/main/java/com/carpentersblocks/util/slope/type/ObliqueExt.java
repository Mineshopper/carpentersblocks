package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class ObliqueExt implements SlopeType {

	@Override
	public int onHammerLeftClick(Slope slope, int slopeID) {
		if (slope.isPositive) {
            if (++slopeID > Slope.ID_OBL_EXT_POS_SW) {
                slopeID = Slope.ID_OBL_EXT_POS_SE;
            }
        } else {
            if (++slopeID > Slope.ID_OBL_EXT_NEG_SW) {
                slopeID = Slope.ID_OBL_EXT_NEG_SE;
            }
        }
		return slopeID;
	}

	@Override
	public int onHammerRightClick(Slope slope, int slopeID) {
		if (slope.isPositive) {
            slopeID -= 4;
        } else {
            slopeID = Slope.ID_PRISM_POS;
        }
		return slopeID;
	}

}
