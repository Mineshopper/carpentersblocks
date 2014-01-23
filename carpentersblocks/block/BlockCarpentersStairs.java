package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Stairs;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.ItemRegistry;
import carpentersblocks.util.stairs.StairsTransform;
import carpentersblocks.util.stairs.StairsUtil;

public class BlockCarpentersStairs extends BlockBase {

    public BlockCarpentersStairs(int blockID)
    {
        super(blockID, Material.wood);
        setHardness(0.2F);
        setUnlocalizedName("blockCarpentersStairs");
        setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
        setTextureName("carpentersblocks:general/quartered_frame");
    }

    @Override
    /**
     * Alters block direction.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int stairsID = BlockProperties.getData(TE);
        Stairs stairs = Stairs.stairsList[stairsID];

        /* Cycle between stairs direction based on current type. */

        switch (stairs.stairsType)
        {
        case NORMAL_XZ:
            if (++stairsID > Stairs.ID_NORMAL_SW) {
                stairsID = Stairs.ID_NORMAL_SE;
            }
            break;
        case NORMAL_Y:
            if (stairs.arePositive) {
                if (++stairsID > Stairs.ID_NORMAL_POS_E) {
                    stairsID = Stairs.ID_NORMAL_POS_N;
                }
            } else {
                if (++stairsID > Stairs.ID_NORMAL_NEG_E) {
                    stairsID = Stairs.ID_NORMAL_NEG_N;
                }
            }
            break;
        case NORMAL_INT:
            if (stairs.arePositive) {
                if ((stairsID += 2) > Stairs.ID_NORMAL_INT_POS_SE) {
                    stairsID = Stairs.ID_NORMAL_INT_POS_NE;
                }
            } else {
                if ((stairsID += 2) > Stairs.ID_NORMAL_INT_NEG_SE) {
                    stairsID = Stairs.ID_NORMAL_INT_NEG_NE;
                }
            }
            break;
        case NORMAL_EXT:
            if (stairs.arePositive) {
                if ((stairsID += 2) > Stairs.ID_NORMAL_EXT_POS_NW) {
                    stairsID = Stairs.ID_NORMAL_EXT_POS_SW;
                }
            }  else {
                if ((stairsID += 2) > Stairs.ID_NORMAL_EXT_NEG_NW) {
                    stairsID = Stairs.ID_NORMAL_EXT_NEG_SW;
                }
            }
            break;
        }

        BlockProperties.setData(TE, stairsID);

