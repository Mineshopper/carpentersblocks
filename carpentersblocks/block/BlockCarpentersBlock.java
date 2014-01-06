package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Slab;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.registry.BlockRegistry;

public class BlockCarpentersBlock extends BlockBase {

    public BlockCarpentersBlock(int blockID)
    {
        super(blockID, Material.wood);
        setHardness(0.2F);
        setUnlocalizedName("blockCarpentersBlock");
        setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
        setTextureName("carpentersblocks:general/quartered_frame");
    }

    @Override
    /**
     * Alter type.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int data = BlockProperties.getData(TE);

        if (++data > Slab.SLAB_Z_POS) {
            data = Slab.BLOCK_FULL;
        }

        BlockProperties.setData(TE, data);

        return true;
    }

    @Override
    /**
     * Alternate between full 1m cube and slab.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int data = BlockProperties.getData(TE);

        if (data == Slab.BLOCK_FULL) {
            switch (EventHandler.eventFace)
            {
            case 0:
                data = Slab.SLAB_Y_POS;
                break;
            case 1:
                data = Slab.SLAB_Y_NEG;
                break;
            case 2:
                data = Slab.SLAB_Z_POS;
                break;
            case 3:
                data = Slab.SLAB_Z_NEG;
                break;
            case 4:
                data = Slab.SLAB_X_POS;
                break;
            case 5:
                data = Slab.SLAB_X_NEG;
                break;
            }
        } else {
            data = Slab.BLOCK_FULL;
        }

        BlockProperties.setData(TE, data);

        return true;
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        int data = BlockProperties.getData(TE);

        float[] bounds = { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };

        switch (data) {
        case Slab.SLAB_X_NEG: {
            float[] tempBounds = { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
            bounds = tempBounds;
            break;
        }
        case Slab.SLAB_X_POS: {
            float[] tempBounds = { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
            bounds = tempBounds;
            break;
        }
        case Slab.SLAB_Y_NEG: {
            float[] tempBounds = { 0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F };
            bounds = tempBounds;
            break;
        }
        case Slab.SLAB_Y_POS: {
            float[] tempBounds = { 0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F };
            bounds = tempBounds;
            break;
        }
        case Slab.SLAB_Z_NEG: {
            float[] tempBounds = { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
            bounds = tempBounds;
            break;
        }
        case Slab.SLAB_Z_POS: {
            float[] tempBounds = { 0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
            bounds = tempBounds;
            break;
        }
        }

        setBlockBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
    }

    @Override
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
    {
        setBlockBoundsBasedOnState(world, x, y, z);
        super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        int data = Slab.BLOCK_FULL;

        // If shift key is down, skip auto-orientation
        if (!entityLiving.isSneaking())
        {
            /*
             * Match block type with adjacent type if possible
             */
            TEBase TE_YN = world.getBlockId(x, y - 1, z) == blockID ? (TEBase)world.getBlockTileEntity(x, y - 1, z) : null;
            TEBase TE_YP = world.getBlockId(x, y + 1, z) == blockID ? (TEBase)world.getBlockTileEntity(x, y + 1, z) : null;
            TEBase TE_XN = world.getBlockId(x - 1, y, z) == blockID ? (TEBase)world.getBlockTileEntity(x - 1, y, z) : null;
            TEBase TE_XP = world.getBlockId(x + 1, y, z) == blockID ? (TEBase)world.getBlockTileEntity(x + 1, y, z) : null;
            TEBase TE_ZN = world.getBlockId(x, y, z - 1) == blockID ? (TEBase)world.getBlockTileEntity(x, y, z - 1) : null;
            TEBase TE_ZP = world.getBlockId(x, y, z + 1) == blockID ? (TEBase)world.getBlockTileEntity(x, y, z + 1) : null;

