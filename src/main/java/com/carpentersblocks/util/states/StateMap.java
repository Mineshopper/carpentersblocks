package com.carpentersblocks.util.states;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carpentersblocks.client.renderer.Quad;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StateMap {

	private Map<String, State> _stateMap;
	
	@OnlyIn(Dist.CLIENT)
	private List<Quad> _cachedInvQuads;
	
	/**
	 * Constructor.
	 * <p>
	 * Package-level access only.
	 */
	StateMap() {
		_stateMap = new HashMap<String, State>();
	}
	
	/**
	 * Puts state into map.
	 * <p>
	 * Package-level access only.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	State putState(String key, State value) {
		return _stateMap.put(key, value);
	}
	
	/**
	 * Returns a deep copy of the state.
	 * 
	 * @param key the state key
	 * @return a state
	 */
	public State getState(String key) {
		return new State(_stateMap.get(key));
	}
	
	/**
	 * Gets inventory quads using {@link StateConstants#DEFAULT_STATE DEFAULT_STATE}.
	 * 
	 * @return inventory quads
	 */
	@OnlyIn(Dist.CLIENT)
	public List<Quad> getInventoryQuads() {
		return _cachedInvQuads != null ? _cachedInvQuads : (_cachedInvQuads = this.getState(StateConstants.DEFAULT_STATE).getQuads());
	}
	
}
