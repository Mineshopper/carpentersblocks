package carpentersblocks.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.block.BlockCarpentersDoor;
import carpentersblocks.data.Door;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCarpentersDoor extends Item
{

	public ItemCarpentersDoor(int itemID)
	{
		super(itemID);
		maxStackSize = 64;
		setUnlocalizedName("itemCarpentersDoor");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("carpentersblocks:door");
	}

	@Override
	/**
	 * Callback for item usage. If the item does something special on right clicking, it will have one of these. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (side == 1)
		{
			++y;
			BlockCarpentersDoor blockRef = (BlockCarpentersDoor) BlockRegistry.blockCarpentersDoor;

			if (entityPlayer.canPlayerEdit(x, y, z, side, itemStack) && entityPlayer.canPlayerEdit(x, y + 1, z, side, itemStack))
			{
				if (!blockRef.canPlaceBlockAt(world, x, y, z)) {
					return false;
				} else {
					int facing = MathHelper.floor_double((entityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F - 0.5D) & 3;
					placeDoorBlock(world, x, y, z, facing, blockRef);
					--itemStack.stackSize;
					return true;
				}
			}
		}
		
		return false;
	}

	private void placeDoorBlock(World world, int x, int y, int z, int facing, Block block)
	{
		world.setBlock(x, y, z, block.blockID);

		BlockProperties.playBlockPlacementSound(world, x, y, z, BlockRegistry.blockCarpentersDoorID);

		/*
		 * Create bottom door piece.
		 */
		TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);
		Door.setFacing(TE, facing);
		Door.setHingeSide(TE, getHingePoint(TE, block.blockID));
		Door.setPiece(TE, Door.PIECE_BOTTOM);

		/*
		 * Match door type and rigidity with adjacent type if possible
		 */
		TEBase TE_XN = world.getBlockId(x - 1, y, z) == block.blockID ? (TEBase) world.getBlockTileEntity(x - 1, y, z) : null;
		TEBase TE_XP = world.getBlockId(x + 1, y, z) == block.blockID ? (TEBase) world.getBlockTileEntity(x + 1, y, z) : null;
		TEBase TE_ZN = world.getBlockId(x, y, z - 1) == block.blockID ? (TEBase) world.getBlockTileEntity(x, y, z - 1) : null;
		TEBase TE_ZP = world.getBlockId(x, y, z + 1) == block.blockID ? (TEBase) world.getBlockTileEntity(x, y, z + 1) : null;

		int type = 0;
		if (TE_XN != null) {
			int temp_data = BlockProperties.getData(TE_XN);
			Door.setType(TE, Door.getType(temp_data));
			Door.setRigidity(TE, Door.getRigidity(temp_data));
			type = Door.getType(temp_data);
		} else if (TE_XP != null) {
			int temp_data = BlockProperties.getData(TE_XP);
			Door.setType(TE, Door.getType(temp_data));
			Door.setRigidity(TE, Door.getRigidity(temp_data));
			type = Door.getType(temp_data);
		} else if (TE_ZN != null) {
			int temp_data = BlockProperties.getData(TE_ZN);
			Door.setType(TE, Door.getType(temp_data));
			Door.setRigidity(TE, Door.getRigidity(temp_data));
			type = Door.getType(temp_data);
		} else if (TE_ZP != null) {
			int temp_data = BlockProperties.getData(TE_ZP);
			Door.setType(TE, Door.getType(temp_data));
			Door.setRigidity(TE, Door.getRigidity(temp_data));
			type = Door.getType(temp_data);
		}

		/*
		 * Create top door piece.
		 */
		 world.setBlock(x, y + 1, z, block.blockID);
		 TEBase TE_YP = (TEBase) world.getBlockTileEntity(x, y + 1, z);
		 Door.setFacing(TE_YP, facing);
		 Door.setType(TE_YP, type);
		 Door.setHingeSide(TE_YP, Door.getHinge(BlockProperties.getData(TE)));
		 Door.setPiece(TE_YP, Door.PIECE_TOP);
		 Door.setRigidity(TE_YP, Door.getRigidity(BlockProperties.getData(TE)));
	}

	/**
	 * Returns a hinge point allowing double-doors if a matching neighboring door is found.
	 * It returns the default hinge point if no neighboring doors are found.
	 */
	private int getHingePoint(TEBase TE, int blockID)
	{
		int data = BlockProperties.getData(TE);
		int facing = Door.getFacing(data);
		Door.getHinge(data);
		Door.getState(data);
		int piece = Door.getPiece(data);

		TEBase TE_ZN = TE.worldObj.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord - 1) == blockID ? (TEBase) TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord - 1) : null;
		TEBase TE_ZP = TE.worldObj.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord + 1) == blockID ? (TEBase) TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord + 1) : null;
		TEBase TE_XN = TE.worldObj.getBlockId(TE.xCoord - 1, TE.yCoord, TE.zCoord) == blockID ? (TEBase) TE.worldObj.getBlockTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord) : null;
		TEBase TE_XP = TE.worldObj.getBlockId(TE.xCoord + 1, TE.yCoord, TE.zCoord) == blockID ? (TEBase) TE.worldObj.getBlockTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord) : null;

		switch (facing)
		{
		case Door.FACING_XN:

			if (TE_ZP != null) {

				int data_ZP = BlockProperties.getData(TE_ZP);
				if (piece == Door.getPiece(data_ZP) && facing == Door.getFacing(data_ZP) && Door.getHinge(data_ZP) == Door.HINGE_LEFT)
					return Door.HINGE_RIGHT;

			}

			break;
		case Door.FACING_XP:

			if (TE_ZN != null) {

				int data_ZN = BlockProperties.getData(TE_ZN);
				if (piece == Door.getPiece(data_ZN) && facing == Door.getFacing(data_ZN) && Door.getHinge(data_ZN) == Door.HINGE_LEFT)
					return Door.HINGE_RIGHT;

			}

			break;
		case Door.FACING_ZN:

			if (TE_XN != null) {
				int data_XN = BlockProperties.getData(TE_XN);
				if (piece == Door.getPiece(data_XN) && facing == Door.getFacing(data_XN) && Door.getHinge(data_XN) == Door.HINGE_LEFT)
					return Door.HINGE_RIGHT;
			}

			break;
		case Door.FACING_ZP:

			if (TE_XP != null) {
				int data_XP = BlockProperties.getData(TE_XP);
				if (piece == Door.getPiece(data_XP) && facing == Door.getFacing(data_XP) && Door.getHinge(data_XP) == Door.HINGE_LEFT)
					return Door.HINGE_RIGHT;
			}

			break;
		}

		return Door.HINGE_LEFT;
	}

}