            if (TE_YN != null) {
                data = BlockProperties.getData(TE_YN);
            } else if (TE_YP != null) {
                data = BlockProperties.getData(TE_YP);
            } else if (TE_XN != null) {
                data = BlockProperties.getData(TE_XN);
            } else if (TE_XP != null) {
                data = BlockProperties.getData(TE_XP);
            } else if (TE_ZN != null) {
                data = BlockProperties.getData(TE_ZN);
            } else if (TE_ZP != null) {
                data = BlockProperties.getData(TE_ZP);
            }
        }

        BlockProperties.setData(TE, data);

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }

    @Override
    /**
     * Return true if the block is a normal, solid cube.  This
     * determines indirect power state, entity ejection from blocks, and a few
     * others.
     */
    public boolean isBlockNormalCube(World world, int x, int y, int z)
    {
        if (extendsBlockBase(world, x, y, z)) {
            TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);
            return BlockProperties.getData(TE) == Slab.BLOCK_FULL;
        }

        return false;
    }

    @Override
    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     */
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        if (isBlockSolid(world, x, y, z))
        {
            TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);
            int data = BlockProperties.getData(TE);

            if (data == Slab.BLOCK_FULL) {
                return true;
            } else if (data == Slab.SLAB_Y_NEG && side == ForgeDirection.DOWN) {
                return true;
            } else if (data == Slab.SLAB_Y_POS && side == ForgeDirection.UP) {
                return true;
            } else if (data == Slab.SLAB_Z_NEG && side == ForgeDirection.NORTH) {
                return true;
            } else if (data == Slab.SLAB_Z_POS && side == ForgeDirection.SOUTH) {
                return true;
            } else if (data == Slab.SLAB_X_NEG && side == ForgeDirection.WEST) {
                return true;
            } else if (data == Slab.SLAB_X_POS && side == ForgeDirection.EAST) {
                return true;
            }
        }

        return false;
    }

    @Override
    /**
     * Compares dimensions and coordinates of two opposite
     * sides to determine whether they share faces.
     */
    protected boolean shareFaces(TEBase TE_adj, TEBase TE_src, ForgeDirection side_adj, ForgeDirection side_src)
    {
        if (TE_adj.getBlockType() == this)
        {
            Block block_src = Block.blocksList[TE_src.worldObj.getBlockId(TE_src.xCoord, TE_src.yCoord, TE_src.zCoord)];

            setBlockBoundsBasedOnState(TE_src.worldObj, TE_src.xCoord, TE_src.yCoord, TE_src.zCoord);

            double[] bounds_src =
                {
                    block_src.getBlockBoundsMinX(),
                    block_src.getBlockBoundsMinY(),
                    block_src.getBlockBoundsMinZ(),
                    block_src.getBlockBoundsMaxX(),
                    block_src.getBlockBoundsMaxY(),
                    block_src.getBlockBoundsMaxZ()
                };

            setBlockBoundsBasedOnState(TE_adj.worldObj, TE_adj.xCoord, TE_adj.yCoord, TE_adj.zCoord);

            /*
             * Check whether faces meet and their dimensions match.
             */
            switch (side_src)
            {
            case DOWN:
                /** -Y */
                return maxY == 1.0D &&
                bounds_src[1] == 0.0D &&
                minX == bounds_src[0] &&
                maxX == bounds_src[3] &&
                minZ == bounds_src[2] &&
                maxZ == bounds_src[5];
            case UP:
                /** +Y */
                return minY == 0.0D &&
                bounds_src[4] == 1.0D &&
                minX == bounds_src[0] &&
                maxX == bounds_src[3] &&
                minZ == bounds_src[2] &&
                maxZ == bounds_src[5];
            case NORTH:
                /** -Z */
                return maxZ == 1.0D &&
                bounds_src[2] == 0.0D &&
                minX == bounds_src[0] &&
                maxX == bounds_src[3] &&
                minY == bounds_src[1] &&
                maxY == bounds_src[4];
            case SOUTH:
                /** +Z */
                return minZ == 0.0D &&
                bounds_src[5] == 1.0D &&
                minX == bounds_src[0] &&
                maxX == bounds_src[3] &&
                minY == bounds_src[1] &&
                maxY == bounds_src[4];
            case WEST:
                /** -X */
                return maxX == 1.0D &&
                bounds_src[0] == 0.0D &&
                minY == bounds_src[1] &&
                maxY == bounds_src[4] &&
                minZ == bounds_src[2] &&
                maxZ == bounds_src[5];
            case EAST:
                /** +X */
                return minX == 0.0D &&
                bounds_src[3] == 1.0D &&
                minY == bounds_src[1] &&
                maxY == bounds_src[4] &&
                minZ == bounds_src[2] &&
                maxZ == bounds_src[5];
            default:
                return false;
            }
        }

        return super.shareFaces(TE_adj, TE_src, side_adj, side_src);
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
        return BlockRegistry.carpentersBlockRenderID;
    }

}
