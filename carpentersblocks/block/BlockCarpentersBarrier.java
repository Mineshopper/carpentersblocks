package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Barrier;
import carpentersblocks.data.Gate;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersBarrier extends BlockBase
{

	public BlockCarpentersBarrier(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersBarrier");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		setTextureName("carpentersblocks:general/generic");
	}

	@Override
	/**
	 * Toggles post.
	 */
	protected boolean onHammerLeftClick(TECarpentersBlock TE, EntityPlayer entityPlayer)
	{
		int data = BlockProperties.getData(TE);

		Barrier.setPost(TE, Barrier.getPost(data) == Barrier.HAS_POST ? Barrier.NO_POST : Barrier.HAS_POST);

		return true;
	}

	@Override
	/**
	 * Alters barrier type or sub-type.
	 */
	protected boolean onHammerRightClick(TECarpentersBlock TE, EntityPlayer entityPlayer, int side, float hitX, float hitZ)
	{
		int data = BlockProperties.getData(TE);
		int type = Barrier.getType(data);

		if (entityPlayer.isSneaking()) {

			/*
			 * Cycle through sub-types
			 */
			if (type <= Barrier.TYPE_VANILLA_X3)
				if (++type > Barrier.TYPE_VANILLA_X3)
					type = Barrier.TYPE_VANILLA;

		} else {

			/*
			 * Cycle through barrier types
			 */
			if (type <= Barrier.TYPE_VANILLA_X3) {
				type = Barrier.TYPE_PICKET;
			} else if (++type > Barrier.TYPE_WALL) {
				type = Barrier.TYPE_VANILLA;
			}

		}

		Barrier.setType(TE, type);

		return true;
	}

	@Override
	/**
	 * Called when the block is placed in the world.
	 */
	public void auxiliaryOnBlockPlacedBy(TECarpentersBlock TE, World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		/*
		 * Match gate type with adjacent type or barrier type if possible
		 */
		TECarpentersBlock TE_YN = (world.getBlockId(x, y - 1, z) == blockID || world.getBlockId(x, y - 1, z) == BlockHandler.blockCarpentersGateID) ? (TECarpentersBlock)world.getBlockTileEntity(x, y - 1, z) : null;
		TECarpentersBlock TE_YP = (world.getBlockId(x, y + 1, z) == blockID || world.getBlockId(x, y + 1, z) == BlockHandler.blockCarpentersGateID) ? (TECarpentersBlock)world.getBlockTileEntity(x, y + 1, z) : null;
		TECarpentersBlock TE_XN = (world.getBlockId(x - 1, y, z) == blockID || world.getBlockId(x - 1, y, z) == BlockHandler.blockCarpentersGateID) ? (TECarpentersBlock)world.getBlockTileEntity(x - 1, y, z) : null;
		TECarpentersBlock TE_XP = (world.getBlockId(x + 1, y, z) == blockID || world.getBlockId(x + 1, y, z) == BlockHandler.blockCarpentersGateID) ? (TECarpentersBlock)world.getBlockTileEntity(x + 1, y, z) : null;
		TECarpentersBlock TE_ZN = (world.getBlockId(x, y, z - 1) == blockID || world.getBlockId(x, y, z - 1) == BlockHandler.blockCarpentersGateID) ? (TECarpentersBlock)world.getBlockTileEntity(x, y, z - 1) : null;
		TECarpentersBlock TE_ZP = (world.getBlockId(x, y, z + 1) == blockID || world.getBlockId(x, y, z + 1) == BlockHandler.blockCarpentersGateID) ? (TECarpentersBlock)world.getBlockTileEntity(x, y, z + 1) : null;

		if (TE_YN != null) {
			int temp_data = BlockProperties.getData(TE_YN);
			Barrier.setType(TE, world.getBlockId(x, y - 1, z) == blockID ? Barrier.getType(temp_data) : Gate.getType(temp_data));
		} else if (TE_YP != null) {
			int temp_data = BlockProperties.getData(TE_YP);
			Barrier.setType(TE, world.getBlockId(x, y + 1, z) == blockID ? Barrier.getType(temp_data) : Gate.getType(temp_data));
		} else if (TE_XN != null) {
			int temp_data = BlockProperties.getData(TE_XN);
			Barrier.setType(TE, world.getBlockId(x - 1, y, z) == blockID ? Barrier.getType(temp_data) : Gate.getType(temp_data));
		} else if (TE_XP != null) {
			int temp_data = BlockProperties.getData(TE_XP);
			Barrier.setType(TE, world.getBlockId(x + 1, y, z) == blockID ? Barrier.getType(temp_data) : Gate.getType(temp_data));
		} else if (TE_ZN != null) {
			int temp_data = BlockProperties.getData(TE_ZN);
			Barrier.setType(TE, world.getBlockId(x, y, z - 1) == blockID ? Barrier.getType(temp_data) : Gate.getType(temp_data));
		} else if (TE_ZP != null) {
			int temp_data = BlockProperties.getData(TE_ZP);
			Barrier.setType(TE, world.getBlockId(x, y, z + 1) == blockID ? Barrier.getType(temp_data) : Gate.getType(temp_data));
		}
	}

	@Override
	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
	 * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
	 */
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
	{
		TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

		boolean connect_ZN = canConnectBarrierTo(TE, world, x, y, z - 1, ForgeDirection.SOUTH);
		boolean connect_ZP = canConnectBarrierTo(TE, world, x, y, z + 1, ForgeDirection.NORTH);
		boolean connect_XN = canConnectBarrierTo(TE, world, x - 1, y, z, ForgeDirection.EAST);
		boolean connect_XP = canConnectBarrierTo(TE, world, x + 1, y, z, ForgeDirection.WEST);

		float x_Low = 0.375F;
		float x_High = 0.625F;
		float z_Low = 0.375F;
		float z_High = 0.625F;

		if (connect_ZN)
			z_Low = 0.0F;

		if (connect_ZP)
			z_High = 1.0F;

		if (connect_ZN || connect_ZP)
		{
			setBlockBounds(x_Low, 0.0F, z_Low, x_High, 1.5F, z_High);
			super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
		}

		z_Low = 0.375F;
		z_High = 0.625F;

		if (connect_XN)
			x_Low = 0.0F;

		if (connect_XP)
			x_High = 1.0F;

		if (connect_XN || connect_XP || !connect_ZN && !connect_ZP)
		{
			setBlockBounds(x_Low, 0.0F, z_Low, x_High, 1.5F, z_High);
			super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
		}

		if (connect_ZN)
			z_Low = 0.0F;

		if (connect_ZP)
			z_High = 1.0F;

		setBlockBounds(x_Low, 0.0F, z_Low, x_High, 1.0F, z_High);
	}

	@Override
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);
		int type = Barrier.getType(BlockProperties.getData(TE));

		boolean connect_ZN = canConnectBarrierTo(TE, world, x, y, z - 1, ForgeDirection.SOUTH);
		boolean connect_ZP = canConnectBarrierTo(TE, world, x, y, z + 1, ForgeDirection.NORTH);
		boolean connect_XN = canConnectBarrierTo(TE, world, x - 1, y, z, ForgeDirection.EAST);
		boolean connect_XP = canConnectBarrierTo(TE, world, x + 1, y, z, ForgeDirection.WEST);

		float x_Low = 0.0F;
		float x_High = 1.0F;
		float z_Low = 0.0F;
		float z_High = 1.0F;

		if (type <= Barrier.TYPE_VANILLA_X3) {

			x_Low = 0.375F;
			x_High = 0.625F;
			z_Low = 0.375F;
			z_High = 0.625F;

			if (connect_ZN)
				z_Low = 0.0F;

			if (connect_ZP)
				z_High = 1.0F;

			if (connect_XN)
				x_Low = 0.0F;

			if (connect_XP)
				x_High = 1.0F;

		} else {

			x_Low = 0.25F;
			x_High = 0.75F;
			z_Low = 0.25F;
			z_High = 0.75F;

			if (connect_ZN)
				z_Low = 0.0F;

			if (connect_ZP)
				z_High = 1.0F;

			if (connect_XN)
				x_Low = 0.0F;

			if (connect_XP)
				x_High = 1.0F;

			if (connect_ZN && connect_ZP && !connect_XN && !connect_XP) {
				x_Low = 0.3125F;
				x_High = 0.6875F;
			} else if (!connect_ZN && !connect_ZP && connect_XN && connect_XP) {
				z_Low = 0.3125F;
				z_High = 0.6875F;
			}

		}

		setBlockBounds(x_Low, 0.0F, z_Low, x_High, 1.0F, z_High);
	}

	/**
	 * Returns true if block can connect to specified side of neighbor block.
	 */
	public boolean canConnectBarrierTo(TECarpentersBlock TE, IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		int data = BlockProperties.getData(TE);
		int blockID = world.getBlockId(x, y, z);

		if (blockID > 0)
		{
			Block block = Block.blocksList[world.getBlockId(x, y, z)];

			/*
			 * For the top side, make it create post if block is flower pot, torch, etc.
			 */
			if (side == ForgeDirection.UP) {
				if (block != null && block.blockMaterial == Material.circuits)
					return true;
			} else {
				if (world.getBlockId(x, y, z) == this.blockID || blockID == BlockHandler.blockCarpentersGateID)
					return true;

				return block.isBlockSolidOnSide(TE.worldObj, x, y, z, side) && Barrier.getPost(data) != Barrier.HAS_POST;
			}
		}

		return false;
	}

	@Override
	/**
	 * Determines if a torch can be placed on the top surface of this block.
	 */
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: world, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

	@Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockHandler.carpentersBarrierRenderID;
	}

}
