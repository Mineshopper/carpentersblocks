package com.carpentersblocks.block;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.data.GarageDoor;
import com.carpentersblocks.data.Hinge;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.EntityLivingUtil;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersGarageDoor extends BlockCoverable {

    public final static String type[] = { "default", "glassTop", "glass", "siding" };
    private static GarageDoor data = new GarageDoor();

    public BlockCarpentersGarageDoor(Material material)
    {
        super(material);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        IconRegistry.icon_garage_glass_top = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "garagedoor/glass_top");
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

        // TODO: Update all connecting blocks
        // TODO: Fix block dropping when hit with hammer
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

        // TODO: Update all connecting blocks
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
            float yMin = data.isTopmost(TE) && data.isOpen(TE) ? 0.5F : 0.0F;
            if (data.isVisible(TE)) {
                switch (data.getDirection(TE)) {
                    case NORTH:
                        setBlockBounds(0.0F, yMin, 0.75F, 1.0F, 1.0F, 0.875F);
                        break;
                    case SOUTH:
                        setBlockBounds(0.0F, yMin, 0.125F, 1.0F, 1.0F, 0.25F);
                        break;
                    case WEST:
                        setBlockBounds(0.75F, yMin, 0.0F, 0.875F, 1.0F, 1.0F);
                        break;
                    case EAST:
                        setBlockBounds(0.125F, yMin, 0.0F, 0.25F, 1.0F, 1.0F);
                        break;
                    default: {}
                }
            }
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        if (TE != null) {
            if (!data.isVisible(TE)) {
                return null;
            }
        }

        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        if (TE != null) {
            if (!data.isVisible(TE)) {
                return null;
            }
        }

        return super.collisionRayTrace(world, x, y, z, startVec, endVec);
    }

    /**
     * Will destroy a column of garage doors starting at the bottommost block.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     */
    private void destroy(World world, int x, int y, int z)
    {
        int baseY = data.getBottommost(world, x, y, z).yCoord;
        do {
            TEBase temp = (TEBase) world.getTileEntity(x, baseY, z);
            if (temp != null) {
                if (data.isTopmost(temp)) {
                    dropBlockAsItem(world, x, y, z, 0, 0);
                }
            }
            world.setBlockToAir(x, baseY++, z);
        } while (world.getBlock(x, baseY, z).equals(this));
    }

    /**
     * Will create a column of garage doors beginning at given coordinates.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     */
    private void create(TEBase TE, World world, int x, int y, int z)
    {
        ForgeDirection dir = data.getDirection(TE);
        int type = data.getType(TE);

        for (int baseY = y; canPlaceBlockAt(world, x, baseY, z); --baseY) {
            world.setBlock(x, baseY, z, this);
            TEBase temp = getTileEntity(world, x, baseY, z);
            if (temp != null) {
                data.setDirection(temp, dir);
                data.setType(temp, type);
            }
        }
    }

    /**
     * Called if cover and decoration checks have been performed but
     * returned no changes.
     */
    protected void postOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, ActionResult actionResult)
    {
        changeState(TE, TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord, true);
        actionResult.setAltered().setNoSound();
    }

    /**
     * Toggles state of block.
     * <p>
     * Will start at bottom of column and finish at the top. If propagate
     * is true, will also notify neighbor blocks of state change.
     *
     * @param TE the {@link TEBase}
     * @param world the {@link World}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param propagate notify adjacent garage doors
     */
    private void changeState(TEBase TE, World world, int x, int y, int z, boolean propagate)
    {
        int state = data.isOpen(TE) ? data.STATE_CLOSED : data.STATE_OPEN;

        if (propagate) {

            /* Propagate at level of top garage door only. */
            int topY = data.getTopmost(world, x, y, z).yCoord;
            int propX = x;
            int propZ = z;

            /* Propagate in first direction. */

            ForgeDirection dir = data.getDirection(TE).getRotation(ForgeDirection.UP);
            do {
                propX += dir.offsetX;
                propZ += dir.offsetZ;
                TEBase temp = (TEBase) world.getTileEntity(propX, topY, propZ);
                if (temp != null && data.getDirection(temp).equals(data.getDirection(TE))) {
                    changeState(temp, world, propX, topY, propZ, false);
                }
            } while (world.getBlock(propX, topY, propZ).equals(this));

            /* Propagate in second direction. */

            dir = dir.getOpposite();
            propX = x;
            propZ = z;
            do {
                propX += dir.offsetX;
                propZ += dir.offsetZ;
                TEBase temp = (TEBase) world.getTileEntity(propX, topY, propZ);
                if (temp != null && data.getDirection(temp).equals(data.getDirection(TE))) {
                    changeState(temp, world, propX, topY, propZ, false);
                }
            } while (world.getBlock(propX, topY, propZ).equals(this));

        }

        y = data.getBottommost(world, x, y, z).yCoord;
        do {
            TEBase temp = getTileEntity(world, x, y, z);
            if (temp != null) {
                data.setState(temp, state, data.isTopmost(temp));
            }
        } while (world.getBlock(x, ++y, z).equals(this));
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
            if (TE != null) {

                if (!(canPlaceBlockOnSide(world, x, y, z, 0) || world.getBlock(x, y + 1, z).equals(this))) {
                    destroy(world, x, y, z);
                    // TODO: Fix destroying topmost dropping two doors
                }

                /* Set block open or closed. */

                if (block != null && block.canProvidePower() && world.isBlockIndirectlyGettingPowered(x, y, z) != data.isOpen(TE)) {
                    changeState(TE, world, x, y, z, true);
                }

            }

        }

        super.onNeighborBlockChange(world, x, y, z, block);
    }

    /**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     *
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     *
     * @param world The current world
     * @param player The player damaging the block, may be null
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param willHarvest True if Block.harvestBlock will be called after this, if the return in true.
     *        Can be useful to delay the destruction of tile entities till after harvestBlock
     * @return True if the block is actually destroyed.
     */
    public boolean removedByPlayer(World world, EntityPlayer entityPlayer, int x, int y, int z, boolean willHarvest)
    {
        if (!world.isRemote) {
            destroy(world, x, y, z);
        }

        return super.removedByPlayer(world, entityPlayer, x, y, z);
    }

    /**
     * This returns a complete list of items dropped from this block.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     * @return A ArrayList containing all items this block drops
     */
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            if (data.isTopmost(TE)) {
                ret.add(createStackedBlock(0));
            }
        }

        return ret;
    }

    @Override
    /**
     * Checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        return world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        if (super.canPlaceBlockAt(world, x, y, z)) {
            return canPlaceBlockOnSide(world, x, y, z, 0) || world.getBlock(x, y + 1, z).equals(this);
        } else {
            return false;
        }
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        /* Set direction based on player facing only. */

        ForgeDirection facing = EntityLivingUtil.getFacing(entityLiving).getOpposite();
        data.setDirection(TE, facing);

        create(TE, world, x, y - 1, z);

        /* Match type above or below block. */

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (world.getBlock(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ).equals(this)) {
                TEBase TE_adj = (TEBase) world.getTileEntity(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ);
                data.setType(TE, data.getType(TE_adj));
            }
        }

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersGarageDoorRenderID;
    }

}
