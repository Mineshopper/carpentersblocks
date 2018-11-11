package com.carpentersblocks.block;

import static com.carpentersblocks.util.block.CollapsibleUtil.QUAD_XZNN;
import static com.carpentersblocks.util.block.CollapsibleUtil.QUAD_XZNP;
import static com.carpentersblocks.util.block.CollapsibleUtil.QUAD_XZPN;
import static com.carpentersblocks.util.block.CollapsibleUtil.QUAD_XZPP;

import java.util.Arrays;
import java.util.List;

import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.block.CollapsibleUtil;
import com.carpentersblocks.util.registry.ConfigRegistry;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCarpentersCollapsibleBlock extends BlockFacing {

    public BlockCarpentersCollapsibleBlock(Material material) {
        super(material);
        setLightOpacity(0);
    }

    @Override
    /**
     * Decrease quad depth.
     */
    protected boolean onHammerLeftClick(CbTileEntity cbTileEntity, EntityPlayer entityPlayer) {
    	CollapsibleUtil util = new CollapsibleUtil(cbTileEntity);
        int quad = util.getHitQuad();
        int depth = util.getQuadDepth(quad);
        util.setQuadDepth(cbTileEntity, quad, --depth);
        smoothAdjacentCollapsibles(cbTileEntity, quad);
        return true;
    }

    @Override
    /**
     * Increase quad depth.
     */
    protected boolean onHammerRightClick(CbTileEntity cbTileEntity, EntityPlayer entityPlayer) {
    	CollapsibleUtil util = new CollapsibleUtil(cbTileEntity);
        int quad = util.getHitQuad();
        int depth = util.getQuadDepth(quad);
        util.setQuadDepth(cbTileEntity, quad, ++depth);
        smoothAdjacentCollapsibles(cbTileEntity, quad);
        return true;
    }

    @Override
    /**
     * Damages hammer with a chance to not damage.
     */
    protected void damageItemWithChance(World world, EntityPlayer entityPlayer, EnumHand hand) {
        if (world.rand.nextFloat() <= ConfigRegistry.itemHammerDamageChanceFromCollapsible) {
            super.damageItemWithChance(world, entityPlayer, hand);
        }
    }
    
    private boolean isSameOrientation(CbTileEntity cbTileEntity1, CbTileEntity cbTileEntity2) {
    	return (new CollapsibleUtil(cbTileEntity1).getFacing()).equals(new CollapsibleUtil(cbTileEntity2).getFacing());
    }

    /**
     * Will attempt to smooth transitions to any adjacent collapsible blocks
     * given a cbTileEntity and source quadrant.
     */
    private void smoothAdjacentCollapsibles(CbTileEntity cbTileEntity, int src_quadrant) {
        CollapsibleUtil util = new CollapsibleUtil(cbTileEntity);
        CbTileEntity cbTileEntity_XN   = getTileEntity(cbTileEntity.getWorld(), cbTileEntity.getPos().add(-1,  0,  0));
        CbTileEntity cbTileEntity_XP   = getTileEntity(cbTileEntity.getWorld(), cbTileEntity.getPos().add( 1,  0,  0));
        CbTileEntity cbTileEntity_ZN   = getTileEntity(cbTileEntity.getWorld(), cbTileEntity.getPos().add( 0,  0, -1));
        CbTileEntity cbTileEntity_ZP   = getTileEntity(cbTileEntity.getWorld(), cbTileEntity.getPos().add( 0,  0,  1));
        CbTileEntity cbTileEntity_XZNN = getTileEntity(cbTileEntity.getWorld(), cbTileEntity.getPos().add(-1,  0, -1));
        CbTileEntity cbTileEntity_XZNP = getTileEntity(cbTileEntity.getWorld(), cbTileEntity.getPos().add(-1,  0,  1));
        CbTileEntity cbTileEntity_XZPN = getTileEntity(cbTileEntity.getWorld(), cbTileEntity.getPos().add( 1,  0, -1));
        CbTileEntity cbTileEntity_XZPP = getTileEntity(cbTileEntity.getWorld(), cbTileEntity.getPos().add( 1,  0,  1));
        int depth = util.getQuadDepth(src_quadrant);

        switch (src_quadrant) {
            case QUAD_XZNN:
                if (cbTileEntity_ZN != null && isSameOrientation(cbTileEntity, cbTileEntity_ZN)) {
                	util.setQuadDepth(cbTileEntity_ZN, QUAD_XZNP, depth);
                }
                if (cbTileEntity_XZNN != null && isSameOrientation(cbTileEntity, cbTileEntity_XZNN)) {
                	util.setQuadDepth(cbTileEntity_XZNN, QUAD_XZPP, depth);
                }
                if (cbTileEntity_XN != null && isSameOrientation(cbTileEntity, cbTileEntity_XN)) {
                	util.setQuadDepth(cbTileEntity_XN, QUAD_XZPN, depth);
                }
                break;
            case QUAD_XZNP:
                if (cbTileEntity_XN != null && isSameOrientation(cbTileEntity, cbTileEntity_XN)) {
                	util.setQuadDepth(cbTileEntity_XN, QUAD_XZPP, depth);
                }
                if (cbTileEntity_XZNP != null && isSameOrientation(cbTileEntity, cbTileEntity_XZNP)) {
                	util.setQuadDepth(cbTileEntity_XZNP, QUAD_XZPN, depth);
                }
                if (cbTileEntity_ZP != null && isSameOrientation(cbTileEntity, cbTileEntity_ZP)) {
                	util.setQuadDepth(cbTileEntity_ZP, QUAD_XZNN, depth);
                }
                break;
            case QUAD_XZPN:
                if (cbTileEntity_XP != null && isSameOrientation(cbTileEntity, cbTileEntity_XP)) {
                	util.setQuadDepth(cbTileEntity_XP, QUAD_XZNN, depth);
                }
                if (cbTileEntity_XZPN != null && isSameOrientation(cbTileEntity, cbTileEntity_XZPN)) {
                	util.setQuadDepth(cbTileEntity_XZPN, QUAD_XZNP, depth);
                }
                if (cbTileEntity_ZN != null && isSameOrientation(cbTileEntity, cbTileEntity_ZN)) {
                	util.setQuadDepth(cbTileEntity_ZN, QUAD_XZPP, depth);
                }
                break;
            case QUAD_XZPP:
                if (cbTileEntity_ZP != null && isSameOrientation(cbTileEntity, cbTileEntity_ZP)) {
                	util.setQuadDepth(cbTileEntity_ZP, QUAD_XZPN, depth);
                }
                if (cbTileEntity_XZPP != null && isSameOrientation(cbTileEntity, cbTileEntity_XZPP)) {
                	util.setQuadDepth(cbTileEntity_XZPP, QUAD_XZNN, depth);
                }
                if (cbTileEntity_XP != null && isSameOrientation(cbTileEntity, cbTileEntity_XP)) {
                	util.setQuadDepth(cbTileEntity_XP, QUAD_XZNP, depth);
                }
                break;
        }
    }
    
    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public AxisAlignedBB getBoundingBox(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
        	CollapsibleUtil util = new CollapsibleUtil(cbTileEntity);
	        float maxDepth = util.getBoundsMaxDepth();
	        if (util.isPositive()) {
	            return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, maxDepth, 1.0F);
	        } else {
	        	return new AxisAlignedBB(0.0F, 1.0F - maxDepth, 0.0F, 1.0F, 1.0F, 1.0F);
	        }
        }
        return super.getBoundingBox(blockState, blockAccess, blockPos);
    }
    
    @Override
    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     */
    public boolean isSideSolid(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, EnumFacing facing) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
        	CollapsibleUtil util = new CollapsibleUtil(cbTileEntity);
	        if (isBlockSolid(blockAccess, blockPos)) {
	            return util.isSideSolid(facing);
	        }
        }
        return false;
    }

    /**
     * Returns true if a slope should end at the given coords
     */
    private boolean isSlopeBoundary(World world, BlockPos blockPos) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {
            return true;
        }
        boolean blocksMovement_Y = world.getBlockState(blockPos).getMaterial().blocksMovement();
        boolean blocksMovement_YN = world.getBlockState(blockPos.add(0, -1, 0)).getMaterial().blocksMovement();
        return blocksMovement_Y || blocksMovement_YN;
    }

    /**
     * Scan X axis for slopes
     */
    private int scanX(World world, BlockPos blockPos, int dir, int maxDist) {
        for (int nx = blockPos.getX() + dir; nx != blockPos.getX() + maxDist * dir; nx += dir) {
            if (isSlopeBoundary(world, new BlockPos(nx, blockPos.getY(), blockPos.getZ()))) {
                return nx;
            }
        }
        return blockPos.getX() + dir;
    }

    /**
     * Scan Z axis for slopes
     */
    private int scanZ(World world, BlockPos blockPos, int dir, int maxDist) {
        for (int nz = blockPos.getZ() + dir; nz != blockPos.getZ() + maxDist * dir; nz += dir) {
            if (isSlopeBoundary(world, new BlockPos(blockPos.getX(), blockPos.getY(), nz))) {
                return nz;
            }
        }
        return blockPos.getZ() + dir;
    }

    /**
     * Returns block height
     */
    private static int getBlockHeight(IBlockAccess blockAccess, BlockPos blockPos) {
    	IBlockState blockState = blockAccess.getBlockState(blockPos);
        if (!blockState.getMaterial().blocksMovement()) {
            return 0;
        }
        return (int) (blockState.getBoundingBox(blockAccess, blockPos).maxY * 16.0);
    }

    @Override
    /**
     * Checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World world, BlockPos blockPos, EnumFacing facing) {
        if (!canAttachToFacing(facing)) {
            EnumFacing.Plane plane = EnumFacing.Plane.VERTICAL;
            for (EnumFacing dir : plane.facings()) {
            	if (world.isSideSolid(blockPos.offset(dir.getOpposite()), dir)) {
            		return true;
            	}
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        super.onBlockPlacedBy(world, blockPos, blockState, entityLivingBase, itemStack);

        // If sneaking, skip auto-setting quadrant depths
        if (!entityLivingBase.isSneaking()) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
                // Create a linear slope from neighbor blocks and collapsible quadrants
            	CollapsibleUtil util = new CollapsibleUtil(cbTileEntity);

                // Minimum and maximum height of quadrants
                final int MIN_HEIGHT = 0;
                final int MAX_HEIGHT = 16;

                // Find slopes in landscape
                int xn = scanX(world, blockPos, -1, MAX_HEIGHT);
                int xp = scanX(world, blockPos, 1, MAX_HEIGHT);
                int zn = scanZ(world, blockPos, -1, MAX_HEIGHT);
                int zp = scanZ(world, blockPos, 1, MAX_HEIGHT);

                CbTileEntity cbTileEntity_XN = getTileEntity(world, blockPos.add(-1, 0,  0));
                CbTileEntity cbTileEntity_XP = getTileEntity(world, blockPos.add( 1, 0,  0));
                CbTileEntity cbTileEntity_ZN = getTileEntity(world, blockPos.add( 0, 0, -1));
                CbTileEntity cbTileEntity_ZP = getTileEntity(world, blockPos.add( 0, 0,  1));

                int height_XZNN = MIN_HEIGHT, height_XZPN = MIN_HEIGHT, height_XZPP = MIN_HEIGHT, height_XZNP = MIN_HEIGHT;
                int hxn1, hxn2;
                
                if(cbTileEntity_XN != null && isSameOrientation(cbTileEntity, cbTileEntity_XN)) {
                	CollapsibleUtil utilXN = new CollapsibleUtil(cbTileEntity_XN);
                    hxn1 = utilXN.getQuadDepth(QUAD_XZPN);
                    hxn2 = utilXN.getQuadDepth(QUAD_XZPP);
                } else {
                    hxn1 = hxn2 = getBlockHeight(world, blockPos.add(-1, 0, 0));
                }

                int hxp1, hxp2;
                if(cbTileEntity_XP != null && isSameOrientation(cbTileEntity, cbTileEntity_XP)) {
                	CollapsibleUtil utilXP = new CollapsibleUtil(cbTileEntity_XP);
                    hxp1 = utilXP.getQuadDepth(QUAD_XZNN);
                    hxp2 = utilXP.getQuadDepth(QUAD_XZNP);
                } else {
                    hxp1 = hxp2 = getBlockHeight(world, blockPos.add(1, 0, 0));
                }

                int hzn1, hzn2;
                if(cbTileEntity_ZN != null && isSameOrientation(cbTileEntity, cbTileEntity_ZN)) {
                	CollapsibleUtil utilZN = new CollapsibleUtil(cbTileEntity_ZN);
                    hzn1 = utilZN.getQuadDepth(QUAD_XZNP);
                    hzn2 = utilZN.getQuadDepth(QUAD_XZPP);
                } else {
                    hzn1 = hzn2 = getBlockHeight(world, blockPos.add(0, 0, -1));
                }

                int hzp1, hzp2;
                if(cbTileEntity_ZP != null && isSameOrientation(cbTileEntity, cbTileEntity_ZP)) {
                	CollapsibleUtil utilZP = new CollapsibleUtil(cbTileEntity_ZP);
                    hzp1 = utilZP.getQuadDepth(QUAD_XZNN);
                    hzp2 = utilZP.getQuadDepth(QUAD_XZPN);
                } else {
                    hzp1 = hzp2 = getBlockHeight(world, blockPos.add(0, 0, 1));
                }

                // Lerp between heights, create smooth slope
                int xdist = blockPos.getX() - blockPos.add(-1, 0, 0).getX();
                double dx1 = (double)(hxp1 - hxn1) / (xp - xn - 1);
                double dx2 = (double)(hxp2 - hxn2) / (xp - xn - 1);
                height_XZNN = Math.max(height_XZNN, (int)(hxn1 + dx1 * (xdist - 1)));
                height_XZNP = Math.max(height_XZNP, (int)(hxn2 + dx2 * (xdist - 1)));
                height_XZPN = Math.max(height_XZPN, (int)(hxn1 + dx1 * xdist));
                height_XZPP = Math.max(height_XZPP, (int)(hxn2 + dx2 * xdist));

                int zdist = blockPos.getZ() - blockPos.add(0, 0, -1).getZ();
                double dz1 = (double)(hzp1 - hzn1) / (zp - zn - 1);
                double dz2 = (double)(hzp2 - hzn2) / (zp - zn - 1);
                height_XZNN = Math.max(height_XZNN, (int)(hzn1 + dz1 * (zdist - 1)));
                height_XZNP = Math.max(height_XZNP, (int)(hzn1 + dz1 * zdist));
                height_XZPN = Math.max(height_XZPN, (int)(hzn2 + dz2 * (zdist - 1)));
                height_XZPP = Math.max(height_XZPP, (int)(hzn2 + dz2 * zdist));

                util.setQuadDepth(cbTileEntity, QUAD_XZNN, height_XZNN);
                util.setQuadDepth(cbTileEntity, QUAD_XZNP, height_XZNP);
                util.setQuadDepth(cbTileEntity, QUAD_XZPP, height_XZPP);
                util.setQuadDepth(cbTileEntity, QUAD_XZPN, height_XZPN);

                for (int quad = 0; quad < 4; ++quad) {
                    smoothAdjacentCollapsibles(cbTileEntity, quad);
                }
            }
        }
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    @Override
    public void addCollisionBoxToList(IBlockState blockState, World world, BlockPos blockPos, AxisAlignedBB axisAlignedBB, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean unused) {
    	CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {
	    	CollapsibleUtil util = new CollapsibleUtil(cbTileEntity);
	        for (int quad = 0; quad < 4; ++quad) {
	        	addCollisionBoxToList(blockPos, axisAlignedBB, collidingBoxes, util.genQuadBoundingBox(quad));
	        }
    	}
    }

    @Override
    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public RayTraceResult collisionRayTrace(IBlockState blockState, World world, BlockPos blockPos, Vec3d start, Vec3d end) {
    	CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity == null) {
        	return super.rayTrace(blockPos, start, end, FULL_BLOCK_AABB);
        }
	    CollapsibleUtil util = new CollapsibleUtil(cbTileEntity);
        RayTraceResult finalTrace = null;
        double currDist = 0.0D;
        double maxDist = 0.0D;

        // Determine if ray trace is a hit on block
        for (int quad = 0; quad < 4; ++quad) {
            AxisAlignedBB box = util.genQuadBoundingBox(quad);
            RayTraceResult traceResult = rayTrace(blockPos, start, end, box);
            if (traceResult != null) {
                currDist = traceResult.hitVec.squareDistanceTo(end);
                if (currDist > maxDist) {
                    finalTrace = traceResult;
                    maxDist = currDist;
                }
            }
        }
        return finalTrace;
    }

    @Override
    /**
     * Returns whether sides share faces based on sloping property and face shape.
     */
    protected boolean shareFaces(CbTileEntity cbTileEntity_adj, CbTileEntity cbTileEntity_src, EnumFacing side_adj, EnumFacing side_src)
    {
/*        DataCollapsible data = DataCollapsible.INSTANCE;
        if (cbTileEntity_adj.getBlockType() == this && data.match(cbTileEntity_src, cbTileEntity_adj)) {
            switch (side_adj) {
                case NORTH:
                    return data.getQuadDepth(cbTileEntity_adj, data.QUAD_XZNN) == data.getQuadDepth(cbTileEntity_src, data.QUAD_XZNP) &&
                           data.getQuadDepth(cbTileEntity_adj, data.QUAD_XZPN) == data.getQuadDepth(cbTileEntity_src, data.QUAD_XZPP);
                case SOUTH:
                    return data.getQuadDepth(cbTileEntity_adj, data.QUAD_XZNP) == data.getQuadDepth(cbTileEntity_src, data.QUAD_XZNN) &&
                           data.getQuadDepth(cbTileEntity_adj, data.QUAD_XZPP) == data.getQuadDepth(cbTileEntity_src, data.QUAD_XZPN);
                case WEST:
                    return data.getQuadDepth(cbTileEntity_adj, data.QUAD_XZNP) == data.getQuadDepth(cbTileEntity_src, data.QUAD_XZPP) &&
                           data.getQuadDepth(cbTileEntity_adj, data.QUAD_XZNN) == data.getQuadDepth(cbTileEntity_src, data.QUAD_XZPN);
                case EAST:
                    return data.getQuadDepth(cbTileEntity_adj, data.QUAD_XZPP) == data.getQuadDepth(cbTileEntity_src, data.QUAD_XZNP) &&
                           data.getQuadDepth(cbTileEntity_adj, data.QUAD_XZPN) == data.getQuadDepth(cbTileEntity_src, data.QUAD_XZNN);
                default: {}
            }
        }

        return super.shareFaces(cbTileEntity_adj, cbTileEntity_src, side_adj, side_src);*/
    	return false;
    }

    /**
     * Whether block can be attached to specified side of another block.
     *
     * @param  side the side
     * @return whether side is supported
     */
    @Override
    public boolean canAttachToFacing(EnumFacing facing) {
        return Arrays.asList(EnumFacing.Plane.VERTICAL.facings()).contains(facing);
    }

    /**
     * Whether block requires an adjacent block with solid side for support.
     *
     * @return whether block can float freely
     */
    @Override
    public boolean canFloat() {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
    	return false;
    }

	@Override
	public void setFacing(CbTileEntity cbTileEntity, EnumFacing facing) {
		new CollapsibleUtil(cbTileEntity).setFacing(cbTileEntity, facing);
	}

	@Override
	public EnumFacing getFacing(CbTileEntity cbTileEntity) {
		return new CollapsibleUtil(cbTileEntity).getFacing();
	}
	
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.SOLID;
    }

}
