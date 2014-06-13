package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
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

        float maxHeight = getMaxHeight(TE);

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

                /* Match adjacent collapsible quadrant heights. */

                TEBase TE_XN = getTileEntity(world, x - 1, y, z);
                TEBase TE_XP = getTileEntity(world, x + 1, y, z);
                TEBase TE_ZN = getTileEntity(world, x, y, z - 1);
                TEBase TE_ZP = getTileEntity(world, x, y, z + 1);

                int height_XZNN = 1, height_XZPN = 1, height_XZPP = 1, height_XZNP = 1;
                if (TE_XN != null) {
                	height_XZNN = Math.max(height_XZNN, Collapsible.getQuadHeight(TE_XN, Collapsible.QUAD_XZPN));
                	height_XZNP = Math.max(height_XZNP, Collapsible.getQuadHeight(TE_XN, Collapsible.QUAD_XZPP));
                }
                else {
                	int height = getBlockHeight(world, x - 1, y, z);
                	height_XZNN = Math.max(height_XZNN, height);
                	height_XZNP = Math.max(height_XZNP, height);
                }

                if (TE_XP != null) {
                	height_XZPN = Math.max(height_XZPN, Collapsible.getQuadHeight(TE_XP, Collapsible.QUAD_XZNN));
                	height_XZPP = Math.max(height_XZPP, Collapsible.getQuadHeight(TE_XP, Collapsible.QUAD_XZNP));
                }
                else {
                	int height = getBlockHeight(world, x + 1, y, z);
                	height_XZPN = Math.max(height_XZPN, height);
                	height_XZPP = Math.max(height_XZPP, height);
                }
                
                if (TE_ZN != null) {
                    height_XZNN = Math.max(height_XZNN, Collapsible.getQuadHeight(TE_ZN, Collapsible.QUAD_XZNP));
                    height_XZPN = Math.max(height_XZPN, Collapsible.getQuadHeight(TE_ZN, Collapsible.QUAD_XZPP));
                }
                else {
                	int height = getBlockHeight(world, x, y, z - 1);
                    height_XZNN = Math.max(height_XZNN, height);
                    height_XZPN = Math.max(height_XZPN, height);
                }
                
                if (TE_ZP != null) {
                    height_XZNP = Math.max(height_XZNP, Collapsible.getQuadHeight(TE_ZP, Collapsible.QUAD_XZNN));
                    height_XZPP = Math.max(height_XZPP, Collapsible.getQuadHeight(TE_ZP, Collapsible.QUAD_XZPN));
                }
                else {
                	int height = getBlockHeight(world, x, y, z + 1);
                    height_XZNP = Math.max(height_XZNP, height);
                    height_XZPP = Math.max(height_XZPP, height);
                }

                Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZNN, height_XZNN);
                Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZNP, height_XZNP);
                Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZPP, height_XZPP);
                Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZPN, height_XZPN);
            }
        }

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }

    /**
     * Returns block height
     */
    private static int getBlockHeight(IBlockAccess world, int x, int y, int z)
    {
    	Block block = world.getBlock(x, y, z);
    	double height = !(block instanceof BlockAir) ? block.getBlockBoundsMaxY() * 15.0 + 1.0 : 1.0;

    	return (int)height;
    }
    /**
     * Returns block height determined by the highest quadrant.
     */
    private float getMaxHeight(TEBase TE)
    {
        float maxHeight = 1.0F / 16.0F;

        for (int quadrant = 0; quadrant < 4; ++quadrant) {
            float quadHeight = Collapsible.getQuadHeight(TE, quadrant) / 16.0F;
            if (quadHeight > maxHeight) {
                maxHeight = quadHeight;
            }
        }

        return maxHeight;
    }

    /**
     * Will generate four boxes with max height represented by quadrant height.
     */
    private float[] genBounds(TEBase TE, int quad)
    {
        float xMin = 0.0F;
        float zMin = 0.0F;
        float xMax = 1.0F;
        float zMax = 1.0F;

        switch (quad)
        {
            case Collapsible.QUAD_XZNN:
                xMax = 0.5F;
                zMax = 0.5F;
                break;
            case Collapsible.QUAD_XZNP:
                xMax = 0.5F;
                zMin = 0.5F;
                break;
            case Collapsible.QUAD_XZPN:
                xMin = 0.5F;
                zMax = 0.5F;
                break;
            case Collapsible.QUAD_XZPP:
                xMin = 0.5F;
                zMin = 0.5F;
                break;
        }

        float maxHeight = getMaxHeight(TE);
        float height = Collapsible.getQuadHeight(TE, quad) / 16.0F;

        /* Make quads stagger no more than 0.5F so player can always walk across them. */
        if (maxHeight - height > 0.5F) {
            height = maxHeight - 0.5F;
        }

        float[] finalBounds = { xMin, 0.0F, zMin, xMax, height, zMax };

        return finalBounds;
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
                float[] bounds = genBounds(TE, quad);
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
                float[] bounds = genBounds(TE, quad);

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

            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

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
