package com.carpentersblocks.renderer.helper;

import com.carpentersblocks.renderer.Quad;
import com.carpentersblocks.util.block.CollapsibleUtil;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelperCollapsible {

    /**
     * Renders the given texture to the top North slope.
     */
    public static Quad getQuadYPosZNeg(CollapsibleUtil util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
        	EnumFacing.UP,
        	sprite,
        	new Vec3d(0.0D, util.offset_XZNN, 0.0D),
	        new Vec3d(0.5D, util.CENTER_YMAX, 0.5D),
	        new Vec3d(1.0D, util.offset_XZPN, 0.0D)
        );
    }

    /**
     * Renders the given texture to the top South slope.
     */
    public static Quad getQuadYPosZPos(CollapsibleUtil util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            EnumFacing.UP,
        	sprite,
	        new Vec3d(0.5D, util.CENTER_YMAX, 0.5D),
	        new Vec3d(0.0D, util.offset_XZNP, 1.0D),
	        new Vec3d(1.0D, util.offset_XZPP, 1.0D)
        );
    }

    /**
     * Renders the given texture to the top West slope.
     */
    public static Quad getQuadXNegYPos(CollapsibleUtil util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            EnumFacing.UP,
        	sprite,
            new Vec3d(0.0D, util.offset_XZNN, 0.0D),
            new Vec3d(0.0D, util.offset_XZNP, 1.0D),
	        new Vec3d(0.5D, util.CENTER_YMAX, 0.5D)
        );
    }

    /**
     * Renders the given texture to the top East slope.
     */
    public static Quad getQuadXPosYPos(CollapsibleUtil util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            EnumFacing.UP,
        	sprite,
            new Vec3d(0.5D, util.CENTER_YMAX, 0.5D),
	        new Vec3d(1.0D, util.offset_XZPP, 1.0D),
	        new Vec3d(1.0D, util.offset_XZPN, 0.0D)
        );
    }

    /**
     * Renders the given texture to the North face of the block.
     */
    public static Quad getQuadZNeg(CollapsibleUtil util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            EnumFacing.NORTH,
        	sprite,
            new Vec3d(1.0D, util.offset_XZPN, 0.0D),
            new Vec3d(1.0D,              0.0D, 0.0D),
            new Vec3d(0.0D,              0.0D, 0.0D),
            new Vec3d(0.0D, util.offset_XZNN, 0.0D)
       	);
    }

    /**
     * Renders the given texture to the South face of the block.
     */
    public static Quad getQuadZPos(CollapsibleUtil util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            EnumFacing.SOUTH,
        	sprite,
            new Vec3d(0.0D, util.offset_XZNP, 1.0D),
            new Vec3d(0.0D,              0.0D, 1.0D),
            new Vec3d(1.0D,              0.0D, 1.0D),
            new Vec3d(1.0D, util.offset_XZPP, 1.0D)
        );
    }

    /**
     * Renders the given texture to the West face of the block.
     */
    public static Quad getQuadXNeg(CollapsibleUtil util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            EnumFacing.WEST,
        	sprite,
        	new Vec3d(0.0D, util.offset_XZNN, 0.0D),
            new Vec3d(0.0D,              0.0D, 0.0D),
            new Vec3d(0.0D,              0.0D, 1.0D),
            new Vec3d(0.0D, util.offset_XZNP, 1.0D)
        );
    }

    /**
     * Renders the given texture to the East face of the block.
     */
    public static Quad getQuadXPos(CollapsibleUtil util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            EnumFacing.EAST,
        	sprite,
        	new Vec3d(1.0D, util.offset_XZPP, 1.0D),
            new Vec3d(1.0D,              0.0D, 1.0D),
            new Vec3d(1.0D,              0.0D, 0.0D),
            new Vec3d(1.0D, util.offset_XZPN, 0.0D)
        );
    }

}
