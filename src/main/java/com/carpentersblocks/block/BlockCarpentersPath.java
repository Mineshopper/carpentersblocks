package com.carpentersblocks.block;

import java.util.List;

import com.carpentersblocks.data.Slab;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersPath extends BlockCoverable {

    private static float[] bounds = { 0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F };

    public BlockCarpentersPath(Material material)
    {
        super(material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns a base icon that doesn't rely on blockIcon, which
     * is set prior to texture stitch events.
     */
    public IIcon getIcon()
    {
        return IconRegistry.icon_uncovered_quartered;
    }

    private boolean onHammerInteraction(TEBase TE)
    {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    /**
     * Alter type.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        return onHammerInteraction(TE);
    }

    @Override
    /**
     * Alter type.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        return onHammerInteraction(TE);
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {
            setBlockBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
        }
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
            TE.setData(1);
        }
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
     * Checks if the block is a solid face on the given side, used by placement logic.
     */
    public boolean isSideSolid(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection side)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {

            if (isBlockSolid(blockAccess, x, y, z)) {

                return true;

            }

        }

        return false;
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
        return BlockRegistry.carpentersPathRenderID;
    }

}
