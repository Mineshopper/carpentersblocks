package carpentersblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Barrier;
import carpentersblocks.data.Gate;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersGate extends BlockBase {

	public BlockCarpentersGate(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersGate");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		setTextureName("carpentersblocks:general/solid");
	}

	@Override
	/**
	 * Alters gate type or sub-type and returns result.
	 */
	protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer, int side)
	{
		int type = Gate.getType(TE);

		if (entityPlayer.isSneaking()) {

			/*
			 * Cycle through sub-types
			 */
			if (type <= Gate.TYPE_VANILLA_X3) {
				if (++type > Gate.TYPE_VANILLA_X3) {
					type = Gate.TYPE_VANILLA;
				}
			}

		} else {

			/*
			 * Cycle through barrier types
			 */
			if (type <= Gate.TYPE_VANILLA_X3) {
				type = Gate.TYPE_PICKET;
			} else if (++type > Gate.TYPE_WALL) {
				type = Gate.TYPE_VANILLA;
			}

		}

		Gate.setType(TE, type);

		return true;
	}

	@Override
	/**
	 * Opens or closes gate on right click.
	 */
	public boolean[] postOnBlockActivated(TEBase TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (Gate.getState(TE) == Gate.STATE_OPEN) {
			Gate.setState(TE, Gate.STATE_CLOSED, true);
			cycleNeighborGate(TE, world, x, y, z);
		}
		else
		{
			int facing = (MathHelper.floor_double(entityPlayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;
			Gate.setState(TE, Gate.STATE_OPEN, true);

			if (Gate.getFacing(TE) == Gate.FACING_ON_X) {
				Gate.setDirOpen(TE, facing == 0 ? Gate.DIR_POS : Gate.DIR_NEG);
			} else {
				Gate.setDirOpen(TE, facing == 3 ? Gate.DIR_POS : Gate.DIR_NEG);
			}

			cycleNeighborGate(TE, world, x, y, z);
		}

		return new boolean[] { true, false };
	}

	@Override
	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return !world.getBlockMaterial(x, y - 1, z).isSolid() ? false : super.canPlaceBlockAt(world, x, y, z);
	}

	@Override
	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	 * cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		TEBase TE = world.getBlockId(x, y, z) == blockID ? (TEBase)world.getBlockTileEntity(x, y, z) : null;

		if (TE != null)
		{
			if (Gate.getState(TE) == Gate.STATE_OPEN) {
				return null;
			} else if (Gate.getFacing(TE) == Gate.FACING_ON_Z) {
				if (Gate.getType(TE) == Gate.TYPE_VANILLA || Gate.getType(TE) == Gate.TYPE_WALL) {
					return AxisAlignedBB.getAABBPool().getAABB(x + 0.4375F, y, z, x + 0.5625F, y + 1.5F, z + 1.0F);
				} else {
					return AxisAlignedBB.getAABBPool().getAABB(x + 0.375F, y, z, x + 0.625F, y + 1.5F, z + 1.0F);
				}
			} else {
				if (Gate.getType(TE) == Gate.TYPE_VANILLA || Gate.getType(TE) == Gate.TYPE_WALL) {
					return AxisAlignedBB.getAABBPool().getAABB(x, y, z + 0.4375F, x + 1.0F, y + 1.5F, z + 0.5625F);
				} else {
					return AxisAlignedBB.getAABBPool().getAABB(x, y, z + 0.375F, x + 1.0F, y + 1.5F, z + 0.625F);
				}
			}
		}

		return null;
	}

	@Override
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TEBase TE = (TEBase)world.getBlockTileEntity(x, y, z);

		if (Gate.getFacing(TE) == Gate.FACING_ON_Z) {
			if (Gate.getType(TE) == Gate.TYPE_VANILLA || Gate.getType(TE) == Gate.TYPE_WALL) {
				setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
			} else {
				setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
			}
		} else {
			if (Gate.getType(TE) == Gate.TYPE_VANILLA || Gate.getType(TE) == Gate.TYPE_WALL) {
				setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
			} else {
				setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
			}
		}
	}

	/**
	 * Opens or closes one neighboring gate above or below block.
	 */
	private void cycleNeighborGate(TEBase TE, World world, int x, int y, int z)
	{
		boolean isGateBelow = world.getBlockId(x, y - 1, z) == blockID ? true : false;
		boolean isGateAbove = world.getBlockId(x, y + 1, z) == blockID ? true : false;

		/*
		 * Will only check for gate above or below, and limit to only activating a single stacked gate.
		 * It is done this way intentionally.
		 */
		if (isGateBelow) {

			TEBase TE_YN = (TEBase)world.getBlockTileEntity(x, y - 1, z);
			if (Gate.getFacing(TE_YN) == Gate.getFacing(TE)) {
				Gate.setDirOpen(TE_YN, Gate.getDirOpen(TE));
				Gate.setState(TE_YN, Gate.getState(TE), false);
			}

		} else if (isGateAbove) {

			TEBase TE_YP = (TEBase)world.getBlockTileEntity(x, y + 1, z);
			if (Gate.getFacing(TE_YP) == Gate.getFacing(TE)) {
				Gate.setDirOpen(TE_YP, Gate.getDirOpen(TE));
				Gate.setState(TE_YP, Gate.getState(TE), false);
			}

		}
	}

	@Override
	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

		int facing = (MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;

		Gate.setFacing(TE, facing == 3 || facing == 1 ? Gate.FACING_ON_Z : Gate.FACING_ON_X);

		/*
		 * Match barrier type with adjacent type or gate type if possible
		 */
		TEBase TE_YN = world.getBlockId(x, y - 1, z) == blockID || world.getBlockId(x, y - 1, z) == BlockRegistry.blockCarpentersBarrierID ? (TEBase)world.getBlockTileEntity(x, y - 1, z) : null;
		TEBase TE_YP = world.getBlockId(x, y + 1, z) == blockID || world.getBlockId(x, y + 1, z) == BlockRegistry.blockCarpentersBarrierID ? (TEBase)world.getBlockTileEntity(x, y + 1, z) : null;
		TEBase TE_XN = world.getBlockId(x - 1, y, z) == blockID || world.getBlockId(x - 1, y, z) == BlockRegistry.blockCarpentersBarrierID ? (TEBase)world.getBlockTileEntity(x - 1, y, z) : null;
		TEBase TE_XP = world.getBlockId(x + 1, y, z) == blockID || world.getBlockId(x + 1, y, z) == BlockRegistry.blockCarpentersBarrierID ? (TEBase)world.getBlockTileEntity(x + 1, y, z) : null;
		TEBase TE_ZN = world.getBlockId(x, y, z - 1) == blockID || world.getBlockId(x, y, z - 1) == BlockRegistry.blockCarpentersBarrierID ? (TEBase)world.getBlockTileEntity(x, y, z - 1) : null;
		TEBase TE_ZP = world.getBlockId(x, y, z + 1) == blockID || world.getBlockId(x, y, z + 1) == BlockRegistry.blockCarpentersBarrierID ? (TEBase)world.getBlockTileEntity(x, y, z + 1) : null;

		if (TE_YN != null) {
			Gate.setType(TE, world.getBlockId(x, y - 1, z) == blockID ? Gate.getType(TE_YN) : Barrier.getType(TE_YN));
		} else if (TE_YP != null) {
			Gate.setType(TE, world.getBlockId(x, y + 1, z) == blockID ? Gate.getType(TE_YP) : Barrier.getType(TE_YP));
		} else if (TE_XN != null) {
			Gate.setType(TE, world.getBlockId(x - 1, y, z) == blockID ? Gate.getType(TE_XN) : Barrier.getType(TE_XN));
		} else if (TE_XP != null) {
			Gate.setType(TE, world.getBlockId(x + 1, y, z) == blockID ? Gate.getType(TE_XP) : Barrier.getType(TE_XP));
		} else if (TE_ZN != null) {
			Gate.setType(TE, world.getBlockId(x, y, z - 1) == blockID ? Gate.getType(TE_ZN) : Barrier.getType(TE_ZN));
		} else if (TE_ZP != null) {
			Gate.setType(TE, world.getBlockId(x, y, z + 1) == blockID ? Gate.getType(TE_ZP) : Barrier.getType(TE_ZP));
		}

		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
	}

	@Override
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID)
	{
		if (!world.isRemote)
		{
			TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

			if (TE != null)
			{
				boolean isPowered = world.isBlockIndirectlyGettingPowered(x, y, z);

				if (isPowered || neighborBlockID > 0 && Block.blocksList[neighborBlockID].canProvidePower())
				{
					int state = Gate.getState(TE);

					if (isPowered && state == Gate.STATE_CLOSED) {
						Gate.setState(TE, Gate.STATE_OPEN, true);
						cycleNeighborGate(TE, world, x, y, z);
					} else if (!isPowered && state == Gate.STATE_OPEN) {
						Gate.setState(TE, Gate.STATE_CLOSED, true);
						cycleNeighborGate(TE, world, x, y, z);
					}
				}
			}
		}

		super.onNeighborBlockChange(world, x, y, z, neighborBlockID);
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
		return BlockRegistry.carpentersGateRenderID;
	}

}
