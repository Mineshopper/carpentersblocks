package carpentersblocks.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import carpentersblocks.data.Door;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import carpentersblocks.util.handler.ItemHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersDoor extends BlockBase
{

	public BlockCarpentersDoor(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersDoor");
		func_111022_d("carpentersblocks:general/generic");
	}

	@Override
	/**
	 * Alters hinge side.
	 */
	protected boolean onHammerLeftClick(TECarpentersBlock TE, EntityPlayer entityPlayer)
	{
		int data = BlockProperties.getData(TE);
		int hinge = Door.getHinge(data);

		setDoorHinge(TE, hinge == Door.HINGE_LEFT ? Door.HINGE_RIGHT : Door.HINGE_LEFT);

		return true;
	}

	@Override
	/**
	 * Alters door type and redstone behavior.
	 */
	protected boolean onHammerRightClick(TECarpentersBlock TE, EntityPlayer entityPlayer, int side)
	{
		int data = BlockProperties.getData(TE);

		if (!entityPlayer.isSneaking())
		{
			if (!TE.worldObj.isRemote)
			{
				int type = Door.getType(data);

				if (++type > 5)
					type = 0;

				setDoorType(TE, type);
			}

		} else {

			int rigidity = Door.getRigidity(data) == Door.HINGED_NONRIGID ? Door.HINGED_RIGID : Door.HINGED_NONRIGID;

			if (!TE.worldObj.isRemote) {
				setDoorRigidity(TE, rigidity);
			} else {
				switch (rigidity) {
				case Door.HINGED_NONRIGID:
					entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.activation_wood.name"));
					break;
				case Door.HINGED_RIGID:
					entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.activation_iron.name"));
				}
			}

		}

		return true;
	}

	@Override
	/**
	 * Opens or closes door on right click.
	 */
	public boolean auxiliaryOnBlockActivated(TECarpentersBlock TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int data = BlockProperties.getData(TE);

		if (!activationRequiresRedstone(TE))
			setDoorState(TE, Door.getState(data) == Door.STATE_OPEN ? Door.STATE_CLOSED : Door.STATE_OPEN);

		return true;
	}

	/**
	 * Returns whether door requires redstone activation.
	 */
	private boolean activationRequiresRedstone(TECarpentersBlock TE)
	{
		return Door.getRigidity(BlockProperties.getData(TE)) == Door.HINGED_RIGID;
	}

	/**
	 * Returns a list of door tile entities that make up either a single door or two connected double doors.
	 */
	private List<TECarpentersBlock> getDoorPieces(TECarpentersBlock TE)
	{
		ArrayList arrayList = new ArrayList();

		int data = BlockProperties.getData(TE);
		int facing = Door.getFacing(data);
		int hinge = Door.getHinge(data);
		int piece = Door.getPiece(data);

		boolean isTop = piece == Door.PIECE_TOP;

		/*
		 * Add calling TE and its matching piece
		 */
		arrayList.add(TE);
		arrayList.add(TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord - (isTop ? 1 : -1), TE.zCoord));

		/*
		 * Begin searching for and adding other neighboring pieces
		 */
		TECarpentersBlock TE_ZN = TE.worldObj.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord - 1) == blockID ? (TECarpentersBlock) TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord - 1) : null;
		TECarpentersBlock TE_ZP = TE.worldObj.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord + 1) == blockID ? (TECarpentersBlock) TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord + 1) : null;
		TECarpentersBlock TE_XN = TE.worldObj.getBlockId(TE.xCoord - 1, TE.yCoord, TE.zCoord) == blockID ? (TECarpentersBlock) TE.worldObj.getBlockTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord) : null;
		TECarpentersBlock TE_XP = TE.worldObj.getBlockId(TE.xCoord + 1, TE.yCoord, TE.zCoord) == blockID ? (TECarpentersBlock) TE.worldObj.getBlockTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord) : null;

		switch (facing)
		{
		case Door.FACING_XN:

			if (TE_ZN != null) {
				int data_ZN = BlockProperties.getData(TE_ZN);
				if (piece == Door.getPiece(data_ZN) && facing == Door.getFacing(data_ZN) && hinge == Door.HINGE_LEFT && Door.getHinge(data_ZN) == Door.HINGE_RIGHT)
				{
					arrayList.add(TE_ZN);
					arrayList.add(TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord - (isTop ? 1 : -1), TE.zCoord - 1));
				}
			}
			if (TE_ZP != null) {
				int data_ZP = BlockProperties.getData(TE_ZP);
				if (piece == Door.getPiece(data_ZP) && facing == Door.getFacing(data_ZP) && hinge == Door.HINGE_RIGHT && Door.getHinge(data_ZP) == Door.HINGE_LEFT)
				{
					arrayList.add(TE_ZP);
					arrayList.add(TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord - (isTop ? 1 : -1), TE.zCoord + 1));
				}
			}
			break;

		case Door.FACING_XP:

			if (TE_ZN != null) {
				int data_ZN = BlockProperties.getData(TE_ZN);
				if (piece == Door.getPiece(data_ZN) && facing == Door.getFacing(data_ZN) && hinge == Door.HINGE_RIGHT && Door.getHinge(data_ZN) == Door.HINGE_LEFT)
				{
					arrayList.add(TE_ZN);
					arrayList.add(TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord - (isTop ? 1 : -1), TE.zCoord - 1));
				}
			}
			if (TE_ZP != null) {
				int data_ZP = BlockProperties.getData(TE_ZP);
				if (piece == Door.getPiece(data_ZP) && facing == Door.getFacing(data_ZP) && hinge == Door.HINGE_LEFT && Door.getHinge(data_ZP) == Door.HINGE_RIGHT)
				{
					arrayList.add(TE_ZP);
					arrayList.add(TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord - (isTop ? 1 : -1), TE.zCoord + 1));
				}
			}
			break;

		case Door.FACING_ZN:
		{

			if (TE_XN != null) {
				int data_XN = BlockProperties.getData(TE_XN);
				if (piece == Door.getPiece(data_XN) && facing == Door.getFacing(data_XN) && hinge == Door.HINGE_RIGHT && Door.getHinge(data_XN) == Door.HINGE_LEFT)
				{
					arrayList.add(TE_XN);
					arrayList.add(TE.worldObj.getBlockTileEntity(TE.xCoord - 1, TE.yCoord - (isTop ? 1 : -1), TE.zCoord));
				}
			}
			if (TE_XP != null) {
				int data_XP = BlockProperties.getData(TE_XP);
				if (piece == Door.getPiece(data_XP) && facing == Door.getFacing(data_XP) && hinge == Door.HINGE_LEFT && Door.getHinge(data_XP) == Door.HINGE_RIGHT)
				{
					arrayList.add(TE_XP);
					arrayList.add(TE.worldObj.getBlockTileEntity(TE.xCoord + 1, TE.yCoord - (isTop ? 1 : -1), TE.zCoord));
				}
			}
			break;
		}
		case Door.FACING_ZP:

			if (TE_XN != null) {
				int data_XN = BlockProperties.getData(TE_XN);
				if (piece == Door.getPiece(data_XN) && facing == Door.getFacing(data_XN) && hinge == Door.HINGE_LEFT && Door.getHinge(data_XN) == Door.HINGE_RIGHT)
				{
					arrayList.add(TE_XN);
					arrayList.add(TE.worldObj.getBlockTileEntity(TE.xCoord - 1, TE.yCoord - (isTop ? 1 : -1), TE.zCoord));
				}
			}
			if (TE_XP != null) {
				int data_XP = BlockProperties.getData(TE_XP);
				if (piece == Door.getPiece(data_XP) && facing == Door.getFacing(data_XP) && hinge == Door.HINGE_RIGHT && Door.getHinge(data_XP) == Door.HINGE_LEFT)
				{
					arrayList.add(TE_XP);
					arrayList.add(TE.worldObj.getBlockTileEntity(TE.xCoord + 1, TE.yCoord - (isTop ? 1 : -1), TE.zCoord));
				}
			}
			break;

		}

		return arrayList;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns the bounding box of the wired rectangular prism to render.
	 */
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		TECarpentersBlock TE = world.getBlockId(x, y, z) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x, y, z) : null;

		if (TE != null)
			setBlockBoundsBasedOnState(world, x, y, z);

		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	 * cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		TECarpentersBlock TE = world.getBlockId(x, y, z) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x, y, z) : null;

		if (TE != null)
			setBlockBoundsBasedOnState(world, x, y, z);

		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);
		int data = BlockProperties.getData(TE);
		int facing = Door.getFacing(data);
		int hinge = Door.getHinge(data);
		boolean isOpen = Door.getState(data) == Door.STATE_OPEN;

		float	x_low = 0.0F,
				z_low = 0.0F,
				x_high = 1.0F,
				z_high = 1.0F;

		switch (facing)
		{
		case Door.FACING_XN:
			if (!isOpen)
				x_low = 0.8125F;
			else if (hinge == Door.HINGE_RIGHT)
				z_high = 0.1875F;
			else
				z_low = 0.8125F;
			break;
		case Door.FACING_XP:
			if (!isOpen)
				x_high = 0.1875F;
			else if (hinge == Door.HINGE_RIGHT)
				z_low = 0.8125F;
			else
				z_high = 0.1875F;
			break;
		case Door.FACING_ZN:
			if (!isOpen)
				z_low = 0.8125F;
			else if (hinge == Door.HINGE_RIGHT)
				x_low = 0.8125F;
			else
				x_high = 0.1875F;
			break;
		case Door.FACING_ZP:
			if (!isOpen)
				z_high = 0.1875F;
			else if (hinge == Door.HINGE_RIGHT)
				x_high = 0.1875F;
			else
				x_low = 0.8125F;
			break;
		}

		setBlockBounds(x_low, 0.0F, z_low, x_high, 1.0F, z_high);
	}

	/**
	 * Cycle door state.
	 * Will update all connecting door pieces.
	 */
	public void setDoorState(TECarpentersBlock TE, int state)
	{
		for (TECarpentersBlock piece : getDoorPieces(TE))
			Door.setState(piece, state, piece == TE ? true : false);
	}

	/**
	 * Updates door type.
	 * Will also update adjoining door piece.
	 */
	public void setDoorType(TECarpentersBlock TE, int type)
	{
		Door.setType(TE, type);
		updateAdjoiningDoorPiece(TE);
	}

	/**
	 * Set door rigidity.
	 * Will update all connecting door pieces.
	 */
	public void setDoorRigidity(TECarpentersBlock TE, int rigidity)
	{
		for (TECarpentersBlock piece : getDoorPieces(TE))
			Door.setRigidity(piece, rigidity);
	}

	/**
	 * Updates door hinge side.
	 * Will also update adjoining door piece.
	 */
	public void setDoorHinge(TECarpentersBlock TE, int hinge)
	{
		Door.setHingeSide(TE, hinge);
		updateAdjoiningDoorPiece(TE);
	}

	@Override
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	protected void auxiliaryOnNeighborBlockChange(TECarpentersBlock TE, World world, int x, int y, int z, int blockID)
	{
		int data = BlockProperties.getData(TE);
		boolean isOpen = Door.getState(data) == Door.STATE_OPEN;

		/*
		 * Check if door piece is orphaned.
		 */
		if (Door.getPiece(data) == Door.PIECE_BOTTOM) {
			if (world.getBlockId(x, y + 1, z) != this.blockID) {
				world.setBlockToAir(x, y, z);
				return;
			} else if (!world.doesBlockHaveSolidTopSurface(x, y - 1, z)) {
				world.setBlockToAir(x, y + 1, z);
				dropBlockAsItem(world, x, y, z, 0, 0);
				return;
			}
		} else if (world.getBlockId(x, y - 1, z) != this.blockID) {
			world.setBlockToAir(x, y, z);
			return;
		}

		/*
		 * Create list of door pieces and check state of each so
		 * that they act as a single entity regardless of which
		 * door piece receives this event.
		 */
		boolean isPowered = false;
		for (TECarpentersBlock piece : getDoorPieces(TE))
			if (world.isBlockIndirectlyGettingPowered(piece.xCoord, piece.yCoord, piece.zCoord))
				isPowered = true;

		/*
		 * Set block open or closed
		 */
		if ((blockID > 0 && Block.blocksList[blockID].canProvidePower()) && isPowered != isOpen)
			setDoorState(TE, isOpen ? Door.STATE_CLOSED : Door.STATE_OPEN);
	}

	@Override
	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int metadata, Random random, int par3)
	{
		return ItemHandler.itemCarpentersDoor.itemID;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World world, int x, int y, int z)
	{
		return ItemHandler.itemCarpentersDoor.itemID;
	}

	/**
	 * Updates state, hinge and type for adjoining door piece.
	 */
	private void updateAdjoiningDoorPiece(TECarpentersBlock TE)
	{
		int data = BlockProperties.getData(TE);
		int state = Door.getState(data);
		int hinge = Door.getHinge(data);
		int type = Door.getType(data);
		int rigidity = Door.getRigidity(data);

		boolean isTop = Door.getPiece(data) == Door.PIECE_TOP;

		TECarpentersBlock TE_adj;
		if (isTop)
			TE_adj = (TECarpentersBlock) TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord - 1, TE.zCoord);
		else
			TE_adj = (TECarpentersBlock) TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord + 1, TE.zCoord);

		Door.setState(TE_adj, state, false);
		Door.setHingeSide(TE_adj, hinge);
		Door.setType(TE_adj, type);
		Door.setRigidity(TE_adj, rigidity);
	}

	@Override
	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return	y >= 255 ? false : world.doesBlockHaveSolidTopSurface(x, y - 1, z) &&
				super.canPlaceBlockAt(world, x, y, z) &&
				super.canPlaceBlockAt(world, x, y + 1, z);
	}

	@Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockHandler.carpentersDoorRenderID;
	}
}
