package com.carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

import com.carpentersblocks.util.block.BlockUtil;
import com.carpentersblocks.util.registry.ConfigRegistry;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OverlayHandler {

    public enum Overlay {
        NONE(new ItemStack(Blocks.AIR)),
        GRASS(new ItemStack(Blocks.GRASS)),
        SNOW(new ItemStack(Blocks.SNOW)),
        WEB(new ItemStack(Blocks.WEB)),
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

    public static Map overlayMap = new HashMap();

    /**
     * Initializes overlay definitions from configuration file.
     */
    public static void init() {
        for (String name : ConfigRegistry.overlayItems) {
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
    public static boolean coversFullSide(Overlay overlay, EnumFacing facing) {
        switch (overlay) {
            case GRASS:
            case SNOW:
            case HAY:
            case MYCELIUM:
                return EnumFacing.UP.equals(facing);
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
    public static Overlay getOverlayType(ItemStack itemStack)
    {
        Object object = overlayMap.get(itemStack.getDisplayName());

        if (object == null) {
            object = overlayMap.get(ChatHandler.getDefaultTranslation(itemStack));
        }

        return object == null ? Overlay.NONE : (Overlay) object;
    }

    @SideOnly(Side.CLIENT)
    /**
     * Returns icon for overlay side.
     *
     * Returns null if there is no icon to return.
     */
    public static TextureAtlasSprite getOverlaySprite(Overlay overlay, EnumFacing facing) {
        ItemStack itemStack = overlay.getItemStack();
        switch (overlay) {
            case GRASS:
            	switch (facing) {
	            	case DOWN:
	            		return null;
	            	case UP:
	            		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/grass_top");
	            	default:
	            		if (Minecraft.getMinecraft().isFancyGraphicsEnabled()) {
                    		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/grass_side_overlay");
                    	} else {
                    		return SpriteRegistry.sprite_overlay_fast_grass_side;
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
                                return SpriteRegistry.sprite_overlay_snow_side;
                            case HAY:
                                return SpriteRegistry.sprite_overlay_hay_side;
                            case MYCELIUM:
                                return SpriteRegistry.sprite_overlay_mycelium_side;
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
