package carpentersblocks.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockQuartz;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.handler.DyeColorHandler;
import carpentersblocks.util.handler.OverlayHandler;

public class BlockProperties
{

	/**
	 * Returns entity facing.
	 */
	public static int getEntityFacing(Entity entity)
	{
		return MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
	}
	
	/**
	 * Will return direction from entity facing.
	 */
	public static ForgeDirection getDirectionFromFacing(int facing)
	{
		switch (facing)
		{
		case 0:
			return ForgeDirection.NORTH;
		case 1:
			return ForgeDirection.EAST;
		case 2:
			return ForgeDirection.SOUTH;
		default:
			return ForgeDirection.WEST;
		}
	}
	
	/**
	 * Will suppress block updates.
	 */
	private static boolean suppressUpdate = false;

	/**
	 * Ejects an item at given coordinates.
	 */
	private static void ejectEntity(TECarpentersBlock TE, ItemStack itemStack)
	{
		if (!TE.worldObj.isRemote)
		{
			float offset = 0.7F;
			double xRand = TE.worldObj.rand.nextFloat() * offset + (1.0F - offset) * 0.5D;
			double yRand = TE.worldObj.rand.nextFloat() * offset + (1.0F - offset) * 0.2D + 0.6D;
			double zRand = TE.worldObj.rand.nextFloat() * offset + (1.0F - offset) * 0.5D;

			EntityItem entityEjectedItem = new EntityItem(TE.worldObj, TE.xCoord + xRand, TE.yCoord + yRand, TE.zCoord + zRand, itemStack);

			entityEjectedItem.delayBeforeCanPickup = 10;
			TE.worldObj.spawnEntityInWorld(entityEjectedItem);
		}
	}
	
	/**
	 * Returns whether block rotates based on placement conditions.
	 * The blocks that utilize this property are mostly atypical, and
	 * must be added manually.
	 */
	public static boolean blockRotates(World world, Block block, int x, int y, int z)
	{
		return	block.isWood(world, x, y, z) ||
				block instanceof BlockQuartz;
	}

	/**
	 * Converts signed data types to unsigned integer.
	 */
	public static int getUnsignedInt(int value)
	{
		return value & 0xffffffff;
	}

	/**
	 * Plays block placement sound.
	 */
	public static void playBlockPlacementSound(TECarpentersBlock TE, int blockID)
	{
		playBlockPlacementSound(TE.worldObj, TE.xCoord, TE.yCoord, TE.zCoord, blockID);
	}

