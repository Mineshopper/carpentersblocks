package com.carpentersblocks.util.states.loader.domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Vec3fDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName(value = "x")
	private float _x;
	
	@SerializedName(value = "y")
	private float _y;
	
	@SerializedName(value = "z")
	private float _z;
	
	public Vec3fDto(float x, float y, float z) {
		_x = x;
		_y = y;
		_z = z;
	}

	public float getX() {
		return _x;
	}
	
	public void setX(float x) {
		_x = x ;
	}
	
	public float getY() {
		return _y;
	}
	
	public void setY(float y) {
		_y = y;
	}
	
	public float getZ() {
		return _z;
	}
	
	public void setZ(float z) {
		_z = z;
	}
	
}
