package carpentersblocks.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.PressurePlate;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersPressurePlate extends BlockBase
{

	public BlockCarpentersPressurePlate(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersPressurePlate");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		setTickRandomly(true);
		setTextureName("carpentersblocks:slope/slope");
	}

	@Override
	/**
	 * Alters polarity.
	 */
	protected boolean onHammerLeftClick(TECarpentersBlock TE, EntityPlayer entityPlayer)
	{
		int data = BlockProperties.getData(TE);
		int polarity = PressurePlate.getPolarity(data) == PressurePlate.POLARITY_POSITIVE ? PressurePlate.POLARITY_NEGATIVE : PressurePlate.POLARITY_POSITIVE;

		if (!TE.worldObj.isRemote) {
			PressurePlate.setPolarity(TE, polarity);
			TE.worldObj.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord - 1, TE.zCoord, blockID);
		} else {
			switch (polarity) {
			case PressurePlate.POLARITY_POSITIVE:
				entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.polarity_pos.name"));
				break;
			case PressurePlate.POLARITY_NEGATIVE:
				entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.polarity_neg.name"));
			}
		}

		return true;
	}

	@Override
	/**
	 * Alters trigger behavior.
	 */
	protected boolean onHammerRightClick(TECarpentersBlock TE, EntityPlayer entityPlayer, int side)
	{
		int data = BlockProperties.getData(TE);

		int trigger;

		switch (PressurePlate.getTriggerEntity(data))
		{
		case PressurePlate.TRIGGER_PLAYER:
			trigger = PressurePlate.TRIGGER_MONSTER;
			break;
		case PressurePlate.TRIGGER_MONSTER:
			trigger = PressurePlate.TRIGGER_ANIMAL;
			break;
		case PressurePlate.TRIGGER_ANIMAL:
			trigger = PressurePlate.TRIGGER_ALL;
			break;
		default:
			trigger = PressurePlate.TRIGGER_PLAYER;
		}

		if (!TE.worldObj.isRemote) {
			PressurePlate.setTriggerEntity(TE, trigger);
		} else {
			switch (trigger) {
			case PressurePlate.TRIGGER_PLAYER:
				entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.trigger_player.name"));
				break;
			case PressurePlate.TRIGGER_MONSTER:
				entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.trigger_monster.name"));
				break;
			case PressurePlate.TRIGGER_ANIMAL:
				entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.trigger_animal.name"));
				break;
			case PressurePlate.TRIGGER_ALL:
				entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.trigger_all.name"));
			}
		}

		return true;
	}

	@Override
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

		setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F - 0.0625F, isDepressed(TE) ? 0.03125F : 0.0625F, 1.0F - 0.0625F);
	}

	@Override
	/**
	 * Called when the block is placed in the world.
	 */
	public void auxiliaryOnBlockPlacedBy(TECarpentersBlock TE, World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		PressurePlate.setType(TE, world.getBlockMetadata(x, y, z));
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
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	 * cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return world.doesBlockHaveSolidTopSurface(x, y - 1, z) || world.getBlockId(x, y - 1, z) == BlockHandler.blockCarpentersBarrierID;
	}

	@Override
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	protected void auxiliaryOnNeighborBlockChange(TECarpentersBlock TE, World world, int x, int y, int z, int blockID)
	{
		boolean dropBlock = false;

		if (!world.doesBlockHaveSolidTopSurface(x, y - 1, z) && world.getBlockId(x, y - 1, z) != BlockHandler.blockCarpentersBarrierID)
			dropBlock = true;

		if (dropBlock)
		{
			int data = BlockProperties.getData(TE);
			int type = PressurePlate.getType(data);

			dropBlockAsItem(world, x, y, z, type, 0);
			world.setBlockToAir(x, y, z);
		}
	}

	@Override
	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!world.isRemote)
		{
			TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

			List entityList = world.getEntitiesWithinAABB(Entity.class, getSensitiveAABB(x, y, z));

			if (!entityList.isEmpty()) {
				for (int count = 0; count < entityList.size(); ++count)
					setStateIfMobCollidesWithPlate(TE, (Entity)entityList.get(count), world, x, y, z);
			} else {
				setStateIfMobCollidesWithPlate(TE, (Entity)null, world, x, y, z);
			}
		}
	}

	@Override
	/**
	 * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
	 */
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (!world.isRemote)
		{
			List entityList = world.getEntitiesWithinAABB(Entity.class, getSensitiveAABB(x, y, z));

			if (!entityList.isEmpty()) {
				TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

				if (!isDepressed(TE))
					for (int count = 0; count < entityList.size(); ++count)
						setStateIfMobCollidesWithPlate(TE, (Entity)entityList.get(count), world, x, y, z);
			}
		}
	}

	/**
	 * Checks if there are mobs on the plate. If a mob is on the plate and it is off, it turns it on, and vice versa.
	 */
	private void setStateIfMobCollidesWithPlate(TECarpentersBlock TE, Entity entity, World world, int x, int y, int z)
	{
		BlockProperties.getData(TE);

		boolean currently_triggered = isDepressed(TE);

		if (shouldTrigger(TE, entity, world, x, y, z))
		{
			PressurePlate.setState(TE, PressurePlate.STATE_ON, true);
			notifyNeighborsOfUpdate(world, x, y, z);
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate(world));
		}
		else if (currently_triggered)
		{
			PressurePlate.setState(TE, PressurePlate.STATE_OFF, true);
			notifyNeighborsOfUpdate(world, x, y, z);
		}
	}

	private AxisAlignedBB getSensitiveAABB(int x, int y, int z)
	{
		return AxisAlignedBB.getAABBPool().getAABB(x + 0.125F, y, z + 0.125F, x + 1.0F - 0.125F, y + 0.25D, z + 1.0F - 0.125F);
	}

	private void notifyNeighborsOfUpdate(World world, int x, int y, int z)
	{
		world.notifyBlocksOfNeighborChange(x, y, z, blockID);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, blockID);
	}

	/**
	 * Returns whether pressure plate is in depressed state
	 */
	private boolean isDepressed(TECarpentersBlock TE)
	{
		int data = BlockProperties.getData(TE);
		return PressurePlate.getState(data) == PressurePlate.STATE_ON;
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

		return side == 1 ? getPowerSupply(TE, data) : 0;
	}

	@Override
	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	public boolean canProvidePower()
	{
		return true;
	}

	/**
	 * Returns power level (0 or 15)
	 */
	private int getPowerSupply(TECarpentersBlock TE, int data)
	{
		int polarity = PressurePlate.getPolarity(data);

		if (isDepressed(TE)) {
			return polarity == PressurePlate.POLARITY_POSITIVE ? 15 : 0;
		} else {
			return polarity == PressurePlate.POLARITY_NEGATIVE ? 15 : 0;
		}
	}

	/**
	 * Returns whether pressure plate should trigger based on entity colliding with it.
	 */
	private boolean shouldTrigger(TECarpentersBlock TE, Entity entity, World world, int x, int y, int z)
	{
		if (entity == null)
			return false;

		int trigger = PressurePlate.getTriggerEntity(BlockProperties.getData(TE));

		switch (trigger) {
		case PressurePlate.TRIGGER_PLAYER:
			return entity instanceof EntityPlayer;
		case PressurePlate.TRIGGER_MONSTER:
			return entity.isCreatureType(EnumCreatureType.monster, false);
		case PressurePlate.TRIGGER_ANIMAL:
			return entity.isCreatureType(EnumCreatureType.creature, false);
		default: // TRIGGER_EVERYTHING
		return true;
		}
	}

	@Override
	/**
	 * Ejects contained items into the world, and notifies neighbours of an update, as appropriate
	 */
	public void auxiliaryBreakBlock(TECarpentersBlock TE, World world, int x, int y, int z, int par5, int metadata)
	{
		if (isDepressed(TE))
			notifyNeighborsOfUpdate(world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: world, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return side != 0;
	}

	@Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockHandler.carpentersPressurePlateRenderID;
	}

}
