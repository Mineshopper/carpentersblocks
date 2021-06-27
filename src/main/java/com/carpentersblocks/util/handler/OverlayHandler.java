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
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = CarpentersBlocks.MOD_ID)
public class OverlayHandler {

    public enum Overlay {
        NONE(new ItemStack(Blocks.AIR)),
        GRASS(new ItemStack(Blocks.GRASS)),
        SNOW(new ItemStack(Blocks.SNOW)),
        WEB(new ItemStack(Blocks.COBWEB)),
        VINE(new ItemStack(Blocks.VINE)),
        HAY(new ItemStack(Blocks.HAY_BLOCK)),
        MYCELIUM(new ItemStack(Blocks.MYCELIUM));

        private ItemStack itemStack;

        private Overlay(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    public static Map<String, Overlay> overlayMap = new HashMap<>();

    /**
     * Initializes overlay definitions from configuration file.
     */
    @SubscribeEvent
    public static void onFMLCommonSetupEvent(FMLCommonSetupEvent event) {
        for (String name : Configuration.getOverlayItems()) {
            String itemName = name.substring(0, name.indexOf(":"));
            if (!overlayMap.containsKey(itemName)) {
                String overlayType = name.substring(name.indexOf(":") + 1).toLowerCase();
                if (overlayType.equalsIgnoreCase("grass")) {
                    overlayMap.put(itemName, Overlay.GRASS);
                } else if (overlayType.equalsIgnoreCase("snow")) {
                    overlayMap.put(itemName, Overlay.SNOW);
                } else if (overlayType.equalsIgnoreCase("web")) {
                    overlayMap.put(itemName, Overlay.WEB);
                } else if (overlayType.equalsIgnoreCase("vine")) {
                    overlayMap.put(itemName, Overlay.VINE);
                } else if (overlayType.equalsIgnoreCase("hay")) {
                    overlayMap.put(itemName, Overlay.HAY);
                } else if (overlayType.equalsIgnoreCase("mycelium")) {
                    overlayMap.put(itemName, Overlay.MYCELIUM);
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
            case GRASS:
            case SNOW:
            case HAY:
            case MYCELIUM:
                return Direction.UP.equals(facing);
            case WEB:
            case VINE:
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
        return overlay == null ? Overlay.NONE : overlay;
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
            case GRASS:
            	switch (facing) {
	            	case DOWN:
	            		return null;
	            	case UP:
	            		return spriteResolver.apply(new ResourceLocation("minecraft:blocks/grass_top"));
	            	default:
	            		if (Minecraft.useFancyGraphics()) {
	            			return spriteResolver.apply(new ResourceLocation("minecraft:blocks/grass_side_overlay"));
                    	} else {
                    		return TextureAtlasSprites.sprite_overlay_fast_grass_side;
                    	}
            	}
            case SNOW:
            case HAY:
            case MYCELIUM:
                switch (facing) {
                    case DOWN:
                        return null;
                    case UP:
                    	return BlockUtil.getParticleTexture(itemStack);
                    default:
                        switch (overlay) {
                            case SNOW:
                                return TextureAtlasSprites.sprite_overlay_snow_side;
                            case HAY:
                                return TextureAtlasSprites.sprite_overlay_hay_side;
                            case MYCELIUM:
                                return TextureAtlasSprites.sprite_overlay_mycelium_side;
                            default:
                                return null;
                        }
                }
            case WEB:
            case VINE:
                return BlockUtil.getParticleTexture(itemStack);
            default: {
                return null;
            }
        }
    }

}
