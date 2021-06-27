package com.carpentersblocks.util;

import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.vector.Vector3d;

public class RotationUtil {
	
	private final static double[] RADS = { 0.0D, Math.PI / 2, Math.PI, 3 * Math.PI / 2 };
	
	private final static int ROT_0 = 0;
	private final static int ROT_90 = 1;
	private final static int ROT_180 = 2;
	private final static int ROT_270 = 3;
	
	private static final CbRotation[][] ROTATIONS = {
		{	CbRotation.X90_Y0_Z0,		CbRotation.X0_Y90_Z0,		CbRotation.X0_Y0_Z90	},
		{	CbRotation.X90_Y90_Z0,		CbRotation.X270_Y0_Z90,		CbRotation.X0_Y0_Z180	},
		{	CbRotation.X90_Y180_Z0,		CbRotation.X180_Y270_Z0,	CbRotation.X0_Y0_Z270	},
		{ 	CbRotation.X90_Y270_Z0,		CbRotation.X90_Y270_Z0,		CbRotation.X0_Y0_Z0		},
		{	CbRotation.X90_Y0_Z270,		CbRotation.X0_Y180_Z0,		CbRotation.X90_Y90_Z0	},
		{	CbRotation.X90_Y0_Z180,		CbRotation.X0_Y270_Z0,		CbRotation.X0_Y180_Z90	},
		{	CbRotation.X0_Y270_Z90,		CbRotation.X90_Y180_Z90,	CbRotation.X180_Y0_Z0	},
		{	CbRotation.X90_Y0_Z90,		CbRotation.X0_Y0_Z0,		CbRotation.X0_Y270_Z90	},
		{	CbRotation.X0_Y0_Z90,		CbRotation.X270_Y0_Z0,		CbRotation.X180_Y270_Z0	},
		{	CbRotation.X180_Y0_Z0,		CbRotation.X90_Y90_Z0,		CbRotation.X90_Y0_Z90	},
		{	CbRotation.X180_Y90_Z0,		CbRotation.X0_Y0_Z90,		CbRotation.X90_Y0_Z180	},
		{	CbRotation.X0_Y0_Z180,		CbRotation.X0_Y270_Z90,		CbRotation.X90_Y0_Z270	},
		{	CbRotation.X180_Y270_Z0,	CbRotation.X0_Y180_Z90,		CbRotation.X90_Y0_Z0	},
		{	CbRotation.X0_Y180_Z90,		CbRotation.X90_Y180_Z0,		CbRotation.X180_Y90_Z0	},
		{	CbRotation.X0_Y180_Z0,		CbRotation.X90_Y270_Z0,		CbRotation.X90_Y180_Z90	},
		{	CbRotation.X0_Y270_Z0,		CbRotation.X0_Y0_Z270,		CbRotation.X270_Y0_Z0	},
		{	CbRotation.X180_Y0_Z90,		CbRotation.X90_Y0_Z0,		CbRotation.X0_Y270_Z0	},
		{	CbRotation.X270_Y0_Z0,		CbRotation.X180_Y90_Z0,		CbRotation.X180_Y0_Z90	},
		{	CbRotation.X270_Y90_Z0,		CbRotation.X90_Y0_Z90,		CbRotation.X0_Y180_Z0	},
		{	CbRotation.X90_Y180_Z90,	CbRotation.X0_Y0_Z180,		CbRotation.X270_Y90_Z0	},
		{	CbRotation.X270_Y0_Z90,		CbRotation.X180_Y0_Z0,		CbRotation.X90_Y270_Z0	},
		{	CbRotation.X0_Y0_Z0,		CbRotation.X270_Y90_Z0,		CbRotation.X270_Y0_Z90	},
		{	CbRotation.X0_Y90_Z0,		CbRotation.X180_Y0_Z90,		CbRotation.X90_Y180_Z0	},
		{	CbRotation.X0_Y0_Z270,		CbRotation.X90_Y0_Z180,		CbRotation.X0_Y90_Z0	}
	};
	
	public enum CbRotation {
		
		/** UP + NORTH */
		X0_Y0_Z0(ROT_0, ROT_0, ROT_0),
		
		/** EAST + NORTH */
		X0_Y0_Z90(ROT_0, ROT_0, ROT_90),
		
		/** DOWN + NORTH */
		X0_Y0_Z180(ROT_0, ROT_0, ROT_180),
		
		/** WEST + NORTH */
		X0_Y0_Z270(ROT_0, ROT_0, ROT_270),
		
