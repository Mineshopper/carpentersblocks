package com.carpentersblocks.util.states.loader.domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public abstract class AbstractStatePartComponentDto extends StateCommonProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName(value = "vertex_min")
	private Vec3fDto _vertexMin;
	
	@SerializedName(value = "vertex_max")
	private Vec3fDto _vertexMax;
	
	public Vec3fDto getVertexMin() {
		return _vertexMin;
	}
	
	public Vec3fDto getVertexMax() {
		return _vertexMax;
	}
	
	public void inheritFrom(AbstractStatePartComponentDto src) {
		super.inheritFrom(src);
		this._vertexMin = this.coalesce(this._vertexMin, src.getVertexMin());
		this._vertexMax = this.coalesce(this._vertexMax, src.getVertexMax());
	}
	
}