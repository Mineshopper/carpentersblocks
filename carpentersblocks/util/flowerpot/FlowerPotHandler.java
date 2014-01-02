package carpentersblocks.util.flowerpot;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.IShearable;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersFlowerPot;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class FlowerPotHandler {

	private final static Map<String, Profile> plantProfile = new HashMap<String, Profile>();

	public enum Profile {
		REDUCED_SCALE_YP,
		REDUCED_SCALE_YN,
		TRUE_SCALE,
		THIN_YP,
		THIN_YN,
		CACTUS,
		LEAVES
	}

	/**
	 * Initializes plant profiles.
	 */
	public static void initPlantProfiles()
	{
		/* Vanilla */

		plantProfile.put("unknown:tile.tallgrass.grass", Profile.THIN_YP);
		plantProfile.put("unknown:tile.tallgrass.fern", Profile.REDUCED_SCALE_YP);
		plantProfile.put("unknown:tile.deadbush", Profile.REDUCED_SCALE_YP);
		plantProfile.put("unknown:tile.cactus", Profile.CACTUS);
		plantProfile.put("unknown:tile.mushroom", Profile.TRUE_SCALE);
		plantProfile.put("unknown:tile.flower", Profile.TRUE_SCALE);
		plantProfile.put("unknown:tile.rose", Profile.TRUE_SCALE);
		plantProfile.put("unknown:tile.reeds", Profile.THIN_YP);
		plantProfile.put("unknown:tile.carrots", Profile.THIN_YP);
		plantProfile.put("unknown:tile.potatoes", Profile.THIN_YP);
		plantProfile.put("unknown:tile.crops", Profile.THIN_YP);

		/* Weee! Flowers */

		plantProfile.put("unknown:orangeflower", Profile.TRUE_SCALE);
		plantProfile.put("unknown:orangemoonflower", Profile.TRUE_SCALE);
		plantProfile.put("unknown:purpleflower", Profile.THIN_YP);
		plantProfile.put("unknown:yellowmoonflower", Profile.THIN_YP);
		plantProfile.put("unknown:pinkmoonflower", Profile.THIN_YP);
		plantProfile.put("unknown:darkgreymoonflower", Profile.THIN_YP);
		plantProfile.put("unknown:lightgreymoonflower", Profile.THIN_YP);
		plantProfile.put("unknown:purplemoonflower", Profile.THIN_YP);
		plantProfile.put("unknown:brownmoonflower", Profile.THIN_YP);
		plantProfile.put("unknown:redmoonflower", Profile.THIN_YP);

		/* Harvestcraft */

		plantProfile.put("unknown:tile.PamHarvestCraft:strawberrycrop_2", Profile.THIN_YP);
		plantProfile.put("unknown:tile.PamHarvestCraft:cranberrycrop_2", Profile.THIN_YP);
		plantProfile.put("unknown:tile.PamHarvestCraft:whitemushroomcrop_2", Profile.THIN_YP);

		/* Biomes O' Plenty */

		plantProfile.put("BiomesOPlenty:tile.bop.foliage.poisonivy", Profile.REDUCED_SCALE_YP);
		plantProfile.put("BiomesOPlenty:tile.bop.flowers.swampflower", Profile.THIN_YP);
		plantProfile.put("BiomesOPlenty:tile.bop.flowers.violet", Profile.THIN_YP);
		plantProfile.put("BiomesOPlenty:tile.bop.flowers.anemone", Profile.THIN_YP);
		plantProfile.put("BiomesOPlenty:tile.bop.flowers2.bluebells", Profile.THIN_YP);
		plantProfile.put("BiomesOPlenty:tile.bop.coral.bluecoral", Profile.THIN_YP);
		plantProfile.put("BiomesOPlenty:tile.bop.plants.thorn", Profile.REDUCED_SCALE_YP);
		plantProfile.put("BiomesOPlenty:tile.bop.treeMoss", Profile.REDUCED_SCALE_YP);
		plantProfile.put("BiomesOPlenty:tile.bop.mushrooms.portobello", Profile.TRUE_SCALE);
		plantProfile.put("BiomesOPlenty:tile.bop.mushrooms.bluemilk", Profile.TRUE_SCALE);
		plantProfile.put("BiomesOPlenty:tile.bop.mushrooms.flatmushroom", Profile.TRUE_SCALE);
		plantProfile.put("BiomesOPlenty:tile.bop.stoneFormations.stalactite", Profile.THIN_YN);

		/* Natura */

		plantProfile.put("Natura:block.sapling.blood", Profile.REDUCED_SCALE_YN);
		plantProfile.put("Natura:block.glowshroom.green", Profile.TRUE_SCALE);
		plantProfile.put("Natura:block.glowshroom.blue", Profile.TRUE_SCALE);
		plantProfile.put("Natura:block.glowshroom.purple", Profile.TRUE_SCALE);

		/* ExtraBiomesXL */

		plantProfile.put("ExtrabiomesXL:tile.extrabiomes.cattail", Profile.THIN_YP);
		plantProfile.put("ExtrabiomesXL:tile.extrabiomes.flower.2", Profile.THIN_YP);
		plantProfile.put("ExtrabiomesXL:tile.extrabiomes.flower.5", Profile.THIN_YP);
		plantProfile.put("ExtrabiomesXL:tile.extrabiomes.flower.6", Profile.THIN_YP);
		plantProfile.put("ExtrabiomesXL:tile.extrabiomes.flower.7", Profile.THIN_YP);
	}

	/**
	 * Returns the plant profile to indicate which render method to use.
	 */
	public static Profile getPlantProfile(TEBase TE)
	{
		Block block = FlowerPotProperties.getPlantBlock(TE);
		int metadata = FlowerPotProperties.getPlantMetadata((TECarpentersFlowerPot)TE);

		String name = getFullUnlocalizedName(new ItemStack(block, 1, metadata));

		if (plantProfile.containsKey(name)) {
			return plantProfile.get(name);
		} else if (block.blockMaterial.equals(Material.leaves) || block.blockMaterial.equals(Material.cactus)) {
			return Profile.LEAVES;
		} else if (block.blockMaterial == Material.vine) {
			return Profile.THIN_YP;
		} else {
			return Profile.REDUCED_SCALE_YP;
		}
	}

	public static Icon getPlantIcon(ItemStack itemStack)
	{
		int metadata = itemStack.getItemDamage();

		// Override BoP's sunflower metadata so it uses the top of the flower.
		if (getFullUnlocalizedName(itemStack).equals("BiomesOPlenty:tile.bop.flowers.sunflowerbottom")) {
			metadata = 14;
		}

		return Block.blocksList[itemStack.itemID].getIcon(2, metadata);
	}

	/**
	 * Returns an unabridged, unlocalized name for item prefixed with
	 * a modId, if applicable.
	 */
	public static String getFullUnlocalizedName(ItemStack itemStack)
	{
		UniqueIdentifier uId = GameRegistry.findUniqueIdentifierFor(Item.itemsList[itemStack.itemID]);

		StringBuilder string = new StringBuilder();
		string.append(uId == null ? "unknown" : uId.modId);
		string.append(":");
		string.append(itemStack.getItem().getUnlocalizedNameInefficiently(itemStack));

		return string.toString();
	}

	/**
	 * Returns equivalent block that represents item.
	 * If no equivalent exists, returns original ItemStack.
	 */
	public static ItemStack getEquivalentBlock(ItemStack itemStack)
	{
		if (itemStack != null)
		{
			Item item = itemStack.getItem();

			if (item.equals(Item.reed)) {
				return new ItemStack(Block.reed, 1, 0);
			} else if (item.equals(Item.wheat)) {
				return new ItemStack(Block.crops, 1, 7);
			} else if (item.equals(Item.carrot)) {
				return new ItemStack(Block.carrot, 1, 7);
			} else if (item.equals(Item.potato)) {
				return new ItemStack(Block.potato, 1, 7);
			}
		}

		return itemStack;
	}

	/**
	 * Converts ItemStack block into appropriate Item where necessary.
	 */
	public static ItemStack getFilteredItem(ItemStack itemStack)
	{
		if (itemStack != null)
		{
			Block block = Block.blocksList[itemStack.itemID];

			if (block instanceof IShearable) {

				return itemStack;

			} else {

				int idDropped = Block.blocksList[itemStack.itemID].idDropped(itemStack.getItemDamage(), null, 0);

				if (idDropped > 0) {
					return new ItemStack(Item.itemsList[idDropped], 1, itemStack.getItemDamage());
				}

			}
		}

		return itemStack;
	}

}
