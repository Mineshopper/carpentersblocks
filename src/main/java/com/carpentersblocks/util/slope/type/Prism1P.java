package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Prism1P implements SlopeType {

	@Override
	public Slope onHammerLeftClick(Slope slope) {
	    int slopeID = slope.slopeID;
		if (++slopeID > Slope.ID_PRISM_1P_POS_E) {
            slopeID = Slope.ID_PRISM_1P_POS_N;
        }
		return Slope.getSlopeById(slopeID);
	}

	@Override
	public Slope onHammerRightClick(Slope slope) {
	    int slopeID = slope.slopeID;
		slopeID = Slope.ID_PRISM_2P_POS_NS;
		return Slope.getSlopeById(slopeID);
	}

}
