package com.carpentersblocks.renderer;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class Quad {

   	private Vec3d[] _vecs;
	private EnumFacing _facing;
	
	private Quad() { }
	
	private Quad(EnumFacing facing, Vec3d ... vecs) {
		_facing = facing;
		_vecs = vecs;
	}
	
	public static Quad getQuad(EnumFacing facing, Vec3d ... vecs) {
		Vec3d[] vec3ds = VecUtil.buildVecs(facing, vecs);
		if (vec3ds == null || vec3ds.length != 4) {
			return null;
		}
		return new Quad(facing, vec3ds);
	}

	public Quad offset(double x, double y, double z) {
		for (int i = 0; i < _vecs.length; ++i) {
			_vecs[i] = _vecs[i].addVector(x, y, z);
		}
		return this;
	}
	
	public EnumFacing getFacing() {
		return _facing;
	}
	
	public void setFacing(EnumFacing facing) {
		_facing = facing;
	}
	
	public Vec3d[] getVecs() {
		return _vecs;
	}
	
/*	public Quad clone() {
		Quad quad = new Quad();
		quad.setFacing(this.getFacing());
		for (int i = 0; i < _vecs.length; ++i) {
			quad.getVecs()[i] = new Vec3d(_vecs[i].xCoord, _vecs[i].yCoord, _vecs[i].zCoord);
		}
		return quad;
	}*/
	
}
