package com.carpentersblocks.client.renderer.helper;

import com.carpentersblocks.client.renderer.Quad;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderHelper {
    
    /**
     * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
     */
    public static Quad getQuadYNeg(TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.DOWN,
            sprite,
        	new Vector3d(0.0D, 0.0D, 1.0D),
        	new Vector3d(0.0D, 0.0D, 0.0D),
    		new Vector3d(1.0D, 0.0D, 0.0D),
    		new Vector3d(1.0D, 0.0D, 1.0D)
    	);
    }

    /**
     * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
     */
    public static Quad getQuadYPos(TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.UP,
            sprite,
        	new Vector3d(0.0D, 1.0D, 0.0D),
        	new Vector3d(0.0D, 1.0D, 1.0D),
        	new Vector3d(1.0D, 1.0D, 1.0D),
        	new Vector3d(1.0D, 1.0D, 0.0D)
    	);
    }

    /**
     * Renders the given texture to the North face of the block.  Args: slope, x, y, z, texture
     */
    public static Quad getQuadZNeg(TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.NORTH,
            sprite,
        	new Vector3d(1.0D, 1.0D, 0.0D),
        	new Vector3d(1.0D, 0.0D, 0.0D),
        	new Vector3d(0.0D, 0.0D, 0.0D),
        	new Vector3d(0.0D, 1.0D, 0.0D)
    	);
    }

    /**
     * Renders the given texture to the South face of the block.  Args: slope, x, y, z, texture
     */
    public static Quad getQuadZPos(TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.SOUTH,
            sprite,
    		new Vector3d(0.0D, 1.0D, 1.0D),
    		new Vector3d(0.0D, 0.0D, 1.0D),
    		new Vector3d(1.0D, 0.0D, 1.0D),
    		new Vector3d(1.0D, 1.0D, 1.0D)
    	);
    }

    /**
     * Renders the given texture to the West face of the block.  Args: slope, x, y, z, texture
     */
    public static Quad getQuadXNeg(TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.WEST,
            sprite,
    		new Vector3d(0.0D, 1.0D, 0.0D),
    		new Vector3d(0.0D, 0.0D, 0.0D),
    		new Vector3d(0.0D, 0.0D, 1.0D),
    		new Vector3d(0.0D, 1.0D, 1.0D)
    	);
    }

    /**
     * Renders the given texture to the East face of the block.  Args: slope, x, y, z, texture
     */
    public static Quad getQuadXPos(TextureAtlasSprite sprite) {
        return Quad.getQuad(
            Direction.EAST,
            sprite,
    		new Vector3d(1.0D, 1.0D, 1.0D),
    		new Vector3d(1.0D, 0.0D, 1.0D),
    		new Vector3d(1.0D, 0.0D, 0.0D),
    		new Vector3d(1.0D, 1.0D, 0.0D)
    	);
    }

}
