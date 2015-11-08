package com.carpentersblocks.block;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.data.GarageDoor;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.tileentity.TECarpentersGarageDoor;
import com.carpentersblocks.util.EntityLivingUtil;
import com.carpentersblocks.util.handler.ChatHandler;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.FeatureRegistry;
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

        ArrayList<TEBase> pieces = data.getConnectingDoors(TE);
        for (TEBase piece : pieces) {
            data.setType(piece, temp);
        }

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

            ArrayList<TEBase> pieces = data.getConnectingDoors(TE);
            for (TEBase piece : pieces) {
                data.setRigidity(piece, rigidity);
            }
        } else {
            ArrayList<TEBase> pieces = data.getConnectingDoors(TE);
            for (TEBase piece : pieces) {
                data.setType(piece, temp);
            }
        }

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
     * Location sensitive version of getExplosionRestance
     *
     * @param par1Entity The entity that caused the explosion
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param explosionX Explosion source X Position
     * @param explosionY Explosion source X Position
     * @param explosionZ Explosion source X Position
     * @return The amount of the explosion absorbed.
     */
    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        if (TE != null) {
            if (data.isOpen(TE) && !data.isHost(TE)) {
                return Blocks.bedrock.getExplosionResistance(entity);
            }
        }

        return getExplosionResistance(entity);
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion)
    {
        // Destroy entire stack of doors
        destroy(world, x, y, z, true);
    }

    private void destroyInDirection(World world, int x, int y, int z, boolean doDrop, ForgeDirection dir)
    {
        if (y < 0 || y > world.getHeight()) {
            return;
        }

        Block block = world.getBlock(x, y, z);

        if (block.equals(this)) {
            boolean dropBlock = false;
            if (doDrop) {
                TEBase temp = getTileEntity(world, x, y, z);
                if (temp != null && data.isHost(temp)) {
                    dropBlock = true;
                }
            }
            destroyBlock(world, x, y, z, dropBlock);
        } else if (!block.equals(Blocks.air)) {
            return;
        }

        destroyInDirection(world, x, y + dir.offsetY, z, doDrop, dir);
    }

    /**
     * Will destroy a column of garage doors.
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
            destroyInDirection(world, x, y, z, doDrop, ForgeDirection.UP);
            destroyInDirection(world, x, y - 1, z, false, ForgeDirection.DOWN);
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
    private void create(TEBase TE, World world, int x, int y, int z)
    {
        for (; canPlaceBlockAt(world, x, y, z); --y) {
            world.setBlock(x, y, z, this);
            TEBase temp = getTileEntity(world, x, y, z);
            if (temp != null) {
                data.replicate(TE, temp);
            }
        }
    }

    /**
     * Allows a tile entity called during block activation to be changed before
     * altering attributes like cover, dye, overlay, etc.
     * <p>
     * Primarily offered for the garage door, when open, to swap the top piece
     * with the bottom piece for consistency.
     *
     * @param  TE the originating {@link TEBase}
     * @return a swapped in {@link TEBase}, or the passed in {@link TEBase}
     */
    @Override
    protected TEBase getTileEntityForBlockActivation(TEBase TE)
    {
        return data.isOpen(TE) ? data.getBottommost(TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord) : TE;
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

            ArrayList<TEBase> pieces = data.getConnectingDoors(TE);
            for (TEBase piece : pieces) {
                data.setState(piece, state);
            }

            actionResult.setAltered().setNoSound();
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
            if (TE != null) {
                if (data.isHost(TE) && !(canPlaceBlockOnSide(world, x, y, z, 0) || world.getBlock(x, y + 1, z).equals(this))) {
                    destroy(world, x, y, z, true);
                } else {
                    // Check for new door state (open or closed)
                    int powerState = world.isBlockIndirectlyGettingPowered(x, y, z) ? GarageDoor.STATE_OPEN : GarageDoor.STATE_CLOSED;
                    if (block != null && block.canProvidePower() && powerState != data.getState(TE)) {
                        int old_state = data.getState(TE);
                        int state = old_state;

                        if (data.isOpen(TE)) {
                            // Check if a garage door piece is still powered
                            boolean garageHasPower = false;
                            ArrayList<TEBase> pieces = data.getConnectingDoors(TE);
                            for (TEBase piece : pieces) {
                                if (world.isBlockIndirectlyGettingPowered(piece.xCoord, piece.yCoord, piece.zCoord)) {
                                    garageHasPower = true;
                                    break;
                                }
                            }
                            if (!garageHasPower) {
                                state = data.STATE_CLOSED;
                            }
                        } else {
                            state = data.STATE_OPEN;
                        }

                        if (state != old_state) {
                            data.setState(TE, state);

                            ArrayList<TEBase> pieces = data.getConnectingDoors(TE);
                            for (TEBase piece : pieces) {
                                data.setState(piece, state);
                            }
                        }
                    }
                }
            }

            // Create new door pieces below if bottom block turned to air
            if (FeatureRegistry.enableGarageDoorVoidFill) {
                if (block != this && world.getBlock(x, y - 1, z).equals(Blocks.air)) {
                    create(TE, world, x, y - 1, z);
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
            list.add(getItemDrop(world, metadata));
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
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

        TEBase TE = getTileEntity(world, x, y, z);

        // Set direction based on player facing
        ForgeDirection facing = EntityLivingUtil.getFacing(entityLiving).getOpposite();
        data.setDirection(TE, facing);
        data.setHost(TE);

        // Find a nearby door to replicate properties
        TEBase temp = data.findReferencePiece(world, x, y, z, facing);
        if (temp != null) {
            data.replicate(temp, TE);
        }

        // Create remainder of stack below host
        create(TE, world, x, y - 1, z);
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersGarageDoorRenderID;
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
        // to correctly support archimedes' ships mod:
        // if Axis is DOWN, block rotates to the left, north -> west -> south -> east
        // if Axis is UP, block rotates to the right:  north -> east -> south -> west

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TEBase)
        {
            TEBase cbTile = (TEBase)tile;
            int dataAngle = (cbTile.getData() & 0x70) >> 4;
            ForgeDirection direction = ForgeDirection.getOrientation(dataAngle);
            int newAngle = 0;

            switch (axis)
            {
                case UP:
                {
                    switch (direction)
                    {
                        case WEST:{newAngle = (cbTile.getData() & ~0x70) | (ForgeDirection.NORTH.ordinal() << 4); break;}
                        case NORTH:{newAngle = (cbTile.getData() & ~0x70) | (ForgeDirection.EAST.ordinal() << 4); break;}
                        case EAST:{newAngle = (cbTile.getData() & ~0x70) | (ForgeDirection.SOUTH.ordinal() << 4); break;}
                        case SOUTH:{newAngle = (cbTile.getData() & ~0x70) | (ForgeDirection.WEST.ordinal() << 4); break;}
                        default: break;
                    }
                    cbTile.setData(newAngle);
                    return true;
                }
                case DOWN:
                {
                    switch (direction)
                    {
                        case WEST:{newAngle = (cbTile.getData() & ~0x70) | (ForgeDirection.SOUTH.ordinal() << 4); break;}
                        case NORTH:{newAngle = (cbTile.getData() & ~0x70) | (ForgeDirection.EAST.ordinal() << 4); break;}
                        case EAST:{newAngle = (cbTile.getData() & ~0x70) | (ForgeDirection.NORTH.ordinal() << 4); break;}
                        case SOUTH:{newAngle = (cbTile.getData() & ~0x70) | (ForgeDirection.WEST.ordinal() << 4); break;}
                        default: break;
                    }
                    cbTile.setData(newAngle);
                    return true;
                }
                default: return false;
            }
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TECarpentersGarageDoor();
    }

}
