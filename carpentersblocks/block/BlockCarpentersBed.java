package carpentersblocks.block;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Bed;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersBed;
import carpentersblocks.util.bed.BedDesignHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersBed extends BlockBase {

	public BlockCarpentersBed(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.4F);
		setUnlocalizedName("blockCarpentersBed");
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		setTextureName("carpentersblocks:general/solid");
	}

	@SideOnly(Side.CLIENT)
	@Override
	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister iconRegister)
	{
		// Bed design icons
		for (int numIcon = 0; numIcon < BedDesignHandler.maxNum; ++numIcon) {
			if (BedDesignHandler.hasPillow[numIcon]) {
				IconRegistry.icon_bed_pillow_custom[numIcon] = iconRegister.registerIcon("carpentersblocks:bed/design_" + numIcon + "/pillow");
			}
		}

		IconRegistry.icon_bed_pillow = iconRegister.registerIcon("carpentersblocks:bed/bed_pillow");

		super.registerIcons(iconRegister);
	}

	@Override
	/**
	 * Determines if this block is classified as a Bed, Allowing
	 * players to sleep in it, though the block has to specifically
	 * perform the sleeping functionality in it's activated event.
	 */
	public boolean isBed(World world, int x, int y, int z, EntityLivingBase entityLiving)
	{
		return true;
	}

	@Override
	/**
	 * Cycle backward through bed designs.
	 */
	protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
	{
		int design = BedDesignHandler.getPrev(Bed.getDesign(TE));

		Bed.setDesign(TE, design);

		TEBase TE_opp = Bed.getOppositeTE(TE);

		if (TE_opp != null) {
			Bed.setDesign(TE_opp, design);
		}

		return true;
	}

	@Override
	/**
	 * Cycle forward through bed designs.
	 */
	protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitZ)
	{
		int design = BedDesignHandler.getNext(Bed.getDesign(TE));

		Bed.setDesign(TE, design);

		TEBase TE_opp = Bed.getOppositeTE(TE);

		if (TE_opp != null) {
			Bed.setDesign(TE_opp, design);
		}

		return true;
	}

	@Override
	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean[] postOnBlockActivated(TEBase TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{

			if (!Bed.isHeadOfBed(TE))
			{
				TEBase TE_opp = Bed.getOppositeTE(TE);

				if (TE_opp != null) {
					x = TE_opp.xCoord;
					z = TE_opp.zCoord;
				} else {
					return new boolean[] { true, false };
				}
			}

			if (world.provider.canRespawnHere() && world.getBiomeGenForCoords(x, z) != BiomeGenBase.hell)
			{
				if (Bed.isOccupied(TE))
				{
					EntityPlayer entityPlayer1 = null;
					Iterator iterator = world.playerEntities.iterator();

					while (iterator.hasNext())
					{
						EntityPlayer entityPlayer2 = (EntityPlayer)iterator.next();

						if (entityPlayer2.isPlayerSleeping())
						{
							ChunkCoordinates chunkCoordinates = entityPlayer2.playerLocation;

							if (chunkCoordinates.posX == x && chunkCoordinates.posY == y && chunkCoordinates.posZ == z) {
								entityPlayer1 = entityPlayer2;
							}
						}
					}

					if (entityPlayer1 != null)
					{
						entityPlayer.addChatMessage("tile.bed.occupied");
						return new boolean[] { true, false };
					}

					setBedOccupied(world, x, y, z, entityPlayer, false);
				}

				EnumStatus enumstatus = entityPlayer.sleepInBedAt(x, y, z);

				if (enumstatus == EnumStatus.OK)
				{
					setBedOccupied(world, x, y, z, entityPlayer, true);
					return new boolean[] { true, false };
				}
				else
				{
					if (enumstatus == EnumStatus.NOT_POSSIBLE_NOW) {
						entityPlayer.addChatMessage("tile.bed.noSleep");
					} else if (enumstatus == EnumStatus.NOT_SAFE) {
						entityPlayer.addChatMessage("tile.bed.notSafe");
					}

					return new boolean[] { true, false };
				}
			}
			else
			{
				world.setBlockToAir(x, y, z);
				world.newExplosion((Entity)null, x + 0.5F, y + 0.5F, z + 0.5F, 5.0F, true, true);
				return new boolean[] { true, false };
			}
		}

		return new boolean[] { true, false };
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
			TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

			if (TE != null) {
				if (Bed.getOppositeTE(TE) == null) {
					world.setBlockToAir(x, y, z);
				}
			}
		}

		super.onNeighborBlockChange(world, x, y, z, blockID);
	}

	@Override
	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, Random random, int par3)
	{
		return ItemRegistry.itemCarpentersBedID;
	}

	@Override
	/**
	 * Called when a user either starts or stops sleeping in the bed.
	 */
	public void setBedOccupied(World world, int x, int y, int z, EntityPlayer entityPlayer, boolean isOccupied)
	{
		TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);
		Bed.setOccupied(TE, isOccupied);

		TEBase TE_opp = Bed.getOppositeTE(TE);

		if (TE_opp != null) {
			Bed.setOccupied(TE_opp, isOccupied);
		}
	}

	@Override
	/**
	 * Returns the direction of the block. Same values that
	 * are returned by BlockDirectional
	 *
	 * @param world The current world
	 * @param x X Position
	 * @param y Y Position
	 * @param z Z Position
	 * @return Bed direction
	 */
	public int getBedDirection(IBlockAccess world, int x, int y, int z)
	{
		TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

		switch (Bed.getDirection(TE))
		{
		case NORTH:
			return 0;
		case SOUTH:
			return 2;
		case WEST:
			return 3;
		default:
			return 1;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Only called by clickMiddleMouseButton, and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World world, int x, int y, int z)
	{
		return ItemRegistry.itemCarpentersBedID;
	}

	/**
	 * Get the rotations that can apply to the block at the specified coordinates. Null means no rotations are possible.
	 * Note, this is up to the block to decide. It may not be accurate or representative.
	 * @param worldObj The world
	 * @param x X position
	 * @param y Y position
	 * @param z Z position
	 * @return An array of valid axes to rotate around, or null for none or unknown
	 */
	@Override
	public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z)
	{
		return new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TECarpentersBed();
	}

	@Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockRegistry.carpentersBedRenderID;
	}

}
