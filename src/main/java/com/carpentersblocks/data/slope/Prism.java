package com.carpentersblocks.data.slope;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.data.SlopeType;

public class Prism extends SlopeType {

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
