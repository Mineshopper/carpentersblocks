package com.carpentersblocks.util.metadata;

import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.util.EntityLivingUtil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;

public class CollapsibleMetadata extends CommonMetadata implements IMetadataFacing {

    public double CENTER_YMAX;
    public double offset_XZNN;
    public double offset_XZNP;
    public double offset_XZPN;
    public double offset_XZPP;
    
    public final static int QUAD_XZNN = 0;
    public final static int QUAD_XZNP = 1;
    public final static int QUAD_XZPN = 2;
    public final static int QUAD_XZPP = 3;
    
    public CollapsibleMetadata(CbTileEntity cbTileEntity) {
    	super(cbTileEntity.getCbMetadata());
    }
    
    public CollapsibleMetadata(int cbMetadata) {
    	super(cbMetadata);
    }

    /**
     * Returns corner number.
     */
    public static int getHitQuad(PlayerEntity playerEntity) {
    	BlockRayTraceResult result = EntityLivingUtil.calculateBlockRayTraceResult(playerEntity);
    	if (result != null) {
	    	if (Math.round(result.getLocation().x()) == 0) {
	    		if (Math.round(result.getLocation().z()) == 0) {
	    			return QUAD_XZNN;
	    		} else {
	    			return QUAD_XZNP;
	    		}
	    	} else {
	    		if (Math.round(result.getLocation().z()) == 0) {
	    			return QUAD_XZPN;
	    		} else {
	    			return QUAD_XZPP;
	    		}
	    	}
    	}
    	return 0;
    }
    
    /**
     * Returns true if fully collapsed.
     */
    public boolean isPlanar() {
        for (int quad = 0; quad < 4; quad++) {
            if (getQuadDepth(quad) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if a full cube.
     */
    public boolean isFullCube() {
        for (int quad = 0; quad < 4; quad++) {
            if (getQuadDepth(quad) < 16) {
                return false;
            }
        }
        return true;
    }

    /**
     * Fills Y-offsets for each corner and center of block for rendering purposes.
     */
    public void computeOffsets() {
        double BIAS = isPlanar() ? 1.0D / 1024.0D : 0.0D; // Small offset to prevent Z-fighting at depth 0
        offset_XZNN = getQuadDepth(QUAD_XZNN) / 16.0D + BIAS;
        offset_XZNP = getQuadDepth(QUAD_XZNP) / 16.0D + BIAS;
        offset_XZPN = getQuadDepth(QUAD_XZPN) / 16.0D + BIAS;
        offset_XZPP = getQuadDepth(QUAD_XZPP) / 16.0D + BIAS;

        // Find highest combined diagonals and set center yMax
        double NW_SE = offset_XZNN + offset_XZPP;
        double NE_SW = offset_XZPN + offset_XZNP;

        if (NW_SE < NE_SW) {
        	CENTER_YMAX = NE_SW / 2.0F;
        } else {
        	CENTER_YMAX = NW_SE / 2.0F;
        }
    }

    /**
     * Returns block depth determined by the largest quadrant.
     */
    public float getBoundsMaxDepth() {
        float maxDepth = 0.0F;
        for (int quad = 0; quad < 4; ++quad) {
        	maxDepth = Math.max(maxDepth, getQuadDepth(quad) / 16.0F);
        }
        return maxDepth;
    }

    /**
     * Will generate four boxes with max height represented by largest quadrant depth.
     */
    public AxisAlignedBB genQuadBoundingBox(int quad) {
        float xMin = 0.0F;
        float zMin = 0.0F;
        float xMax = 1.0F;
        float zMax = 1.0F;
        switch (quad) {
            case QUAD_XZNN:
                xMax = 0.5F;
                zMax = 0.5F;
                break;
            case QUAD_XZNP:
                xMax = 0.5F;
                zMin = 0.5F;
                break;
            case QUAD_XZPN:
                xMin = 0.5F;
                zMax = 0.5F;
                break;
            case QUAD_XZPP:
                xMin = 0.5F;
                zMin = 0.5F;
                break;
        }

        float maxDepth = getBoundsMaxDepth();
        float depth = getQuadDepth(quad) / 16.0F;

        // Make quads stagger no more than 0.5F so player can always walk across them
        if (isPositive()) {
            if (maxDepth - depth > 0.5F) {
                depth = maxDepth - 0.5F;
            }
            return new AxisAlignedBB(xMin, 0.0F, zMin, xMax, depth, zMax);
        } else {
            return new AxisAlignedBB(xMin, 1.0F - depth, zMin, xMax, 1.0F, zMax);
        }
    }
    
    /**
     * Sets quad depth as value from 0 to 16.
     */
    public static void setQuadDepth(CbTileEntity cbTileEntity, int quad, int depth) {
        if (depth < 0 || depth > 16) {
            return;
        }
        int data = cbTileEntity.getCbMetadata();
        switch (quad) {
            case QUAD_XZNN:
                data &= ~0x7c0000;
                data |= depth << 18;
                break;
            case QUAD_XZNP:
                data &= ~0x3e000;
                data |= depth << 13;
                break;
            case QUAD_XZPN:
                data &= ~0x1f00;
                data |= depth << 8;
                break;
            case QUAD_XZPP:
                data &= ~0xf8;
                data |= depth << 3;
                break;
        }
        cbTileEntity.setCbMetadata(data);
    }

    /**
     * Returns quad depth as value from 0 to 16.
     */
    public int getQuadDepth(int quad) {
        int depth = 0;
        int cbMetadata = getData();
        switch (quad) {
            case QUAD_XZNN:
                depth = (cbMetadata &= 0x7c0000) >> 18;
                break;
            case QUAD_XZNP:
                depth = (cbMetadata &= 0x3e000) >> 13;
                break;
            case QUAD_XZPN:
                depth = (cbMetadata &= 0x1f00) >> 8;
                break;
            case QUAD_XZPP:
                depth = (cbMetadata &= 0xf8) >> 3;
                break;
        }
        return depth;
    }

    public boolean isSideSolid(Direction facing) {
    	if (isFullCube()) {
    		return true;
    	} else {
	        switch (facing) {
	            case DOWN:
	                return isPositive();
	            case UP:
	                return !isPositive();
	            case NORTH:
	                return getQuadDepth(QUAD_XZNN) + getQuadDepth(QUAD_XZPN) == 32;
	            case SOUTH:
	                return getQuadDepth(QUAD_XZNP) + getQuadDepth(QUAD_XZPP) == 32;
	            case WEST:
	                return getQuadDepth(QUAD_XZNP) + getQuadDepth(QUAD_XZNN) == 32;
	            case EAST:
	                return getQuadDepth(QUAD_XZPN) + getQuadDepth(QUAD_XZPP) == 32;
	            default:
	                return true;
	        }
    	}
    }
    
    @Override
    public boolean setFacing(CbTileEntity cbTileEntity, Direction facing) {
        int temp = (cbTileEntity.getCbMetadata() & ~0x7) | facing.ordinal();
        return cbTileEntity.setCbMetadata(temp);
    }

    @Override
    public Direction getFacing() {
        return Direction.from3DDataValue(getData() & 0x7);
    }

    public boolean isPositive() {
        return getFacing().equals(Direction.UP);
    }

}
