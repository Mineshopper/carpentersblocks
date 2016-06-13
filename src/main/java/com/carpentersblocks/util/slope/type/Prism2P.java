package com.carpentersblocks.util.slope.type;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.slope.SlopeType;

public class Prism2P implements SlopeType {

	@Override
	public Slope onHammerLeftClick(Slope slope) {
	    int slopeID = slope.slopeID;
		if (slope.equals(Slope.PRISM_2P_POS_NS)) {
            slopeID = Slope.ID_PRISM_2P_POS_WE;
        } else if (slope.equals(Slope.PRISM_2P_POS_WE)) {
            slopeID = Slope.ID_PRISM_2P_POS_NS;
        } else if (++slopeID > Slope.ID_PRISM_2P_POS_SW) {
            slopeID = Slope.ID_PRISM_2P_POS_SE;
        }
		return Slope.getSlopeById(slopeID);
	}

	@Override
	public Slope onHammerRightClick(Slope slope) {
	    int slopeID = slope.slopeID;
		if (slope.equals(Slope.PRISM_2P_POS_NS) || slope.equals(Slope.PRISM_2P_POS_WE)) {
            slopeID = Slope.ID_PRISM_2P_POS_SE;
        } else {
            slopeID = Slope.ID_PRISM_3P_POS_NWE;
        }
		return Slope.getSlopeById(slopeID);
	}

}
