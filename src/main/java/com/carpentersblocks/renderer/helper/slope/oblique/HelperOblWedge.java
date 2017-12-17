package com.carpentersblocks.renderer.helper.slope.oblique;

import com.carpentersblocks.block.data.SlopeData;
import com.carpentersblocks.renderer.Quad;
import com.carpentersblocks.renderer.helper.RenderHelper;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperOblWedge extends RenderHelper {

    /**
     * Returns quad for the North sloped face of the block.
     *//*
    public static Quad getQuadSlopeZNeg(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_WEDGE_POS_N:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            case Slope.ID_WEDGE_NEG_N:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
            case Slope.ID_WEDGE_NW:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            default: //Slope.ID_WEDGE_NE:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D));
        }
    }

    *//**
     * Returns quad for the South sloped face of the block.
     *//*
    public static Quad getQuadSlopeZPos(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_WEDGE_POS_S:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
            case Slope.ID_WEDGE_NEG_S:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            case Slope.ID_WEDGE_SW:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
            default: //Slope.ID_WEDGE_SE:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D));
        }
    }

    *//**
     * Returns quad for the West sloped face of the block.
     *//*
    public static Quad getQuadSlopeXNeg(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_WEDGE_POS_W:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 1.0D, 1.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 0.0D, 1.0D));
            default: //Slope.ID_WEDGE_NEG_W:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 1.0D, 1.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 0.0D, 1.0D));
        }
    }

    *//**
     * Returns quad for the East sloped face of the block.
     *//*
    public static Quad getQuadSlopeXPos(int slopeID, TextureAtlasSprite sprite) {
        switch (slopeID) {
            case Slope.ID_WEDGE_POS_E:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(1.0D, 0.0D, 1.0D),
	                new Vec3d(1.0D, 0.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 0.0D),
	                new Vec3d(0.0D, 1.0D, 1.0D));
            default: //Slope.ID_WEDGE_NEG_E:
                return Quad.getQuad(
                    EnumFacing.NORTH,
                    sprite,
	                new Vec3d(0.0D, 0.0D, 1.0D),
	                new Vec3d(0.0D, 0.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 0.0D),
	                new Vec3d(1.0D, 1.0D, 1.0D));
        }
    }*/

}