		/** UP + EAST */
		X0_Y90_Z0(ROT_0, ROT_90, ROT_0),
		
		/** UP + SOUTH */
		X0_Y180_Z0(ROT_0, ROT_180, ROT_0),
		
		/** EAST + SOUTH */
		X0_Y180_Z90(ROT_0, ROT_180, ROT_90),
		
		/** UP + WEST */
		X0_Y270_Z0(ROT_0, ROT_270, ROT_0),
		
		/** EAST + DOWN */
		X0_Y270_Z90(ROT_0, ROT_270, ROT_90),
		
		/** NORTH + UP */
		X90_Y0_Z0(ROT_90, ROT_0, ROT_0),
		
		/** NORTH + EAST */
		X90_Y0_Z90(ROT_90, ROT_0, ROT_90),
		
		/** NORTH + DOWN */
		X90_Y0_Z180(ROT_90, ROT_0, ROT_180),
		
		/** NORTH + WEST */
		X90_Y0_Z270(ROT_90, ROT_0, ROT_270),
		
		/** EAST + UP */
		X90_Y90_Z0(ROT_90, ROT_90, ROT_0),
		
		/** SOUTH + UP */
		X90_Y180_Z0(ROT_90, ROT_180, ROT_0),
		
		/** SOUTH + WEST */
		X90_Y180_Z90(ROT_90, ROT_180, ROT_90),
		
		/** WEST + UP */
		X90_Y270_Z0(ROT_90, ROT_270, ROT_0),
		
		/** DOWN + SOUTH */
		X180_Y0_Z0(ROT_180, ROT_0, ROT_0),
		
		/** WEST + SOUTH */
		X180_Y0_Z90(ROT_180, ROT_0, ROT_90),
		
		/** DOWN + WEST */
		X180_Y90_Z0(ROT_180, ROT_90, ROT_0),
		
		/** DOWN + EAST */
		X180_Y270_Z0(ROT_180, ROT_270, ROT_0),
		
		/** SOUTH + DOWN */
		X270_Y0_Z0(ROT_270, ROT_0, ROT_0),
		
		/** SOUTH + EAST */
		X270_Y0_Z90(ROT_270, ROT_0, ROT_90),
		
		/** WEST + DOWN */
		X270_Y90_Z0(ROT_270, ROT_90, ROT_0);
		
		/** Holds axis-based rotated directions in [input_direction][axis] array. */
		private final Direction[][] _rotatedDirections = {
				{ Direction.SOUTH, Direction.DOWN,  Direction.WEST },
				{ Direction.NORTH,   Direction.UP,  Direction.EAST },
				{ Direction.DOWN,  Direction.EAST, Direction.NORTH },
				{ Direction.UP,    Direction.WEST, Direction.SOUTH },
				{ Direction.WEST, Direction.NORTH,    Direction.UP },
				{ Direction.EAST, Direction.SOUTH,  Direction.DOWN }
		};
		
		private int _x;
		private int _y;
		private int _z;
		
		CbRotation(int x, int y, int z) {
			_x = x;
			_y = y;
			_z = z;
		}
		
		/**
		 * Extracts rotation from cbMetadata.
		 * 
		 * @param cbMetadata the metadata
		 * @return a rotation
		 */
		public static CbRotation get(int cbMetadata) {
			int val = cbMetadata & 0x3f;
			for (CbRotation rotation : values()) {
				if (rotation.asInt() == val) {
					return rotation;
				}
			}
			return X0_Y0_Z0;
		}
		
		/**
		 * Gets radians of rotation for axis.
		 * 
		 * @param axis the input axis
		 * @return radians of rotation
		 */
		public double getRadians(Axis axis) {
			switch (axis) {
				case X:
					return RADS[_x];
				case Y:
					return RADS[_y];
				default: // Z
					return RADS[_z];
			}
		}
		
		/**
		 * Gets rotated direction.
		 * 
		 * @param direction the input direction
		 * @return a rotated direction
		 */
		public Direction getRotatedDirection(Direction direction) {
			int x = 0;
			while (x++ < _x) {
				direction = _rotatedDirections[direction.ordinal()][0];
			}
			int y = 0;
			while (y++ < _y) {
				direction = _rotatedDirections[direction.ordinal()][1];
			}
			int z = 0;
			while (z++ < _z) {
				direction = _rotatedDirections[direction.ordinal()][2];
			}
			return direction;
		}
		
