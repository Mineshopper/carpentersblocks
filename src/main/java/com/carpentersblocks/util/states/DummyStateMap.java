package com.carpentersblocks.util.states;

import java.util.HashMap;
import java.util.Map;

public class DummyStateMap extends StateMap {
	
	private static final String STATE_DEFAULT = "default";
	private static final String STATE_PART_BASE = "base";
	
	public DummyStateMap() {
		super();
		Map<String, StatePart> statePartMap = new HashMap<String, StatePart>();
		State state = new State(statePartMap);
		this.putState(STATE_DEFAULT, state);
		statePartMap.put(STATE_PART_BASE, StatePart.getEmptyStatePart());
	}
	
	/**
	 * 
	 */
	public State getState(String key) {
		return super.getState(STATE_DEFAULT);
	}
	
}