package carpentersblocks.block;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Bed;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BedDesignHandler;
import carpentersblocks.util.handler.BlockHandler;
import carpentersblocks.util.handler.ItemHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersBed extends BlockBase
{

	public BlockCarpentersBed(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.4F);
		setUnlocalizedName("blockCarpentersBed");
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		func_111022_d("carpentersblocks:general/generic");
	}

	/**
	 * Determines if this block is classified as a Bed, Allowing
	 * players to sleep in it, though the block has to specifically
	 * perform the sleeping functionality in it's activated event.
	 */
	@Override
	public boolean isBed(World world, int x, int y, int z, EntityLivingBase entityLiving)
	{
		return true;
	}

	@Override
	/**
	 * Cycle backward through bed designs.
	 */
	protected boolean onHammerLeftClick(TECarpentersBlock TE, EntityPlayer entityPlayer)
	{
		int data = BlockProperties.getData(TE);
		int design = BedDesignHandler.getPrev(Bed.getDesign(data));

		Bed.setDesign(TE, design);

		TECarpentersBlock TE_opp = Bed.getOppositeTE(TE);

		if (TE_opp != null)
			Bed.setDesign(TE_opp, design);

		return true;
	}

	@Override
	/**
	 * Cycle forward through bed designs.
	 */
	protected boolean onHammerRightClick(TECarpentersBlock TE, EntityPlayer entityPlayer, int side)
	{
		int data = BlockProperties.getData(TE);
		int design = BedDesignHandler.getNext(Bed.getDesign(data));

		Bed.setDesign(TE, design);

		TECarpentersBlock TE_opp = Bed.getOppositeTE(TE);

		if (TE_opp != null)
			Bed.setDesign(TE_opp, design);

		return true;
	}

	@Override
	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean auxiliaryOnBlockActivated(TECarpentersBlock TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			ForgeDirection dir = Bed.getDirection(TE);

			if (!isBedFoot(world, x, y, z))
			{
				x -= dir.offsetX;
				z -= dir.offsetZ;

				if (world.getBlockId(x, y, z) != blockID)
					return true;
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

							if (chunkCoordinates.posX == x && chunkCoordinates.posY == y && chunkCoordinates.posZ == z)
								entityPlayer1 = entityPlayer2;
						}
					}

					if (entityPlayer1 != null)
					{
						entityPlayer.addChatMessage("tile.bed.occupied");
						return true;
					}

					setBedOccupied(world, x, y, z, entityPlayer, false);
				}

				EnumStatus enumstatus = entityPlayer.sleepInBedAt(x, y, z);

				if (enumstatus == EnumStatus.OK)
				{
					setBedOccupied(world, x, y, z, entityPlayer, true);
					return true;
				}
				else
				{
					if (enumstatus == EnumStatus.NOT_POSSIBLE_NOW) {
						entityPlayer.addChatMessage("tile.bed.noSleep");
					} else if (enumstatus == EnumStatus.NOT_SAFE) {
						entityPlayer.addChatMessage("tile.bed.notSafe");
					}

					return true;
				}
			}
			else
			{
				double xOffset = x + 0.5D;
				double yOffset = y + 0.5D;
				double zOffset = z + 0.5D;
				world.setBlockToAir(x, y, z);
				x -= dir.offsetX;
				z -= dir.offsetZ;

				if (world.getBlockId(x, y, z) == blockID)
				{
					world.setBlockToAir(x, y, z);
					xOffset = (xOffset + x + 0.5D) / 2.0D;
					yOffset = (yOffset + y + 0.5D) / 2.0D;
					zOffset = (zOffset + z + 0.5D) / 2.0D;
				}

				world.newExplosion((Entity)null, x + 0.5F, y + 0.5F, z + 0.5F, 5.0F, true, true);
				return true;
			}
		}

		return true;
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	@Override
	protected void auxiliaryOnNeighborBlockChange(TECarpentersBlock TE, World world, int x, int y, int z, int blockID)
	{
		ForgeDirection dir = Bed.getDirection(TE);

		if (isBedFoot(world, x, y, z)) {
			if (world.getBlockId(x + dir.offsetX, y, z + dir.offsetZ) != this.blockID) {
				world.setBlockToAir(x, y, z);
			}
		} else if (world.getBlockId(x - dir.offsetX, y, z - dir.offsetZ) != this.blockID) {
			world.setBlockToAir(x, y, z);
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	@Override
	public int idDropped(int par1, Random random, int par3)
	{
		return ItemHandler.itemCarpentersBedID;
	}

	@Override
	/**
	 * Called when a user either starts or stops sleeping in the bed.
	 */
	public void setBedOccupied(World world, int x, int y, int z, EntityPlayer entityPlayer, boolean isOccupied)
	{
    	TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);
    	Bed.setOccupied(TE, isOccupied);

		TECarpentersBlock TE_opp = Bed.getOppositeTE(TE);

		Bed.setOccupied(TE, isOccupied);

		if (TE_opp != null)
			Bed.setOccupied(TE_opp, isOccupied);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Only called by clickMiddleMouseButton, and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World world, int x, int y, int z)
	{
		return ItemHandler.itemCarpentersBedID;
	}

	@Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockHandler.carpentersBedRenderID;
	}

}
