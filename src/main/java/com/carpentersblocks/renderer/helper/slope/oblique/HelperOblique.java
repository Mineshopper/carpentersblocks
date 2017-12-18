package com.carpentersblocks.renderer.helper.slope.oblique;

import com.carpentersblocks.renderer.helper.RenderHelper;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperOblique extends RenderHelper {

    /**
     * Returns quad for the interior oblique on the bottom sloped face.
     *//*
    public static Quad getQuadSlopeIntObliqueYNeg(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_OBL_INT_NEG_NW:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D));
            case Slope.ID_OBL_INT_NEG_SW:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_OBL_INT_NEG_NE:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            default: //Slope.ID_OBL_INT_NEG_SE:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
        }
    }

    *//**
     * Returns quad for the interior oblique on the top sloped face.
     *//*
    public static Quad getQuadSlopeIntObliqueYPos(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_OBL_INT_POS_NW:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            case Slope.ID_OBL_INT_POS_SW:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
            case Slope.ID_OBL_INT_POS_NE:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D));
            default: //Slope.ID_OBL_INT_POS_SE:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
        }
    }

    *//**
     * Returns quad for the exterior oblique bottom face on right.
     *//*
    public static Quad getQuadSlopeExtObliqueYNegLeft(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_OBL_EXT_NEG_NW:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.5D, 1.0D, 0.5D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_OBL_EXT_NEG_SW:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.5D, 1.0D, 0.5D));
            case Slope.ID_OBL_EXT_NEG_NE:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.5D, 1.0D, 0.5D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            default: //Slope.ID_OBL_EXT_NEG_SE:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.5D, 1.0D, 0.5D));
        }
    }

    *//**
     * Returns quad for the exterior oblique bottom face on right.
     *//*
    public static Quad getQuadSlopeExtObliqueYNegRight(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_OBL_EXT_NEG_NW:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.5D, 1.0D, 0.5D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_OBL_EXT_NEG_SW:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.5D, 1.0D, 0.5D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            case Slope.ID_OBL_EXT_NEG_NE:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.5D, 1.0D, 0.5D));
            default: //Slope.ID_OBL_EXT_NEG_SE:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.5D, 1.0D, 0.5D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D));
        }
    }

    *//**
     * Returns quad for the exterior oblique top face on left.
     *//*
    public static Quad getQuadSlopeExtObliqueYPosLeft(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_OBL_EXT_POS_NW:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.5D, 0.0D, 0.5D));
            case Slope.ID_OBL_EXT_POS_SW:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.5D, 0.0D, 0.5D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D));
            case Slope.ID_OBL_EXT_POS_NE:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(0.5D, 0.0D, 0.5D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            default: //Slope.ID_OBL_EXT_POS_SE:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.5D, 0.0D, 0.5D));
        }
    }

    *//**
     * Returns quad for the exterior oblique top face on right.
     *//*
    public static Quad getQuadSlopeExtObliqueYPosRight(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_OBL_EXT_POS_NW:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(0.5D, 0.0D, 0.5D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
            case Slope.ID_OBL_EXT_POS_SW:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.5D, 0.0D, 0.5D));
            case Slope.ID_OBL_EXT_POS_NE:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.5D, 0.0D, 0.5D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            default: //Slope.ID_OBL_EXT_POS_SE:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.5D, 0.0D, 0.5D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D));
        }
    }*/

}
