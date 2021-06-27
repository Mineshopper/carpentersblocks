package com.carpentersblocks.util.states.loader.domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public abstract class StateCommonProperties implements Serializable {

	private static final long serialVersionUID = -96822486873819912L;

	@SerializedName(value = "rotation_deg")
	protected Vec3fDto _rotationDeg;
		
	@SerializedName(value = "render_layer")
	protected String _renderLayer;
	
	@SerializedName(value = "max_brightness")
	protected String _maxBrightness;
	
	@SerializedName(value = "diffuse_lighting")
	protected String _diffuseLighting;
	
	@SerializedName(value = "icon_name")
	protected String _iconName;
	
	@SerializedName(value = "rgb")
	protected String _rgb;
	
	public Vec3fDto getRotationDeg() {
		return _rotationDeg;
	}
	
	public String getRenderLayer() {
		return _renderLayer;
	}
	
	public String isMaxBrightness() {
		return _maxBrightness;
	}
	
	public String getIconName() {
		return _iconName;
	}
	
	public String getRgb() {
		return _rgb;
	}
	
	public String hasDiffuseLighting() {
		return _diffuseLighting;
	}
	
	final public void inheritFrom(StateCommonProperties src) {
		this._rotationDeg = this.coalesce(this._rotationDeg, src.getRotationDeg());
		this._renderLayer = this.coalesce(this._renderLayer, src.getRenderLayer());
		this._maxBrightness = this.coalesce(this._maxBrightness, src.isMaxBrightness());
		this._diffuseLighting = this.coalesce(this._diffuseLighting, src.hasDiffuseLighting());
		this._iconName = this.coalesce(this._iconName, src.getIconName());
		this._rgb = this.coalesce(this._rgb, src.getRgb());
	}
	
	/**
	 * Returns first non-null input.
	 * 
	 * @param <T> object type
	 * @param t1 the first object
	 * @param t2 the second object
	 * @return the first non-null input
	 */
	protected <T> T coalesce(T t1, T t2) {
		return t1 == null ? t2 : t1;
	}
	
}
