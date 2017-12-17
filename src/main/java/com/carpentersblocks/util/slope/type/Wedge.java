package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.block.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Wedge implements SlopeType {

	@Override
	public Slope getNextSlopeType(Slope slope) {
	    int slopeID = slope.slopeID;
		if (slope.isPositive) {
            slopeID -= 4;
        } else {
            slopeID += 12;
        }
		return Slope.getSlopeById(slopeID);
	}

}