        return true;
    }

    @Override
    /**
     * Alters block type.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int stairsID = BlockProperties.getData(TE);
        Stairs stairs = Stairs.stairsList[stairsID];

        /* Transform stairs to next type. */

        switch (stairs.stairsType)
        {
        case NORMAL_XZ:
            stairsID = Stairs.ID_NORMAL_POS_N;
            break;
        case NORMAL_Y:
            if (stairs.arePositive) {
                stairsID -= 4;
            } else {
                stairsID = Stairs.ID_NORMAL_INT_POS_NE;
            }
            break;
        case NORMAL_INT:
            if (stairs.arePositive) {
                stairsID += 1;
            } else {
                if (stairsID == Stairs.ID_NORMAL_INT_NEG_NE || stairsID == Stairs.ID_NORMAL_INT_NEG_NW) {
                    stairsID += 11;
                } else {
                    stairsID += 3;
                }
            }
            break;
        case NORMAL_EXT:
            if (stairs.arePositive) {
                stairsID += 1;
            } else {
                stairsID = Stairs.ID_NORMAL_SE;
            }
            break;
        }

        BlockProperties.setData(TE, stairsID);

        return true;
    }

    @Override
    /**
     * Damages hammer with a chance to not damage.
     */
    protected void damageItemWithChance(World world, EntityPlayer entityPlayer)
    {
        if (world.rand.nextFloat() <= ItemRegistry.itemHammerDamageChanceFromStairs) {
            super.damageItemWithChance(world, entityPlayer);
        }
    }

    @Override
    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
        TEBase TE = (TEBase)world.getBlockTileEntity(x, y, z);

        MovingObjectPosition finalTrace = null;

        Stairs stairs = Stairs.stairsList[BlockProperties.getData(TE)];
        StairsUtil stairsUtil = new StairsUtil();

        double currDist = 0.0D;
        double maxDist = 0.0D;

        // Determine if ray trace is a hit on stairs
        for (int box = 0; box < 3; ++box)
        {
            float[] bounds = stairsUtil.genBounds(box, stairs);

            if (bounds != null)
            {
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
        }

        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return finalTrace;
    }

    @Override
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
    {
        AxisAlignedBB colBox = null;

        TEBase TE = (TEBase)world.getBlockTileEntity(x, y, z);

        Stairs stairs = Stairs.stairsList[BlockProperties.getData(TE)];
        StairsUtil stairsUtil = new StairsUtil();

        for (int box = 0; box < 3; ++box)
        {
            float[] bounds = stairsUtil.genBounds(box, stairs);

            if (bounds != null) {
                colBox = AxisAlignedBB.getAABBPool().getAABB(x + bounds[0], y + bounds[1], z + bounds[2], x + bounds[3], y + bounds[4], z + bounds[5]);
            }
            if (colBox != null && axisAlignedBB.intersectsWith(colBox)) {
                list.add(colBox);
            }
        }
    }

    @Override
    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     */
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        if (isBlockSolid(world, x, y, z)) {
            TEBase TE = (TEBase)world.getBlockTileEntity(x, y, z);
            return Stairs.stairsList[BlockProperties.getData(TE)].isFaceFull(side);
        }

        return false;
    }

    @Override
    /**
     * Called when block is placed in world.
     * Sets stairs angle depending on click coordinates on block face.
     *
     *    Metadata values:
     *      0 - 11    -    Identifies stairs angle in x, y, z space.
     *     12 - 13    -    Top or bottom side of block clicked.  onBlockPlacedBy() determines
     *                 direction and sets interpolated value from 0 - 11.
     */
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        // Normalize face coordinates
        switch (side) {
        case 2:
            hitX = 1.0F - hitX;
            break;
        case 4:
            hitX = hitZ;
            break;
        case 5:
            hitX = 1.0F - hitZ;
            break;
        }

        if (side > 1) {
            if (hitY > 0.5F && hitX > 1.0F - hitY && hitX < hitY) {
                return side + 2;
            } else if (hitY < 0.5F && hitX < 1.0F - hitY && hitX > hitY) {
                return side + 6;
            } else if (hitX < 0.2F) {
                return side == 2 ? 1 : side == 3 ? 0 : side == 4 ? 3 : 2;
            } else if (hitX > 0.8F){
                return side == 2 ? 2 : side == 3 ? 3 : side == 4 ? 1 : 0;
            } else if (hitY > 0.5F) {
                return side + 2;
            } else { // hitY < 0.5F
                return side + 6;
            }
        } else {
            return side + 12;
        }
    }

    @Override
    /**
     * Called when the block is placed in the world.
     * Uses cardinal direction to adjust metadata if player clicks top or bottom face of block.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        BlockProperties.setData(TE, world.getBlockMetadata(x, y, z));
        int stairsID = BlockProperties.getData(TE);

        if (stairsID > 11)
        {
            switch (facing) {
            case 0:
                stairsID = stairsID == 12 ? Stairs.ID_NORMAL_NEG_N : Stairs.ID_NORMAL_POS_N;
                break;
            case 1:
                stairsID = stairsID == 12 ? Stairs.ID_NORMAL_NEG_E : Stairs.ID_NORMAL_POS_E;
                break;
            case 2:
                stairsID = stairsID == 12 ? Stairs.ID_NORMAL_NEG_S : Stairs.ID_NORMAL_POS_S;
                break;
            case 3:
                stairsID = stairsID == 12 ? Stairs.ID_NORMAL_NEG_W : Stairs.ID_NORMAL_POS_W;
                break;
            }
        }

        BlockProperties.setData(TE, stairsID);

        /* If shift key is down, skip auto-orientation. */
        if (!entityLiving.isSneaking()) {
            StairsTransform stairsTransform = new StairsTransform(TE);
            stairsTransform.begin();
        }

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }

    @Override
    /**
     * Returns whether block can support cover on side.
     */
    public boolean canCoverSide(TEBase TE, World world, int x, int y, int z, int side)
    {
        return true;
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersStairsRenderID;
    }

}
