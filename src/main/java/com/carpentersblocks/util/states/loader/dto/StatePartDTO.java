package com.carpentersblocks.util.states.loader.dto;

import java.io.Serializable;
import java.util.Set;

import com.carpentersblocks.util.states.loader.Vec3f;
import com.google.gson.annotations.SerializedName;

public class StatePartDTO implements Serializable {
	
	@SerializedName(value = "vertex_min")
	private Vec3f _vertexMin;
	
	@SerializedName(value = "vertex_max")
	private Vec3f _vertexMax;
	
	@SerializedName(value = "render_faces")
	private Set<String> _renderFaces;
	
	@SerializedName(value = "render_layer")
	private String _renderLayer;
	
	@SerializedName(value = "max_brightness")
	private boolean _isMaxBrightness;
	
	@SerializedName(value = "icon_name")
	private String _iconName;
	
	@SerializedName(value = "rgb")
	private String _rgb;
	
	public Vec3f getVertexMin() {
		return _vertexMin;
	}
	
	public void setVertexMin(Vec3f vertexMin) {
		_vertexMin = vertexMin;
	}
	
	public Vec3f getVertexMax() {
		return _vertexMax;
	}
	
	public void setVertexMax(Vec3f vertexMax) {
		_vertexMax = vertexMax;
	}
	
	public Set<String> getRenderFaces() {
		return _renderFaces;
	}
	
	public void setRenderFaces(Set<String> renderFaces) {
		_renderFaces = renderFaces;
	}
	
	public String getRenderLayer() {
		return _renderLayer;
	}
	
	public void setRenderLayer(String renderLayer) {
		_renderLayer = renderLayer;
	}
	
	public boolean isMaxBrightness() {
		return _isMaxBrightness;
	}
	
	public void setMaxBrightness(boolean isMaxBrightness) {
		_isMaxBrightness = isMaxBrightness;
	}
	
	public String getIconName() {
		return _iconName;
	}
	
	public void setIconName(String iconName) {
		_iconName = iconName;
	}
	
	public String getRgb() {
		return _rgb;
	}
	
	public void setRgb(String rgb) {
		_rgb = rgb;
	}
	
}
