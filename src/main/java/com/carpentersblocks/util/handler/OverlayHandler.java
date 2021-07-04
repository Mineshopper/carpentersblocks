package com.carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.config.Configuration;
import com.carpentersblocks.util.BlockUtil;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = CarpentersBlocks.MOD_ID, bus = Bus.MOD)
public class OverlayHandler {
	
    public enum Overlay {
    	
        none(new ItemStack(Blocks.AIR)),
        grass(new ItemStack(Blocks.GRASS_BLOCK)),
        snow(new ItemStack(Blocks.SNOW)),
        web(new ItemStack(Blocks.COBWEB)),
        vine(new ItemStack(Blocks.VINE)),
        hay(new ItemStack(Blocks.HAY_BLOCK)),
        mycelium(new ItemStack(Blocks.MYCELIUM));
    	
        private ItemStack itemStack;

        private Overlay(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
        
    }
    
    public static final String OVERLAY_TYPE_SEPARATOR = "|";
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
    	Function<ResourceLocation, TextureAtlasSprite> spriteResolver = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS);
        ItemStack itemStack = overlay.getItemStack();
        switch (overlay) {
            case grass:
            	switch (facing) {
	            	case DOWN:
	            		return null;
	            	case UP:
	            		return spriteResolver.apply(new ResourceLocation("minecraft:block/grass_block_top"));
	            	default:
	            		if (Minecraft.useFancyGraphics()) {
	            			return spriteResolver.apply(new ResourceLocation("minecraft:block/grass_block_side_overlay"));
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
                    	return BlockUtil.getParticleTexture(itemStack);
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
                return BlockUtil.getParticleTexture(itemStack);
            default: {
                return null;
            }
        }
    }

}
