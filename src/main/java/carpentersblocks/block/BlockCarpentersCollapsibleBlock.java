package carpentersblocks.block;

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
import carpentersblocks.data.Collapsible;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.collapsible.CollapsibleUtil;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.ItemRegistry;

public class BlockCarpentersCollapsibleBlock extends BlockCoverable {

    public BlockCarpentersCollapsibleBlock(Material material)
    {
        super(material);
    }

    @Override
    /**
     * Raise quadrant of block.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int quad = Collapsible.getQuad(EventHandler.hitX, EventHandler.hitZ);
        int quadHeight = Collapsible.getQuadHeight(TE, quad);

        Collapsible.setQuadHeight(TE, quad, --quadHeight);
        smoothAdjacentCollapsibles(TE, quad);

        return true;
    }

    @Override
    /**
     * Lower quadrant of block.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int quad = Collapsible.getQuad(EventHandler.hitX, EventHandler.hitZ);
        int quadHeight = Collapsible.getQuadHeight(TE, quad);

        Collapsible.setQuadHeight(TE, quad, ++quadHeight);
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
        World world = TE.getWorldObj();

        TEBase TE_XN   = getTileEntity(world, TE.xCoord - 1, TE.yCoord, TE.zCoord);
        TEBase TE_XP   = getTileEntity(world, TE.xCoord + 1, TE.yCoord, TE.zCoord);
        TEBase TE_ZN   = getTileEntity(world, TE.xCoord, TE.yCoord, TE.zCoord - 1);
        TEBase TE_ZP   = getTileEntity(world, TE.xCoord, TE.yCoord, TE.zCoord + 1);
        TEBase TE_XZNN = getTileEntity(world, TE.xCoord - 1, TE.yCoord, TE.zCoord - 1);
        TEBase TE_XZNP = getTileEntity(world, TE.xCoord - 1, TE.yCoord, TE.zCoord + 1);
        TEBase TE_XZPN = getTileEntity(world, TE.xCoord + 1, TE.yCoord, TE.zCoord - 1);
        TEBase TE_XZPP = getTileEntity(world, TE.xCoord + 1, TE.yCoord, TE.zCoord + 1);

        int height = Collapsible.getQuadHeight(TE, src_quadrant);

        switch (src_quadrant)
        {
            case Collapsible.QUAD_XZNN:
                if (TE_ZN != null) {
                    Collapsible.setQuadHeight(TE_ZN, Collapsible.QUAD_XZNP, height);
                }
                if (TE_XZNN != null) {
                    Collapsible.setQuadHeight(TE_XZNN, Collapsible.QUAD_XZPP, height);
                }
                if (TE_XN != null) {
                    Collapsible.setQuadHeight(TE_XN, Collapsible.QUAD_XZPN, height);
                }
                break;
            case Collapsible.QUAD_XZNP:
                if (TE_XN != null) {
                    Collapsible.setQuadHeight(TE_XN, Collapsible.QUAD_XZPP, height);
                }
                if (TE_XZNP != null) {
                    Collapsible.setQuadHeight(TE_XZNP, Collapsible.QUAD_XZPN, height);
                }
                if (TE_ZP != null) {
                    Collapsible.setQuadHeight(TE_ZP, Collapsible.QUAD_XZNN, height);
                }
                break;
            case Collapsible.QUAD_XZPN:
                if (TE_XP != null) {
                    Collapsible.setQuadHeight(TE_XP, Collapsible.QUAD_XZNN, height);
                }
                if (TE_XZPN != null) {
                    Collapsible.setQuadHeight(TE_XZPN, Collapsible.QUAD_XZNP, height);
                }
                if (TE_ZN != null) {
                    Collapsible.setQuadHeight(TE_ZN, Collapsible.QUAD_XZPP, height);
                }
                break;
            case Collapsible.QUAD_XZPP:
                if (TE_ZP != null) {
                    Collapsible.setQuadHeight(TE_ZP, Collapsible.QUAD_XZPN, height);
                }
                if (TE_XZPP != null) {
                    Collapsible.setQuadHeight(TE_XZPP, Collapsible.QUAD_XZNN, height);
                }
                if (TE_XP != null) {
                    Collapsible.setQuadHeight(TE_XP, Collapsible.QUAD_XZNP, height);
                }
                break;
        }
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        float maxHeight = CollapsibleUtil.getBoundsMaxHeight(TE);

        if (maxHeight != 1.0F) {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, maxHeight, 1.0F);
        }
    }

    @Override
    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     */
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            if (isBlockSolid(world, x, y, z)) {

                switch (side) {
                    case UP:
                        return BlockProperties.getMetadata(TE) == 0;
                    case NORTH:
                        return Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN) == 32;
                    case SOUTH:
                        return Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP) == 32;
                    case WEST:
                        return Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN) == 32;
                    case EAST:
                        return Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP) == 32;
                    default:
                        return true;
                }

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
    private static int getBlockHeight(IBlockAccess world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);

        if (!block.getMaterial().blocksMovement()) {
            return 1;
        }

        return (int) (block.getBlockBoundsMaxY() * 15.0 + 1.0);
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        /* If shift key is down, skip auto-setting quadrant heights. */

        if (!entityLiving.isSneaking()) {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {

                /* Create a linear slope from neighbor blocks and collapsible quadrants. */

                /* Mininum and maximum height of quadrants */
                final int MIN_HEIGHT = 1;
                final int MAX_HEIGHT = 16;

                /* find slopes in landscape */
                int xn = scanX(world, x, y, z, -1, MAX_HEIGHT);
                int xp = scanX(world, x, y, z, 1, MAX_HEIGHT);
                int zn = scanZ(world, x, y, z, -1, MAX_HEIGHT);
                int zp = scanZ(world, x, y, z, 1, MAX_HEIGHT);

                TEBase TE_XN = getTileEntity(world, xn, y, z);
                TEBase TE_XP = getTileEntity(world, xp, y, z);
                TEBase TE_ZN = getTileEntity(world, x, y, zn);
                TEBase TE_ZP = getTileEntity(world, x, y, zp);

                int height_XZNN = MIN_HEIGHT, height_XZPN = MIN_HEIGHT, height_XZPP = MIN_HEIGHT, height_XZNP = MIN_HEIGHT;

                int hxn1, hxn2;
                if(TE_XN != null) {
                    hxn1 = Collapsible.getQuadHeight(TE_XN, Collapsible.QUAD_XZPN);
                    hxn2 = Collapsible.getQuadHeight(TE_XN, Collapsible.QUAD_XZPP);
                } else {
                    hxn1 = hxn2 = getBlockHeight(world, xn, y, z);
                }

                int hxp1, hxp2;
                if(TE_XP != null) {
                    hxp1 = Collapsible.getQuadHeight(TE_XP, Collapsible.QUAD_XZNN);
                    hxp2 = Collapsible.getQuadHeight(TE_XP, Collapsible.QUAD_XZNP);
                } else {
                    hxp1 = hxp2 = getBlockHeight(world, xp, y, z);
                }

                int hzn1, hzn2;
                if(TE_ZN != null) {
                    hzn1 = Collapsible.getQuadHeight(TE_ZN, Collapsible.QUAD_XZNP);
                    hzn2 = Collapsible.getQuadHeight(TE_ZN, Collapsible.QUAD_XZPP);
                } else {
                    hzn1 = hzn2 = getBlockHeight(world, x, y, zn);
                }

                int hzp1, hzp2;
                if(TE_ZP != null) {
                    hzp1 = Collapsible.getQuadHeight(TE_ZP, Collapsible.QUAD_XZNN);
                    hzp2 = Collapsible.getQuadHeight(TE_ZP, Collapsible.QUAD_XZPN);
                } else {
                    hzp1 = hzp2 = getBlockHeight(world, x, y, zp);
                }

                /* lerp between heights, create smooth slope */
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

                Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZNN, height_XZNN);
                Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZNP, height_XZNP);
                Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZPP, height_XZPP);
                Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZPN, height_XZPN);

                for (int quad = 0; quad < 4; ++quad) {
                    smoothAdjacentCollapsibles(TE, quad);
                }
            }

        }

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
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
                colBox = AxisAlignedBB.getAABBPool().getAABB(x + bounds[0], y + bounds[1], z + bounds[2], x + bounds[3], y + bounds[4], z + bounds[5]);

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
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersCollapsibleBlockRenderID;
    }

}
