package carpentersblocks.block;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Button;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import carpentersblocks.util.handler.IconHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersButton extends BlockBase
{

	public BlockCarpentersButton(int blockID)
	{
		super(blockID, Material.circuits);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersButton");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		setTickRandomly(true);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister)
    {
		this.blockIcon = IconHandler.icon_generic;
    }

	@Override
	/**
	 * Alters polarity.
	 */
	protected boolean onHammerLeftClick(TECarpentersBlock TE, EntityPlayer entityPlayer)
	{
		int data = BlockProperties.getData(TE);
		int polarity = Button.getPolarity(data) == Button.POLARITY_POSITIVE ? Button.POLARITY_NEGATIVE : Button.POLARITY_POSITIVE;

		if (!TE.worldObj.isRemote) {
			Button.setPolarity(TE, polarity);
			notifySideNeighbor(TE.worldObj, TE.xCoord, TE.yCoord, TE.zCoord, Button.getType(data));
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
	 * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
	 */
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
	{
		ForgeDirection dir = ForgeDirection.getOrientation(side);
		return (dir == NORTH && world.isBlockSolidOnSide(x, y, z + 1, NORTH)) ||
				(dir == SOUTH && world.isBlockSolidOnSide(x, y, z - 1, SOUTH)) ||
				(dir == WEST  && world.isBlockSolidOnSide(x + 1, y, z, WEST)) ||
				(dir == EAST  && world.isBlockSolidOnSide(x - 1, y, z, EAST));
	}

	@Override
	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return (world.isBlockSolidOnSide(x - 1, y, z, EAST)) ||
				(world.isBlockSolidOnSide(x + 1, y, z, WEST)) ||
				(world.isBlockSolidOnSide(x, y, z - 1, SOUTH)) ||
				(world.isBlockSolidOnSide(x, y, z + 1, NORTH));
	}

	@Override
	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		metadata &= 7;

		ForgeDirection dir = ForgeDirection.getOrientation(side);

		if (dir == NORTH && world.isBlockSolidOnSide(x, y, z + 1, NORTH)) {
			metadata = 4;
		} else if (dir == SOUTH && world.isBlockSolidOnSide(x, y, z - 1, SOUTH)) {
			metadata = 3;
		} else if (dir == WEST && world.isBlockSolidOnSide(x + 1, y, z, WEST)) {
			metadata = 2;
		} else if (dir == EAST && world.isBlockSolidOnSide(x - 1, y, z, EAST)) {
			metadata = 1;
		} else {
			metadata = getOrientation(world, x, y, z);
		}

		return metadata;
	}

	@Override
	/**
	 * Called when the block is placed in the world.
	 */
	public void auxiliaryOnBlockPlacedBy(TECarpentersBlock TE, World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
	{
		Button.setType(TE, world.getBlockMetadata(x, y, z));
	}

	/**
	 * Get side which this button is facing.
	 */
	private int getOrientation(World world, int x, int y, int z)
	{
		if (world.isBlockSolidOnSide(x - 1, y, z, EAST)) return 1;
		if (world.isBlockSolidOnSide(x + 1, y, z, WEST)) return 2;
		if (world.isBlockSolidOnSide(x, y, z - 1, SOUTH)) return 3;
		if (world.isBlockSolidOnSide(x, y, z + 1, NORTH)) return 4;
		return 1;
	}

	@Override
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	protected void auxiliaryOnNeighborBlockChange(TECarpentersBlock TE, World world, int x, int y, int z, int blockID)
	{
		int data = BlockProperties.getData(TE);
		int type = Button.getType(data);

		boolean dropBlock = true;

		if (canPlaceBlockAt(world, x, y, z))
		{
			if (world.isBlockSolidOnSide(x - 1, y, z, EAST) && type == 1)
				dropBlock = false;

			if (world.isBlockSolidOnSide(x + 1, y, z, WEST) && type == 2)
				dropBlock = false;

			if (world.isBlockSolidOnSide(x, y, z - 1, SOUTH) && type == 3)
				dropBlock = false;

			if (world.isBlockSolidOnSide(x, y, z + 1, NORTH) && type == 4)
				dropBlock = false;
		}

		if (dropBlock)
		{
			dropBlockAsItem(world, x, y, z, type, 0);
			world.setBlockToAir(x, y, z);
		}
	}

	@Override
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

		int data = BlockProperties.getData(TE);
		int type = Button.getType(data);

		float depth = isDepressed(TE) ? 0.0625F : 0.125F;

		switch (type) {
		case 1:
			setBlockBounds(0.0F, 0.375F, 0.5F - 0.1875F, depth, 0.625F, 0.5F + 0.1875F);
			break;
		case 2:
			setBlockBounds(1.0F - depth, 0.375F, 0.5F - 0.1875F, 1.0F, 0.625F, 0.5F + 0.1875F);
			break;
		case 3:
			setBlockBounds(0.5F - 0.1875F, 0.375F, 0.0F, 0.5F + 0.1875F, 0.625F, depth);
			break;
		case 4:
			setBlockBounds(0.5F - 0.1875F, 0.375F, 1.0F - depth, 0.5F + 0.1875F, 0.625F, 1.0F);
			break;
		}
	}

	@Override
	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean auxiliaryOnBlockActivated(TECarpentersBlock TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int data = BlockProperties.getData(TE);
		int type = Button.getType(data);

		if (!isDepressed(TE))
		{
			Button.setState(TE, Button.STATE_ON, true);
			notifySideNeighbor(world, x, y, z, type);
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
		int data = BlockProperties.getData(TE);
		int type = Button.getType(data);

		if (isDepressed(TE))
			notifySideNeighbor(world, x, y, z, type);
	}

	/**
	 * Returns whether button is in depressed state
	 */
	private boolean isDepressed(TECarpentersBlock TE)
	{
		int data = BlockProperties.getData(TE);
		return Button.getState(data) == Button.STATE_ON;
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

		int data = BlockProperties.getData(TE);

		return getPowerSupply(TE, data);
	}

	@Override
	/**
	 * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
	 * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
	{
		TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

		int data = BlockProperties.getData(TE);
		int type = Button.getType(data);

		return type == 5 && side == 1 ? getPowerSupply(TE, data) : (type == 4 && side == 2 ? getPowerSupply(TE, data) : (type == 3 && side == 3 ? getPowerSupply(TE, data) : (type == 2 && side == 4 ? getPowerSupply(TE, data) : (type == 1 && side == 5 ? getPowerSupply(TE, data) : 0))));
	}

	/**
	 * Returns power level (0 or 15)
	 */
	private int getPowerSupply(TECarpentersBlock TE, int data)
	{
		int polarity = Button.getPolarity(data);

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

			int data = BlockProperties.getData(TE);
			int type = Button.getType(data);

			Button.setState(TE, Button.STATE_OFF, true);
			notifySideNeighbor(world, x, y, z, type);
		}
	}

	private void notifySideNeighbor(World world, int x, int y, int z, int side)
	{
		world.notifyBlocksOfNeighborChange(x, y, z, blockID);

		switch (side) {
		case 1:
			world.notifyBlocksOfNeighborChange(x - 1, y, z, blockID);
			break;
		case 2:
			world.notifyBlocksOfNeighborChange(x + 1, y, z, blockID);
			break;
		case 3:
			world.notifyBlocksOfNeighborChange(x, y, z - 1, blockID);
			break;
		case 4:
			world.notifyBlocksOfNeighborChange(x, y, z + 1, blockID);
			break;
		default:
			world.notifyBlocksOfNeighborChange(x, y - 1, z, blockID);
			break;
		}
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
