package com.carpentersblocks.renderer.helper.slope.orthogonal;

import com.carpentersblocks.renderer.Quad;
import com.carpentersblocks.renderer.helper.RenderHelper;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperTriangle extends RenderHelper {

    /**
     * Returns quad for a half triangle on the North left face of the block.
     */
    public static Quad getQuadFaceZNegXPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.NORTH,
            sprite,
	        new Vec3d(1.0D, 0.0D, 0.0D),
	        new Vec3d(0.0D, 0.0D, 0.0D),
	        new Vec3d(0.0D, 1.0D, 0.0D));
    }

    /**
     * Returns quad for a half triangle on the North right face of the block.
     */
    public static Quad getQuadFaceZNegXNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.NORTH,
            sprite,
	        new Vec3d(1.0D, 1.0D, 0.0D),
	        new Vec3d(1.0D, 0.0D, 0.0D),
	        new Vec3d(0.0D, 0.0D, 0.0D));
    }

    /**
     * Returns quad for a half triangle on the South left face of the block.
     */
    public static Quad getQuadFaceZPosXNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.SOUTH,
            sprite,
	        new Vec3d(0.0D, 0.0D, 1.0D),
	        new Vec3d(1.0D, 0.0D, 1.0D),
	        new Vec3d(1.0D, 1.0D, 1.0D));
    }

    /**
     * Returns quad for a half triangle on the South right face of the block.
     */
    public static Quad getQuadFaceZPosXPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.SOUTH,
            sprite,
	        new Vec3d(0.0D, 0.0D, 1.0D),
	        new Vec3d(1.0D, 0.0D, 1.0D),
	        new Vec3d(0.0D, 1.0D, 1.0D));
    }

    /**
     * Returns quad for a half triangle on the West left face of the block.
     */
    public static Quad getQuadFaceXNegZNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.WEST,
            sprite,
	        new Vec3d(0.0D, 0.0D, 0.0D),
	        new Vec3d(0.0D, 0.0D, 1.0D),
	        new Vec3d(0.0D, 1.0D, 1.0D));
    }

    /**
     * Returns quad for a half triangle on the West right face of the block.
     */
    public static Quad getQuadFaceXNegZPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.WEST,
            sprite,
	        new Vec3d(0.0D, 1.0D, 0.0D),
	        new Vec3d(0.0D, 0.0D, 0.0D),
	        new Vec3d(0.0D, 0.0D, 1.0D));
    }

    /**
     * Returns quad for a half triangle on the East left face of the block.
     */
    public static Quad getQuadFaceXPosZPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.EAST,
            sprite,
	        new Vec3d(1.0D, 0.0D, 1.0D),
	        new Vec3d(1.0D, 0.0D, 0.0D),
	        new Vec3d(1.0D, 1.0D, 0.0D));
    }

    /**
     * Returns quad for a half triangle on the East right face of the block.
     */
    public static Quad getQuadFaceXPosZNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.EAST,
            sprite,
	        new Vec3d(1.0D, 1.0D, 1.0D),
	        new Vec3d(1.0D, 0.0D, 1.0D),
	        new Vec3d(1.0D, 0.0D, 0.0D));
    }

}
