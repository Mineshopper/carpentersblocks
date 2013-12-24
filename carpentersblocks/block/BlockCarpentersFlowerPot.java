package carpentersblocks.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.FlowerPot;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersFlowerPot;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.flowerpot.FlowerPotDesignHandler;
import carpentersblocks.util.flowerpot.FlowerPotHandler;
import carpentersblocks.util.flowerpot.FlowerPotHandler.Profile;
import carpentersblocks.util.flowerpot.FlowerPotProperties;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersFlowerPot extends BlockBase {

	public BlockCarpentersFlowerPot(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.5F);
		setStepSound(soundPowderFootstep);
		setUnlocalizedName("blockCarpentersFlowerPot");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		setTextureName("carpentersblocks:general/solid");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		for (int numIcon = 0; numIcon < FlowerPotDesignHandler.maxNum; ++numIcon) {
			if (FlowerPotDesignHandler.hasDesign[numIcon]) {
				IconRegistry.icon_flower_pot_design[numIcon] = iconRegister.registerIcon("carpentersblocks:flowerpot/design/design_" + numIcon);
			}
		}

		IconRegistry.icon_flower_pot = iconRegister.registerIcon("carpentersblocks:flowerpot/flower_pot");
		IconRegistry.icon_flower_pot_glass = iconRegister.registerIcon("carpentersblocks:flowerpot/flower_pot_glass");

		super.registerIcons(iconRegister);
	}

	@SideOnly(Side.CLIENT)
	@Override
	/**
	 * Returns the icon on the side given the block metadata.
	 */
	public Icon getIcon(int side, int metadata)
	{
		return IconRegistry.icon_flower_pot;
	}

	@Override
	/**
	 * Cycle backward through bed designs.
	 * Sneak-click removes plant and/or soil.
	 */
	protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
	{
		if (entityPlayer.isSneaking()) {

			if (EventHandler.hitY > 0.375F) {

				if (FlowerPotProperties.hasPlant(TE)) {
					FlowerPotProperties.setPlant((TECarpentersFlowerPot)TE, (ItemStack)null);
					return true;
				}

			} else {

				if (FlowerPotProperties.hasSoil(TE))
				{
					boolean soilAreaClicked = EventHandler.eventFace == 1 && EventHandler.hitX > 0.375F && EventHandler.hitX < 0.625F && EventHandler.hitZ > 0.375F && EventHandler.hitZ < 0.625F;

					if (soilAreaClicked || !BlockProperties.hasAttribute(TE, 6))
					{
						if (FlowerPotProperties.hasPlant(TE)) {
							FlowerPotProperties.setPlant((TECarpentersFlowerPot)TE, (ItemStack)null);
						}

						FlowerPotProperties.setSoil((TECarpentersFlowerPot)TE, (ItemStack)null);
						return true;
					}
				}

			}

		} else {

			int design = FlowerPotDesignHandler.getPrev(FlowerPot.getDesign(TE));
			FlowerPot.setDesign(TE, design);
			return true;

		}

		return false;
	}

	@Override
	/**
	 * Cycle forward through bed designs.
	 */
	protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer, int side)
	{
		if (entityPlayer.isSneaking()) {
			FlowerPot.setDesign(TE, 0);
		} else {
			int design = FlowerPotDesignHandler.getNext(FlowerPot.getDesign(TE));
			FlowerPot.setDesign(TE, design);
		}

		return true;
	}

	@Override
	protected boolean[] preOnBlockActivated(TEBase TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		ItemStack itemStack = entityPlayer.getCurrentEquippedItem();

		List<Boolean> altered = new ArrayList<Boolean>();
		List<Boolean> decInv = new ArrayList<Boolean>();

		if (itemStack != null)
		{
			boolean hasCover = BlockProperties.hasCover(TE, 6);
			boolean hasOverlay = BlockProperties.hasOverlay(TE, 6);
			boolean soilAreaClicked = side == 1 && hitX > 0.375F && hitX < 0.625F && hitZ > 0.375F && hitZ < 0.625F;

			if (FlowerPotProperties.hasSoil(TE)) {

				/*
				 * Leaf blocks can be plants or covers.  We need to differentiate
				 * it based on where the block is clicked, and whether it already
				 * has a cover.
				 */
				if (!soilAreaClicked) {
					if (!hasCover && BlockProperties.isCover(itemStack) || !hasOverlay && BlockProperties.isOverlay(itemStack)) {
						return new boolean[] { false, false };
					}
				}

				if (!FlowerPotProperties.hasPlant(TE))
				{
					if (FlowerPotProperties.isPlant(itemStack))
					{
						FlowerPotProperties.setPlant((TECarpentersFlowerPot)TE, itemStack);
						altered.add(true);
						decInv.add(true);
					}
				}

			} else {

				if (FlowerPotProperties.isSoil(itemStack))
				{
					if (hasCover || soilAreaClicked)
					{
						FlowerPotProperties.setSoil((TECarpentersFlowerPot)TE, itemStack);
						altered.add(true);
						decInv.add(true);
					}
				}

			}
		}

		if (altered.contains(true)) {

			BlockProperties.playBlockSound(TE, BlockProperties.getCoverBlock(TE, 6));

		}

		return new boolean[] { altered.contains(true), decInv.contains(true) };
	}

	@Override
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		if (!world.isRemote)
		{
			if (!canPlaceBlockAt(world, x, y, z)) {
				dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
				world.setBlockToAir(x, y, z);
			} else {
				super.onNeighborBlockChange(world, x, y, z, blockID);
			}
		}

		super.onNeighborBlockChange(world, x, y, z, blockID);
	}

	@Override
	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		int blockID = world.getBlockId(x, y - 1, z);

		return	blockID > 0 &&
				(Block.blocksList[blockID].isBlockSolidOnSide(world, x, y - 1, z, ForgeDirection.UP) || Block.blocksList[blockID].canPlaceTorchOnTop(world, x, y - 1, z));
	}

	@Override
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TECarpentersFlowerPot TE = (TECarpentersFlowerPot) world.getBlockTileEntity(x, y, z);

		if (FlowerPotProperties.hasPlant(TE)) {

			Profile profile = FlowerPotHandler.getPlantProfile(TE);

			if (profile.equals(Profile.CACTUS) || profile.equals(Profile.LEAVES)) {
				setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.99F, 0.6875F);
				return;
			} else {
				setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.75F, 0.6875F);
			}

		} else {

			setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.375F, 0.6875F);

		}
	}

	@Override
	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
	 * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
	 */
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
	{
		setBlockBoundsBasedOnState(world, x, y, z);

		super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
	}

	@Override
	/**
	 * Returns light value based on plant and soil in pot.
	 */
	public int auxiliaryGetLightValue(TEBase TE_base, IBlockAccess blockAccess, int x, int y, int z)
	{
		if (TE_base != null && TE_base instanceof TECarpentersFlowerPot)
		{
			int temp_lightValue = lightValue[blockID];

			if (FlowerPotProperties.hasSoil(TE_base))
			{
				int soil_lightValue = lightValue[FlowerPotProperties.getSoilBlock(TE_base).blockID];

				if (soil_lightValue > temp_lightValue) {
					temp_lightValue = soil_lightValue;
				}
			}

			if (FlowerPotProperties.hasPlant(TE_base))
			{
				int plant_lightValue = lightValue[FlowerPotProperties.getPlantBlock(TE_base).blockID];

				if (plant_lightValue > temp_lightValue) {
					temp_lightValue = plant_lightValue;
				}
			}

			return temp_lightValue;
		}

		return 0;
	}

	@Override
	/**
	 * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
	 */
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

		if (TE != null && TE instanceof TECarpentersFlowerPot)
		{
			if (FlowerPotProperties.hasPlant(TE))
			{
				world.setBlockMetadataWithNotify(x, y, z, FlowerPotProperties.getPlantMetadata((TECarpentersFlowerPot)TE), 4);
				FlowerPotProperties.getPlantBlock(TE).onEntityCollidedWithBlock(world, x, y, z, entity);
				world.setBlockMetadataWithNotify(x, y, z, BlockProperties.getCoverMetadata(TE, 6), 4);
			}
		}

		super.onEntityCollidedWithBlock(world, x, y, z, entity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true only if block is flowerPot
	 */
	public boolean isFlowerPot()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		if (world.getBlockId(x, y, z) == blockID)
		{
			TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

			if (TE != null && TE instanceof TECarpentersFlowerPot)
			{
				/*
				 * Metadata at coordinates are for the base cover only.
				 * We need to set it for appropriate attributes in order
				 * to get accurate results.
				 */
				if (FlowerPotProperties.hasPlant(TE))
				{
					world.setBlockMetadataWithNotify(x, y, z, FlowerPotProperties.getPlantMetadata((TECarpentersFlowerPot) TE), 4);
					FlowerPotProperties.getPlantBlock(TE).randomDisplayTick(world, x, y, z, random);
					world.setBlockMetadataWithNotify(x, y, z, BlockProperties.getCoverMetadata(TE, 6), 4);
				}
				if (FlowerPotProperties.hasSoil(TE))
				{
					world.setBlockMetadataWithNotify(x, y, z, FlowerPotProperties.getSoilMetadata((TECarpentersFlowerPot) TE), 4);
					FlowerPotProperties.getSoilBlock(TE).randomDisplayTick(world, x, y, z, random);
					world.setBlockMetadataWithNotify(x, y, z, BlockProperties.getCoverMetadata(TE, 6), 4);
				}
			}
		}

		super.randomDisplayTick(world, x, y, z, random);
	}

	/**
	 * Ejects contained items into the world, and notifies neighbors of an update, as appropriate
	 */
	@Override
	public void breakBlock(World world, int x, int y, int z, int var5, int metadata)
	{
		TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

		if (TE != null && TE instanceof TECarpentersFlowerPot)
		{
			if (FlowerPotProperties.hasPlant(TE)) {
				FlowerPotProperties.setPlant((TECarpentersFlowerPot)TE, (ItemStack)null);
			}
			if (FlowerPotProperties.hasSoil(TE)) {
				FlowerPotProperties.setSoil((TECarpentersFlowerPot)TE, (ItemStack)null);
			}
		}

		super.breakBlock(world, x, y, z, var5, metadata);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TECarpentersFlowerPot();
	}

	@Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockRegistry.carpentersFlowerPotRenderID;
	}

}