		/**
		 * Gets next rotation.
		 * 
		 * @param axis axis of rotation
		 * @return a rotation
		 */
		public CbRotation getNext(Axis axis) {
			return ROTATIONS[this.ordinal()][axis.ordinal()];
		}
		
		/**
		 * Gets rotation as 6-bit integer.
		 * 
		 * @return integer representation of rotation
		 */
		public Integer asInt() {
			return _x << 4 | _y << 2 | _z;
		}
		
	}
	
	/**
	 * Rotates quads about axis. Positive axis is clockwise rotation.
	 * Negative axis is counter-clockwise rotation.
	 * 
	 * @param axis the axis of rotation
	 * @param rotation the rotation enum
	 */
	public static Vector3d[] rotate(Vector3d[] vec3ds, CbRotation rotation) {
		Vector3d[] newVecs = new Vector3d[vec3ds.length];
		for (int i = 0; i < vec3ds.length; ++i) {
			Vector3d vec3d = vec3ds[i];
			Vector3d vec3dRot = vec3d.add(Vector3d.ZERO);
			if (rotation.getRadians(Axis.X) > 0) {
				vec3dRot = rotateAroundX(vec3dRot, rotation.getRadians(Axis.X));
			}
			if (rotation.getRadians(Axis.Y) > 0) {
				vec3dRot = rotateAroundY(vec3dRot, rotation.getRadians(Axis.Y));
			}
			if (rotation.getRadians(Axis.Z) > 0) {
				vec3dRot = rotateAroundZ(vec3dRot, rotation.getRadians(Axis.Z));
			}
			newVecs[i] = vec3dRot;
		}
		return newVecs;
	}
	
	/**
	 * Rotates quads about axis. Positive axis is clockwise rotation.
	 * Negative axis is counter-clockwise rotation.
	 * 
	 * @param axis the axis of rotation
	 * @param rotation the rotation enum
	 */
	public static Vector3d[] rotate(Vector3d[] vec3ds, Vector3d rotation) {
		Vector3d[] newVecs = new Vector3d[vec3ds.length];
		for (int i = 0; i < vec3ds.length; ++i) {
			Vector3d vec3d = vec3ds[i];
			Vector3d vec3dRot = vec3d.add(Vector3d.ZERO);
			if (rotation.x > 0) {
				vec3dRot = rotateAroundX(vec3dRot, rotation.x);
			}
			if (rotation.y > 0) {
				vec3dRot = rotateAroundY(vec3dRot, rotation.y);
			}
			if (rotation.z > 0) {
				vec3dRot = rotateAroundZ(vec3dRot, rotation.z);
			}
			newVecs[i] = vec3dRot;
		}
		return newVecs;
	}
	
	/**
	 * Rotates vec3d about X axis.
	 * 
	 * @param vec3d the vec3d
	 * @param radians amount of rotation
	 * @return the rotated vec3d
	 */
	private static Vector3d rotateAroundX(Vector3d vec3d, double radians) {
    	double y = 0.5D + (vec3d.z - 0.5D) * Math.sin(radians) + (vec3d.y - 0.5D) * Math.cos(radians);
    	double z = 0.5D + (vec3d.z - 0.5D) * Math.cos(radians) - (vec3d.y - 0.5D) * Math.sin(radians);
    	return new Vector3d(vec3d.x, y, z);
    }
	
	/**
	 * Rotates vec3d about Y axis.
	 * 
	 * @param vec3d the vec3d
	 * @param radians amount of rotation
	 * @return the rotated vec3d
	 */
	private static Vector3d rotateAroundY(Vector3d vec3d, double radians) {
    	double z = 0.5D + (vec3d.x - 0.5D) * Math.sin(radians) + (vec3d.z - 0.5D) * Math.cos(radians);
    	double x = 0.5D + (vec3d.x - 0.5D) * Math.cos(radians) - (vec3d.z - 0.5D) * Math.sin(radians);
    	return new Vector3d(x, vec3d.y, z);
    }
    
	/**
	 * Rotates vec3d about Z axis.
	 * 
	 * @param vec3d the vec3d
	 * @param radians amount of rotation
	 * @return the rotated vec3d
	 */
	private static Vector3d rotateAroundZ(Vector3d vec3d, double radians) {
    	double x = 0.5D + (vec3d.y - 0.5D) * Math.sin(radians) + (vec3d.x - 0.5D) * Math.cos(radians);
    	double y = 0.5D + (vec3d.y - 0.5D) * Math.cos(radians) - (vec3d.x - 0.5D) * Math.sin(radians);
    	return new Vector3d(x, y, vec3d.z);
    }
	
}