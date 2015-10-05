package com.carpentersblocks.block;

import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.data.Ladder;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.EntityLivingUtil;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.FeatureRegistry;

public class BlockCarpentersLadder extends BlockSided {

    public final static String type[] = { "default", "rail", "pole" };
    private static Ladder data = new Ladder();

    public BlockCarpentersLadder(Material material)
    {
        super(material, data);
    }

    @Override
    /**
     * Cycle forwards through types.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int temp = data.getType(TE);

        if (++temp > type.length - 1) {
            temp = 0;
        }

        data.setType(TE, temp);
        return true;
    }

    @Override
    /**
     * Cycle backwards through types.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int temp = data.getType(TE);

        if (--temp < 0) {
            temp = type.length - 1;
        }

        data.setType(TE, temp);
        return true;
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {
            ForgeDirection dir =data.getDirection(TE);
            switch (dir) {
                case DOWN: // DIR_ON_X
                    setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
                    break;
                case UP: // DIR_ON_Z
                    setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
                    break;
                default:
                    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F, dir);
            }
        }
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        /* Check for solid face. */

        boolean result = super.canPlaceBlockOnSide(world, x, y, z, side);

        /* For DOWN and UP orientation, also check for ladder. */

        if (!result && side < 2) {
            ForgeDirection dir = ForgeDirection.getOrientation(side);
            result = world.getBlock(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ).equals(this);
        }

        return result;
    }

    @Override
    /**
     * Whether block requires an adjacent block with solid side for support.
     *
     * @return whether block can float freely
     */
    public boolean canFloat()
    {
        return FeatureRegistry.enableFreeStandingLadders;
    }

    @Override
    /**
     * Gets placement direction when first placed in world.
     *
     * @param world the {@link World}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the {@link ForgeDirection}
     */
    protected ForgeDirection getPlacementDirection(World world, int x, int y, int z, EntityLivingBase entityLiving)
    {
        // Need to interpret DOWN and UP orientation as axis assignment
        if (world.getBlockMetadata(x, y, z) < 2) {
            ForgeDirection facing = EntityLivingUtil.getFacing(entityLiving).getOpposite();
            if (facing.offsetX != 0) {
                return ForgeDirection.getOrientation(Ladder.DIR_ON_Z);
            } else {
                return ForgeDirection.getOrientation(Ladder.DIR_ON_X);
            }
        } else {
            return super.getPlacementDirection(world, x, y, z, entityLiving);
        }
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

        if (!entityLiving.isSneaking())
        {
            // Copy type and direction of adjacent ladder type
            for (int side = 0; side < 2; ++side) {
                TEBase TE = getTileEntity(world, x, y, z);
                ForgeDirection dir = ForgeDirection.getOrientation(side);
                if (world.getBlock(x, y - dir.offsetY, z).equals(this)) {
                    TEBase TE_adj = (TEBase) world.getTileEntity(x, y - dir.offsetY, z);
                    data.setType(TE, data.getType(TE_adj));
                    data.setDirection(TE, data.getDirection(TE_adj));
                }
            }
        }
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (!world.isRemote) {
            TEBase TE = getTileEntity(world, x, y, z);
            if (TE != null && !data.isFreestanding(TE)) {
                super.onNeighborBlockChange(world, x, y, z, block);
            }
        }
    }

    @Override
    public boolean isLadder(IBlockAccess blockAccess, int x, int y, int z, EntityLivingBase entityLiving)
    {
        return true;
    }

    @Override
    public ForgeDirection[] getValidRotations(World worldObj, int x, int y,int z)
    {
        ForgeDirection[] axises = {ForgeDirection.UP, ForgeDirection.DOWN};
        return axises;
    }

    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis)
    {
        if (Arrays.asList(getRotationAxes()).contains(axis)) {
            TEBase TE = getTileEntity(world, x, y, z);
            if (TE != null && data.isFreestanding(TE)) {
                int side = data.getDirection(TE).ordinal() == data.DIR_ON_X ? data.DIR_ON_Z : data.DIR_ON_X;
                return data.setDirection(TE, ForgeDirection.getOrientation(side));
            }
        }

        return super.rotateBlock(world, x, y, z, axis);
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersLadderRenderID;
    }

}
