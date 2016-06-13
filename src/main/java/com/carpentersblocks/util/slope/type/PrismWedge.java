package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class PrismWedge implements SlopeType {

	@Override
	public Slope onHammerLeftClick(Slope slope) {
	    int slopeID = slope.slopeID;
		if (++slopeID > Slope.ID_PRISM_WEDGE_POS_E) {
            slopeID = Slope.ID_PRISM_WEDGE_POS_N;
        }
		return Slope.getSlopeById(slopeID);
	}

	@Override
	public Slope onHammerRightClick(Slope slope) {
	    int slopeID = slope.slopeID;
		slopeID = Slope.ID_WEDGE_SE;
		return Slope.getSlopeById(slopeID);
	}

}
