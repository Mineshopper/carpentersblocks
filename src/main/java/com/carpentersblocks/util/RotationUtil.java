package com.carpentersblocks.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

public class RotationUtil {
	
	private final static double[] RADS = { 0.0D, Math.PI / 2, Math.PI, 3 * Math.PI / 2 };
	
	private final static int ROT_0 = 0;
	private final static int ROT_90 = 1;
	private final static int ROT_180 = 2;
	private final static int ROT_270 = 3;
	
	private static final Rotation[][] ROTATIONS = {
		{	Rotation.X90_Y0_Z0,		Rotation.X0_Y90_Z0,		Rotation.X0_Y0_Z90		},
		{	Rotation.X90_Y90_Z0,	Rotation.X270_Y0_Z90,	Rotation.X0_Y0_Z180		},
		{	Rotation.X90_Y180_Z0,	Rotation.X180_Y270_Z0,	Rotation.X0_Y0_Z270		},
		{ 	Rotation.X90_Y270_Z0,	Rotation.X90_Y270_Z0,	Rotation.X0_Y0_Z0		},
		{	Rotation.X90_Y0_Z270,	Rotation.X0_Y180_Z0,	Rotation.X90_Y90_Z0		},
		{	Rotation.X90_Y0_Z180,	Rotation.X0_Y270_Z0,	Rotation.X0_Y180_Z90	},
		{	Rotation.X0_Y270_Z90,	Rotation.X90_Y180_Z90,	Rotation.X180_Y0_Z0		},
		{	Rotation.X90_Y0_Z90,	Rotation.X0_Y0_Z0,		Rotation.X0_Y270_Z90	},
		{	Rotation.X0_Y0_Z90,		Rotation.X270_Y0_Z0,	Rotation.X180_Y270_Z0	},
		{	Rotation.X180_Y0_Z0,	Rotation.X90_Y90_Z0,	Rotation.X90_Y0_Z90		},
		{	Rotation.X180_Y90_Z0,	Rotation.X0_Y0_Z90,		Rotation.X90_Y0_Z180	},
		{	Rotation.X0_Y0_Z180,	Rotation.X0_Y270_Z90,	Rotation.X90_Y0_Z270	},
		{	Rotation.X180_Y270_Z0,	Rotation.X0_Y180_Z90,	Rotation.X90_Y0_Z0		},
		{	Rotation.X0_Y180_Z90,	Rotation.X90_Y180_Z0,	Rotation.X180_Y90_Z0	},
		{	Rotation.X0_Y180_Z0,	Rotation.X90_Y270_Z0,	Rotation.X90_Y180_Z90	},
		{	Rotation.X0_Y270_Z0,	Rotation.X0_Y0_Z270,	Rotation.X270_Y0_Z0		},
		{	Rotation.X180_Y0_Z90,	Rotation.X90_Y0_Z0,		Rotation.X0_Y270_Z0		},
		{	Rotation.X270_Y0_Z0,	Rotation.X180_Y90_Z0,	Rotation.X180_Y0_Z90	},
		{	Rotation.X270_Y90_Z0,	Rotation.X90_Y0_Z90,	Rotation.X0_Y180_Z0		},
		{	Rotation.X90_Y180_Z90,	Rotation.X0_Y0_Z180,	Rotation.X270_Y90_Z0	},
		{	Rotation.X270_Y0_Z90,	Rotation.X180_Y0_Z0,	Rotation.X90_Y270_Z0	},
		{	Rotation.X0_Y0_Z0,		Rotation.X270_Y90_Z0,	Rotation.X270_Y0_Z90	},
		{	Rotation.X0_Y90_Z0,		Rotation.X180_Y0_Z90,	Rotation.X90_Y180_Z0	},
		{	Rotation.X0_Y0_Z270,	Rotation.X90_Y0_Z180,	Rotation.X0_Y90_Z0		}
	};
	
	public enum Rotation {
		
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
		
		private int _x;
		
		private int _y;
		
		private int _z;
		
		Rotation(int x, int y, int z) {
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
		public static Rotation get(int cbMetadata) {
			int val = cbMetadata & 0x3f;
			for (Rotation rotation : values()) {
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
		 * Gets rotated facing.
		 * 
		 * @param facing the input facing
		 * @return a rotated facing
		 */
		public EnumFacing getRotatedFacing(EnumFacing facing) {
			EnumFacing rotation = facing;
			int x = 0;
			while (x++ < _x) {
				rotation = rotation.rotateAround(EnumFacing.Axis.X);
			}
			int y = 0;
			while (y++ < _y) {
				rotation = rotation.rotateAround(EnumFacing.Axis.Y);
			}
			int z = 0;
			while (z++ < _z) {
				rotation = rotation.rotateAround(EnumFacing.Axis.Z);
			}
			return rotation;
		}
		
		/**
		 * Gets next rotation.
		 * 
		 * @param axis axis of rotation
		 * @return a rotation
		 */
		public Rotation getNext(Axis axis) {
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

}
