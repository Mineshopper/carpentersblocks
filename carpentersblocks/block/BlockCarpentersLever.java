package carpentersblocks.block;

import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Lever;
import carpentersblocks.data.Lever.Axis;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersLever extends BlockBase {

    public BlockCarpentersLever(int blockID)
    {
        super(blockID, Material.circuits);
        setHardness(0.2F);
        setUnlocalizedName("blockCarpentersLever");
        setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
        setTextureName("carpentersblocks:general/solid");
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister)
    {
        IconRegistry.icon_lever = iconRegister.registerIcon("carpentersblocks:lever/lever");

        super.registerIcons(iconRegister);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Returns the icon on the side given the block metadata.
     */
    public Icon getIcon(int side, int metadata)
    {
        return IconRegistry.icon_lever;
    }

    @Override
    /**
     * Alters polarity.
     * Handled differently for Levers since type is split into sub-components.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        BlockProperties.getData(TE);
        int polarity = Lever.getPolarity(TE) == Lever.POLARITY_POSITIVE ? Lever.POLARITY_NEGATIVE : Lever.POLARITY_POSITIVE;

        Lever.setPolarity(TE, polarity);
        notifySideNeighbor(TE.worldObj, TE.xCoord, TE.yCoord, TE.zCoord, ForgeDirection.OPPOSITES[Lever.getFacing(TE).ordinal()]);

        switch (polarity) {
        case Lever.POLARITY_POSITIVE:
            entityPlayer.addChatMessage("message.polarity_pos.name");
            break;
        case Lever.POLARITY_NEGATIVE:
            entityPlayer.addChatMessage("message.polarity_neg.name");
        }

        return true;
    }

    @Override
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side);

        return world.isBlockSolidOnSide(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ, dir);
    }

    @Override
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        return side;
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);
        int facing = world.getBlockMetadata(x, y, z);

        Lever.setFacing(TE, facing);
        Lever.setReady(TE);

        /* For vertical facings, set axis rotation. */
        if (facing < 2)
        {
            ForgeDirection dir = BlockProperties.getDirectionFromFacing(BlockProperties.getEntityFacing(entityLiving));

            if (dir.equals(NORTH) || dir.equals(SOUTH)) {
                Lever.setAxis(TE, Axis.Z);
            }
        } else {
            if (facing == NORTH.ordinal() || facing == SOUTH.ordinal()) {
                Lever.setAxis(TE, Axis.Z);
            }
        }

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        if (!world.isRemote)
        {
            TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

            if (TE != null)
            {
                if (Lever.isReady(TE))
                {
                    ForgeDirection facing = Lever.getFacing(TE);

                    if (!canPlaceBlockOnSide(world, x, y, z, facing.ordinal())) {
                        dropBlockAsItem(world, x, y, z, 0, 0);
                        world.setBlockToAir(x, y, z);
                    }
                }
            }
        }

        super.onNeighborBlockChange(world, x, y, z, blockID);
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = (TEBase)world.getBlockTileEntity(x, y, z);

        ForgeDirection facing = Lever.getFacing(TE);
        Axis axis = Lever.getAxis(TE);

        float offset = 0.1875F;

        switch (facing) {
        case DOWN:
            if (axis.equals(Axis.X)) {
                setBlockBounds(0.2F, 1.0F - offset, 0.5F - offset, 0.8F, 1.0F, 0.5F + offset);
            } else {
                setBlockBounds(0.5F - offset, 1.0F - offset, 0.2F, 0.5F + offset, 1.0F, 0.8F);
            }
            break;
        case UP:
            if (axis.equals(Axis.X)) {
                setBlockBounds(0.2F, 0.0F, 0.5F - offset, 0.8F, offset, 0.5F + offset);
            } else {
                setBlockBounds(0.5F - offset, 0.0F, 0.2F, 0.5F + offset, offset, 0.8F);
            }
            break;
        case NORTH:
            setBlockBounds(0.5F - offset, 0.2F, 1.0F - offset, 0.5F + offset, 0.8F, 1.0F);
            break;
        case SOUTH:
            setBlockBounds(0.5F - offset, 0.2F, 0.0F, 0.5F + offset, 0.8F, offset);
            break;
        case WEST:
            setBlockBounds(1.0F - offset, 0.2F, 0.5F - offset, 1.0F, 0.8F, 0.5F + offset);
            break;
        case EAST:
            setBlockBounds(0.0F, 0.2F, 0.5F - offset, offset, 0.8F, 0.5F + offset);
            break;
        default: {}
        }

    }

    @Override
    /**
     * Called upon block activation.
     */
    public boolean[] postOnBlockActivated(TEBase TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        ForgeDirection facing = Lever.getFacing(TE);

        Lever.setState(TE, isActive(TE) ? Lever.STATE_OFF : Lever.STATE_ON, true);

        world.notifyBlocksOfNeighborChange(x, y, z, blockID);
        notifySideNeighbor(world, x, y, z, facing.ordinal());

        boolean[] result = { true, false };
        return result;
    }

    /**
     * Returns whether lever is in active state
     */
    private boolean isActive(TEBase TE)
    {
        return Lever.getState(TE) == Lever.STATE_ON;
    }

    @Override
    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        return getPowerSupply(TE);
    }

    @Override
    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        return Lever.getFacing(TE).ordinal() == side ? getPowerSupply(TE) : 0;
    }

    /**
     * Returns power level (0 or 15)
     */
    private int getPowerSupply(TEBase TE)
    {
        if (isActive(TE)) {
            return Lever.getPolarity(TE) == Lever.POLARITY_POSITIVE ? 15 : 0;
        } else {
            return Lever.getPolarity(TE) == Lever.POLARITY_NEGATIVE ? 15 : 0;
        }
    }

    private void notifySideNeighbor(World world, int x, int y, int z, int side)
    {
        world.notifyBlocksOfNeighborChange(x, y, z, blockID);

        switch (side) {
        case 0:
            world.notifyBlocksOfNeighborChange(x, y + 1, z, blockID);
            break;
        case 1:
            world.notifyBlocksOfNeighborChange(x, y - 1, z, blockID);
            break;
        case 2:
            world.notifyBlocksOfNeighborChange(x, y, z + 1, blockID);
            break;
        case 3:
            world.notifyBlocksOfNeighborChange(x, y, z - 1, blockID);
            break;
        case 4:
            world.notifyBlocksOfNeighborChange(x + 1, y, z, blockID);
            break;
        case 5:
            world.notifyBlocksOfNeighborChange(x - 1, y, z, blockID);
            break;
        }
    }

    @Override
    /**
     * Ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, int par5, int metadata)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        if (TE != null)
        {
            if (isActive(TE)) {
                world.notifyBlocksOfNeighborChange(x, y, z, blockID);
                notifySideNeighbor(world, x, y, z, Lever.getFacing(TE).ordinal());
            }
        }

        super.breakBlock(world, x, y, z, par5, metadata);
    }

    @Override
    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersLeverRenderID;
    }

}
