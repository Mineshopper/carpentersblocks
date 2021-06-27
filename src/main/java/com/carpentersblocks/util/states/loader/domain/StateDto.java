package com.carpentersblocks.util.states.loader.domain;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class StateDto extends StateCommonProperties {
	
	private static final long serialVersionUID = 5872330761312843655L;
	
	private StateDto childOf;
	
	@SerializedName(value = "parent")
	String _parent;
	
	@SerializedName(value = "parts")
	Map<String, StatePartDto> _parts;
	
	public void setParent(StateDto stateDto) {
		childOf = stateDto;
	}
	
	public String getParent() {
		return _parent;
	}
	
	public Map<String, StatePartDto> getParts() {
		return _parts;
	}
	
	public void inheritFromParent() {
		if (childOf != null) {
			childOf.inheritFromParent();
			super.inheritFrom(childOf);
			for (Map.Entry<String, StatePartDto> statePartDtoEntry : childOf.getParts().entrySet()) {
				String identifier = statePartDtoEntry.getKey();
				StatePartDto quad = statePartDtoEntry.getValue();
				if (!this.getParts().containsKey(identifier)) {
					this.getParts().put(identifier, quad);
				} else {
					this.getParts().get(identifier).inheritFrom(quad);
				}
			}
		}
	}
	
}