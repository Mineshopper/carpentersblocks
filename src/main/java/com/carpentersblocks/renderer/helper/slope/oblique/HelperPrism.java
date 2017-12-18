package com.carpentersblocks.renderer.helper.slope.oblique;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperPrism {

    /**
     * Returns quad for the negative North slope of the block.
     *//*
    public static Quad getQuadSlopeYNegZNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.NORTH,
            sprite,
            new Vec3d(1.0D, 1.0D, 0.0D),
			new Vec3d(0.5D, 0.0D, 1.0D),
			new Vec3d(0.0D, 1.0D, 0.0D));
    }

    *//**
     * Returns quad for the negative South slope of the block.
     *//*
    public static Quad getQuadSlopeYNegZPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.SOUTH,
            sprite,
	        new Vec3d(0.0D, 1.0D, 1.0D),
	        new Vec3d(0.5D, 0.0D, 0.0D),
	        new Vec3d(1.0D, 1.0D, 1.0D));
    }

    *//**
     * Returns quad for the negative West slope of the block.
     *//*
    public static Quad getQuadSlopeYNegXNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.WEST,
            sprite,
	        new Vec3d(0.0D, 1.0D, 0.0D),
	        new Vec3d(1.0D, 0.0D, 0.5D),
	        new Vec3d(0.0D, 1.0D, 1.0D));
    }

    *//**
     * Returns quad for the negative East slope of the block.
     *//*
    public static Quad getQuadSlopeYNegXPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.EAST,
            sprite,
	        new Vec3d(1.0D, 1.0D, 1.0D),
	        new Vec3d(0.0D, 0.0D, 0.5D),
	        new Vec3d(1.0D, 1.0D, 0.0D));
    }

    *//**
     * Returns quad for the positive North slope of the block.
     *//*
    public static Quad getQuadSlopeYPosZNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.NORTH,
            sprite,
	        new Vec3d(0.5D, 1.0D, 1.0D),
	        new Vec3d(1.0D, 0.0D, 0.0D),
	        new Vec3d(0.0D, 0.0D, 0.0D));
    }

    *//**
     * Returns quad for the positive South slope of the block.
     *//*
    public static Quad getQuadSlopeYPosZPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.SOUTH,
            sprite,
	        new Vec3d(0.5D, 1.0D, 0.0D),
	        new Vec3d(0.0D, 0.0D, 1.0D),
	        new Vec3d(1.0D, 0.0D, 1.0D));
    }

    *//**
     * Returns quad for the positive West slope of the block.
     *//*
    public static Quad getQuadSlopeYPosXNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.WEST,
            sprite,
	        new Vec3d(1.0D, 1.0D, 0.5D),
	        new Vec3d(0.0D, 0.0D, 0.0D),
	        new Vec3d(0.0D, 0.0D, 1.0D));
    }

    *//**
     * Returns quad for the positive East slope of the block.
     *//*
    public static Quad getQuadSlopeYPosXPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.EAST,
            sprite,
	        new Vec3d(0.0D, 1.0D, 0.5D),
	        new Vec3d(1.0D, 0.0D, 1.0D),
	        new Vec3d(1.0D, 0.0D, 0.0D));
    }

    *//**
     * Returns quad for the West prism on the North face.
     *//*
    public static Quad getQuadWestPointSlopeZNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.WEST,
            sprite,
	        new Vec3d(1.0D, 1.0D, 1.0D),
	        new Vec3d(0.0D, 0.0D, 0.0D),
	        new Vec3d(0.0D, 1.0D, 1.0D));
    }

    *//**
     * Returns quad for the West prism on the South face.
     *//*
    public static Quad getQuadWestPointSlopeZPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.WEST,
            sprite,
	        new Vec3d(0.0D, 1.0D, 0.0D),
	        new Vec3d(0.0D, 0.0D, 1.0D),
	        new Vec3d(1.0D, 1.0D, 0.0D));
    }

    *//**
     * Returns quad for the East prism on the North face.
     *//*
    public static Quad getQuadEastPointSlopeZNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.EAST,
            sprite,
	        new Vec3d(1.0D, 1.0D, 1.0D),
	        new Vec3d(1.0D, 0.0D, 0.0D),
	        new Vec3d(0.0D, 1.0D, 1.0D));
    }

    *//**
     * Returns quad for the East prism on the South face.
     *//*
    public static Quad getQuadEastPointSlopeZPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.EAST,
            sprite,
	        new Vec3d(0.0D, 1.0D, 0.0D),
	        new Vec3d(1.0D, 0.0D, 1.0D),
	        new Vec3d(1.0D, 1.0D, 0.0D));
    }

    *//**
     * Returns quad for the North prism on the West face.
     *//*
    public static Quad getQuadNorthPointSlopeXNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.NORTH,
            sprite,
	        new Vec3d(1.0D, 1.0D, 0.0D),
	        new Vec3d(0.0D, 0.0D, 0.0D),
	        new Vec3d(1.0D, 1.0D, 1.0D));
    }

    *//**
     * Returns quad for the North prism on the East face.
     *//*
    public static Quad getQuadNorthPointSlopeXPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.NORTH,
            sprite,
	        new Vec3d(0.0D, 1.0D, 1.0D),
	        new Vec3d(1.0D, 0.0D, 0.0D),
	        new Vec3d(0.0D, 1.0D, 0.0D));
    }

    *//**
     * Returns quad for the South prism on the West face.
     *//*
    public static Quad getQuadSouthPointSlopeXNeg(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.SOUTH,
            sprite,
	        new Vec3d(1.0D, 1.0D, 0.0D),
	        new Vec3d(0.0D, 0.0D, 1.0D),
	        new Vec3d(1.0D, 1.0D, 1.0D));
    }

    *//**
     * Returns quad for the South prism on the East face.
     *//*
    public static Quad getQuadSouthPointSlopeXPos(TextureAtlasSprite sprite) {
    	return Quad.getQuad(
			EnumFacing.SOUTH,
            sprite,
	        new Vec3d(0.0D, 1.0D, 1.0D),
	        new Vec3d(1.0D, 0.0D, 1.0D),
	        new Vec3d(0.0D, 1.0D, 0.0D));
    }

    *//**
     * Returns quad for the North sloped face of the block.
     *//*
    public static Quad getQuadWedgeSlopeZNeg(TextureAtlasSprite sprite) {
    	// TODO: Create two quad methods to handle shape
        return HelperOblWedge.getQuadSlopeZNeg(Slope.ID_WEDGE_POS_N, sprite);
    }

    *//**
     * Returns quad for the South sloped face of the block.
     *//*
    public static Quad getQuadWedgeSlopeZPos(TextureAtlasSprite sprite) {
    	// TODO: Create two quad methods to handle shape
    	return HelperOblWedge.getQuadSlopeZPos(Slope.ID_WEDGE_POS_S, sprite);
    }

    *//**
     * Returns quad for the West sloped face of the block.
     *//*
    public static Quad getQuadWedgeSlopeXNeg(TextureAtlasSprite sprite) {
    	// TODO: Create two quad methods to handle shape
    	return HelperOblWedge.getQuadSlopeXNeg(Slope.ID_WEDGE_POS_W, sprite);
    }

    *//**
     * Returns quad for the East sloped face of the block.
     *//*
    public static Quad getQuadWedgeSlopeXPos(TextureAtlasSprite sprite) {
    	// TODO: Create two quad methods to handle shape
    	return HelperOblWedge.getQuadSlopeXPos(Slope.ID_WEDGE_POS_E, sprite);
    }*/

}
