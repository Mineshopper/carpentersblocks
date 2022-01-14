package com.carpentersblocks.util;

public class Point2d {

	private double u;
	private double v;
	
	public Point2d(double u, double v) {
		this.u = u;
		this.v = v;
	}

	public double getU() {
		return u;
	}

	public void setU(double u) {
		this.u = u;
	}

	public double getV() {
		return v;
	}

	public void setV(double v) {
		this.v = v;
	}

	public double distance(Point2d point2d) {
		return Math.sqrt(Math.pow(point2d.u - this.u, 2) + Math.pow(point2d.v - this.v,2));
	}
	
	@Override
	public int hashCode() {
		long j = Double.doubleToLongBits(this.u);
		int i = (int)(j ^ j >>> 32);
		j = Double.doubleToLongBits(this.v);
		return 31 * i + (int)(j ^ j >>> 32);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof Point2d)) {
			return false;
		} else {
			Point2d point2d = (Point2d) obj;
			if (Double.compare(point2d.u, this.u) != 0) {
				return false;
			} else {
				return Double.compare(point2d.v, this.v) == 0;
			}
		}
	}

}
