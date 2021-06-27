package com.carpentersblocks.client.renderer.helper;

import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.client.renderer.Quad;

import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;

public class RenderHelperSlope {

	public static Quad getWedgeXNeg() {
    	return Quad.getQuad(
			Direction.WEST,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.0D, 1.0D, 0.0D),
			new Vector3d(0.0D, 0.0D, 0.0D),
			new Vector3d(0.0D, 0.0D, 1.0D));
	}
	
	public static Quad getWedgeXPos() {
    	return Quad.getQuad(
			Direction.EAST,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(1.0D, 0.0D, 1.0D),
			new Vector3d(1.0D, 0.0D, 0.0D),
			new Vector3d(1.0D, 1.0D, 0.0D));
	}
	
	public static Quad getWedgeInteriorZPos() {
    	return Quad.getQuad(
			Direction.SOUTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.0D, 1.0D, 1.0D),
			new Vector3d(0.0D, 0.0D, 1.0D),
			new Vector3d(1.0D, 0.0D, 1.0D));
	}
	
	public static Quad getWedgeExteriorZNeg() {
		return Quad.getQuad(
			Direction.NORTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(1.0D, 0.0D, 0.0D),
			new Vector3d(0.0D, 0.0D, 0.0D),
			new Vector3d(0.0D, 1.0D, 0.0D));
	}
	
	public static Quad getPrismSlopeZNeg() {
		return Quad.getQuad(
			Direction.NORTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.5D, 0.5D, 0.5D),
			new Vector3d(1.0D, 0.0D, 0.0D),
			new Vector3d(0.0D, 0.0D, 0.0D));
	}
	
	public static Quad getPrismZPosXNeg() {
		return Quad.getQuad(
			Direction.SOUTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.5D, 0.5D, 1.0D),
			new Vector3d(0.0D, 0.0D, 1.0D),
			new Vector3d(0.5D, 0.0D, 1.0D));
	}
	
	public static Quad getPrismZPosXPos() {
		return Quad.getQuad(
			Direction.SOUTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.5D, 0.5D, 1.0D),
			new Vector3d(0.5D, 0.0D, 1.0D),
			new Vector3d(1.0D, 0.0D, 1.0D));
	}
	
	public static Quad getPrismZNegXNeg() {
		return Quad.getQuad(
			Direction.NORTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.5D, 0.5D, 0.0D),
			new Vector3d(0.5D, 0.0D, 0.0D),
			new Vector3d(0.0D, 0.0D, 0.0D));
	}
	
	public static Quad getPrismZNegXPos() {
		return Quad.getQuad(
			Direction.NORTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.5D, 0.5D, 0.0D),
			new Vector3d(1.0D, 0.0D, 0.0D),
			new Vector3d(0.5D, 0.0D, 0.0D));
	}
	
	public static Quad getPrismSlopedZPosXNeg() {
		return Quad.getQuad(
			Direction.SOUTH,
			TextureAtlasSprites.sprite_uncovered_quartered,
			new Vector3d(0.0D, 0.5D, 0.5D),
			new Vector3d(0.0D, 0.0D, 1.0D),
			new Vector3d(0.5D, 0.5D, 0.5D));
	}
	
	public static Quad getPrismSlopedZPosXPos() {
		return Quad.getQuad(
			Direction.SOUTH,
			TextureAtlasSprites.sprite_uncovered_quartered,
			new Vector3d(0.5D, 0.5D, 0.5D),
			new Vector3d(1.0D, 0.0D, 1.0D),
			new Vector3d(1.0D, 0.5D, 0.5D));
	}
	
	public static Quad getPrismXNegZNeg() {
		return Quad.getQuad(
			Direction.WEST,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.0D, 0.0D, 0.0D),
			new Vector3d(0.0D, 0.0D, 0.5D),
			new Vector3d(0.0D, 0.5D, 0.5D));
	}
	
	public static Quad getPrismXNegZPos() {
		return Quad.getQuad(
			Direction.WEST,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.0D, 0.5D, 0.5D),
			new Vector3d(0.0D, 0.0D, 0.5D),
			new Vector3d(0.0D, 0.0D, 1.0D));
	}
	
	public static Quad getPrismSlopedXNegZPos() {
		return Quad.getQuad(
			Direction.WEST,
			TextureAtlasSprites.sprite_uncovered_quartered,
			new Vector3d(0.5D, 0.5D, 0.5D),
			new Vector3d(0.0D, 0.0D, 1.0D),
			new Vector3d(0.5D, 0.5D, 1.0D));
	}
	
	public static Quad getPrismXPosZPos() {
		return Quad.getQuad(
			Direction.EAST,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(1.0D, 0.0D, 1.0D),
			new Vector3d(1.0D, 0.0D, 0.5D),
			new Vector3d(1.0D, 0.5D, 0.5D));
	}
	
	public static Quad getPrismXPosZNeg() {
		return Quad.getQuad(
			Direction.EAST,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(1.0D, 0.5D, 0.5D),
			new Vector3d(1.0D, 0.0D, 0.5D),
			new Vector3d(1.0D, 0.0D, 0.0D));
	}
	
	public static Quad getPrismSlopedXPosZPos() {
		return Quad.getQuad(
			Direction.EAST,
			TextureAtlasSprites.sprite_uncovered_quartered,
			new Vector3d(0.5D, 0.5D, 1.0D),
			new Vector3d(1.0D, 0.0D, 1.0D),
			new Vector3d(0.5D, 0.5D, 0.5D));
	}
	
	public static Quad getInvertedPrismSlopedZPos() {
		return Quad.getQuad(
			Direction.SOUTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.0D, 1.0D, 0.0D),
			new Vector3d(0.5D, 0.5D, 0.5D),
			new Vector3d(1.0D, 1.0D, 0.0D));
	}
	
	public static Quad getInvertedPrismZPosXNeg() {
		return Quad.getQuad(
			Direction.SOUTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.0D, 1.0D, 1.0D),
			new Vector3d(0.0D, 0.0D, 1.0D),
			new Vector3d(0.5D, 0.0D, 1.0D),
			new Vector3d(0.5D, 0.5D, 1.0D));
	}
	
	public static Quad getInvertedPrismZPosXPos() {
		return Quad.getQuad(
			Direction.SOUTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.5D, 0.5D, 1.0D),
			new Vector3d(0.5D, 0.0D, 1.0D),
			new Vector3d(1.0D, 0.0D, 1.0D),
			new Vector3d(1.0D, 1.0D, 1.0D));
	}
	
	public static Quad getInvertedPrismZNegXPos() {
		return Quad.getQuad(
			Direction.NORTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(1.0D, 1.0D, 0.0D),
			new Vector3d(1.0D, 0.0D, 0.0D),
			new Vector3d(0.5D, 0.0D, 0.0D),
			new Vector3d(0.5D, 0.5D, 0.0D));
	}
	
	public static Quad getInvertedPrismZNegXNeg() {
		return Quad.getQuad(
			Direction.NORTH,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.5D, 0.5D, 0.0D),
			new Vector3d(0.5D, 0.0D, 0.0D),
			new Vector3d(0.0D, 0.0D, 0.0D),
			new Vector3d(0.0D, 1.0D, 0.0D));
	}
	
	public static Quad getInvertedPrismXNegZNeg() {
		return Quad.getQuad(
			Direction.WEST,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.0D, 1.0D, 0.0D),
			new Vector3d(0.0D, 0.0D, 0.0D),
			new Vector3d(0.0D, 0.0D, 0.5D),
			new Vector3d(0.0D, 0.5D, 0.5D));
	}
	
	public static Quad getInvertedPrismXNegZPos() {
		return Quad.getQuad(
			Direction.WEST,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(0.0D, 0.5D, 0.5D),
			new Vector3d(0.0D, 0.0D, 0.5D),
			new Vector3d(0.0D, 0.0D, 1.0D),
			new Vector3d(0.0D, 1.0D, 1.0D));
	}
	
	public static Quad getInvertedPrismXPosZPos() {
		return Quad.getQuad(
			Direction.EAST,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(1.0D, 1.0D, 1.0D),
			new Vector3d(1.0D, 0.0D, 1.0D),
			new Vector3d(1.0D, 0.0D, 0.5D),
			new Vector3d(1.0D, 0.5D, 0.5D));
	}
	
	public static Quad getInvertedPrismXPosZNeg() {
		return Quad.getQuad(
			Direction.EAST,
			TextureAtlasSprites.sprite_uncovered_full,
			new Vector3d(1.0D, 0.5D, 0.5D),
			new Vector3d(1.0D, 0.0D, 0.5D),
			new Vector3d(1.0D, 0.0D, 0.0D),
			new Vector3d(1.0D, 1.0D, 0.0D));
	}
	
	public static Quad getInvertedPrismSlopeZNegXPos() {
		return Quad.getQuad(
			Direction.NORTH,
			TextureAtlasSprites.sprite_uncovered_quartered,
			new Vector3d(1.0D, 1.0D, 1.0D),
			new Vector3d(1.0D, 0.5D, 0.5D),
			new Vector3d(0.5D, 0.5D, 0.5D));
	}
	
	public static Quad getInvertedPrismSlopeZNegXNeg() {
		return Quad.getQuad(
			Direction.NORTH,
			TextureAtlasSprites.sprite_uncovered_quartered,
			new Vector3d(0.5D, 0.5D, 0.5D),
			new Vector3d(0.0D, 0.5D, 0.5D),
			new Vector3d(0.0D, 1.0D, 1.0D));
	}
	
	public static Quad getInvertedPrismSlopeXNegZPos() {
		return Quad.getQuad(
			Direction.WEST,
			TextureAtlasSprites.sprite_uncovered_quartered,
			new Vector3d(0.5D, 0.5D, 0.5D),
			new Vector3d(0.5D, 0.5D, 1.0D),
			new Vector3d(1.0D, 1.0D, 1.0D));
	}
	
	public static Quad getInvertedPrismSlopeXPosZPos() {
		return Quad.getQuad(
			Direction.EAST,
			TextureAtlasSprites.sprite_uncovered_quartered,
			new Vector3d(0.0D, 1.0D, 1.0D),
			new Vector3d(0.5D, 0.5D, 1.0D),
			new Vector3d(0.5D, 0.5D, 0.5D));
	}
	
}