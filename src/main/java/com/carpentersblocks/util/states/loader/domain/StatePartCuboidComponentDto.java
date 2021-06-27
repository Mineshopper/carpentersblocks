package com.carpentersblocks.util.states.loader.domain;

import java.util.Set;

import com.google.gson.annotations.SerializedName;

public class StatePartCuboidComponentDto extends AbstractStatePartComponentDto {

	private static final long serialVersionUID = 1L;
	
	@SerializedName(value = "render_faces")
	private Set<String> _renderFaces;
	
	public Set<String> getRenderFaces() {
		return _renderFaces;
	}
	
	public void inheritFrom(StatePartCuboidComponentDto dto) {
		super.inheritFrom(dto);
		this._renderFaces = this.coalesce(this._renderFaces, dto.getRenderFaces());
	}
	
}
