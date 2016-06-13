package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Wedge implements SlopeType {

	@Override
	public Slope onHammerLeftClick(Slope slope) {
	    int slopeID = slope.slopeID;
		if (slope.isPositive) {
            if (++slopeID > Slope.ID_WEDGE_POS_E) {
                slopeID = Slope.ID_WEDGE_POS_N;
            }
        } else {
            if (++slopeID > Slope.ID_WEDGE_NEG_E) {
                slopeID = Slope.ID_WEDGE_NEG_N;
            }
        }
		return Slope.getSlopeById(slopeID);
	}

	@Override
	public Slope onHammerRightClick(Slope slope) {
	    int slopeID = slope.slopeID;
		if (slope.isPositive) {
            slopeID -= 4;
        } else {
            slopeID += 12;
        }
		return Slope.getSlopeById(slopeID);
	}

}
