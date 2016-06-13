package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Prism implements SlopeType {

	@Override
	public Slope onHammerLeftClick(Slope slope) {
		return slope;
	}

	@Override
	public Slope onHammerRightClick(Slope slope) {
	    int slopeID = slope.slopeID;
		if (slope.isPositive) {
            slopeID = Slope.ID_PRISM_NEG;
        } else {
            slopeID = Slope.ID_PRISM_1P_POS_N;
        }
		return Slope.getSlopeById(slopeID);
	}

}
