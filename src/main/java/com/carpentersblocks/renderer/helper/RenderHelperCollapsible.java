package com.carpentersblocks.renderer.helper;

import com.carpentersblocks.renderer.AbstractBakedModel;
import com.carpentersblocks.renderer.Quad;
import com.carpentersblocks.util.block.CollapsibleUtil;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelperCollapsible extends RenderHelper {

	private CollapsibleUtil _util;
	
    public RenderHelperCollapsible(AbstractBakedModel model) {
		_util = new CollapsibleUtil(model.getData());
		_util.computeOffsets();
	}
    
    public CollapsibleUtil getUtil() {
    	return _util;
    }

    /**
     * Renders the given texture to the top North slope.
     */
    public Quad getQuadYPosZNeg() {
        return Quad.getQuad(
        	EnumFacing.UP,
        	new Vec3d(0.0D, _util.offset_XZNN, 0.0D),
	        new Vec3d(0.5D, _util.CENTER_YMAX, 0.5D),
	        new Vec3d(1.0D, _util.offset_XZPN, 0.0D)
        );
    }

    /**
     * Renders the given texture to the top South slope.
     */
    public Quad getQuadYPosZPos() {
        return Quad.getQuad(
            EnumFacing.UP,
	        new Vec3d(0.5D, _util.CENTER_YMAX, 0.5D),
	        new Vec3d(0.0D, _util.offset_XZNP, 1.0D),
	        new Vec3d(1.0D, _util.offset_XZPP, 1.0D)
        );
    }

    /**
     * Renders the given texture to the top West slope.
     */
    public Quad getQuadXNegYPos() {
        return Quad.getQuad(
            EnumFacing.UP,
            new Vec3d(0.0D, _util.offset_XZNN, 0.0D),
            new Vec3d(0.0D, _util.offset_XZNP, 1.0D),
	        new Vec3d(0.5D, _util.CENTER_YMAX, 0.5D)
        );
    }

    /**
     * Renders the given texture to the top East slope.
     */
    public Quad getQuadXPosYPos() {
        return Quad.getQuad(
            EnumFacing.UP,
            new Vec3d(0.5D, _util.CENTER_YMAX, 0.5D),
	        new Vec3d(1.0D, _util.offset_XZPP, 1.0D),
	        new Vec3d(1.0D, _util.offset_XZPN, 0.0D)
        );
    }

    /**
     * Renders the given texture to the North face of the block.
     */
    public Quad getQuadZNeg() {
        return Quad.getQuad(
            EnumFacing.NORTH,
            new Vec3d(1.0D, _util.offset_XZPN, 0.0D),
            new Vec3d(1.0D,              0.0D, 0.0D),
            new Vec3d(0.0D,              0.0D, 0.0D),
            new Vec3d(0.0D, _util.offset_XZNN, 0.0D)
       	);
    }

    /**
     * Renders the given texture to the South face of the block.
     */
    public Quad getQuadZPos() {
        return Quad.getQuad(
            EnumFacing.SOUTH,
            new Vec3d(0.0D, _util.offset_XZNP, 1.0D),
            new Vec3d(0.0D,              0.0D, 1.0D),
            new Vec3d(1.0D,              0.0D, 1.0D),
            new Vec3d(1.0D, _util.offset_XZPP, 1.0D)
        );
    }

    /**
     * Renders the given texture to the West face of the block.
     */
    public Quad getQuadXNeg() {
        return Quad.getQuad(
            EnumFacing.WEST,
        	new Vec3d(0.0D, _util.offset_XZNN, 0.0D),
            new Vec3d(0.0D,              0.0D, 0.0D),
            new Vec3d(0.0D,              0.0D, 1.0D),
            new Vec3d(0.0D, _util.offset_XZNP, 1.0D)
        );
    }

    /**
     * Renders the given texture to the East face of the block.
     */
    public Quad getQuadXPos() {
        return Quad.getQuad(
            EnumFacing.EAST,
        	new Vec3d(1.0D, _util.offset_XZPP, 1.0D),
            new Vec3d(1.0D,              0.0D, 1.0D),
            new Vec3d(1.0D,              0.0D, 0.0D),
            new Vec3d(1.0D, _util.offset_XZPN, 0.0D)
        );
    }

}
