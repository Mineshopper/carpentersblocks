package com.carpentersblocks.renderer.helper.slope.orthogonal;

import com.carpentersblocks.block.data.SlopeData;
import com.carpentersblocks.renderer.Quad;
import com.carpentersblocks.renderer.helper.RenderHelper;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperOrthoWedge extends RenderHelper {

    /**
     * Returns quad for the bottom face of the block.
     *//*
    public static Quad getQuadFaceYNeg(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_OBL_EXT_POS_NW:
            case Slope.ID_OBL_INT_NEG_NW:
            case Slope.ID_WEDGE_NW:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
        			sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_OBL_EXT_POS_SW:
            case Slope.ID_OBL_INT_NEG_SW:
            case Slope.ID_WEDGE_SW:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
        			sprite,
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_OBL_EXT_POS_NE:
            case Slope.ID_OBL_INT_NEG_NE:
            case Slope.ID_WEDGE_NE:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
        			sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            default:
            //case Slope.ID_OBL_EXT_POS_SE:
            //case Slope.ID_OBL_INT_NEG_SE:
            //case Slope.ID_WEDGE_SE:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
        			sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D));
        }
    }

    *//**
     * Returns quad for the top face of the block.
     *//*
    public static Quad getQuadFaceYPos(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_OBL_EXT_NEG_NW:
            case Slope.ID_OBL_INT_POS_NW:
            case Slope.ID_WEDGE_NW:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
        			sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            case Slope.ID_OBL_EXT_NEG_SW:
            case Slope.ID_OBL_INT_POS_SW:
            case Slope.ID_WEDGE_SW:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
        			sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D));
            case Slope.ID_OBL_EXT_NEG_NE:
            case Slope.ID_OBL_INT_POS_NE:
            case Slope.ID_WEDGE_NE:
            	return Quad.getQuad(
        			EnumFacing.NORTH,
        			sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            default:
            //case Slope.ID_OBL_EXT_NEG_SE:
            //case Slope.ID_OBL_INT_POS_SE:
            //case Slope.ID_WEDGE_SE:
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
        			sprite,
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
        }
    }

    *//**
     * Returns quad for the North face of the block.
     *//*
    public static Quad getQuadFaceZNeg(int slopeID, TextureAtlasSprite sprite) {
        Slope slope = Slope.getSlopeById(slopeID);
        if (slope.isPositive) {
            if (slope.facings.contains(EnumFacing.WEST)) {
            	return Quad.getQuad(
        			EnumFacing.NORTH,
        			sprite,
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D));
            } else {
            	return Quad.getQuad(
        			EnumFacing.NORTH,
        			sprite,
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D));
            }
        } else {
            if (slope.facings.contains(EnumFacing.WEST)) {
            	return Quad.getQuad(
        			EnumFacing.NORTH,
        			sprite,
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D));
            } else {
            	return Quad.getQuad(
        			EnumFacing.NORTH,
        			sprite,
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D));
            }
        }
    }

    *//**
     * Returns quad for the South face of the block.
     *//*
    public static Quad getQuadFaceZPos(int slopeID, TextureAtlasSprite sprite) {
        Slope slope = Slope.getSlopeById(slopeID);
        if (slope.isPositive) {
            if (slope.facings.contains(EnumFacing.WEST)) {
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
        			sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            } else {
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
        			sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            }
        } else {
            if (slope.facings.contains(EnumFacing.WEST)) {
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
        			sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            } else {
            	return Quad.getQuad(
        			EnumFacing.SOUTH,
        			sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            }
        }
    }

    *//**
     * Returns quad for the West face of the block.
     *//*
    public static Quad getQuadFaceXNeg(int slopeID, TextureAtlasSprite sprite) {
        Slope slope = Slope.getSlopeById(slopeID);
        if (slope.isPositive) {
            if (slope.facings.contains(EnumFacing.NORTH)) {
            	return Quad.getQuad(
        			EnumFacing.WEST,
        			sprite,
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            } else {
            	return Quad.getQuad(
        			EnumFacing.WEST,
        			sprite,
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
            }
        } else {
            if (slope.facings.contains(EnumFacing.NORTH)) {
            	return Quad.getQuad(
        			EnumFacing.WEST,
        			sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
            } else {
            	return Quad.getQuad(
        			EnumFacing.WEST,
        			sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D));
            }
        }
    }

    *//**
     * Returns quad for the East face of the block.
     *//*
    public static Quad getQuadFaceXPos(int slopeID, TextureAtlasSprite sprite) {
        Slope slope = Slope.getSlopeById(slopeID);
        if (slope.isPositive) {
            if (slope.facings.contains(EnumFacing.NORTH)) {
            	return Quad.getQuad(
        			EnumFacing.EAST,
        			sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D));
            } else {
            	return Quad.getQuad(
        			EnumFacing.EAST,
        			sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D));
            }
        } else {
            if (slope.facings.contains(EnumFacing.NORTH)) {
            	return Quad.getQuad(
        			EnumFacing.EAST,
        			sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            } else {
            	return Quad.getQuad(
        			EnumFacing.EAST,
        			sprite,
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            }
        }
    }*/

}
