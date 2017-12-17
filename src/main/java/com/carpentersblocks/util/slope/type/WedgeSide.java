package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.block.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class WedgeSide implements SlopeType {

	@Override
	public Slope getNextSlopeType(Slope slope) {
	    int slopeID = slope.slopeID;
		slopeID += 8;
		return Slope.getSlopeById(slopeID);
	}
	
}
