package com.carpentersblocks.util.states.loader.domain;

import com.google.gson.annotations.SerializedName;

public class StatePartQuadComponentDto extends AbstractStatePartComponentDto {

	private static final long serialVersionUID = 1L;

	@SerializedName(value = "facing")
	private String _facing;
		
	@SerializedName(value = "draw_reverse")
	private String _drawReverse;
	
	public String getFacing() {
		return _facing;
	}
	
	public String getDrawReverse() {
		return _drawReverse;
	}
	
	public void inheritFrom(StatePartQuadComponentDto dto) {
		super.inheritFrom(dto);
		this._facing = this.coalesce(this._facing, dto.getFacing());
		this._drawReverse = this.coalesce(this._drawReverse, dto.getDrawReverse());
	}
	
}