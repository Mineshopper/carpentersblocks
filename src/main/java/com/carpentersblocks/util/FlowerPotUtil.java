package com.carpentersblocks.util;

import java.util.HashMap;
import java.util.Map;

import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.nbt.attribute.EnumAttributeType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.IShearable;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;

public class FlowerPotUtil {

	/** Maps Items to render profiles */
    private final static Map<String, Profile> plantProfile;
    static {
        plantProfile = new HashMap<String, Profile>();
        plantProfile.put("tile.doublePlant.sunflower", Profile.DOUBLEPLANT     );
        plantProfile.put("tile.doublePlant.syringa"  , Profile.DOUBLEPLANT     );
        plantProfile.put("tile.doublePlant.grass"    , Profile.THIN_DOUBLEPLANT);
        plantProfile.put("tile.doublePlant.fern"     , Profile.DOUBLEPLANT     );
        plantProfile.put("tile.doublePlant.rose"     , Profile.DOUBLEPLANT     );
        plantProfile.put("tile.doublePlant.paeonia"  , Profile.DOUBLEPLANT     );
        plantProfile.put("tile.tallgrass.grass"      , Profile.THIN_YP         );
        plantProfile.put("tile.tallgrass.fern"       , Profile.REDUCED_SCALE_YP);
        plantProfile.put("tile.deadbush"             , Profile.REDUCED_SCALE_YP);
        plantProfile.put("tile.cactus"               , Profile.CACTUS          );
        plantProfile.put("tile.mushroom"             , Profile.TRUE_SCALE      );
        plantProfile.put("item.reeds"                , Profile.THIN_YP         );
        plantProfile.put("item.carrots"              , Profile.THIN_YP         );
        plantProfile.put("item.potato"               , Profile.THIN_YP         );
        plantProfile.put("item.wheat"                , Profile.THIN_YP         );
        plantProfile.put("tile.flower1.dandelion"    , Profile.TRUE_SCALE      );
        plantProfile.put("tile.flower2.houstonia"    , Profile.TRUE_SCALE      );
    }

    /** Maps Items to Blocks */
    private final static Map<Item, Block> itemPlant;
    static {
        itemPlant = new HashMap<Item, Block>();
        itemPlant.put(Items.CARROT, Blocks.CARROTS);
        itemPlant.put(Items.POTATO, Blocks.POTATOES);
        itemPlant.put(Items.WHEAT, Blocks.WHEAT);
    }
    
    /** Render profiles for plants */
    public enum Profile {
        DOUBLEPLANT,
        THIN_DOUBLEPLANT,
        REDUCED_SCALE_YP,
        REDUCED_SCALE_YN,
        TRUE_SCALE,
        THIN_YP,
        THIN_YN,
        CACTUS,
        LEAVES
    }
	
	/**
     * Will return block from ItemStack. This is to be used for plants only.
     * 
     * @param itemStack the ItemStack
     * @return a {@link Block}
     */
    public static Block toBlock(ItemStack itemStack) {
        Block block = itemPlant.get(itemStack.getItem());
        if (block != null) {
            return block;
        } else {
            return BlockUtil.toBlock(itemStack);
        }
    }
    
    /**
     * Returns plant color.
     * 
     * @param cbTileEntity the tile entity
     * @return the plant color
     */
    @OnlyIn(Dist.CLIENT)
    public static int getPlantColor(CbTileEntity cbTileEntity) {
        ItemStack itemStack = (ItemStack) cbTileEntity.getAttributeHelper().getAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT).getModel();
        BlockState blockState = Block.stateById(itemStack.getDamageValue());
        cbTileEntity.setCbMetadata(itemStack.getDamageValue());
        int blockMapColor = blockState.getMapColor(cbTileEntity.getLevel(), cbTileEntity.getBlockPos()).col;
        int colorMultiplier = Minecraft.getInstance().getBlockColors().getColor(blockState, cbTileEntity.getLevel(), cbTileEntity.getBlockPos(), blockMapColor);
        return blockMapColor < colorMultiplier ? blockMapColor : colorMultiplier;
    }
    
    /**
     * Returns whether plant can be colored - leaves, grass, etc.
     * 
     * @param cbTileEntity the tile entity
     * @return <code>true</code> if plant can be colored
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean isPlantColorable(CbTileEntity cbTileEntity) {
        return getPlantColor(cbTileEntity) != 16777215;
    }

    /**
     * Returns whether ItemStack contains a soil block.
     * 
     * @param itemStack the ItemStack
     * @return <code>true</code> if ItemStack contains a soil block
     */
    public static boolean isSoil(ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem) {
            Block block = BlockUtil.toBlock(itemStack);
            if (!block.hasTileEntity(block.defaultBlockState())) {
                Material material = block.defaultBlockState().getMaterial();
                return Material.GRASS.equals(material) || Material.DIRT.equals(material) || Material.SAND.equals(material);
            }
        }
        return false;
    }

    /**
     * Returns whether ItemStack contains a plant.
     * 
     * @param itemStack the ItemStack
     * @return <code>true</code> if ItemStack contains a plant
     */
    public static boolean isPlant(ItemStack itemStack) {
        Block block = BlockUtil.toBlock(itemStack);
        if (!block.equals(Blocks.AIR)) {
        	if (!block.hasTileEntity(block.defaultBlockState())) {
                return block instanceof IPlantable || block instanceof IShearable;
            } else {
                return false;
            }
        } else {
            return itemPlant.containsKey(itemStack.getItem());
        }
    }
    
    /**
     * Returns the plant profile to indicate which render method to use.
     * 
     * @param cbTileEntity the tile entity
     * @return a render {@link Profile}
     */
    public static Profile getPlantProfile(CbTileEntity cbTileEntity) {
        return getPlantProfile((ItemStack)cbTileEntity.getAttributeHelper().getAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT).getModel());
    }

    /**
     * Returns the plant profile to indicate which render method to use.
     * 
     * @param itemStack the plant ItemStack
     * @return a render {@link Profile}
     */
    public static Profile getPlantProfile(ItemStack itemStack) {
        BlockState blockState = BlockUtil.getBlockState(itemStack);
        String name = itemStack.getItem().getRegistryName().toString();
        Material material = blockState.getMaterial();
        if (plantProfile.containsKey(name)) {
            return plantProfile.get(name);
        } else if (material.equals(Material.LEAVES) || material.equals(Material.CACTUS)) {
            return Profile.LEAVES;
        //} else if (material.equals(Material.TALL_PLANTS)) {
        //    return Profile.THIN_YP;
        } else {
            return Profile.REDUCED_SCALE_YP;
        }
    }
	
}
