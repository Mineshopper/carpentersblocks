package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.block.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Prism implements SlopeType {

	@Override
	public Slope getNextSlopeType(Slope slope) {
	    int slopeID = slope.slopeID;
		if (slope.isPositive) {
            slopeID = Slope.ID_PRISM_NEG;
        } else {
            slopeID = Slope.ID_PRISM_1P_POS_N;
        }
		return Slope.getSlopeById(slopeID);
	}

}
