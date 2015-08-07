package com.carpentersblocks.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.data.Collapsible;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.collapsible.CollapsibleUtil;
import com.carpentersblocks.util.handler.EventHandler;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.ItemRegistry;

public class BlockCarpentersCollapsibleBlock extends BlockSided {

    public BlockCarpentersCollapsibleBlock(Material material)
    {
        super(material, new Collapsible());
    }

    @Override
    /**
     * Increase quad depth.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int stepOffset = Collapsible.INSTANCE.isPositive(TE) ? -1 : 1;
        int quad = Collapsible.getQuad(EventHandler.hitX, EventHandler.hitZ);
        int depth = Collapsible.getQuadDepth(TE, quad);

        Collapsible.setQuadDepth(TE, quad, depth + stepOffset, false);
        smoothAdjacentCollapsibles(TE, quad);

        return true;
    }

    @Override
    /**
     * Decrease quad depth.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int stepOffset = Collapsible.INSTANCE.isPositive(TE) ? 1 : -1;
        int quad = Collapsible.getQuad(EventHandler.hitX, EventHandler.hitZ);
        int depth = Collapsible.getQuadDepth(TE, quad);

        Collapsible.setQuadDepth(TE, quad, depth + stepOffset, false);
        smoothAdjacentCollapsibles(TE, quad);

        return true;
    }

    @Override
    /**
     * Damages hammer with a chance to not damage.
     */
    protected void damageItemWithChance(World world, EntityPlayer entityPlayer)
    {
        if (world.rand.nextFloat() <= ItemRegistry.itemHammerDamageChanceFromCollapsible) {
            super.damageItemWithChance(world, entityPlayer);
        }
    }

    /**
     * Will attempt to smooth transitions to any adjacent collapsible blocks
     * given a TE and source quadrant.
     */
    private void smoothAdjacentCollapsibles(TEBase TE, int src_quadrant)
    {
        Collapsible data = Collapsible.INSTANCE;
        World world = TE.getWorldObj();

        TEBase TE_XN   = getTileEntity(world, TE.xCoord - 1, TE.yCoord, TE.zCoord);
        TEBase TE_XP   = getTileEntity(world, TE.xCoord + 1, TE.yCoord, TE.zCoord);
        TEBase TE_ZN   = getTileEntity(world, TE.xCoord, TE.yCoord, TE.zCoord - 1);
        TEBase TE_ZP   = getTileEntity(world, TE.xCoord, TE.yCoord, TE.zCoord + 1);
        TEBase TE_XZNN = getTileEntity(world, TE.xCoord - 1, TE.yCoord, TE.zCoord - 1);
        TEBase TE_XZNP = getTileEntity(world, TE.xCoord - 1, TE.yCoord, TE.zCoord + 1);
        TEBase TE_XZPN = getTileEntity(world, TE.xCoord + 1, TE.yCoord, TE.zCoord - 1);
        TEBase TE_XZPP = getTileEntity(world, TE.xCoord + 1, TE.yCoord, TE.zCoord + 1);

        int depth = Collapsible.getQuadDepth(TE, src_quadrant);

        switch (src_quadrant)
        {
            case Collapsible.QUAD_XZNN:
                if (TE_ZN != null && data.match(TE, TE_ZN)) {
                    Collapsible.setQuadDepth(TE_ZN, Collapsible.QUAD_XZNP, depth, true);
                }
                if (TE_XZNN != null && data.match(TE, TE_XZNN)) {
                    Collapsible.setQuadDepth(TE_XZNN, Collapsible.QUAD_XZPP, depth, true);
                }
                if (TE_XN != null && data.match(TE, TE_XN)) {
                    Collapsible.setQuadDepth(TE_XN, Collapsible.QUAD_XZPN, depth, true);
                }
                break;
            case Collapsible.QUAD_XZNP:
                if (TE_XN != null && data.match(TE, TE_XN)) {
                    Collapsible.setQuadDepth(TE_XN, Collapsible.QUAD_XZPP, depth, true);
                }
                if (TE_XZNP != null && data.match(TE, TE_XZNP)) {
                    Collapsible.setQuadDepth(TE_XZNP, Collapsible.QUAD_XZPN, depth, true);
                }
                if (TE_ZP != null && data.match(TE, TE_ZP)) {
                    Collapsible.setQuadDepth(TE_ZP, Collapsible.QUAD_XZNN, depth, true);
                }
                break;
            case Collapsible.QUAD_XZPN:
                if (TE_XP != null && data.match(TE, TE_XP)) {
                    Collapsible.setQuadDepth(TE_XP, Collapsible.QUAD_XZNN, depth, true);
                }
                if (TE_XZPN != null && data.match(TE, TE_XZPN)) {
                    Collapsible.setQuadDepth(TE_XZPN, Collapsible.QUAD_XZNP, depth, true);
                }
                if (TE_ZN != null && data.match(TE, TE_ZN)) {
                    Collapsible.setQuadDepth(TE_ZN, Collapsible.QUAD_XZPP, depth, true);
                }
                break;
            case Collapsible.QUAD_XZPP:
                if (TE_ZP != null && data.match(TE, TE_ZP)) {
                    Collapsible.setQuadDepth(TE_ZP, Collapsible.QUAD_XZPN, depth, true);
                }
                if (TE_XZPP != null && data.match(TE, TE_XZPP)) {
                    Collapsible.setQuadDepth(TE_XZPP, Collapsible.QUAD_XZNN, depth, true);
                }
                if (TE_XP != null && data.match(TE, TE_XP)) {
                    Collapsible.setQuadDepth(TE_XP, Collapsible.QUAD_XZNP, depth, true);
                }
                break;
        }
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null)
        {
            float maxDepth = CollapsibleUtil.getBoundsMaxDepth(TE);
            if (Collapsible.INSTANCE.isPositive(TE)) {
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, maxDepth, 1.0F);
            } else {
                setBlockBounds(0.0F, 1.0F - maxDepth, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    @Override
    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     */
    public boolean isSideSolid(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection side)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {
            if (isBlockSolid(blockAccess, x, y, z)) {
                return Collapsible.isSideSolid(TE, side);
            }
        }

        return false;
    }

    /**
     * Returns true if a slope should end at the given coords
     */
    private boolean isSlopeBoundary(World world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            return true;
        }

        return world.getBlock(x, y, z).getMaterial().blocksMovement() || !world.getBlock(x, y - 1, z).getMaterial().blocksMovement();
    }

