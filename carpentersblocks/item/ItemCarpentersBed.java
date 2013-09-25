package carpentersblocks.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Bed;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCarpentersBed extends Item
{

	public ItemCarpentersBed(int itemID)
	{
		super(itemID);
		maxStackSize = 64;
		setUnlocalizedName("itemCarpentersBed");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("carpentersblocks:bed");
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote && side == 1)
		{
			++y;
			int facing = MathHelper.floor_double(entityPlayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

			ForgeDirection dir = Bed.getDirection(facing);

			int x_offset = x - dir.offsetX;
			int z_offset = z - dir.offsetZ;

			if (entityPlayer.canPlayerEdit(x, y, z, side, itemStack) && entityPlayer.canPlayerEdit(x_offset, y, z_offset, side, itemStack)) {

				if (world.isAirBlock(x, y, z) && world.isAirBlock(x_offset, y, z_offset) && world.doesBlockHaveSolidTopSurface(x, y - 1, z) && world.doesBlockHaveSolidTopSurface(x_offset, y - 1, z_offset)) {

					/*
					 * Set foot of bed.
					 * 
					 * Metadata indicates direction and bed piece for identifying
					 * direction and coordinates for sleeping players.
					 * 
					 * Metadata for foot of bed is set to facing.
					 */
					world.setBlock(x, y, z, BlockHandler.blockCarpentersBedID, facing, 3);

					/*
					 * Set head of bed.
					 * 
					 * Metadata indicates direction and bed piece for identifying
					 * direction and coordinates for sleeping players.
					 * 
					 * Metadata for head of bed is set to facing + 8.
					 */
					world.setBlock(x_offset, y, z_offset, BlockHandler.blockCarpentersBedID, facing + 8, 3);

					BlockProperties.playBlockPlacementSound(world, x, y, z, BlockHandler.blockCarpentersBedID);

					--itemStack.stackSize;
					return true;

				}  else {

					return false;

				}

			} else {

				return false;

			}
		}

		return false;
	}

}