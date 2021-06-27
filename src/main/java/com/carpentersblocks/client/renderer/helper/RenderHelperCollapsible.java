package com.carpentersblocks.client.renderer.helper;

import com.carpentersblocks.client.renderer.Quad;
import com.carpentersblocks.util.metadata.CollapsibleMetadata;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderHelperCollapsible {

    /**
     * Renders the given texture to the top North slope.
     */
    public static Quad getQuadYPosZNeg(CollapsibleMetadata util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
        	Direction.UP,
        	sprite,
        	new Vector3d(0.0D, util.offset_XZNN, 0.0D),
	        new Vector3d(0.5D, util.CENTER_YMAX, 0.5D),
	        new Vector3d(1.0D, util.offset_XZPN, 0.0D)
        );
    }

    /**
     * Renders the given texture to the top South slope.
     */
    public static Quad getQuadYPosZPos(CollapsibleMetadata util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.UP,
        	sprite,
	        new Vector3d(0.5D, util.CENTER_YMAX, 0.5D),
	        new Vector3d(0.0D, util.offset_XZNP, 1.0D),
	        new Vector3d(1.0D, util.offset_XZPP, 1.0D)
        );
    }

    /**
     * Renders the given texture to the top West slope.
     */
    public static Quad getQuadXNegYPos(CollapsibleMetadata util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.UP,
        	sprite,
            new Vector3d(0.0D, util.offset_XZNN, 0.0D),
            new Vector3d(0.0D, util.offset_XZNP, 1.0D),
	        new Vector3d(0.5D, util.CENTER_YMAX, 0.5D)
        );
    }

    /**
     * Renders the given texture to the top East slope.
     */
    public static Quad getQuadXPosYPos(CollapsibleMetadata util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.UP,
        	sprite,
            new Vector3d(0.5D, util.CENTER_YMAX, 0.5D),
	        new Vector3d(1.0D, util.offset_XZPP, 1.0D),
	        new Vector3d(1.0D, util.offset_XZPN, 0.0D)
        );
    }

    /**
     * Renders the given texture to the North face of the block.
     */
    public static Quad getQuadZNeg(CollapsibleMetadata util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.NORTH,
        	sprite,
            new Vector3d(1.0D, util.offset_XZPN, 0.0D),
            new Vector3d(1.0D,              0.0D, 0.0D),
            new Vector3d(0.0D,              0.0D, 0.0D),
            new Vector3d(0.0D, util.offset_XZNN, 0.0D)
       	);
    }

    /**
     * Renders the given texture to the South face of the block.
     */
    public static Quad getQuadZPos(CollapsibleMetadata util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.SOUTH,
        	sprite,
            new Vector3d(0.0D, util.offset_XZNP, 1.0D),
            new Vector3d(0.0D,              0.0D, 1.0D),
            new Vector3d(1.0D,              0.0D, 1.0D),
            new Vector3d(1.0D, util.offset_XZPP, 1.0D)
        );
    }

    /**
     * Renders the given texture to the West face of the block.
     */
    public static Quad getQuadXNeg(CollapsibleMetadata util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.WEST,
        	sprite,
        	new Vector3d(0.0D, util.offset_XZNN, 0.0D),
            new Vector3d(0.0D,              0.0D, 0.0D),
            new Vector3d(0.0D,              0.0D, 1.0D),
            new Vector3d(0.0D, util.offset_XZNP, 1.0D)
        );
    }

    /**
     * Renders the given texture to the East face of the block.
     */
    public static Quad getQuadXPos(CollapsibleMetadata util, TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.EAST,
        	sprite,
        	new Vector3d(1.0D, util.offset_XZPP, 1.0D),
            new Vector3d(1.0D,              0.0D, 1.0D),
            new Vector3d(1.0D,              0.0D, 0.0D),
            new Vector3d(1.0D, util.offset_XZPN, 0.0D)
        );
    }

}
