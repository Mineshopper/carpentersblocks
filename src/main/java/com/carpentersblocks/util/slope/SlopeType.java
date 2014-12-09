package com.carpentersblocks.util.slope;

import com.carpentersblocks.data.Slope;

public interface SlopeType {

	public int onHammerLeftClick(Slope slope, int slopeID);

	public int onHammerRightClick(Slope slope, int slopeID);

}
