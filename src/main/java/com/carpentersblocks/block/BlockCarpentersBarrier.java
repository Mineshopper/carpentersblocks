package com.carpentersblocks.block;

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
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.data.Barrier;
import com.carpentersblocks.data.Gate;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersBarrier extends BlockCoverable {

    public BlockCarpentersBarrier(Material material)
    {
        super(material);
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
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            /* Match block type with adjacent type if possible. */

            TEBase[] TE_list = getAdjacentTileEntities(world, x, y, z);

            for (TEBase TE_current : TE_list) {

                if (TE_current != null) {

                    Block block = TE_current.getBlockType();

                    if (block.equals(this)) {
                        Barrier.setType(TE, Barrier.getType(TE_current));
                    } else if (block.equals(BlockRegistry.blockCarpentersGate)) {
                        Barrier.setType(TE, Gate.getType(TE_current));
                    }

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
        boolean connect_ZN = canConnectBarrierTo(world, x, y, z - 1, ForgeDirection.SOUTH);
        boolean connect_ZP = canConnectBarrierTo(world, x, y, z + 1, ForgeDirection.NORTH);
        boolean connect_XN = canConnectBarrierTo(world, x - 1, y, z, ForgeDirection.EAST);
        boolean connect_XP = canConnectBarrierTo(world, x + 1, y, z, ForgeDirection.WEST);

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
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);
        int type = Barrier.getType(TE);

        boolean connect_ZN = canConnectBarrierTo(blockAccess, x, y, z - 1, ForgeDirection.SOUTH);
        boolean connect_ZP = canConnectBarrierTo(blockAccess, x, y, z + 1, ForgeDirection.NORTH);
        boolean connect_XN = canConnectBarrierTo(blockAccess, x - 1, y, z, ForgeDirection.EAST);
        boolean connect_XP = canConnectBarrierTo(blockAccess, x + 1, y, z, ForgeDirection.WEST);

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
    public boolean canConnectBarrierTo(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection side)
    {
        Block block = blockAccess.getBlock(x, y, z);

        if (block.equals(this) || block.equals(BlockRegistry.blockCarpentersGate)) {
            return true;
        } else {
            return block.isSideSolid(blockAccess, x, y, z, side);
        }
    }

    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     *
     * @param blockAccess The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param side The side to check
     * @return True if the block is solid on the specified side.
     */
    @Override
    public boolean isSideSolid(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection side)
    {
        return side.equals(ForgeDirection.UP);
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
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side)
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
