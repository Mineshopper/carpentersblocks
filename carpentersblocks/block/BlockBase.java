package carpentersblocks.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockEventHandler;
import carpentersblocks.util.handler.FeatureHandler;
import carpentersblocks.util.handler.ItemHandler;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.handler.PatternHandler;
import carpentersblocks.util.handler.PlantHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBase extends BlockContainer
{

	public BlockBase(int blockID, Material material)
	{
		super(blockID, material);
	}

	/**
	 * Returns whether cover will return the calling block, resulting in loop.
	 * This happens because if the block has no cover, it will instead return
	 * the block at the given coordinates.
	 */
	protected boolean willCoverRecurse(IBlockAccess world, int x, int y, int z)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

		return BlockProperties.getCoverBlock(TE, 6).blockID == this.blockID;
	}

	/**
	 * Returns whether the block at given coordinates is an instance of
	 * a Carpenter's block.
	 */
	protected boolean extendsBlockBase(IBlockAccess world, int x, int y, int z)
	{
		int blockID = world.getBlockId(x, y, z);

		return blockID > 0 && Block.blocksList[blockID] instanceof BlockBase;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
	 */
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

		return BlockProperties.getCoverBlock(TE, 6).getIcon(side, BlockProperties.getCoverMetadata(TE, 6));
	}

	@Override
	/**
	 * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
	 */
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

		ItemStack itemStack = entityPlayer.getCurrentEquippedItem();

		int side = BlockEventHandler.eventFace;
		
		if (itemStack != null && entityPlayer.canPlayerEdit(x, y, z, side, itemStack))
		{
			int effectiveSide = BlockProperties.hasCover(TE, side) ? side : 6;
			Item item = itemStack.getItem();

			if (item.equals(ItemHandler.itemCarpentersHammer)) {
				
				boolean dataAltered = false;

				if (entityPlayer.isSneaking()) {
					
					if (!world.isRemote) {
						
						if (BlockProperties.hasOverlay(TE, effectiveSide)) {
							dataAltered = BlockProperties.setOverlay(TE, effectiveSide, (ItemStack)null);
						} else if (BlockProperties.hasDyeColor(TE, effectiveSide)) {
							dataAltered = BlockProperties.setDyeColor(TE, effectiveSide, 0);
						} else if (BlockProperties.hasCover(TE, effectiveSide)) {
							dataAltered = BlockProperties.setCover(TE, effectiveSide, 0, (ItemStack)null);
							dataAltered = BlockProperties.setPattern(TE, effectiveSide, 0);
						}
						
					}

				} else {

					dataAltered = onHammerLeftClick(TE, entityPlayer);

				}

				if (dataAltered) {

					if (!entityPlayer.capabilities.isCreativeMode) {
						world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "dig.wood", 4.0F, 1.0F);
					}

					onNeighborBlockChange(world, x, y, z, blockID);
					world.notifyBlocksOfNeighborChange(x, y, z, blockID);

				}

			} else if (!world.isRemote && item.equals(ItemHandler.itemCarpentersChisel)) {

				if (entityPlayer.isSneaking()) {

					if (BlockProperties.hasPattern(TE, effectiveSide)) {
						BlockProperties.setPattern(TE, effectiveSide, 0);
					}

				} else if (BlockProperties.hasCover(TE, effectiveSide) && BlockProperties.getCoverBlock(TE, effectiveSide).isOpaqueCube()) {

					onChiselClick(TE, effectiveSide, true);
				}

			}
		}

		auxiliaryOnBlockClicked(TE, world, x, y, z, entityPlayer);
	}

	@Override
	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);
		ItemStack itemStack = entityPlayer.getCurrentEquippedItem();
		boolean actionPerformed = false;
		boolean decrementInventory = false;

		if (itemStack != null && entityPlayer.canPlayerEdit(x, y, z, side, itemStack))
		{
			/*
			 * If the side is not covered, we're using side 6
			 * to indicate the block itself.  Otherwise, we're
			 * working with a side cover.
			 */
			int effectiveSide = BlockProperties.hasCover(TE, side) ? side : 6;
			
			if (itemStack.getItem() == ItemHandler.itemCarpentersHammer) {

				actionPerformed = onHammerRightClick(TE, entityPlayer, side, hitX, hitZ);

			} else if (ItemHandler.enableChisel && itemStack.getItem() == ItemHandler.itemCarpentersChisel) {

				/* Skip clientside otherwise it will desynchronize server data. */
				if (world.isRemote)
					return true;
				
				if (BlockProperties.hasCover(TE, effectiveSide) && BlockProperties.getCoverBlock(TE, effectiveSide).isOpaqueCube())
					actionPerformed = onChiselClick(TE, effectiveSide, false);

			} else if (FeatureHandler.enableCovers && BlockProperties.isCover(itemStack)) {

				Block block = Block.blocksList[itemStack.itemID];
				int metadata = block instanceof BlockDirectional ? MathHelper.floor_double(entityPlayer.rotationYaw * 4.0F / 360.0F + 2.5D) & 3 : itemStack.getItemDamage();		
				
				if (!BlockProperties.hasCover(TE, 6)) {

					if (BlockProperties.blockRotates(world, block, x, y, z)) {
						metadata = block.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, metadata);
					}

					actionPerformed = decrementInventory = BlockProperties.setCover(TE, 6, metadata, itemStack);

				} else if (FeatureHandler.enableSideCovers) {

					if (!BlockProperties.hasCover(TE, side) && this.canCoverSide(TE, world, x, y, z, side)) {
						
						if (BlockProperties.blockRotates(world, block, x, y, z))
						{
							/*
							 * Blocks that determine direction based on side clicked
							 * have an obvious limitation in regards to being used as
							 * side covers: it's always the side clicked.
							 * 
							 * Therefore, we'll instead utilize the player's pitch
							 * and yaw for rotation when the block is used as a side
							 * cover.
							 */

							int facing = BlockProperties.getEntityFacing(BlockEventHandler.eventEntity);
							int side_interpolated =	entityPlayer.rotationPitch < -45.0F ? 0 : entityPlayer.rotationPitch > 45 ? 1 : facing == 0 ? 3 : facing == 1 ? 4 : facing == 2 ? 2 : 5;
							metadata = block.onBlockPlaced(world, x, y, z, side_interpolated, hitX, hitY, hitZ, metadata);
						}
						
						actionPerformed = decrementInventory = BlockProperties.setCover(TE, side, metadata, itemStack);
						
					}
						
				}

			} else if (FeatureHandler.enableOverlays && BlockProperties.isOverlay(itemStack)) {

				/* Skip clientside otherwise it will desynchronize server data. */
				if (world.isRemote)
					return true;
				
				if (!BlockProperties.hasOverlay(TE, effectiveSide) && (effectiveSide < 6 && BlockProperties.hasCover(TE, effectiveSide) || effectiveSide == 6))
					actionPerformed = decrementInventory = BlockProperties.setOverlay(TE, effectiveSide, itemStack);

			} else if (FeatureHandler.enableDyeColors && itemStack.getItem() == Item.dyePowder && itemStack.getItemDamage() != 15) {

				/* Skip clientside otherwise it will desynchronize server data. */
				if (world.isRemote)
					return true;
				
				if (!BlockProperties.hasDyeColor(TE, effectiveSide))
					actionPerformed = decrementInventory = BlockProperties.setDyeColor(TE, effectiveSide, (15 - itemStack.getItemDamage()));

			}
		}

		/* Should probably only do this if nothing happened. */
		if (!actionPerformed) {
			actionPerformed = auxiliaryOnBlockActivated(TE, world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
		}
		
		if (actionPerformed)
		{
			this.damageHammerWithChance(world, entityPlayer);

			if (!entityPlayer.capabilities.isCreativeMode)
				world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "dig.wood", 4.0F, 1.0F);

			this.onNeighborBlockChange(world, x, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);

		}

		if (!world.isRemote && decrementInventory)
			if (!entityPlayer.capabilities.isCreativeMode && --itemStack.stackSize <= 0)
				entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, (ItemStack)null);
		
		return actionPerformed;
	}

	/**
	 * Cycles through chisel patterns.
	 */
	public boolean onChiselClick(TECarpentersBlock TE, int side, boolean leftClick)
	{
		int pattern = BlockProperties.getPattern(TE, side);

		/*
		 * Try to match neighboring chisel pattern.
		 */
		int neighbor_pattern = 0;
		if (pattern == 0)
		{
			TECarpentersBlock TE_XN = extendsBlockBase(TE.worldObj, TE.xCoord - 1, TE.yCoord, TE.zCoord) ? (TECarpentersBlock)TE.worldObj.getBlockTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord) : null;
			TECarpentersBlock TE_XP = extendsBlockBase(TE.worldObj, TE.xCoord + 1, TE.yCoord, TE.zCoord) ? (TECarpentersBlock)TE.worldObj.getBlockTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord) : null;
			TECarpentersBlock TE_YN = extendsBlockBase(TE.worldObj, TE.xCoord, TE.yCoord - 1, TE.zCoord) ? (TECarpentersBlock)TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord - 1, TE.zCoord) : null;
			TECarpentersBlock TE_YP = extendsBlockBase(TE.worldObj, TE.xCoord, TE.yCoord + 1, TE.zCoord) ? (TECarpentersBlock)TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord + 1, TE.zCoord) : null;
			TECarpentersBlock TE_ZN = extendsBlockBase(TE.worldObj, TE.xCoord, TE.yCoord, TE.zCoord - 1) ? (TECarpentersBlock)TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord - 1) : null;
			TECarpentersBlock TE_ZP = extendsBlockBase(TE.worldObj, TE.xCoord, TE.yCoord, TE.zCoord + 1) ? (TECarpentersBlock)TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord + 1) : null;

			if (TE_XN != null && BlockProperties.hasPattern(TE_XN, side)) {
				pattern = BlockProperties.getPattern(TE_XN, side);
				neighbor_pattern = pattern;
			} else if (TE_XP != null && BlockProperties.hasPattern(TE_XP, side)) {
				pattern = BlockProperties.getPattern(TE_XP, side);
				neighbor_pattern = pattern;
			} else if (TE_YN != null && BlockProperties.hasPattern(TE_YN, side)) {
				pattern = BlockProperties.getPattern(TE_YN, side);
				neighbor_pattern = pattern;
			} else if (TE_YP != null && BlockProperties.hasPattern(TE_YP, side)) {
				pattern = BlockProperties.getPattern(TE_YP, side);
				neighbor_pattern = pattern;
			} else if (TE_ZN != null && BlockProperties.hasPattern(TE_ZN, side)) {
				pattern = BlockProperties.getPattern(TE_ZN, side);
				neighbor_pattern = pattern;
			} else if (TE_ZP != null && BlockProperties.hasPattern(TE_ZP, side)) {
				pattern = BlockProperties.getPattern(TE_ZP, side);
				neighbor_pattern = pattern;
			}
		}

		if (neighbor_pattern == 0) {
			pattern = leftClick ? PatternHandler.getPrev(pattern) : PatternHandler.getNext(pattern);
		}

		BlockProperties.setPattern(TE, side, pattern);

		return true;
	}

	@Override
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

		if (!world.isRemote)
		{
			/*
			 * Check for and eject side covers that are blocked by a
			 * solid adjacent block.
			 */
			if (BlockProperties.hasSideCovers(TE))
			{
				for (int side = 0; side < 6; ++side)
				{
					if (BlockProperties.hasCover(TE, side))
					{
						/*
						 * If block state no longer allows a side cover on
						 * side, eject side cover.
						 */
						if (!canCoverSide(TE, world, x, y, z, side))
						{
							BlockProperties.clearAttributes(TE, side);
							return;
						}

						/*
						 * If side is obstructed, eject side cover.
						 * Currently does not check for side depth.
						 */
						ForgeDirection dir = ForgeDirection.getOrientation(side);
						int x_offset = x + dir.offsetX;
						int y_offset = y + dir.offsetY;
						int z_offset = z + dir.offsetZ;

						if (world.getBlockId(x_offset, y_offset, z_offset) > 0)
						{
							Block adjBlock = Block.blocksList[world.getBlockId(x_offset, y_offset, z_offset)];
							if (adjBlock.isBlockSolidOnSide(world, x_offset, y_offset, z_offset, ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[side])))
								BlockProperties.clearAttributes(TE, side);
						}
					}
				}
			}
		}

		auxiliaryOnNeighborBlockChange(TE, world, x, y, z, blockID);
	}

	@Override
	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
	 * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
	 * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
	{
		if (!willCoverRecurse(world, x, y, z))
		{
			TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);
			int effectiveSide = ForgeDirection.OPPOSITES[side];

			int power = BlockProperties.getCoverBlock(world, 6, x, y, z).isProvidingWeakPower(world, x, y, z, side);
			int power_side = BlockProperties.hasCover(TE, effectiveSide) ? BlockProperties.getCoverBlock(TE, effectiveSide).isProvidingWeakPower(world, x, y, z, side) : 0;

			return power_side > power ? power_side : power;
		}

		return 0;
	}

	@Override
	/**
	 * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
	 * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
	{
		if (!willCoverRecurse(world, x, y, z))
		{
			TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);
			int effectiveSide = ForgeDirection.OPPOSITES[side];

			int power = BlockProperties.getCoverBlock(world, 6, x, y, z).isProvidingStrongPower(world, x, y, z, side);
			int power_side = BlockProperties.hasCover(TE, effectiveSide) ? BlockProperties.getCoverBlock(TE, effectiveSide).isProvidingStrongPower(world, x, y, z, side) : 0;

			return power_side > power ? power_side : power;
		}

		return 0;
	}

	/**
	 * Indicates whether block destruction should be suppressed when block is clicked.
	 * Will return true if player is holding a Carpenter's tool in creative mode.
	 */
	private boolean suppressDestroyBlock(EntityPlayer entityPlayer, ItemStack heldItem)
	{
		return	entityPlayer.capabilities.isCreativeMode &&
				heldItem != null &&
				(
					heldItem.getItem() == ItemHandler.itemCarpentersHammer ||
					heldItem.getItem() == ItemHandler.itemCarpentersChisel
				);
	}

	@Override
	/**
	 * Called when a player removes a block.
	 * This controls block break behavior when a player in creative mode left-clicks on block while holding a Carpenter's Hammer
	 */
	public boolean removeBlockByPlayer(World world, EntityPlayer entityPlayer, int x, int y, int z)
	{
		if (!suppressDestroyBlock(entityPlayer, entityPlayer.getHeldItem()))
			return world.setBlockToAir(x, y, z);

		onBlockClicked(world, x, y, z, entityPlayer);

		return false;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    /**
     * Spawn a digging particle effect in the world, this is a wrapper
     * around EffectRenderer.addBlockHitEffects to allow the block more
     * control over the particles. Useful when you have entirely different
     * texture sheets for different sides/locations in the world.
     *
     * @param world The current world
     * @param target The target the player is looking at {x/y/z/side/sub}
     * @param effectRenderer A reference to the current effect renderer.
     * @return True to prevent vanilla digging particles form spawning.
     */
    public boolean addBlockHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(target.blockX, target.blockY, target.blockZ);

		if (BlockProperties.hasCover(TE, 6))
		{
			Block block = BlockProperties.getCoverBlock(TE, 6);
			int metadata = world.getBlockMetadata(target.blockX, target.blockY, target.blockZ);

			double xOffset = target.blockX + world.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinX();
			double yOffset = target.blockY + world.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinY();
			double zOffset = target.blockZ + world.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinZ();

			switch (target.sideHit) {
			case 0:
				yOffset = target.blockY + block.getBlockBoundsMinY() - 0.1D;
				break;
			case 1:
				yOffset = target.blockY + block.getBlockBoundsMaxY() + 0.1D;
				break;
			case 2:
				zOffset = target.blockZ + block.getBlockBoundsMinZ() - 0.1D;
				break;
			case 3:
				zOffset = target.blockZ + block.getBlockBoundsMaxZ() + 0.1D;
				break;
			case 4:
				xOffset = target.blockX + block.getBlockBoundsMinX() - 0.1D;
				break;
			case 5:
				xOffset = target.blockX + block.getBlockBoundsMaxX() + 0.1D;
				break;
			}

			EntityDiggingFX particle = new EntityDiggingFX(world, xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D, block, metadata);
			effectRenderer.addEffect(particle.applyColourMultiplier(target.blockX, target.blockY, target.blockZ).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));

			return true;
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Renders block destruction effects.
	 * This is controlled to prevent block destroy effects if left-clicked with a Carpenter's Hammer while player is in creative mode.
	 * 
	 * Returns false to display effects.  True suppresses them (backwards).
	 */
	public boolean addBlockDestroyEffects(World world, int x, int y, int z, int metadata, EffectRenderer effectRenderer)
	{
		/*
		 * We don't have the ability to accurately determine
		 * the entity that is hitting the block.  So, instead
		 * we're guessing based on who is closest.  This should
		 * be adequate most of the time.
		 */

		if (world.getBlockId(x, y, z) == blockID)
		{
			EntityPlayer entityPlayer = world.getClosestPlayer(x, y, z, 6.5F);

			if (entityPlayer != null)
				return suppressDestroyBlock(entityPlayer, entityPlayer.getHeldItem());
		}

		return false;
	}

	@Override
	/**
	 * Returns light value based on cover or side covers.
	 */
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		Block block = blocksList[world.getBlockId(x, y, z)];

		if (block != null && block.blockID == blockID)
		{
			TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);
			int lightOutput = Block.lightValue[this.blockID];

			for (int side = 0; side < 7; ++side)
			{
				if (BlockProperties.hasCover(TE, side))
				{
					int tempLightOutput = lightValue[BlockProperties.getCoverID(TE, side)];

					if (tempLightOutput > lightOutput)
						lightOutput = tempLightOutput;
				}
			}

			return lightOutput;
		}

		return lightValue[blockID];
	}

	@Override
	/**
	 * Returns the block hardness at a location. Args: world, x, y, z
	 */
	public float getBlockHardness(World world, int x, int y, int z)
	{
		if (world.getBlockId(x, y, z) == blockID && !willCoverRecurse(world, x, y, z))
			return BlockProperties.getCoverBlock(world, 6, x, y, z).getBlockHardness(world,  x,  y,  z);

		return blockHardness;
	}

	@Override
	/**
	 * Chance that fire will spread and consume this block.
	 */
	public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection dir)
	{
		return blockFlammability[BlockProperties.getCoverBlock(world, 6, x, y, z).blockID];
	}

	@Override
	/**
	 * Called when fire is updating on a neighbor block.
	 */
	public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection dir)
	{
		return blockFlammability[BlockProperties.getCoverBlock(world, 6, x, y, z).blockID];
	}

	@Override
	/**
	 * Currently only called by fire when it is on top of this block.
	 * Returning true will prevent the fire from naturally dying during updating.
	 * Also prevents fire from dying from rain.
	 */
	public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side)
	{
		if (!willCoverRecurse(world, x, y, z)) {
			Block coverBlock = BlockProperties.getCoverBlock(world, 6, x, y, z);
			if (coverBlock.isBlockSolidOnSide(world, x, y, z, ForgeDirection.UP) && side == ForgeDirection.UP && coverBlock.isFireSource(world, x, y, z, metadata, side))
				return true;
		}

		return false;
	}

	@Override
	/**
	 * Location sensitive version of getExplosionRestance
	 */
	public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		if (!willCoverRecurse(world, x, y, z))
			return BlockProperties.getCoverBlock(world, 6, x, y, z).getExplosionResistance(entity);

		return this.getExplosionResistance(entity);
	}

	@Override
	/**
	 * Returns whether block is wood
	 */
	public boolean isWood(World world, int x, int y, int z)
	{
		if (!willCoverRecurse(world, x, y, z))
			return BlockProperties.getCoverBlock(world, 6, x, y, z).isWood(world,  x,  y,  z);

		return false;
	}

	/**
	 * Determines if this block is can be destroyed by the specified entities normal behavior.
	 */
	@Override
	public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity)
	{
		if (!willCoverRecurse(world, x, y, z))
		{
			int blockID = BlockProperties.getCoverBlock(world, 6, x, y, z).blockID;

			if (entity instanceof EntityWither) {
				return blockID != Block.bedrock.blockID && blockID != Block.endPortal.blockID && blockID != Block.endPortalFrame.blockID;
			} else if (entity instanceof EntityDragon) {
				return canDragonDestroy(world, x, y, z);
			}
		}

		return true;
	}

	@Override
	/**
	 * Determines if this block is destroyed when a ender dragon tries to fly through it.
	 * The block will be set to 0, nothing will drop.
	 */
	public boolean canDragonDestroy(World world, int x, int y, int z)
	{
		if (!willCoverRecurse(world, x, y, z))
			return BlockProperties.getCoverBlock(world, 6, x, y, z).canDragonDestroy(world, x, y, z);

		return super.canDragonDestroy(world, x, y, z);
	}

	@Override
	/**
	 * Ejects contained items into the world, and notifies neighbors of an update, as appropriate
	 */
	public void breakBlock(World world, int x, int y, int z, int blockID, int metadata)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

		if (TE != null) {
			for (int side = 0; side < 7; ++side)
				BlockProperties.clearAttributes(TE, side);

			auxiliaryBreakBlock(TE, world, x, y, z, blockID, metadata);
		}

		super.breakBlock(world, x, y, z, blockID, metadata);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		if (!willCoverRecurse(world, x, y, z))
			BlockProperties.getCoverBlock(world, 6, x, y, z).randomDisplayTick(world, x, y, z, random);

		if (world.getBlockId(x, y, z) == blockID)
		{
			TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

			if (BlockProperties.getOverlay(TE, 6) == OverlayHandler.OVERLAY_MYCELIUM)
				Block.mycelium.randomDisplayTick(world, x, y, z, random);
		}
	}

	@Override
	/**
	 * Determines if this block can support the passed in plant, allowing it to be planted and grow.
	 */
	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection dir, IPlantable plant)
	{
		TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

		Block srcBlock = BlockProperties.getCoverBlock(TE, 6);
		Block topBlock = BlockProperties.getCoverBlock(TE, 1);

		int baseOverlay = BlockProperties.getOverlay(TE, 6);
		int topOverlay = BlockProperties.getOverlay(TE, 1);

		boolean canSupportPlant = false;

		int tempBlockID = blockID;

		for (int count = 0; count < 4; ++count)
		{
			switch (count)
			{
			case 0:
				tempBlockID = srcBlock.blockID;
				break;
			case 1:
				tempBlockID = topBlock.blockID;
				break;
			case 2:
				tempBlockID = baseOverlay == OverlayHandler.OVERLAY_GRASS || topOverlay == OverlayHandler.OVERLAY_GRASS ? Block.grass.blockID : blockID;
				break;
			case 3:
				tempBlockID = baseOverlay == OverlayHandler.OVERLAY_MYCELIUM || topOverlay == OverlayHandler.OVERLAY_MYCELIUM ? Block.mycelium.blockID : blockID;
				break;
			}

			if (canSustainPlantWithBlockIdOverride(TE, world, x, y, z, tempBlockID, dir, plant))
				canSupportPlant = true;
		}

		return canSupportPlant && isBlockSolidOnSide(world, x, y, z, ForgeDirection.UP);
	}

	/**
	 * Copy of super function with block ID override.
	 */
	protected boolean canSustainPlantWithBlockIdOverride(TECarpentersBlock TE, World world, int x, int y, int z, int blockID, ForgeDirection dir, IPlantable plant)
	{
		if (FeatureHandler.enablePlantSupport)
		{
			int plantID = plant.getPlantID(world, x, y + 1, z);
			EnumPlantType plantType = plant.getPlantType(world, x, y + 1, z);

			if (
					plantID == cactus.blockID && blockID == cactus.blockID ||
					plantID == reed.blockID && blockID == reed.blockID ||
					plant instanceof BlockFlower && PlantHandler.canThisPlantGrowOnThisBlockID(blockID)
					)
				return true;

			switch (plantType)
			{
			case Desert: return blockID == sand.blockID;
			case Nether: return blockID == slowSand.blockID;
			case Crop:   return blockID == tilledField.blockID;
			case Cave:   return true;
			case Plains: return blockID == grass.blockID || blockID == dirt.blockID;
			case Water:  return BlockProperties.getCoverBlock(TE, 6).blockMaterial == Material.water && world.getBlockMetadata(x, y, z) == 0;
			case Beach:
				boolean isBeach = (blockID == Block.grass.blockID || blockID == Block.dirt.blockID || blockID == Block.sand.blockID);
				boolean hasWater = (world.getBlockMaterial(x - 1, y, z    ) == Material.water ||
						world.getBlockMaterial(x + 1, y, z    ) == Material.water ||
						world.getBlockMaterial(x,     y, z - 1) == Material.water ||
						world.getBlockMaterial(x,     y, z + 1) == Material.water);
				return isBeach && hasWater;
			}
		}

		return super.canSustainPlant(world, x, y, z, dir, plant);
	}

	/**
	 * Returns whether this block is considered solid.
	 */
	protected boolean isBlockSolid(World world, int x, int y, int z)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

		return !BlockProperties.hasCover(TE, 6) || BlockProperties.getCoverBlock(TE, 6).isOpaqueCube();
	}

	@Override
	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

		auxiliaryOnBlockPlacedBy(TE, world, x, y, z, entityLiving, itemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: world, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		if (extendsBlockBase(world, x, y, z))
		{
			ForgeDirection side_src = ForgeDirection.getOrientation(side);
			ForgeDirection side_adj = ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[side]);

			TECarpentersBlock TE_adj = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);
			TECarpentersBlock TE_src = (TECarpentersBlock) world.getBlockTileEntity(x + side_adj.offsetX, y + side_adj.offsetY, z + side_adj.offsetZ);

			if (TE_adj.getBlockType().isBlockSolidOnSide(TE_adj.worldObj, x, y, z, side_adj) == TE_src.getBlockType().isBlockSolidOnSide(TE_src.worldObj, x + side_adj.offsetX, y + side_adj.offsetY, z + side_adj.offsetZ, ForgeDirection.getOrientation(side))) {
				if (shareFaces(TE_adj, TE_src, side_adj, side_src)) {
					return BlockProperties.shouldRenderSharedFaceBasedOnCovers(TE_adj, TE_src);
				}
			}
		}

		return super.shouldSideBeRendered(world, x, y, z, side);
	}

	/**
	 * Returns whether two blocks share faces.
	 * Primarily for slopes, stairs and slabs.
	 */
	protected boolean shareFaces(TECarpentersBlock TE_adj, TECarpentersBlock TE_src, ForgeDirection side_adj, ForgeDirection side_src)
	{
		return	TE_adj.getBlockType().isBlockSolidOnSide(TE_adj.worldObj, TE_adj.xCoord, TE_adj.yCoord, TE_adj.zCoord, side_adj) &&
				TE_src.getBlockType().isBlockSolidOnSide(TE_src.worldObj, TE_src.xCoord, TE_src.yCoord, TE_src.zCoord, side_src);
	}

	@Override
	/**
	 * Determines if this block should render in this pass.
	 */
	public boolean canRenderInPass(int pass)
	{
		ForgeHooksClient.setRenderPass(pass);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns which pass this block be rendered on. 0 for solids and 1 for alpha.
	 */
	public int getRenderBlockPass()
	{
		return 1;
	}

	@Override
	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World world, int x, int y, int z)
	{
		world.setBlockTileEntity(x, y, z, createNewTileEntity(world));
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TECarpentersBlock();
	}

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}

	protected void auxiliaryOnBlockClicked(TECarpentersBlock TE, World world, int x, int y, int z, EntityPlayer entityPlayer) {}

	protected void auxiliaryOnBlockPlacedBy(TECarpentersBlock TE, World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {}

	protected boolean auxiliaryOnBlockActivated(TECarpentersBlock TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	protected void auxiliaryBreakBlock(TECarpentersBlock TE, World world, int x, int y, int z, int var5, int var6) {}

	protected boolean onHammerLeftClick(TECarpentersBlock TE, EntityPlayer entityPlayer)
	{
		return false;
	}

	protected boolean onHammerRightClick(TECarpentersBlock TE, EntityPlayer entityPlayer, int side, float hitX, float hitZ)
	{
		return false;
	}
	
	protected void damageHammerWithChance(World world, EntityPlayer entityPlayer)
	{
		entityPlayer.getCurrentEquippedItem().damageItem(1, entityPlayer);
	}

	protected boolean canCoverSide(TECarpentersBlock TE, World world, int x, int y, int z, int side)
	{
		return false;
	}

	protected void auxiliaryOnNeighborBlockChange(TECarpentersBlock TE, World world, int x, int y, int z, int blockID) {}

}
