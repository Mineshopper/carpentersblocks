package com.carpentersblocks.data.slope;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.data.SlopeType;

public class Prism4P implements SlopeType {

	@Override
	public int onHammerLeftClick(Slope slope, int slopeID) {
		return slopeID;
	}

	@Override
	public int onHammerRightClick(Slope slope, int slopeID) {
		slopeID = Slope.ID_PRISM_WEDGE_POS_N;
		return slopeID;
	}

}
