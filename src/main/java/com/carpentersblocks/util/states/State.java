package com.carpentersblocks.util.states;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class State {
	
	Map<String, StatePart> _parts;
	
	public State() {}
	
	public State(State state) {
		_parts = new HashMap<String, StatePart>();
		Iterator iter = state.getStateParts().entrySet().iterator();
	    while (iter.hasNext()) {
	        Map.Entry<String, StatePart> pair = (Map.Entry) iter.next();
	        _parts.put(pair.getKey(), pair.getValue().copyOf());
	    }
	}
	
	public Map<String, StatePart> getStateParts() {
		return _parts;
	}
	
	public void setStateParts(Map<String, StatePart> stateParts) {
		_parts = stateParts;
	}
	
}
