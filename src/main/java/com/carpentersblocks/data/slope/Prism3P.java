package com.carpentersblocks.data.slope;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.data.SlopeType;

public class Prism3P extends SlopeType {

	@Override
	public int onHammerLeftClick(Slope slope, int slopeID) {
		if (++slopeID > Slope.ID_PRISM_3P_POS_NSE) {
            slopeID = Slope.ID_PRISM_3P_POS_NWE;
        }
		return slopeID;
	}

	@Override
	public int onHammerRightClick(Slope slope, int slopeID) {
		slopeID = Slope.ID_PRISM_POS_4P;
		return slopeID;
	}

}
