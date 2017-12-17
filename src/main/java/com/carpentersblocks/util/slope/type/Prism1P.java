package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.block.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Prism1P implements SlopeType {

	@Override
	public Slope getNextSlopeType(Slope slope) {
	    int slopeID = slope.slopeID;
		slopeID = Slope.ID_PRISM_2P_POS_NS;
		return Slope.getSlopeById(slopeID);
	}

}
