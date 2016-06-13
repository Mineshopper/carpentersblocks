package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Prism4P implements SlopeType {

	@Override
	public Slope onHammerLeftClick(Slope slope) {
	    return slope;
	}

	@Override
	public Slope onHammerRightClick(Slope slope) {
	    int slopeID = slope.slopeID;
		slopeID = Slope.ID_PRISM_WEDGE_POS_N;
		return Slope.getSlopeById(slopeID);
	}

}
