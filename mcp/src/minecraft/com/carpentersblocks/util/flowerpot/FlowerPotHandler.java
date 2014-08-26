package com.carpentersblocks.util.flowerpot;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.carpentersblocks.tileentity.TEBase;

public class FlowerPotHandler {

    public enum Profile {
        REDUCED_SCALE_YP,
        REDUCED_SCALE_YN,
        TRUE_SCALE,
        THIN_YP,
        THIN_YN,
        CACTUS,
        LEAVES
    }

    public final static Map<String, Profile> plantProfile;
    static {
        plantProfile = new HashMap<String, Profile>();

        /* Vanilla */

        plantProfile.put("tile.tallgrass.grass", Profile.THIN_YP         );
        plantProfile.put("tile.tallgrass.fern" , Profile.REDUCED_SCALE_YP);
        plantProfile.put("tile.deadbush"       , Profile.REDUCED_SCALE_YP);
        plantProfile.put("tile.cactus"         , Profile.CACTUS          );
        plantProfile.put("tile.mushroom"       , Profile.TRUE_SCALE      );
        plantProfile.put("item.reeds"          , Profile.THIN_YP         );
        plantProfile.put("item.carrots"        , Profile.THIN_YP         );
        plantProfile.put("item.potato"         , Profile.THIN_YP         );
        plantProfile.put("item.wheat"          , Profile.THIN_YP         );
        plantProfile.put("tile.flower"         , Profile.TRUE_SCALE      );
        plantProfile.put("tile.rose"           , Profile.TRUE_SCALE      );

        /* Weee! Flowers */

        plantProfile.put("orangeflower"       , Profile.TRUE_SCALE);
        plantProfile.put("orangemoonflower"   , Profile.TRUE_SCALE);
        plantProfile.put("purpleflower"       , Profile.THIN_YP   );
        plantProfile.put("yellowmoonflower"   , Profile.THIN_YP   );
        plantProfile.put("pinkmoonflower"     , Profile.THIN_YP   );
        plantProfile.put("darkgreymoonflower" , Profile.THIN_YP   );
        plantProfile.put("lightgreymoonflower", Profile.THIN_YP   );
        plantProfile.put("purplemoonflower"   , Profile.THIN_YP   );
        plantProfile.put("brownmoonflower"    , Profile.THIN_YP   );
        plantProfile.put("redmoonflower"      , Profile.THIN_YP   );

        /* Harvestcraft */

        plantProfile.put("tile.PamHarvestCraft:strawberrycrop_2"   , Profile.THIN_YP);
        plantProfile.put("tile.PamHarvestCraft:cranberrycrop_2"    , Profile.THIN_YP);
        plantProfile.put("tile.PamHarvestCraft:whitemushroomcrop_2", Profile.THIN_YP);

        /* Biomes O' Plenty */

        plantProfile.put("tile.bop.foliage.poisonivy"         , Profile.REDUCED_SCALE_YP);
        plantProfile.put("tile.bop.flowers.swampflower"       , Profile.THIN_YP         );
        plantProfile.put("tile.bop.flowers.violet"            , Profile.THIN_YP         );
        plantProfile.put("tile.bop.flowers.anemone"           , Profile.THIN_YP         );
        plantProfile.put("tile.bop.flowers2.bluebells"        , Profile.THIN_YP         );
        plantProfile.put("tile.bop.coral.bluecoral"           , Profile.THIN_YP         );
        plantProfile.put("tile.bop.plants.thorn"              , Profile.REDUCED_SCALE_YP);
        plantProfile.put("tile.bop.treeMoss"                  , Profile.REDUCED_SCALE_YP);
        plantProfile.put("tile.bop.mushrooms.portobello"      , Profile.TRUE_SCALE      );
        plantProfile.put("tile.bop.mushrooms.bluemilk"        , Profile.TRUE_SCALE      );
        plantProfile.put("tile.bop.mushrooms.flatmushroom"    , Profile.TRUE_SCALE      );
        plantProfile.put("tile.bop.stoneFormations.stalactite", Profile.THIN_YN         );

        /* Natura */

        plantProfile.put("block.sapling.blood"    , Profile.REDUCED_SCALE_YN);
        plantProfile.put("block.glowshroom.green" , Profile.TRUE_SCALE      );
        plantProfile.put("block.glowshroom.blue"  , Profile.TRUE_SCALE      );
        plantProfile.put("block.glowshroom.purple", Profile.TRUE_SCALE      );

        /* ExtraBiomesXL */

        plantProfile.put("tile.extrabiomes.cattail" , Profile.THIN_YP);
        plantProfile.put("tile.extrabiomes.flower.2", Profile.THIN_YP);
        plantProfile.put("tile.extrabiomes.flower.5", Profile.THIN_YP);
        plantProfile.put("tile.extrabiomes.flower.6", Profile.THIN_YP);
        plantProfile.put("tile.extrabiomes.flower.7", Profile.THIN_YP);
    }

    /** Maps Items to Blocks. */
    public final static Map<Item, Block> itemPlant;
    static {
        itemPlant = new HashMap<Item, Block>();
        itemPlant.put(Item.carrot, Block.carrot);
        itemPlant.put(Item.potato, Block.potato);
        itemPlant.put(Item.reed  , Block.reed  );
        itemPlant.put(Item.wheat , Block.crops );
    }

    /**
     * Returns the plant profile to indicate which render method to use.
     */
    public static Profile getPlantProfile(TEBase TE)
    {
        return getPlantProfile(FlowerPotProperties.getPlant(TE));
    }

    /**
     * Returns the plant profile to indicate which render method to use.
     */
    public static Profile getPlantProfile(ItemStack itemStack)
    {
        Block block = FlowerPotProperties.toBlock(itemStack);
        String name = itemStack.getUnlocalizedName();
        Material material = block.blockMaterial;

        if (plantProfile.containsKey(name)) {
            return plantProfile.get(name);
        } else if (material.equals(Material.leaves) || material.equals(Material.cactus)) {
            return Profile.LEAVES;
        } else if (material.equals(Material.vine)) {
            return Profile.THIN_YP;
        } else {
            return Profile.REDUCED_SCALE_YP;
        }
    }

}
