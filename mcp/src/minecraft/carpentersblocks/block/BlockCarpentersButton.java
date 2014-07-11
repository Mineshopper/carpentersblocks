package carpentersblocks.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Button;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.handler.ChatHandler;
import carpentersblocks.util.registry.BlockRegistry;

public class BlockCarpentersButton extends BlockCoverable {

    public BlockCarpentersButton(int blockID, Material material)
    {
        super(blockID, material);
    }

    @Override
    /**
     * Alters polarity.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int polarity = Button.getPolarity(TE) == Button.POLARITY_POSITIVE ? Button.POLARITY_NEGATIVE : Button.POLARITY_POSITIVE;

        Button.setPolarity(TE, polarity);
        notifySideNeighbor(TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord, ForgeDirection.OPPOSITES[Button.getFacing(TE).ordinal()]);

        switch (polarity) {
            case Button.POLARITY_POSITIVE:
                ChatHandler.sendMessageToPlayer("message.polarity_pos.name", entityPlayer);
                break;
            case Button.POLARITY_NEGATIVE:
                ChatHandler.sendMessageToPlayer("message.polarity_neg.name", entityPlayer);
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
     * How many world ticks before ticking
     */
    public int tickRate(World world)
    {
        return 20;
    }

    @Override
    /**
     * Checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        Block block = Block.blocksList[world.getBlockId(x - dir.offsetX, y, z - dir.offsetZ)];

        return block == null ? false : block.isBlockSolidOnSide(world, x - dir.offsetX, y, z - dir.offsetZ, dir);
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
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            Button.setFacing(TE, world.getBlockMetadata(x, y, z));
            Button.setReady(TE);
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
        if (!world.isRemote) {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {

                if (Button.isReady(TE)) {

                    ForgeDirection dir = Button.getFacing(TE);

                    if (!canPlaceBlockOnSide(world, x, y, z, dir.ordinal())) {
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
        TEBase TE = getTileEntity(world, x, y, z);

        ForgeDirection dir = Button.getFacing(TE);

        float depth = isDepressed(TE) ? 0.0625F : 0.125F;

        switch (dir) {
            case NORTH:
                setBlockBounds(0.5F - 0.1875F, 0.375F, 1.0F - depth, 0.5F + 0.1875F, 0.625F, 1.0F);
                break;
            case SOUTH:
                setBlockBounds(0.5F - 0.1875F, 0.375F, 0.0F, 0.5F + 0.1875F, 0.625F, depth);
                break;
            case WEST:
                setBlockBounds(1.0F - depth, 0.375F, 0.5F - 0.1875F, 1.0F, 0.625F, 0.5F + 0.1875F);
                break;
            case EAST:
                setBlockBounds(0.0F, 0.375F, 0.5F - 0.1875F, depth, 0.625F, 0.5F + 0.1875F);
                break;
            default: {}
        }
    }

    @Override
    /**
     * Called upon block activation (right click on the block.)
     */
    protected void postOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, List<Boolean> altered, List<Boolean> decInv)
    {
        if (!isDepressed(TE)) {

            World world = TE.getWorldObj();
            ForgeDirection facing = Button.getFacing(TE);
            Button.setState(TE, Button.STATE_ON, true);
            notifySideNeighbor(world, TE.xCoord, TE.yCoord, TE.zCoord, facing.ordinal());
            world.scheduleBlockUpdate(TE.xCoord, TE.yCoord, TE.zCoord, blockID, tickRate(world));
            altered.add(true);

        }
    }

    @Override
    /**
     * Ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, int blockID, int metadata)
    {
        TEBase TE = getSimpleTileEntity(world, x, y, z);

        if (TE != null) {
            if (isDepressed(TE)) {
                notifySideNeighbor(world, x, y, z, Button.getFacing(TE).ordinal());
            }
        }

        super.breakBlock(world, x, y, z, blockID, metadata);
    }

    /**
     * Returns whether button is in depressed state
     */
    private boolean isDepressed(TEBase TE)
    {
        return Button.getState(TE) == Button.STATE_ON;
    }

    @Override
    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            return getPowerSupply((TEBase) world.getBlockTileEntity(x, y, z));
        }

        return 0;
    }

    @Override
    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            return Button.getFacing(TE).ordinal() == side ? getPowerSupply(TE) : 0;
        }

        return 0;
    }

    /**
     * Returns power level (0 or 15)
     */
    private int getPowerSupply(TEBase TE)
    {
        int polarity = Button.getPolarity(TE);

        if (isDepressed(TE)) {
            return polarity == Button.POLARITY_POSITIVE ? 15 : 0;
        } else {
            return polarity == Button.POLARITY_NEGATIVE ? 15 : 0;
        }
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
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (!world.isRemote && TE != null) {
            Button.setState(TE, Button.STATE_OFF, true);
            notifySideNeighbor(world, x, y, z, Button.getFacing(TE).ordinal());
        }
    }

    private void notifySideNeighbor(World world, int x, int y, int z, int side)
    {
        world.notifyBlocksOfNeighborChange(x, y, z, blockID);
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        world.notifyBlocksOfNeighborChange(x - dir.offsetX, y, z - dir.offsetZ, blockID);
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersButtonRenderID;
    }

}
