package com.carpentersblocks.renderer.helper.slope.oblique;

import com.carpentersblocks.renderer.helper.RenderHelper;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperCorner extends RenderHelper {

    /**
     * Returns quad for the North sloped face of the block.
     *//*
    public static Quad getQuadSlopeZNeg(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_WEDGE_INT_NEG_NW:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
                    new Vec3d(0.0D, 0.0D, 1.0D),
                    new Vec3d(0.0D, 1.0D, 0.0D),
                    new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_WEDGE_INT_NEG_NE:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_WEDGE_EXT_NEG_NW:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_WEDGE_EXT_NEG_NE:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D));
            case Slope.ID_WEDGE_INT_POS_NW:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            case Slope.ID_WEDGE_INT_POS_NE:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            case Slope.ID_WEDGE_EXT_POS_NW:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D));
            default: //Slope.ID_WEDGE_EXT_POS_NE:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
        }
    }

    *//**
     * Returns quad for the South sloped face of the block.
     *//*
    public static Quad getQuadSlopeZPos(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_WEDGE_INT_NEG_SW:
                return Quad.getQuad(
                    EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D));
            case Slope.ID_WEDGE_INT_NEG_SE:
                return Quad.getQuad(
                    EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D));
            case Slope.ID_WEDGE_EXT_NEG_SW:
                return Quad.getQuad(
                    EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            case Slope.ID_WEDGE_EXT_NEG_SE:
                return Quad.getQuad(
                    EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            case Slope.ID_WEDGE_INT_POS_SW:
                return Quad.getQuad(
                    EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
            case Slope.ID_WEDGE_INT_POS_SE:
                return Quad.getQuad(
                    EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D));
            case Slope.ID_WEDGE_EXT_POS_SW:
                return Quad.getQuad(
                    EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
            default: //Slope.ID_WEDGE_EXT_POS_SE:
                return Quad.getQuad(
                    EnumFacing.SOUTH,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
        }
    }

    *//**
     * Returns quad for the West sloped face of the block.
     *//*
    public static Quad getQuadSlopeXNeg(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_WEDGE_INT_NEG_NW:
                return Quad.getQuad(
                    EnumFacing.WEST,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_WEDGE_INT_NEG_SW:
                return Quad.getQuad(
                    EnumFacing.WEST,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_WEDGE_EXT_NEG_NW:
                return Quad.getQuad(
                    EnumFacing.WEST,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_WEDGE_EXT_NEG_SW:
                return Quad.getQuad(
                    EnumFacing.WEST,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D));
            case Slope.ID_WEDGE_INT_POS_NW:
                return Quad.getQuad(
                    EnumFacing.WEST,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D));
            case Slope.ID_WEDGE_INT_POS_SW:
                return Quad.getQuad(
                    EnumFacing.WEST,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
            case Slope.ID_WEDGE_EXT_POS_NW:
                return Quad.getQuad(
                    EnumFacing.WEST,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
            default: //Slope.ID_WEDGE_EXT_POS_SW:
                return Quad.getQuad(
                    EnumFacing.WEST,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
        }
    }

    *//**
     * Returns quad for the East sloped face of the block.
     *//*
    public static Quad getQuadSlopeXPos(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_WEDGE_INT_NEG_NE:
                return Quad.getQuad(
                    EnumFacing.EAST,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D));
            case Slope.ID_WEDGE_INT_NEG_SE:
                return Quad.getQuad(
                    EnumFacing.EAST,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            case Slope.ID_WEDGE_EXT_NEG_NE:
                return Quad.getQuad(
                    EnumFacing.EAST,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            case Slope.ID_WEDGE_EXT_NEG_SE:
                return Quad.getQuad(
                    EnumFacing.EAST,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            case Slope.ID_WEDGE_INT_POS_NE:
                return Quad.getQuad(
                    EnumFacing.EAST,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            case Slope.ID_WEDGE_INT_POS_SE:
                return Quad.getQuad(
                    EnumFacing.EAST,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            case Slope.ID_WEDGE_EXT_POS_NE:
                return Quad.getQuad(
                    EnumFacing.EAST,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            default: //Slope.ID_WEDGE_EXT_POS_SE:
                return Quad.getQuad(
                    EnumFacing.EAST,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D));
        }
    }*/

}
