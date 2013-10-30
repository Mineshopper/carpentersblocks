package carpentersblocks.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Button;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.handler.BlockHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BlockCarpentersButton extends BlockBase
{

	public BlockCarpentersButton(int blockID)
	{
		super(blockID, Material.circuits);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersButton");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		setTickRandomly(true);
		setTextureName("carpentersblocks:general/generic");
	}

	@Override
	/**
	 * Alters polarity.
	 */
	protected boolean onHammerLeftClick(TECarpentersBlock TE, EntityPlayer entityPlayer)
	{
		int polarity = Button.getPolarity(TE) == Button.POLARITY_POSITIVE ? Button.POLARITY_NEGATIVE : Button.POLARITY_POSITIVE;

		if (!TE.worldObj.isRemote) {
			Button.setPolarity(TE, polarity);
			notifySideNeighbor(TE.worldObj, TE.xCoord, TE.yCoord, TE.zCoord, ForgeDirection.OPPOSITES[Button.getFacing(TE).ordinal()]);
		} else {
			switch (polarity) {
			case Button.POLARITY_POSITIVE:
				entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.polarity_pos.name"));
				break;
			case Button.POLARITY_NEGATIVE:
				entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.polarity_neg.name"));
			}
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

		return world.isBlockSolidOnSide(x - dir.offsetX, y, z - dir.offsetZ, dir);
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
	public void auxiliaryOnBlockPlacedBy(TECarpentersBlock TE, World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		Button.setFacing(TE, world.getBlockMetadata(x, y, z));
	}
	
	@Override
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	protected void auxiliaryOnNeighborBlockChange(TECarpentersBlock TE, World world, int x, int y, int z, int blockID)
	{
		ForgeDirection dir = Button.getFacing(TE);

		/*
		 * When the block is placed in the world, the SERVER
		 * hasn't caught up setting TE parameters before neighbor
		 * blocks have a chance to notify this block of updates.
		 * To correct this, the block starts with a facing out of
		 * range in order to detect this anomaly.
		 */
		if (dir != ForgeDirection.DOWN && !canPlaceBlockOnSide(world, x, y, z, dir.ordinal())) {
			dropBlockAsItem(world, x, y, z, 0, 0);
			world.setBlockToAir(x, y, z);
		}
	}

	@Override
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

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
	public boolean auxiliaryOnBlockActivated(TECarpentersBlock TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		ForgeDirection dir = Button.getFacing(TE);

		if (!isDepressed(TE))
		{
			Button.setState(TE, Button.STATE_ON, true);
			notifySideNeighbor(world, x, y, z, dir.ordinal());
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate(world));
			return true;
		}

		return false;
	}

	@Override
	/**
	 * Ejects contained items into the world, and notifies neighbours of an update, as appropriate
	 */
	public void auxiliaryBreakBlock(TECarpentersBlock TE, World world, int x, int y, int z, int par5, int metadata)
	{
		ForgeDirection dir = Button.getFacing(TE);

		if (isDepressed(TE)) {
			notifySideNeighbor(world, x, y, z, dir.ordinal());
		}
	}

	/**
	 * Returns whether button is in depressed state
	 */
	private boolean isDepressed(TECarpentersBlock TE)
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
		TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

		return getPowerSupply(TE);
	}

	@Override
	/**
	 * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
	 * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
	{
		TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

		ForgeDirection dir = Button.getFacing(TE);

		return dir.ordinal() == side ? getPowerSupply(TE) : 0;
	}

	/**
	 * Returns power level (0 or 15)
	 */
	private int getPowerSupply(TECarpentersBlock TE)
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
		if (!world.isRemote)
		{
			TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

			ForgeDirection dir = Button.getFacing(TE);

			Button.setState(TE, Button.STATE_OFF, true);
			notifySideNeighbor(world, x, y, z, dir.ordinal());
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
		return BlockHandler.carpentersButtonRenderID;
	}

}