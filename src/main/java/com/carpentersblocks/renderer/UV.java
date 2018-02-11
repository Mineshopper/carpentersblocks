package com.carpentersblocks.renderer;

public class UV {

	private double _u;
	private double _v;
	
	public UV(double u, double v) {
		
		/*if (_translation != null) {
			u = Math.min(1, Math.max(0, u));
			v = Math.min(1, Math.max(0, v));
		}*/
		
		_u = u * 16;
		_v = v * 16;
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
	
	public double getU() {
		return _u;
	}
	
	public void offsetU(double offset) {
		//_u += offset * 16;
	}
	
	public double getV() {
		return _v;
	}
	
	public UV offsetV(double offset) {
		_v += offset * 16;
		return this;
	}
	
}
