package com.carpentersblocks.util.states.loader.domain;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class StatePartDto extends StateCommonProperties {
	
	private static final long serialVersionUID = -3244985874738133539L;

	@SerializedName(value = "quads")
	private Map<String, StatePartQuadComponentDto> _quads;
	
	@SerializedName(value = "cuboids")
	private Map<String, StatePartCuboidComponentDto> _cuboids;
	
	@SerializedName(value = "has_collision")
	private String _hasCollision;
	
	public Map<String, StatePartQuadComponentDto> getQuads() {
		if (_quads == null) {
			_quads = new HashMap<>();
		}
		return _quads;
	}

	public Map<String, StatePartCuboidComponentDto> getCuboids() {
		if (_cuboids == null) {
			_cuboids = new HashMap<>();
		}
		return _cuboids;
	}

	public String hasCollision() {
		return _hasCollision;
	}
	
	public void inheritFrom(StatePartDto statePartDto) {
		super.inheritFrom(statePartDto);
		this._hasCollision = this.coalesce(this._hasCollision, statePartDto.hasCollision());
		for (Map.Entry<String, StatePartQuadComponentDto> quadMapEntry : statePartDto.getQuads().entrySet()) {
			String identifier = quadMapEntry.getKey();
			StatePartQuadComponentDto quad = quadMapEntry.getValue();
			if (!this.getQuads().containsKey(identifier)) {
				this.getQuads().put(identifier, quad);
			} else {
				this.getQuads().get(identifier).inheritFrom(quad);
			}
		}
    	for (Map.Entry<String, StatePartCuboidComponentDto> cuboidMapEntry : statePartDto.getCuboids().entrySet()) {
    		String identifier = cuboidMapEntry.getKey();
			StatePartCuboidComponentDto cuboid = cuboidMapEntry.getValue();
			if (!this.getQuads().containsKey(identifier)) {
				this.getCuboids().put(identifier, cuboid);
			} else {
				this.getCuboids().get(identifier).inheritFrom(cuboid);
			}
    	}
	}
	
}