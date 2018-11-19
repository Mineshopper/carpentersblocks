package com.carpentersblocks.renderer.helper;

import com.carpentersblocks.renderer.Quad;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class RenderHelperSlope {

	public static Quad getWedgeXNeg() {
    	return Quad.getQuad(
			EnumFacing.WEST,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.0D, 1.0D, 0.0D),
			new Vec3d(0.0D, 0.0D, 0.0D),
			new Vec3d(0.0D, 0.0D, 1.0D));
	}
	
	public static Quad getWedgeXPos() {
    	return Quad.getQuad(
			EnumFacing.EAST,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(1.0D, 0.0D, 1.0D),
			new Vec3d(1.0D, 0.0D, 0.0D),
			new Vec3d(1.0D, 1.0D, 0.0D));
	}
	
	public static Quad getWedgeInteriorZPos() {
    	return Quad.getQuad(
			EnumFacing.SOUTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.0D, 1.0D, 1.0D),
			new Vec3d(0.0D, 0.0D, 1.0D),
			new Vec3d(1.0D, 0.0D, 1.0D));
	}
	
	public static Quad getWedgeExteriorZNeg() {
		return Quad.getQuad(
			EnumFacing.NORTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(1.0D, 0.0D, 0.0D),
			new Vec3d(0.0D, 0.0D, 0.0D),
			new Vec3d(0.0D, 1.0D, 0.0D));
	}
	
	public static Quad getPrismSlopeZNeg() {
		return Quad.getQuad(
			EnumFacing.NORTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.5D, 0.5D, 0.5D),
			new Vec3d(1.0D, 0.0D, 0.0D),
			new Vec3d(0.0D, 0.0D, 0.0D));
	}
	
	public static Quad getPrismZPosXNeg() {
		return Quad.getQuad(
			EnumFacing.SOUTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.5D, 0.5D, 1.0D),
			new Vec3d(0.0D, 0.0D, 1.0D),
			new Vec3d(0.5D, 0.0D, 1.0D));
	}
	
	public static Quad getPrismZPosXPos() {
		return Quad.getQuad(
			EnumFacing.SOUTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.5D, 0.5D, 1.0D),
			new Vec3d(0.5D, 0.0D, 1.0D),
			new Vec3d(1.0D, 0.0D, 1.0D));
	}
	
	public static Quad getPrismZNegXNeg() {
		return Quad.getQuad(
			EnumFacing.NORTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.5D, 0.5D, 0.0D),
			new Vec3d(0.5D, 0.0D, 0.0D),
			new Vec3d(0.0D, 0.0D, 0.0D));
	}
	
	public static Quad getPrismZNegXPos() {
		return Quad.getQuad(
			EnumFacing.NORTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.5D, 0.5D, 0.0D),
			new Vec3d(1.0D, 0.0D, 0.0D),
			new Vec3d(0.5D, 0.0D, 0.0D));
	}
	
	public static Quad getPrismSlopedZPosXNeg() {
		return Quad.getQuad(
			EnumFacing.SOUTH,
			SpriteRegistry.sprite_uncovered_quartered,
			new Vec3d(0.0D, 0.5D, 0.5D),
			new Vec3d(0.0D, 0.0D, 1.0D),
			new Vec3d(0.5D, 0.5D, 0.5D));
	}
	
	public static Quad getPrismSlopedZPosXPos() {
		return Quad.getQuad(
			EnumFacing.SOUTH,
			SpriteRegistry.sprite_uncovered_quartered,
			new Vec3d(0.5D, 0.5D, 0.5D),
			new Vec3d(1.0D, 0.0D, 1.0D),
			new Vec3d(1.0D, 0.5D, 0.5D));
	}
	
	public static Quad getPrismXNegZNeg() {
		return Quad.getQuad(
			EnumFacing.WEST,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.0D, 0.0D, 0.0D),
			new Vec3d(0.0D, 0.0D, 0.5D),
			new Vec3d(0.0D, 0.5D, 0.5D));
	}
	
	public static Quad getPrismXNegZPos() {
		return Quad.getQuad(
			EnumFacing.WEST,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.0D, 0.5D, 0.5D),
			new Vec3d(0.0D, 0.0D, 0.5D),
			new Vec3d(0.0D, 0.0D, 1.0D));
	}
	
	public static Quad getPrismSlopedXNegZPos() {
		return Quad.getQuad(
			EnumFacing.WEST,
			SpriteRegistry.sprite_uncovered_quartered,
			new Vec3d(0.5D, 0.5D, 0.5D),
			new Vec3d(0.0D, 0.0D, 1.0D),
			new Vec3d(0.5D, 0.5D, 1.0D));
	}
	
	public static Quad getPrismXPosZPos() {
		return Quad.getQuad(
			EnumFacing.EAST,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(1.0D, 0.0D, 1.0D),
			new Vec3d(1.0D, 0.0D, 0.5D),
			new Vec3d(1.0D, 0.5D, 0.5D));
	}
	
	public static Quad getPrismXPosZNeg() {
		return Quad.getQuad(
			EnumFacing.EAST,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(1.0D, 0.5D, 0.5D),
			new Vec3d(1.0D, 0.0D, 0.5D),
			new Vec3d(1.0D, 0.0D, 0.0D));
	}
	
	public static Quad getPrismSlopedXPosZPos() {
		return Quad.getQuad(
			EnumFacing.EAST,
			SpriteRegistry.sprite_uncovered_quartered,
			new Vec3d(0.5D, 0.5D, 1.0D),
			new Vec3d(1.0D, 0.0D, 1.0D),
			new Vec3d(0.5D, 0.5D, 0.5D));
	}
	
	public static Quad getInvertPrismSlopedZPos() {
		return Quad.getQuad(
			EnumFacing.SOUTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.0D, 1.0D, 0.0D),
			new Vec3d(0.5D, 0.5D, 0.5D),
			new Vec3d(1.0D, 1.0D, 0.0D));
	}
	
	public static Quad getInvertPrismZPosXNeg() {
		return Quad.getQuad(
			EnumFacing.SOUTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.0D, 1.0D, 1.0D),
			new Vec3d(0.0D, 0.0D, 1.0D),
			new Vec3d(0.5D, 0.0D, 1.0D),
			new Vec3d(0.5D, 0.5D, 1.0D));
	}
	
	public static Quad getInvertPrismZPosXPos() {
		return Quad.getQuad(
			EnumFacing.SOUTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.5D, 0.5D, 1.0D),
			new Vec3d(0.5D, 0.0D, 1.0D),
			new Vec3d(1.0D, 0.0D, 1.0D),
			new Vec3d(1.0D, 1.0D, 1.0D));
	}
	
	public static Quad getInvertPrismZNegXPos() {
		return Quad.getQuad(
			EnumFacing.NORTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(1.0D, 1.0D, 0.0D),
			new Vec3d(1.0D, 0.0D, 0.0D),
			new Vec3d(0.5D, 0.0D, 0.0D),
			new Vec3d(0.5D, 0.5D, 0.0D));
	}
	
	public static Quad getInvertPrismZNegXNeg() {
		return Quad.getQuad(
			EnumFacing.NORTH,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.5D, 0.5D, 0.0D),
			new Vec3d(0.5D, 0.0D, 0.0D),
			new Vec3d(0.0D, 0.0D, 0.0D),
			new Vec3d(0.0D, 1.0D, 0.0D));
	}
	
	public static Quad getInvertPrismXNegZNeg() {
		return Quad.getQuad(
			EnumFacing.WEST,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.0D, 1.0D, 0.0D),
			new Vec3d(0.0D, 0.0D, 0.0D),
			new Vec3d(0.0D, 0.0D, 0.5D),
			new Vec3d(0.0D, 0.5D, 0.5D));
	}
	
	public static Quad getInvertPrismXNegZPos() {
		return Quad.getQuad(
			EnumFacing.WEST,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(0.0D, 0.5D, 0.5D),
			new Vec3d(0.0D, 0.0D, 0.5D),
			new Vec3d(0.0D, 0.0D, 1.0D),
			new Vec3d(0.0D, 1.0D, 1.0D));
	}
	
	public static Quad getInvertPrismXPosZPos() {
		return Quad.getQuad(
			EnumFacing.EAST,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(1.0D, 1.0D, 1.0D),
			new Vec3d(1.0D, 0.0D, 1.0D),
			new Vec3d(1.0D, 0.0D, 0.5D),
			new Vec3d(1.0D, 0.5D, 0.5D));
	}
	
	public static Quad getInvertPrismXPosZNeg() {
		return Quad.getQuad(
			EnumFacing.EAST,
			SpriteRegistry.sprite_uncovered_full,
			new Vec3d(1.0D, 0.5D, 0.5D),
			new Vec3d(1.0D, 0.0D, 0.5D),
			new Vec3d(1.0D, 0.0D, 0.0D),
			new Vec3d(1.0D, 1.0D, 0.0D));
	}
	
	public static Quad getInvertPrismSlopeZNegXPos() {
		return Quad.getQuad(
			EnumFacing.NORTH,
			SpriteRegistry.sprite_uncovered_quartered,
			new Vec3d(1.0D, 1.0D, 1.0D),
			new Vec3d(1.0D, 0.5D, 0.5D),
			new Vec3d(0.5D, 0.5D, 0.5D));
	}
	
	public static Quad getInvertPrismSlopeZNegXNeg() {
		return Quad.getQuad(
			EnumFacing.NORTH,
			SpriteRegistry.sprite_uncovered_quartered,
			new Vec3d(0.5D, 0.5D, 0.5D),
			new Vec3d(0.0D, 0.5D, 0.5D),
			new Vec3d(0.0D, 1.0D, 1.0D));
	}
	
	public static Quad getInvertPrismSlopeXNegZPos() {
		return Quad.getQuad(
			EnumFacing.WEST,
			SpriteRegistry.sprite_uncovered_quartered,
			new Vec3d(0.5D, 0.5D, 0.5D),
			new Vec3d(0.5D, 0.5D, 1.0D),
			new Vec3d(1.0D, 1.0D, 1.0D));
	}
	
	public static Quad getInvertPrismSlopeXPosZPos() {
		return Quad.getQuad(
			EnumFacing.EAST,
			SpriteRegistry.sprite_uncovered_quartered,
			new Vec3d(0.0D, 1.0D, 1.0D),
			new Vec3d(0.5D, 0.5D, 1.0D),
			new Vec3d(0.5D, 0.5D, 0.5D));
	}
	
}