	/**
	 * Plays block placement sound.
	 */
	public static void playBlockPlacementSound(World world, int x, int y, int z, int blockID)
	{
		if (!world.isRemote && blockID > 0)
		{
			Block block = Block.blocksList[blockID];
			world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, block.stepSound.getPlaceSound(), block.stepSound.getVolume() + 1.0F / 2.0F, block.stepSound.getPitch() * 0.8F);
		}
	}

	/**
	 * Strips side of all properties.
	 */
	public static void clearAttributes(TECarpentersBlock TE, int side)
	{
		suppressUpdate = true;

		setDyeColor(TE, side, DyeColorHandler.NO_COLOR);
		setOverlay(TE, side, (ItemStack)null);
		setCover(TE, side, 0, (ItemStack)null);
		setPattern(TE, side, 0);

		suppressUpdate = false;

		TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
	}

	/**
	 * Returns cover block ID.
	 */
	public static int getCoverID(TECarpentersBlock TE, int side)
	{
		return TE.cover[side] & 0xfff;
	}

	/**
	 * Returns cover block metadata.
	 */
	public static int getCoverMetadata(TECarpentersBlock TE, int side)
	{
		return (TE.cover[side] & 0xf000) >>> 12;
	}

	/**
	 * Returns whether block has a cover.
	 * Checks if block ID exists and whether it is a valid cover block.
	 */
	public static boolean hasCover(TECarpentersBlock TE, int side)
	{
		int coverID = getCoverID(TE, side);
		int metadata = getCoverMetadata(TE, side);

		return	coverID > 0 &&
				Block.blocksList[coverID] != null &&
				isCover(new ItemStack(coverID, 1, metadata));
	}

	/**
	 * Returns whether block has side covers.
	 */
	public static boolean hasSideCovers(TECarpentersBlock TE)
	{
		for (int side = 0; side < 6; ++side) {
			if (hasCover(TE, side)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns cover block.
	 */
	public static Block getCoverBlock(IBlockAccess world, int side, int x, int y, int z)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

		return getCoverBlock(TE, side);
	}

	/**
	 * Returns cover block.
	 */
	public static Block getCoverBlock(TECarpentersBlock TE, int side)
	{
		Block coverBlock;

		if (hasCover(TE, side)) {
			coverBlock = Block.blocksList[getCoverID(TE, side)];
		} else {
			coverBlock = Block.blocksList[TE.worldObj.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord)];
		}

		return coverBlock;
	}

	/**
	 * Returns whether block is a cover.
	 */
	public static boolean isCover(ItemStack itemStack)
	{
		if (itemStack.getItem() instanceof ItemBlock && !isOverlay(itemStack))
		{
			Block block = Block.blocksList[itemStack.getItem().itemID];

			return	!block.hasTileEntity(itemStack.getItemDamage()) &&
					(
						block.renderAsNormalBlock() ||
						block instanceof BlockHalfSlab ||
						block instanceof BlockPane ||
						block instanceof BlockBreakable
					);
		}

		return false;
	}

	/**
	 * Sets cover block.
	 */
	public static boolean setCover(TECarpentersBlock TE, int side, int metadata, ItemStack itemStack)
	{		
		if (hasCover(TE, side))
			ejectEntity(TE, new ItemStack(getCoverID(TE, side), 1, getCoverMetadata(TE, side)));

		int blockID = itemStack == null ? 0 : itemStack.itemID;

		if (itemStack != null)
			playBlockPlacementSound(TE, blockID);

		TE.cover[side] = (short) (blockID + (metadata << 12));
		
		if (side == 6)
			TE.worldObj.setBlockMetadataWithNotify(TE.xCoord, TE.yCoord, TE.zCoord, metadata, 0);
		
		TE.worldObj.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, blockID);
		TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);

		return true;
	}

	/**
	 * Get block data.
	 */
	public final static int getData(TECarpentersBlock TE)
	{
		return getUnsignedInt(TE.data);
	}

	/**
	 * Set block data.
	 */
	public static void setData(TECarpentersBlock TE, int data)
	{
		/*
		 * No need to update if data hasn't changed.
		 */
		if (data != getData(TE)) {
			TE.data = (short) data;

			if (!suppressUpdate) {
				TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
			}
		}
	}

	/**
	 * Returns whether side has cover.
	 */
	public static boolean hasDyeColor(TECarpentersBlock TE, int side)
	{
		return TE.color[side] > 0;
	}

	/**
	 * Sets color for side.
	 */
	public static boolean setDyeColor(TECarpentersBlock TE, int side, int metadata)
	{
		if (TE.color[side] > 0) {
			ejectEntity(TE, new ItemStack(Item.dyePowder, 1, (15 - TE.color[side])));
		}

		TE.color[side] = (byte) metadata;

		if (!suppressUpdate) {
			TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
		}

		return true;
	}

	/**
	 * Returns dye color for side.
	 */
	public static int getDyeColor(TECarpentersBlock TE, int side)
	{
		return TE.color[side];
	}

	/**
	 * Sets overlay.
	 */
	public static boolean setOverlay(TECarpentersBlock TE, int side, ItemStack itemStack)
	{
		if (hasOverlay(TE, side)) {
			ejectEntity(TE, OverlayHandler.getItemStack(TE.overlay[side]));
		}

		TE.overlay[side] = (byte) OverlayHandler.getKey(itemStack);

		if (!suppressUpdate) {
			TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
		}

		return true;
	}

	/**
	 * Returns overlay.
	 */
	public static int getOverlay(TECarpentersBlock TE, int side)
	{
		return TE.overlay[side];
	}

	/**
	 * Returns whether block has overlay.
	 */
	public static boolean hasOverlay(TECarpentersBlock TE, int side)
	{
		return TE.overlay[side] > 0;
	}

	/**
	 * Returns whether ItemStack contains a valid overlay item or block.
	 */
	public static boolean isOverlay(ItemStack itemStack)
	{
		return OverlayHandler.overlayMap.containsValue(itemStack.itemID);
	}

	/**
	 * Returns whether block has pattern.
	 */
	public static boolean hasPattern(TECarpentersBlock TE, int side)
	{
		return getPattern(TE, side) > 0;
	}

	/**
	 * Returns pattern.
	 */
	public static int getPattern(TECarpentersBlock TE, int side)
	{
		return getUnsignedInt(TE.pattern[side]);
	}

	/**
	 * Sets pattern.
	 */
	public static boolean setPattern(TECarpentersBlock TE, int side, int pattern)
	{
		TE.pattern[side] = (byte) pattern;

		if (!suppressUpdate) {
			TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
		}

		return true;
	}

	/**
	 * Returns whether side should render based on cover blocks
	 * of both source and adjacent block.
	 */
	public static boolean shouldRenderSharedFaceBasedOnCovers(TECarpentersBlock TE_adj, TECarpentersBlock TE_src)
	{
		Block block_adj= BlockProperties.getCoverBlock(TE_adj, 6);
		Block block_src = BlockProperties.getCoverBlock(TE_src, 6);

		if (!BlockProperties.hasCover(TE_adj, 6)) {
			return BlockProperties.hasCover(TE_src, 6);
		} else {
			if (!BlockProperties.hasCover(TE_src, 6) && block_adj.getRenderBlockPass() == 0) {
				return !block_adj.isOpaqueCube();
			} else if (BlockProperties.hasCover(TE_src, 6) && block_src.isOpaqueCube() == block_adj.isOpaqueCube() && block_src.getRenderBlockPass() == block_adj.getRenderBlockPass()) {
				return false;
			} else {
				return true;
			}
		}
	}

}
