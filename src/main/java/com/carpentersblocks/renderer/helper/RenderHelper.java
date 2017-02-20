package com.carpentersblocks.renderer.helper;

import com.carpentersblocks.renderer.Quad;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelper {
    
    /**
     * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
     */
    public Quad getQuadYNeg() {
        return Quad.getQuad(
            EnumFacing.DOWN,
        	new Vec3d(0.0D, 0.0D, 1.0D),
        	new Vec3d(0.0D, 0.0D, 0.0D),
    		new Vec3d(1.0D, 0.0D, 0.0D),
    		new Vec3d(1.0D, 0.0D, 1.0D)
    	);
    }

    /**
     * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
     */
    public Quad getQuadYPos() {
        return Quad.getQuad(
            EnumFacing.UP,
        	new Vec3d(0.0D, 1.0D, 0.0D),
        	new Vec3d(0.0D, 1.0D, 1.0D),
        	new Vec3d(1.0D, 1.0D, 1.0D),
        	new Vec3d(1.0D, 1.0D, 0.0D)
    	);
    }

    /**
     * Renders the given texture to the North face of the block.  Args: slope, x, y, z, texture
     */
    public Quad getQuadZNeg() {
        return Quad.getQuad(
            EnumFacing.NORTH,
        	new Vec3d(1.0D, 1.0D, 0.0D),
        	new Vec3d(1.0D, 0.0D, 0.0D),
        	new Vec3d(0.0D, 0.0D, 0.0D),
        	new Vec3d(0.0D, 1.0D, 0.0D)
    	);
    }

    /**
     * Renders the given texture to the South face of the block.  Args: slope, x, y, z, texture
     */
    public Quad getQuadZPos() {
        return Quad.getQuad(
            EnumFacing.SOUTH,
    		new Vec3d(0.0D, 1.0D, 1.0D),
    		new Vec3d(0.0D, 0.0D, 1.0D),
    		new Vec3d(1.0D, 0.0D, 1.0D),
    		new Vec3d(1.0D, 1.0D, 1.0D)
    	);
    }

    /**
     * Renders the given texture to the West face of the block.  Args: slope, x, y, z, texture
     */
    public Quad getQuadXNeg() {
        return Quad.getQuad(
            EnumFacing.WEST,
    		new Vec3d(0.0D, 1.0D, 0.0D),
    		new Vec3d(0.0D, 0.0D, 0.0D),
    		new Vec3d(0.0D, 0.0D, 1.0D),
    		new Vec3d(0.0D, 1.0D, 1.0D)
    	);
    }

    /**
     * Renders the given texture to the East face of the block.  Args: slope, x, y, z, texture
     */
    public Quad getQuadXPos() {
        return Quad.getQuad(
            EnumFacing.EAST,
    		new Vec3d(1.0D, 1.0D, 1.0D),
    		new Vec3d(1.0D, 0.0D, 1.0D),
    		new Vec3d(1.0D, 0.0D, 0.0D),
    		new Vec3d(1.0D, 1.0D, 0.0D)
    	);
    }

}
