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
import carpentersblocks.data.Barrier;
import carpentersblocks.data.Gate;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersBarrier extends BlockCoverable {

    public BlockCarpentersBarrier(int blockID)
    {
        super(blockID, Material.wood);
        setHardness(0.2F);
        setUnlocalizedName("blockCarpentersBarrier");
        setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
        setTextureName("carpentersblocks:general/solid");
    }

    @Override
    /**
     * Toggles post.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        Barrier.setPost(TE, Barrier.getPost(TE) == Barrier.HAS_POST ? Barrier.NO_POST : Barrier.HAS_POST);

        return true;
    }

    @Override
    /**
     * Alters barrier type or sub-type.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int type = Barrier.getType(TE);

        if (entityPlayer.isSneaking()) {

            /*
             * Cycle through sub-types
             */
            if (type <= Barrier.TYPE_VANILLA_X3) {
                if (++type > Barrier.TYPE_VANILLA_X3) {
                    type = Barrier.TYPE_VANILLA;
                }
            }

        } else {

            /*
             * Cycle through barrier types
             */
            if (type <= Barrier.TYPE_VANILLA_X3) {
                type = Barrier.TYPE_PICKET;
            } else if (++type > Barrier.TYPE_WALL) {
                type = Barrier.TYPE_VANILLA;
            }

        }

        Barrier.setType(TE, type);

        return true;
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        /*
         * Match gate type with adjacent type or barrier type if possible
         */
        TEBase TE_YN = world.getBlockId(x, y - 1, z) == blockID || world.getBlockId(x, y - 1, z) == BlockRegistry.blockCarpentersGateID ? (TEBase)world.getBlockTileEntity(x, y - 1, z) : null;
        TEBase TE_YP = world.getBlockId(x, y + 1, z) == blockID || world.getBlockId(x, y + 1, z) == BlockRegistry.blockCarpentersGateID ? (TEBase)world.getBlockTileEntity(x, y + 1, z) : null;
        TEBase TE_XN = world.getBlockId(x - 1, y, z) == blockID || world.getBlockId(x - 1, y, z) == BlockRegistry.blockCarpentersGateID ? (TEBase)world.getBlockTileEntity(x - 1, y, z) : null;
        TEBase TE_XP = world.getBlockId(x + 1, y, z) == blockID || world.getBlockId(x + 1, y, z) == BlockRegistry.blockCarpentersGateID ? (TEBase)world.getBlockTileEntity(x + 1, y, z) : null;
        TEBase TE_ZN = world.getBlockId(x, y, z - 1) == blockID || world.getBlockId(x, y, z - 1) == BlockRegistry.blockCarpentersGateID ? (TEBase)world.getBlockTileEntity(x, y, z - 1) : null;
        TEBase TE_ZP = world.getBlockId(x, y, z + 1) == blockID || world.getBlockId(x, y, z + 1) == BlockRegistry.blockCarpentersGateID ? (TEBase)world.getBlockTileEntity(x, y, z + 1) : null;

        if (TE_YN != null) {
            Barrier.setType(TE, world.getBlockId(x, y - 1, z) == blockID ? Barrier.getType(TE_YN) : Gate.getType(TE_YN));
        } else if (TE_YP != null) {
            Barrier.setType(TE, world.getBlockId(x, y + 1, z) == blockID ? Barrier.getType(TE_YP) : Gate.getType(TE_YP));
        } else if (TE_XN != null) {
            Barrier.setType(TE, world.getBlockId(x - 1, y, z) == blockID ? Barrier.getType(TE_XN) : Gate.getType(TE_XN));
        } else if (TE_XP != null) {
            Barrier.setType(TE, world.getBlockId(x + 1, y, z) == blockID ? Barrier.getType(TE_XP) : Gate.getType(TE_XP));
        } else if (TE_ZN != null) {
            Barrier.setType(TE, world.getBlockId(x, y, z - 1) == blockID ? Barrier.getType(TE_ZN) : Gate.getType(TE_ZN));
        } else if (TE_ZP != null) {
            Barrier.setType(TE, world.getBlockId(x, y, z + 1) == blockID ? Barrier.getType(TE_ZP) : Gate.getType(TE_ZP));
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
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        boolean connect_ZN = canConnectBarrierTo(TE, world, x, y, z - 1, ForgeDirection.SOUTH);
        boolean connect_ZP = canConnectBarrierTo(TE, world, x, y, z + 1, ForgeDirection.NORTH);
        boolean connect_XN = canConnectBarrierTo(TE, world, x - 1, y, z, ForgeDirection.EAST);
        boolean connect_XP = canConnectBarrierTo(TE, world, x + 1, y, z, ForgeDirection.WEST);

        float x_Low = 0.375F;
        float x_High = 0.625F;
        float z_Low = 0.375F;
        float z_High = 0.625F;

        if (connect_ZN) {
            z_Low = 0.0F;
        }

        if (connect_ZP) {
            z_High = 1.0F;
        }

        if (connect_ZN || connect_ZP)
        {
            setBlockBounds(x_Low, 0.0F, z_Low, x_High, 1.5F, z_High);
            super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
        }

        z_Low = 0.375F;
        z_High = 0.625F;

        if (connect_XN) {
            x_Low = 0.0F;
        }

        if (connect_XP) {
            x_High = 1.0F;
        }

        if (connect_XN || connect_XP || !connect_ZN && !connect_ZP)
        {
            setBlockBounds(x_Low, 0.0F, z_Low, x_High, 1.5F, z_High);
            super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
        }

        if (connect_ZN) {
            z_Low = 0.0F;
        }

        if (connect_ZP) {
            z_High = 1.0F;
        }

        setBlockBounds(x_Low, 0.0F, z_Low, x_High, 1.0F, z_High);
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);
        int type = Barrier.getType(TE);

        boolean connect_ZN = canConnectBarrierTo(TE, world, x, y, z - 1, ForgeDirection.SOUTH);
        boolean connect_ZP = canConnectBarrierTo(TE, world, x, y, z + 1, ForgeDirection.NORTH);
        boolean connect_XN = canConnectBarrierTo(TE, world, x - 1, y, z, ForgeDirection.EAST);
        boolean connect_XP = canConnectBarrierTo(TE, world, x + 1, y, z, ForgeDirection.WEST);

        float x_Low = 0.0F;
        float x_High = 1.0F;
        float z_Low = 0.0F;
        float z_High = 1.0F;

        if (type <= Barrier.TYPE_VANILLA_X3) {

            x_Low = 0.375F;
            x_High = 0.625F;
            z_Low = 0.375F;
            z_High = 0.625F;

            if (connect_ZN) {
                z_Low = 0.0F;
            }

            if (connect_ZP) {
                z_High = 1.0F;
            }

            if (connect_XN) {
                x_Low = 0.0F;
            }

            if (connect_XP) {
                x_High = 1.0F;
            }

        } else {

            x_Low = 0.25F;
            x_High = 0.75F;
            z_Low = 0.25F;
            z_High = 0.75F;

            if (connect_ZN) {
                z_Low = 0.0F;
            }

            if (connect_ZP) {
                z_High = 1.0F;
            }

            if (connect_XN) {
                x_Low = 0.0F;
            }

            if (connect_XP) {
                x_High = 1.0F;
            }

            if (connect_ZN && connect_ZP && !connect_XN && !connect_XP) {
                x_Low = 0.3125F;
                x_High = 0.6875F;
            } else if (!connect_ZN && !connect_ZP && connect_XN && connect_XP) {
                z_Low = 0.3125F;
                z_High = 0.6875F;
            }

        }

        setBlockBounds(x_Low, 0.0F, z_Low, x_High, 1.0F, z_High);
    }

    /**
     * Returns true if block can connect to specified side of neighbor block.
     */
    public boolean canConnectBarrierTo(TEBase TE, IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
        int blockID = world.getBlockId(x, y, z);

        if (blockID > 0)
        {
            Block block = Block.blocksList[world.getBlockId(x, y, z)];

            /*
             * For the top side, make it create post if block is flower pot, torch, etc.
             */
            if (side == ForgeDirection.UP) {
                if (block != null && block.blockMaterial == Material.circuits) {
                    return true;
                }
            } else {
                if (world.getBlockId(x, y, z) == this.blockID || blockID == BlockRegistry.blockCarpentersGateID) {
                    return true;
                }

                return block.isBlockSolidOnSide(TE.worldObj, x, y, z, side) && Barrier.getPost(TE) != Barrier.HAS_POST;
            }
        }

        return false;
    }

    @Override
    /**
     * Determines if a torch can be placed on the top surface of this block.
     */
    public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: world, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        return true;
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersBarrierRenderID;
    }

}
