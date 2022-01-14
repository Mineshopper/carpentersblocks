package com.carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.config.Configuration;
import com.carpentersblocks.util.BlockUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = CarpentersBlocks.MOD_ID, bus = Bus.MOD)
public class OverlayHandler {
	
    public enum Overlay {
    	
        none(Blocks.AIR, false),
        grass(Blocks.GRASS_BLOCK, true),
        snow(Blocks.SNOW, true),
        web(Blocks.COBWEB, false),
        vine(Blocks.VINE, false),
        hay(Blocks.HAY_BLOCK, true),
        mycelium(Blocks.MYCELIUM, true);
    	
    	private boolean hasDrapedSides;
        private Block block;

        private Overlay(Block block, boolean hasDrapedSides) {
            this.block = block;
            this.hasDrapedSides = hasDrapedSides;
        }

        public ItemStack getItemStack() {
            return new ItemStack(block);
        }
        
        public BlockState getBlockState() {
	        return block.defaultBlockState();
	    }
        
        public boolean hasDrapedSides() {
        	return hasDrapedSides;
        }
        
        public static Overlay fromBlock(Block block) {
        	if (Overlay.grass.block.equals(block)) {
        		return Overlay.grass;
        	} else if (Overlay.snow.block.equals(block)) {
        		return Overlay.snow;
        	} else if (Overlay.web.block.equals(block)) {
        		return Overlay.web;
        	} else if (Overlay.vine.block.equals(block)) {
        		return Overlay.vine;
        	} else if (Overlay.hay.block.equals(block)) {
        		return Overlay.hay;
        	} else if (Overlay.mycelium.block.equals(block)) {
        		return Overlay.mycelium;
        	}
        	return Overlay.none;
        }
        
    }
    
    public static final String OVERLAY_TYPE_SEPARATOR = "=";
    public static Map<String, Overlay> overlayMap = new HashMap<>();

    /**
     * Initializes overlay definitions from configuration file.
     */
    @SubscribeEvent
    public static void onFMLCommonSetupEvent(FMLCommonSetupEvent event) {
    	for (String overlay : Configuration.getOverlayItems()) {
            String resourceLocation = overlay.substring(0, overlay.indexOf(OVERLAY_TYPE_SEPARATOR));
            if (!overlayMap.containsKey(resourceLocation)) {
                String overlayType = overlay.substring(overlay.indexOf(OVERLAY_TYPE_SEPARATOR) + 1);
                if (overlayType.equals(Overlay.grass.name())) {
                    overlayMap.put(resourceLocation, Overlay.grass);
                } else if (overlayType.equals(Overlay.snow.name())) {
                    overlayMap.put(resourceLocation, Overlay.snow);
                } else if (overlayType.equals(Overlay.web.name())) {
                    overlayMap.put(resourceLocation, Overlay.web);
                } else if (overlayType.equals(Overlay.vine.name())) {
                    overlayMap.put(resourceLocation, Overlay.vine);
                } else if (overlayType.equals(Overlay.hay.name())) {
                    overlayMap.put(resourceLocation, Overlay.hay);
                } else if (overlayType.equals(Overlay.mycelium.name())) {
                    overlayMap.put(resourceLocation, Overlay.mycelium);
                }
            }
        }
    }

    /**
     * Returns true if overlay covers a majority or all of side.
     * 
     * @param overlay the overlay
     * @param facing the facing
     * @return <code>true</code> if overlay fully covers side
     */
    public static boolean coversFullSide(Overlay overlay, Direction facing) {
        switch (overlay) {
            case grass:
            case snow:
            case hay:
            case mycelium:
                return Direction.UP.equals(facing);
            case web:
            case vine:
                return true;
            default: {}
        }
        return true;
    }

    /**
     * Returns overlay from qualified ItemStack.
     */
    public static Overlay getOverlayType(ItemStack itemStack) {
        Overlay overlay = overlayMap.get(itemStack.getItem().getRegistryName().toString());
        return overlay == null ? Overlay.none : overlay;
    }

    /**
     * Returns icon for overlay side.
     *
     * Returns null if there is no icon to return.
     */
    @OnlyIn(Dist.CLIENT)
    public static TextureAtlasSprite getOverlaySprite(Overlay overlay, Direction facing) {
        ItemStack itemStack = overlay.getItemStack();
        switch (overlay) {
            case grass:
            	switch (facing) {
	            	case DOWN:
	            		return null;
	            	case UP:
	            		return TextureAtlasSprites.sprite_grass_top.get();
	            	default:
	            		if (Minecraft.useFancyGraphics()) {
	            			return TextureAtlasSprites.sprite_grass_tinted_side.get();
                    	} else {
                    		return TextureAtlasSprites.sprite_overlay_fast_grass_side;
                    	}
            	}
            case snow:
            case hay:
            case mycelium:
                switch (facing) {
                    case DOWN:
                        return null;
                    case UP:
                    	return BlockUtil.getQuadTexture(itemStack, facing);
                    default:
                        switch (overlay) {
                            case snow:
                                return TextureAtlasSprites.sprite_overlay_snow_side;
                            case hay:
                                return TextureAtlasSprites.sprite_overlay_hay_side;
                            case mycelium:
                                return TextureAtlasSprites.sprite_overlay_mycelium_side;
                            default:
                                return null;
                        }
                }
            case web:
            case vine:
                return BlockUtil.getQuadTexture(itemStack, facing);
            default: {
                return null;
            }
        }
    }
    
    /**
     * Whether texture atlas sprite represents a side overlay,
     * including grass, that drape over the top half of the side.
     * 
     * @param sprite the texture atlas sprite
     * @return <code>true</code> if texture atlas sprite is a floating
     * side overlay
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean isFloatingSprite(TextureAtlasSprite sprite) {
    	return TextureAtlasSprites.sprite_grass_tinted_side.get().equals(sprite)
    			|| TextureAtlasSprites.sprite_overlay_snow_side.equals(sprite)
    			|| TextureAtlasSprites.sprite_overlay_hay_side.equals(sprite)
    			|| TextureAtlasSprites.sprite_overlay_mycelium_side.equals(sprite)
    			|| TextureAtlasSprites.sprite_overlay_fast_grass_side.equals(sprite);
    }
    
    /**
     * Gets whether block state represents an overlay
     * that has floating side sprites.
     * 
     * @param blockState a block state
     * @return <code>true</code> if floating block state
     */
    public static boolean isFloatingSpriteBlockState(BlockState blockState) {
    	return Overlay.fromBlock(blockState.getBlock()).hasDrapedSides;
    }

}
