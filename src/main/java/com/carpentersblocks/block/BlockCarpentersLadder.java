package com.carpentersblocks.block;

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
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            switch (data.getDirection(TE)) {
                case DOWN: // DIR_ON_X
                    setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
                    break;
                case UP: // DIR_ON_Z
                    setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
                    break;
                case NORTH:
                    setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
                    break;
                case SOUTH:
                    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
                    break;
                case WEST:
                    setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;
                case EAST:
                    setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
                    break;
                default: {}
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
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        /* Need to interpret DOWN and UP orientation as axis assignment. */

        if (world.getBlockMetadata(x, y, z) < 2) {
            ForgeDirection facing = EntityLivingUtil.getFacing(entityLiving).getOpposite();
            if (facing.offsetX != 0) {
                world.setBlockMetadataWithNotify(x, y, z, Ladder.DIR_ON_Z, 0);
            } else {
                world.setBlockMetadataWithNotify(x, y, z, Ladder.DIR_ON_X, 0);
            }
        }

        /* Match type above or below ladder. */

        for (int side = 0; side < 2; ++side) {
            TEBase TE = getTileEntity(world, x, y, z);
            ForgeDirection dir = ForgeDirection.getOrientation(side);
            if (world.getBlock(x, y + dir.offsetY, z).equals(this)) {
                TEBase TE_adj = (TEBase) world.getTileEntity(x, y + dir.offsetY, z);
                data.setType(TE, data.getType(TE_adj));
            }
        }

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
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
    public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entityLiving)
    {
        return true;
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
