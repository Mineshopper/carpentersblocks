package com.carpentersblocks.data;

import com.carpentersblocks.data.Slope.Type;
import com.carpentersblocks.data.slope.*;

public interface SlopeType {
	
	public int onHammerLeftClick(Slope slope, int slopeID);
	
	public int onHammerRightClick(Slope slope, int slopeID);

}
