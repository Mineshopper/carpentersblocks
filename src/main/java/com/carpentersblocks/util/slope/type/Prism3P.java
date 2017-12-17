package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.block.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Prism3P implements SlopeType {

	@Override
	public Slope getNextSlopeType(Slope slope) {
	    int slopeID = slope.slopeID;
		slopeID = Slope.ID_PRISM_POS_4P;
		return Slope.getSlopeById(slopeID);
	}

}
