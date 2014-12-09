package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Prism implements SlopeType {

	@Override
	public int onHammerLeftClick(Slope slope, int slopeID) {
		return slopeID;
	}

	@Override
	public int onHammerRightClick(Slope slope, int slopeID) {
		if (slope.isPositive) {
            slopeID = Slope.ID_PRISM_NEG;
        } else {
            slopeID = Slope.ID_PRISM_1P_POS_N;
        }
		return slopeID;
	}

}
