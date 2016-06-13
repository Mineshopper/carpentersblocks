package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Prism3P implements SlopeType {

	@Override
	public Slope onHammerLeftClick(Slope slope) {
	    int slopeID = slope.slopeID;
		if (++slopeID > Slope.ID_PRISM_3P_POS_NSE) {
            slopeID = Slope.ID_PRISM_3P_POS_NWE;
        }
		return Slope.getSlopeById(slopeID);
	}

	@Override
	public Slope onHammerRightClick(Slope slope) {
	    int slopeID = slope.slopeID;
		slopeID = Slope.ID_PRISM_POS_4P;
		return Slope.getSlopeById(slopeID);
	}

}