    /**
     * Scan X axis for slopes
     */
    private int scanX(World world, int x, int y, int z, int dir, int maxDist)
    {
        for (int nx = x + dir; nx != x + maxDist * dir; nx += dir) {
            if (isSlopeBoundary(world, nx, y, z)) {
                return nx;
            }
        }

        return x + dir;
    }

    /**
     * Scan Z axis for slopes
     */
    private int scanZ(World world, int x, int y, int z, int dir, int maxDist)
    {
        for (int nz = z + dir; nz != z + maxDist * dir; nz += dir) {
            if (isSlopeBoundary(world, x, y, nz)) {
                return nz;
            }
        }

        return z + dir;
    }

    /**
     * Returns block height
     */
    private static int getBlockHeight(IBlockAccess blockAccess, int x, int y, int z)
    {
        Block block = blockAccess.getBlock(x, y, z);

        if (!block.getMaterial().blocksMovement()) {
            return 0;
        }

        return (int) (block.getBlockBoundsMaxY() * 16.0);
    }

    @Override
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        // If side not supported, select best side based on y hit coordinates
        if (!canAttachToSide(side)) {
            return hitY > 0.5F ? 0 : 1;
        }

        return side;
    }

    @Override
    /**
     * Checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        if (!canAttachToSide(side)) {
            ForgeDirection VALID_SIDES[] = { ForgeDirection.DOWN, ForgeDirection.UP };
            for (ForgeDirection dir : VALID_SIDES) {
                // If side is not supported, check that either the block above or below CAN support it
                if (world.getBlock(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ).isSideSolid(world, x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ, dir)) {
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

        // If sneaking, skip auto-setting quadrant depths

        if (!entityLiving.isSneaking())
        {
            TEBase TE = getTileEntity(world, x, y, z);
            if (TE != null)
            {
                // Create a linear slope from neighbor blocks and collapsible quadrants

                // Mininum and maximum height of quadrants
                final int MIN_HEIGHT = 0;
                final int MAX_HEIGHT = 16;

                // Find slopes in landscape
                int xn = scanX(world, x, y, z, -1, MAX_HEIGHT);
                int xp = scanX(world, x, y, z, 1, MAX_HEIGHT);
                int zn = scanZ(world, x, y, z, -1, MAX_HEIGHT);
                int zp = scanZ(world, x, y, z, 1, MAX_HEIGHT);

                TEBase TE_XN = getTileEntity(world, xn, y, z);
                TEBase TE_XP = getTileEntity(world, xp, y, z);
                TEBase TE_ZN = getTileEntity(world, x, y, zn);
                TEBase TE_ZP = getTileEntity(world, x, y, zp);

                int height_XZNN = MIN_HEIGHT, height_XZPN = MIN_HEIGHT, height_XZPP = MIN_HEIGHT, height_XZNP = MIN_HEIGHT;
                Collapsible data = Collapsible.INSTANCE;

                int hxn1, hxn2;
                if(TE_XN != null && data.match(TE, TE_XN)) {
                    hxn1 = Collapsible.getQuadDepth(TE_XN, Collapsible.QUAD_XZPN);
                    hxn2 = Collapsible.getQuadDepth(TE_XN, Collapsible.QUAD_XZPP);
                } else {
                    hxn1 = hxn2 = getBlockHeight(world, xn, y, z);
                }

                int hxp1, hxp2;
                if(TE_XP != null && data.match(TE, TE_XP)) {
                    hxp1 = Collapsible.getQuadDepth(TE_XP, Collapsible.QUAD_XZNN);
                    hxp2 = Collapsible.getQuadDepth(TE_XP, Collapsible.QUAD_XZNP);
                } else {
                    hxp1 = hxp2 = getBlockHeight(world, xp, y, z);
                }

                int hzn1, hzn2;
                if(TE_ZN != null && data.match(TE, TE_ZN)) {
                    hzn1 = Collapsible.getQuadDepth(TE_ZN, Collapsible.QUAD_XZNP);
                    hzn2 = Collapsible.getQuadDepth(TE_ZN, Collapsible.QUAD_XZPP);
                } else {
                    hzn1 = hzn2 = getBlockHeight(world, x, y, zn);
                }

                int hzp1, hzp2;
                if(TE_ZP != null && data.match(TE, TE_ZP)) {
                    hzp1 = Collapsible.getQuadDepth(TE_ZP, Collapsible.QUAD_XZNN);
                    hzp2 = Collapsible.getQuadDepth(TE_ZP, Collapsible.QUAD_XZPN);
                } else {
                    hzp1 = hzp2 = getBlockHeight(world, x, y, zp);
                }

                // Lerp between heights, create smooth slope
                int xdist = x - xn;
                double dx1 = (double)(hxp1 - hxn1) / (xp - xn - 1);
                double dx2 = (double)(hxp2 - hxn2) / (xp - xn - 1);
                height_XZNN = Math.max(height_XZNN, (int)(hxn1 + dx1 * (xdist - 1)));
                height_XZNP = Math.max(height_XZNP, (int)(hxn2 + dx2 * (xdist - 1)));
                height_XZPN = Math.max(height_XZPN, (int)(hxn1 + dx1 * xdist));
                height_XZPP = Math.max(height_XZPP, (int)(hxn2 + dx2 * xdist));

                int zdist = z - zn;
                double dz1 = (double)(hzp1 - hzn1) / (zp - zn - 1);
                double dz2 = (double)(hzp2 - hzn2) / (zp - zn - 1);
                height_XZNN = Math.max(height_XZNN, (int)(hzn1 + dz1 * (zdist - 1)));
                height_XZNP = Math.max(height_XZNP, (int)(hzn1 + dz1 * zdist));
                height_XZPN = Math.max(height_XZPN, (int)(hzn2 + dz2 * (zdist - 1)));
                height_XZPP = Math.max(height_XZPP, (int)(hzn2 + dz2 * zdist));

                Collapsible.setQuadDepth(TE, Collapsible.QUAD_XZNN, height_XZNN, false);
                Collapsible.setQuadDepth(TE, Collapsible.QUAD_XZNP, height_XZNP, false);
                Collapsible.setQuadDepth(TE, Collapsible.QUAD_XZPP, height_XZPP, false);
                Collapsible.setQuadDepth(TE, Collapsible.QUAD_XZPN, height_XZPN, false);

                for (int quad = 0; quad < 4; ++quad) {
                    smoothAdjacentCollapsibles(TE, quad);
                }
            }
        }
    }

    @Override
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            AxisAlignedBB colBox = null;

            for (int quad = 0; quad < 4; ++quad)
            {
                float[] bounds = CollapsibleUtil.genBounds(TE, quad);
                colBox = AxisAlignedBB.getBoundingBox(x + bounds[0], y + bounds[1], z + bounds[2], x + bounds[3], y + bounds[4], z + bounds[5]);

                if (axisAlignedBB.intersectsWith(colBox)) {
                    list.add(colBox);
                }
            }

        }
    }

    @Override
    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        MovingObjectPosition finalTrace = null;

        if (TE != null) {

            double currDist = 0.0D;
            double maxDist = 0.0D;

            // Determine if ray trace is a hit on block
            for (int quad = 0; quad < 4; ++quad)
            {
                float[] bounds = CollapsibleUtil.genBounds(TE, quad);

                setBlockBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
                MovingObjectPosition traceResult = super.collisionRayTrace(world, x, y, z, startVec, endVec);

                if (traceResult != null)
                {
                    currDist = traceResult.hitVec.squareDistanceTo(endVec);
                    if (currDist > maxDist) {
                        finalTrace = traceResult;
                        maxDist = currDist;
                    }
                }
            }

            /* Determine true face hit since it's built of quadrants. */

            if (finalTrace != null) {
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                finalTrace = super.collisionRayTrace(world, x, y, z, startVec, endVec);
            }

        }

        return finalTrace;
    }

    @Override
    /**
     * Returns whether sides share faces based on sloping property and face shape.
     */
    protected boolean shareFaces(TEBase TE_adj, TEBase TE_src, ForgeDirection side_adj, ForgeDirection side_src)
    {
        Collapsible data = Collapsible.INSTANCE;
        if (TE_adj.getBlockType() == this && data.match(TE_src, TE_adj)) {
            switch (side_adj) {
                case NORTH:
                    return data.getQuadDepth(TE_adj, data.QUAD_XZNN) == data.getQuadDepth(TE_src, data.QUAD_XZNP) &&
                           data.getQuadDepth(TE_adj, data.QUAD_XZPN) == data.getQuadDepth(TE_src, data.QUAD_XZPP);
                case SOUTH:
                    return data.getQuadDepth(TE_adj, data.QUAD_XZNP) == data.getQuadDepth(TE_src, data.QUAD_XZNN) &&
                           data.getQuadDepth(TE_adj, data.QUAD_XZPP) == data.getQuadDepth(TE_src, data.QUAD_XZPN);
                case WEST:
                    return data.getQuadDepth(TE_adj, data.QUAD_XZNP) == data.getQuadDepth(TE_src, data.QUAD_XZPP) &&
                           data.getQuadDepth(TE_adj, data.QUAD_XZNN) == data.getQuadDepth(TE_src, data.QUAD_XZPN);
                case EAST:
                    return data.getQuadDepth(TE_adj, data.QUAD_XZPP) == data.getQuadDepth(TE_src, data.QUAD_XZNP) &&
                           data.getQuadDepth(TE_adj, data.QUAD_XZPN) == data.getQuadDepth(TE_src, data.QUAD_XZNN);
                default: {}
            }
        }

        return super.shareFaces(TE_adj, TE_src, side_adj, side_src);
    }

    /**
     * Whether block can be attached to specified side of another block.
     *
     * @param  side the side
     * @return whether side is supported
     */
    @Override
    public boolean canAttachToSide(int side)
    {
        return side < 2;
    }

    /**
     * Whether block requires an adjacent block with solid side for support.
     *
     * @return whether block can float freely
     */
    @Override
    public boolean canFloat()
    {
        return true;
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersCollapsibleBlockRenderID;
    }

}
