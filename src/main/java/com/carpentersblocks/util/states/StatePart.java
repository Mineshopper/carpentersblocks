package com.carpentersblocks.util.states;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class StatePart {
	
	private Vec3d _vertexMin;
	private Vec3d _vertexMax;
	private Set<EnumFacing> _renderFaces;
	private BlockRenderLayer _renderLayer;
	private boolean _isMaxBrightness;
	private String _iconName;
	private int _rgb;
	private boolean _canSeeSky;
	
	public Vec3d getVertexMin() {
		return _vertexMin;
	}
	
	public void setVertexMin(Vec3d vertexMin) {
		_vertexMin = vertexMin;
	}
	
	public Vec3d getVertexMax() {
		return _vertexMax;
	}
	
	public void setVertexMax(Vec3d vertexMax) {
		_vertexMax = vertexMax;
	}
	
	public Set<EnumFacing> getRenderFaces() {
		return _renderFaces;
	}
	
	public void setRenderFaces(Set<EnumFacing> renderFaces) {
		_renderFaces = renderFaces;
	}
	
	public BlockRenderLayer getRenderLayer() {
		return _renderLayer;
	}
	
	public void setRenderLayer(BlockRenderLayer renderLayer) {
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
	
	public int getRgb() {
		return _rgb;
	}
	
	public void setRgb(int rgb) {
		_rgb = rgb;
	}

	public boolean canSeeSky() {
		return _canSeeSky;
	}

	public void setCanSeeSky(boolean _canSeeSky) {
		this._canSeeSky = _canSeeSky;
	}
	
	public boolean canCover() {
		return _iconName.indexOf("uncovered_") != -1;
	}
	
	public StatePart copyOf() {
		StatePart statePart = new StatePart();
		statePart.setMaxBrightness(_isMaxBrightness);
		statePart.setCanSeeSky(_canSeeSky);
		statePart.setRenderFaces(new HashSet(_renderFaces));
		statePart.setRenderLayer(_renderLayer);
		statePart.setRgb(_rgb);
		statePart.setIconName(_iconName);
		statePart.setVertexMin(new Vec3d(_vertexMin.x, _vertexMin.y, _vertexMin.z));
		statePart.setVertexMax(new Vec3d(_vertexMax.x, _vertexMax.y, _vertexMax.z));
		return statePart;
	}
	
}