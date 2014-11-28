package com.carpentersblocks.data.slope;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.data.SlopeType;

public class Prism2P extends SlopeType {

	@Override
	public int onHammerLeftClick(Slope slope, int slopeID) {
		if (slope.equals(Slope.PRISM_2P_POS_NS)) {
            slopeID = Slope.ID_PRISM_2P_POS_WE;
        } else if (slope.equals(Slope.PRISM_2P_POS_WE)) {
            slopeID = Slope.ID_PRISM_2P_POS_NS;
        } else if (++slopeID > Slope.ID_PRISM_2P_POS_SW) {
            slopeID = Slope.ID_PRISM_2P_POS_SE;
        }
		return slopeID;
	}

	@Override
	public int onHammerRightClick(Slope slope, int slopeID) {
		if (slope.equals(Slope.PRISM_2P_POS_NS) || slope.equals(Slope.PRISM_2P_POS_WE)) {
            slopeID = Slope.ID_PRISM_2P_POS_SE;
        } else {
            slopeID = Slope.ID_PRISM_3P_POS_NWE;
        }
		return slopeID;
	}

}
