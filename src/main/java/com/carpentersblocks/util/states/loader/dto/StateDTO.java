package com.carpentersblocks.util.states.loader.dto;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class StateDTO {
	
	@SerializedName(value = "parts")
	Map<String, StatePartDTO> _parts;
		
	public Map<String, StatePartDTO> getParts() {
		return _parts;
	}
	
}