package com.carpentersblocks.util.states;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.carpentersblocks.renderer.Quad;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	@SideOnly(Side.CLIENT)
	public List<Quad> getInventoryQuads() {
		State state = getState(StateConstants.DEFAULT_STATE);
		List<Quad> list = new ArrayList<Quad>();
		Iterator<Entry<String, StatePart>> iter = state.getStateParts().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, StatePart> entry = iter.next();
			list.addAll(StateUtil.getQuads(entry.getValue()));
		}
		return list;
	}
	
}
