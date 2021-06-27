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

}
