package com.carpentersblocks.renderer;

public class UV {

	private int _u;
	private int _v;
	
	public UV(double u, double v) {
		_u = Math.round((float)u * 16);
		_v = Math.round((float)v * 16);
		boolean resize = false;
		if (_u > 16 || _u < 0) {
			_u = Math.abs(_u % 16);
		}
		if (_v > 16 || _v < 0) {
			_v = Math.abs(_v % 16);
		}
	}
	
	public UV invertUV() {
		return invertU().invertV();
	}
	
	public UV invertU() {
		_u = 16 - _u;
		return this;
	}
	
	public UV invertV() {
		_v = 16 - _v;
		return this;
	}
	
	public int getU() {
		return _u;
	}
	
	public void offsetU(double offset) {
		//_u += Math.round((float)offset * 16);
	}
	
	public int getV() {
		return _v;
	}
	
	public UV offsetV(double offset) {
		_v += Math.round((float)offset * 16);
		return this;
	}
	
}
