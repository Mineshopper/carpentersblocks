package carpentersblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Barrier;
import carpentersblocks.data.Gate;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import carpentersblocks.util.handler.IconHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersGate extends BlockBase
{

	public BlockCarpentersGate(int blockID)
	{
		super(blockID, Material.wood);
		this.setHardness(0.2F);
		this.setUnlocalizedName("blockCarpentersGate");
		this.setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
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
	 * Alters gate type or sub-type and returns result.
	 */
	protected boolean onHammerRightClick(TECarpentersBlock TE, EntityPlayer entityPlayer, int side)
	{
    	int data = BlockProperties.getData(TE);
		int type = Gate.getType(data);

		if (entityPlayer.isSneaking()) {
			
			/*
			 * Cycle through sub-types
			 */
			if (type <= Gate.TYPE_VANILLA_X3)
				if (++type > Gate.TYPE_VANILLA_X3)
					type = Gate.TYPE_VANILLA;
			
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
	public boolean auxiliaryOnBlockActivated(TECarpentersBlock TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int data = BlockProperties.getData(TE);

		if (Gate.getState(data) == Gate.STATE_OPEN) {
			Gate.setState(TE, Gate.STATE_CLOSED, true);
			cycleNeighborGate(world, BlockProperties.getData(TE), x, y, z);
		}
		else
		{
			int facing = (MathHelper.floor_double(entityPlayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;
			Gate.setState(TE, Gate.STATE_OPEN, true);

			if (Gate.getFacing(data) == Gate.FACING_ON_X) {
				Gate.setDirOpen(TE, facing == 0 ? Gate.DIR_POS : Gate.DIR_NEG);
			} else {
				Gate.setDirOpen(TE, facing == 3 ? Gate.DIR_POS : Gate.DIR_NEG);
			}

			cycleNeighborGate(world, BlockProperties.getData(TE), x, y, z);
		}
		
		return true;
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
		TECarpentersBlock TE = world.getBlockId(x, y, z) == this.blockID ? (TECarpentersBlock)world.getBlockTileEntity(x, y, z) : null;

		if (TE != null) {
			int data = BlockProperties.getData(TE);

			if (Gate.getState(data) == Gate.STATE_OPEN) {
				return null;
			} else if (Gate.getFacing(data) == Gate.FACING_ON_Z) {
				if (Gate.getType(data) == Gate.TYPE_VANILLA || Gate.getType(data) == Gate.TYPE_WALL)
					return AxisAlignedBB.getAABBPool().getAABB(x + 0.4375F, y, z, x + 0.5625F, y + 1.5F, z + 1.0F);
				else
					return AxisAlignedBB.getAABBPool().getAABB(x + 0.375F, y, z, x + 0.625F, y + 1.5F, z + 1.0F);
			} else {
				if (Gate.getType(data) == Gate.TYPE_VANILLA || Gate.getType(data) == Gate.TYPE_WALL)
					return AxisAlignedBB.getAABBPool().getAABB(x, y, z + 0.4375F, x + 1.0F, y + 1.5F, z + 0.5625F);
				else
					return AxisAlignedBB.getAABBPool().getAABB(x, y, z + 0.375F, x + 1.0F, y + 1.5F, z + 0.625F);
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
		TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

		int data = BlockProperties.getData(TE);

		if (Gate.getFacing(data) == Gate.FACING_ON_Z) {
			if (Gate.getType(data) == Gate.TYPE_VANILLA || Gate.getType(data) == Gate.TYPE_WALL)
				this.setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
			else
				this.setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
		} else {
			if (Gate.getType(data) == Gate.TYPE_VANILLA || Gate.getType(data) == Gate.TYPE_WALL)
				this.setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
			else
				this.setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
		}
	}
	
	/**
	 * Opens or closes one neighboring gate above or below block.
	 */
	private void cycleNeighborGate(World world, int data, int x, int y, int z)
	{
		boolean isGateBelow = world.getBlockId(x, y - 1, z) == this.blockID ? true : false;
		boolean isGateAbove = world.getBlockId(x, y + 1, z) == this.blockID ? true : false;

		/* 
		 * Will only check for gate above or below, and limit to only activating a single stacked gate.
		 * It is done this way intentionally.
		 */
		if (isGateBelow) {
			
			TECarpentersBlock TE_YN = (TECarpentersBlock)world.getBlockTileEntity(x, y - 1, z);
			if (Gate.getFacing(BlockProperties.getData(TE_YN)) == Gate.getFacing(data)) {
				Gate.setDirOpen(TE_YN, Gate.getDirOpen(data));
				Gate.setState(TE_YN, Gate.getState(data), false);
			}
			
		} else if (isGateAbove) {
			
			TECarpentersBlock TE_YP = (TECarpentersBlock)world.getBlockTileEntity(x, y + 1, z);
			if (Gate.getFacing(BlockProperties.getData(TE_YP)) == Gate.getFacing(data)) {
				Gate.setDirOpen(TE_YP, Gate.getDirOpen(data));
				Gate.setState(TE_YP, Gate.getState(data), false);
			}
			
		}
	}

    @Override
	/**
	 * Called when the block is placed in the world.
	 */
	public void auxiliaryOnBlockPlacedBy(TECarpentersBlock TE, World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
	{
		int facing = (MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;
	
		Gate.setFacing(TE, facing == 3 || facing == 1 ? Gate.FACING_ON_Z : Gate.FACING_ON_X);

		/*
		 * Match barrier type with adjacent type or gate type if possible
		 */
		TECarpentersBlock TE_YN = (world.getBlockId(x, y - 1, z) == this.blockID || world.getBlockId(x, y - 1, z) == BlockHandler.blockCarpentersBarrierID) ? (TECarpentersBlock)world.getBlockTileEntity(x, y - 1, z) : null;
		TECarpentersBlock TE_YP = (world.getBlockId(x, y + 1, z) == this.blockID || world.getBlockId(x, y + 1, z) == BlockHandler.blockCarpentersBarrierID) ? (TECarpentersBlock)world.getBlockTileEntity(x, y + 1, z) : null;
		TECarpentersBlock TE_XN = (world.getBlockId(x - 1, y, z) == this.blockID || world.getBlockId(x - 1, y, z) == BlockHandler.blockCarpentersBarrierID) ? (TECarpentersBlock)world.getBlockTileEntity(x - 1, y, z) : null;
		TECarpentersBlock TE_XP = (world.getBlockId(x + 1, y, z) == this.blockID || world.getBlockId(x + 1, y, z) == BlockHandler.blockCarpentersBarrierID) ? (TECarpentersBlock)world.getBlockTileEntity(x + 1, y, z) : null;
		TECarpentersBlock TE_ZN = (world.getBlockId(x, y, z - 1) == this.blockID || world.getBlockId(x, y, z - 1) == BlockHandler.blockCarpentersBarrierID) ? (TECarpentersBlock)world.getBlockTileEntity(x, y, z - 1) : null;
		TECarpentersBlock TE_ZP = (world.getBlockId(x, y, z + 1) == this.blockID || world.getBlockId(x, y, z + 1) == BlockHandler.blockCarpentersBarrierID) ? (TECarpentersBlock)world.getBlockTileEntity(x, y, z + 1) : null;

		if (TE_YN != null) {
			int data = BlockProperties.getData(TE_YN);
			Gate.setType(TE, world.getBlockId(x, y - 1, z) == this.blockID ? Gate.getType(data) : Barrier.getType(data));
		} else if (TE_YP != null) {
			int data = BlockProperties.getData(TE_YP);
			Gate.setType(TE, world.getBlockId(x, y + 1, z) == this.blockID ? Gate.getType(data) : Barrier.getType(data));
		} else if (TE_XN != null) {
			int data = BlockProperties.getData(TE_XN);
			Gate.setType(TE, world.getBlockId(x - 1, y, z) == this.blockID ? Gate.getType(data) : Barrier.getType(data));
		} else if (TE_XP != null) {
			int data = BlockProperties.getData(TE_XP);
			Gate.setType(TE, world.getBlockId(x + 1, y, z) == this.blockID ? Gate.getType(data) : Barrier.getType(data));
		} else if (TE_ZN != null) {
			int data = BlockProperties.getData(TE_ZN);
			Gate.setType(TE, world.getBlockId(x, y, z - 1) == this.blockID ? Gate.getType(data) : Barrier.getType(data));
		} else if (TE_ZP != null) {
			int data = BlockProperties.getData(TE_ZP);
			Gate.setType(TE, world.getBlockId(x, y, z + 1) == this.blockID ? Gate.getType(data) : Barrier.getType(data));
		}
	}

    @Override
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	protected void auxiliaryOnNeighborBlockChange(TECarpentersBlock TE, World world, int x, int y, int z, int neighborBlockID)
	{
    	boolean isPowered = world.isBlockIndirectlyGettingPowered(x, y, z);

    	if (isPowered || neighborBlockID > 0 && Block.blocksList[neighborBlockID].canProvidePower())
    	{
    		int data = BlockProperties.getData(TE);
    		int state = Gate.getState(data);

    		if (isPowered && state == Gate.STATE_CLOSED) {
    			Gate.setState(TE, Gate.STATE_OPEN, true);
    			cycleNeighborGate(world, BlockProperties.getData(TE), x, y, z);
    		} else if (!isPowered && state == Gate.STATE_OPEN) {
    			Gate.setState(TE, Gate.STATE_CLOSED, true);
    			cycleNeighborGate(world, BlockProperties.getData(TE), x, y, z);
    		}
    	}
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
		return BlockHandler.carpentersGateRenderID;
	}

}
