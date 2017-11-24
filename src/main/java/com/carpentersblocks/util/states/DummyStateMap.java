package com.carpentersblocks.util.states;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.carpentersblocks.util.IConstants;

import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class DummyStateMap extends StateMap {
	
	private static final String STATE_DEFAULT = "default";
	private static final String PART_MAIN = "main";
	
	public DummyStateMap() {
		super();
		Map<String, StatePart> statePartMap = new HashMap<String, StatePart>();
		State state = new State();
		_stateMap.put(STATE_DEFAULT, state);
		state.setStateParts(statePartMap);
		StatePart statePart = new StatePart();
		statePart.setIconName("");
		statePart.setRenderLayer(BlockRenderLayer.SOLID);
		statePart.setRgb(IConstants.DEFAULT_RGB);
		statePart.setVertexMin(new Vec3d(0, 0, 0));
		statePart.setVertexMax(new Vec3d(1, 1, 1));
		statePart.setRenderFaces(new HashSet<EnumFacing>(Arrays.asList(EnumFacing.values())));
		statePartMap.put(PART_MAIN, statePart);
	}
	
	public State getState(String key) {
		return _stateMap.get(STATE_DEFAULT);
	}
	
}
