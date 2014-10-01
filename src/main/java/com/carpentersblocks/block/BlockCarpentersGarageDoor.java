package com.carpentersblocks.block;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.data.GarageDoor;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.EntityLivingUtil;
import com.carpentersblocks.util.handler.ChatHandler;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersGarageDoor extends BlockCoverable {

    public final static String type[] = { "default", "glassTop", "glass", "siding", "hidden" };
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
        IconRegistry.icon_garage_glass     = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "garagedoor/glass");
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
        propagateChanges(TE, TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord, true);
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

        if (entityPlayer.isSneaking()) {
            int rigidity = data.isRigid(TE) ? GarageDoor.DOOR_NONRIGID : GarageDoor.DOOR_RIGID;

            switch (rigidity) {
                case GarageDoor.DOOR_NONRIGID:
                    ChatHandler.sendMessageToPlayer("message.activation_wood.name", entityPlayer);
                    break;
                case GarageDoor.DOOR_RIGID:
                    ChatHandler.sendMessageToPlayer("message.activation_iron.name", entityPlayer);
            }

            data.setRigidity(TE, rigidity);
        } else {
            data.setType(TE, temp);
        }

        propagateChanges(TE, TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord, true);
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
            float yMin = data.isHost(TE) && data.isOpen(TE) ? 0.5F : 0.0F;
            ForgeDirection dir = data.getDirection(TE);

            if (data.isVisible(TE)) {
                if (data.getType(TE) == GarageDoor.TYPE_HIDDEN) {
                    setBlockBounds(0.0F, yMin, 0.0F, 1.0F, 1.0F, 0.125F, dir);
                } else {
                    setBlockBounds(0.0F, yMin, 0.125F, 1.0F, 1.0F, 0.25F, dir);
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
    @Override
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
     * @param dropSource whether player destroyed topmost block
     */
    private void destroy(World world, int x, int y, int z, boolean doDrop)
    {
        if (!world.isRemote) {
            int baseY = data.getBottommost(world, x, y, z).yCoord;
            do {
                if (doDrop) {
                    TEBase temp = getTileEntity(world, x, baseY, z);
                    if (temp != null && data.isHost(temp)) {
                        dropBlockAsItem(world, x, y, z, createStackedBlock(0));
                    }
                }
                world.setBlockToAir(x, baseY++, z);
            } while (world.getBlock(x, baseY, z).equals(this));
        }
    }

    /**
     * Will create a column of garage doors beginning at given coordinates.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     */
    private void create(TEBase TE, World world, EntityLivingBase entity, int x, int y, int z)
    {
        ForgeDirection dir = data.getDirection(TE);
        int type = data.getType(TE);
        int state = data.getState(TE);
        int rigid = data.getRigidity(TE);

        for (int baseY = y; canPlaceBlockAt(world, x, baseY, z); --baseY) {
            world.setBlock(x, baseY, z, this);
            TEBase temp = getTileEntity(world, x, baseY, z);
            if (temp != null) {
                data.setDirection(temp, dir);
                data.setType(temp, type);
                data.setState(temp, state, false);
                data.setRigidity(temp, rigid);
                temp.setOwner(entity.getUniqueID());
            }
        }
    }

    /**
     * Called if cover and decoration checks have been performed but
     * returned no changes.
     */
    @Override
    protected void postOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, ActionResult actionResult)
    {
        if (!data.isRigid(TE)) {
            int state = data.getState(TE) == GarageDoor.STATE_OPEN ? GarageDoor.STATE_CLOSED : GarageDoor.STATE_OPEN;
            data.setState(TE, state, true);
            propagateChanges(TE, TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord, true);
            actionResult.setAltered().setNoSound();
        }
    }

    /**
     * Propagates block properties to all connecting blocks.
     * <p>
     * If notifyNeighbors is true, will notify all columns along a horizontal
     * line to update.  Only the first method call should set this to true.
     *
     * @param TE the host {@link TEBase}
     * @param world the {@link World}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param propagate notify adjacent garage doors
     */
    private void propagateChanges(TEBase TE, World world, int x, int y, int z, boolean notifyNeighbors)
    {
        int state = data.getState(TE);
        int type = data.getType(TE);
        int rigid = data.getRigidity(TE);

        if (notifyNeighbors) {

            /* Propagate at level of top garage door only. */

            ForgeDirection facing = data.getDirection(TE);
            int topY = data.getTopmost(world, x, y, z).yCoord;
            int propX = x;
            int propZ = z;

            /* Propagate in first direction. */

            ForgeDirection dir = facing.getRotation(ForgeDirection.UP);
            do {
                propX += dir.offsetX;
                propZ += dir.offsetZ;
                TEBase temp = getTileEntity(world, propX, topY, propZ);
                if (temp != null && data.getDirection(temp).equals(facing)) {
                    propagateChanges(TE, world, propX, topY, propZ, false);
                }
            } while (world.getBlock(propX, topY, propZ).equals(this));

            /* Propagate in second direction. */

            dir = dir.getOpposite();
            propX = x;
            propZ = z;
            do {
                propX += dir.offsetX;
                propZ += dir.offsetZ;
                TEBase temp = getTileEntity(world, propX, topY, propZ);
                if (temp != null && data.getDirection(temp).equals(facing)) {
                    propagateChanges(TE, world, propX, topY, propZ, false);
                }
            } while (world.getBlock(propX, topY, propZ).equals(this));

        }

        y = data.getBottommost(world, x, y, z).yCoord;
        do {
            TEBase temp = getTileEntity(world, x, y, z);
            if (temp != null) {
                data.setType(temp, type);
                data.setState(temp, state, data.isHost(temp) && data.getState(temp) != state);
                data.setRigidity(temp, rigid);
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

                // TODO: Try to resolve Buildcraft builder issues here
                if (data.isHost(TE) && !(canPlaceBlockOnSide(world, x, y, z, 0) || world.getBlock(x, y + 1, z).equals(this))) {
                    destroy(world, x, y, z, true);
                }

                /* Set block open or closed. */

                int powerState = world.isBlockIndirectlyGettingPowered(x, y, z) ? GarageDoor.STATE_OPEN : GarageDoor.STATE_CLOSED;
                if (block != null && block.canProvidePower() && powerState != data.getState(TE)) {
                    int state = data.isOpen(TE) ? GarageDoor.STATE_CLOSED : GarageDoor.STATE_OPEN;
                    data.setState(TE, state, false);
                    propagateChanges(TE, world, x, y, z, true);
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
    @Override
    public boolean removedByPlayer(World world, EntityPlayer entityPlayer, int x, int y, int z)
    {
        if (!suppressDestroyBlock(entityPlayer)) {
            destroy(world, x, y, z, !entityPlayer.capabilities.isCreativeMode);
            return false;
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
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();

        TEBase TE = getTileEntity(world, x, y, z);
        if (TE != null && data.isHost(TE)) {
            list.add(createStackedBlock(0));
        }

        list.addAll(super.getDrops(world, x, y, z, METADATA_DROP_ATTR_ONLY, fortune));
        return list;
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
    @Override
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
        data.setHost(TE);

        /* Match type above or below block. */

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (world.getBlock(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ).equals(this)) {
                TEBase TE_adj = (TEBase) world.getTileEntity(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ);
                data.setType(TE, data.getType(TE_adj));
            }
        }

        create(TE, world, entityLiving, x, y - 1, z);

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
