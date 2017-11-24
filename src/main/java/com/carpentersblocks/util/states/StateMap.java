package com.carpentersblocks.util.states;

import java.util.HashMap;
import java.util.Map;

public class StateMap {

	protected Map<String, State> _stateMap;
	
	protected StateMap() {
		_stateMap = new HashMap<String, State>();
	}
	
	State putState(String key, State value) {
		return _stateMap.put(key, value);
	}
	
	public State getState(String key) {
		return _stateMap.get(key);
	}
	
}
