package carpentersblocks.util.flowerpot;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import carpentersblocks.data.FlowerPot;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersFlowerPot;
import carpentersblocks.util.BlockProperties;

public class FlowerPotProperties {

	/**
	 * Ejects an item at given coordinates.
	 */
	public static void ejectEntity(TEBase TE, ItemStack itemStack)
	{
		BlockProperties.ejectEntity(TE, FlowerPotHandler.getFilteredItem(itemStack));
	}

	/**
	 * Returns whether flower pot has a design.
	 */
	public final static boolean hasDesign(TEBase TE)
	{
		return FlowerPot.getDesign(TE) > 0;
	}

	/**
	 * Returns soil block ID.
	 */
	public static int getSoilID(TECarpentersFlowerPot TE)
	{
		return TE.soil & 0xfff;
	}

	/**
	 * Returns soil block metadata.
	 */
	public static int getSoilMetadata(TECarpentersFlowerPot TE)
	{
		return (TE.soil & 0xf000) >>> 12;
	}

	/**
	 * Returns plant block ID.
	 */
	public static int getPlantID(TECarpentersFlowerPot TE)
	{
		return TE.plant & 0xfff;
	}

	/**
	 * Returns plant block metadata.
	 */
	public static int getPlantMetadata(TECarpentersFlowerPot TE)
	{
		return (TE.plant & 0xf000) >>> 12;
	}

	/**
	 * Returns soil block.
	 */
	public final static Block getSoilBlock(TEBase TE)
	{
		return Block.blocksList[getSoilID((TECarpentersFlowerPot)TE)];
	}

	/**
	 * Returns plant block.
	 */
	public final static Block getPlantBlock(TEBase TE)
	{
		return Block.blocksList[getPlantID((TECarpentersFlowerPot)TE)];
	}

	/**
	 * Returns whether pot has soil.
	 */
	public final static boolean hasSoil(TEBase TE)
	{
		int blockID = getSoilID((TECarpentersFlowerPot)TE);
		int metadata = getSoilMetadata((TECarpentersFlowerPot)TE);

		return	blockID > 0 &&
				Block.blocksList[blockID] != null &&
				isSoil(new ItemStack(blockID, 1, metadata));
	}

	/**
	 * Returns whether pot has plant.
	 */
	public static boolean hasPlant(TEBase TE)
	{
		int blockID = getPlantID((TECarpentersFlowerPot)TE);
		int metadata = getPlantMetadata((TECarpentersFlowerPot)TE);

		return	blockID > 0 &&
				Block.blocksList[blockID] != null &&
				isPlant(new ItemStack(blockID, 1, metadata));
	}

	/**
	 * Returns whether ItemStack contains soil.
	 */
	public static boolean isSoil(ItemStack itemStack)
	{
		if (itemStack.getItem() instanceof ItemBlock)
		{
			Block block = Block.blocksList[itemStack.itemID];

			return	!block.hasTileEntity(itemStack.getItemDamage()) &&
					(block.blockMaterial == Material.grass || block.blockMaterial == Material.ground || block.blockMaterial == Material.sand);
		}

		return false;
	}

	/**
	 * Returns whether ItemStack contains a plant.
	 */
	public static boolean isPlant(ItemStack itemStack)
	{
		itemStack = FlowerPotHandler.getEquivalentBlock(itemStack);

		if (itemStack.getItem() instanceof ItemBlock)
		{
			Block block = Block.blocksList[itemStack.itemID];
			return block instanceof IPlantable || block instanceof IShearable;
		}

		return false;
	}

	/**
	 * Sets soil block.
	 */
	public static boolean setSoil(TECarpentersFlowerPot TE, ItemStack itemStack)
	{
		if (hasSoil(TE)) {
			ejectEntity(TE, new ItemStack(getSoilID(TE), 1, getSoilMetadata(TE)));
		}

		int blockID = itemStack == null ? 0 : itemStack.itemID;
		int metadata = itemStack == null ? 0 : itemStack.getItemDamage();

		TE.soil = (short) (blockID + (metadata << 12));

		TE.worldObj.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, blockID);
		TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);

		return true;
	}

	/**
	 * Sets plant block.
	 */
	public static boolean setPlant(TECarpentersFlowerPot TE, ItemStack itemStack)
	{
		if (hasPlant(TE)) {
			ejectEntity(TE, new ItemStack(getPlantID(TE), 1, getPlantMetadata(TE)));
		}

		itemStack = FlowerPotHandler.getEquivalentBlock(itemStack);

		int blockID = itemStack == null ? 0 : itemStack.itemID;
		int metadata = itemStack == null ? 0 : itemStack.getItemDamage();

		TE.plant = (short) (blockID + (metadata << 12));

		TE.worldObj.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, blockID);
		TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);

		return true;
	}

}