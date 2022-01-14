package com.carpentersblocks.client;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

import com.carpentersblocks.CarpentersBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = CarpentersBlocks.MOD_ID, value = { Dist.CLIENT }, bus = Bus.MOD)
public class TextureAtlasSprites {

	public static final ResourceLocation RESOURCE_UNCOVERED_SOLID = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/general/uncovered_solid");
	public static final ResourceLocation RESOURCE_UNCOVERED_FULL = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/general/uncovered_full_frame");
	public static final ResourceLocation RESOURCE_UNCOVERED_QUARTERED = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/general/uncovered_quartered_frame");
	public static final ResourceLocation RESOURCE_UNCOVERED_OBLIQUE_POS = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/slope/uncovered_oblique_pos");
	public static final ResourceLocation RESOURCE_UNCOVERED_OBLIQUE_NEG = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/slope/uncovered_oblique_neg");
	public static final ResourceLocation RESOURCE_OVERLAY_FAST_GRASS_SIDE = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/overlay/overlay_fast_grass_side");
	public static final ResourceLocation RESOURCE_OVERLAY_HAY_SIDE = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/overlay/overlay_hay_side");
	public static final ResourceLocation RESOURCE_OVERLAY_SNOW_SIDE = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/overlay/overlay_snow_side");
	public static final ResourceLocation RESOURCE_OVERLAY_MYCELIUM_SIDE = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/overlay/overlay_mycelium_side");
	public static final ResourceLocation RESOURCE_TILE_BLANK = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/tile/blank");
	
	public static final ResourceLocation RESOURCE_DEBUG_ONE = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/one");
	public static final ResourceLocation RESOURCE_DEBUG_TWO = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/two");
	
    public static TextureAtlasSprite sprite_uncovered_full;
    public static TextureAtlasSprite sprite_uncovered_quartered;
    public static TextureAtlasSprite sprite_uncovered_solid;
    public static TextureAtlasSprite sprite_uncovered_oblique_pos;
    public static TextureAtlasSprite sprite_uncovered_oblique_neg;
    public static TextureAtlasSprite sprite_overlay_fast_grass_side;
    public static TextureAtlasSprite sprite_overlay_hay_side;
    public static TextureAtlasSprite sprite_overlay_snow_side;
    public static TextureAtlasSprite sprite_overlay_mycelium_side;
    public static TextureAtlasSprite sprite_lever;
    public static TextureAtlasSprite sprite_torch;
    public static TextureAtlasSprite sprite_torch_head_lit;
    public static TextureAtlasSprite sprite_torch_head_smoldering;
    public static TextureAtlasSprite sprite_torch_head_unlit;
    public static TextureAtlasSprite sprite_lantern_glass;
    public static TextureAtlasSprite sprite_door_screen_tall;
    public static TextureAtlasSprite sprite_door_glass_tall_top;
    public static TextureAtlasSprite sprite_door_glass_tall_bottom;
    public static TextureAtlasSprite sprite_door_glass_top;
    public static TextureAtlasSprite sprite_door_french_glass_top;
    public static TextureAtlasSprite sprite_door_french_glass_bottom;
    public static TextureAtlasSprite sprite_hatch_french_glass;
    public static TextureAtlasSprite sprite_hatch_glass;
    public static TextureAtlasSprite sprite_hatch_screen;
    public static TextureAtlasSprite sprite_daylight_sensor_glass_top;
    public static TextureAtlasSprite sprite_safe_light;
    public static TextureAtlasSprite sprite_flower_pot;
    public static TextureAtlasSprite sprite_flower_pot_glass;
    public static TextureAtlasSprite sprite_tile_blank;
    public static TextureAtlasSprite sprite_bed_pillow;
    public static TextureAtlasSprite sprite_garage_glass_top;
    public static TextureAtlasSprite sprite_garage_glass;
    public static TextureAtlasSprite sprite_hammer;
    
    public static TextureAtlasSprite sprite_debug_one;
    public static TextureAtlasSprite sprite_debug_two;

    public static ArrayList<TextureAtlasSprite> sprite_design_chisel = new ArrayList<TextureAtlasSprite>();
    public static ArrayList<TextureAtlasSprite[]> sprite_design_bed = new ArrayList<TextureAtlasSprite[]>();
    public static ArrayList<TextureAtlasSprite> sprite_design_flower_pot = new ArrayList<TextureAtlasSprite>();
    public static ArrayList<TextureAtlasSprite> sprite_design_tile = new ArrayList<TextureAtlasSprite>();
    
    // references to tinted grass sprites since they're called frequently
    public static Supplier<TextureAtlasSprite> sprite_grass_tinted_side;
    public static Supplier<TextureAtlasSprite> sprite_grass_top;
    
    /**
     * Add sprites.
     * 
     * @param event the texture stitch event
     */
    @SubscribeEvent
    public static void onPreTextureStitchEvent(TextureStitchEvent.Pre event) {
    	if (!AtlasTexture.LOCATION_BLOCKS.equals(event.getMap().location())) {
    		return;
    	}
        event.addSprite(RESOURCE_UNCOVERED_SOLID);
        event.addSprite(RESOURCE_UNCOVERED_FULL);
        event.addSprite(RESOURCE_UNCOVERED_QUARTERED);
        event.addSprite(RESOURCE_UNCOVERED_OBLIQUE_POS);
        event.addSprite(RESOURCE_UNCOVERED_OBLIQUE_NEG);
        event.addSprite(RESOURCE_OVERLAY_FAST_GRASS_SIDE);
        event.addSprite(RESOURCE_OVERLAY_HAY_SIDE);
        event.addSprite(RESOURCE_OVERLAY_SNOW_SIDE);
        event.addSprite(RESOURCE_OVERLAY_MYCELIUM_SIDE);
        event.addSprite(RESOURCE_TILE_BLANK);
        event.addSprite(RESOURCE_DEBUG_ONE);
        event.addSprite(RESOURCE_DEBUG_TWO);
    }
    
    /**
     * Resolve registered sprites.
     * 
     * @param event the texture stitch event
     */
    @SubscribeEvent
    public static void onPostTextureStitchEvent(TextureStitchEvent.Post event) {
    	if (!AtlasTexture.LOCATION_BLOCKS.equals(event.getMap().location())) {
    		return;
    	}
        sprite_uncovered_solid = event.getMap().getSprite(RESOURCE_UNCOVERED_SOLID);
        sprite_uncovered_full = event.getMap().getSprite(RESOURCE_UNCOVERED_FULL);
        sprite_uncovered_quartered = event.getMap().getSprite(RESOURCE_UNCOVERED_QUARTERED);
        sprite_uncovered_oblique_pos = event.getMap().getSprite(RESOURCE_UNCOVERED_OBLIQUE_POS);
        sprite_uncovered_oblique_neg = event.getMap().getSprite(RESOURCE_UNCOVERED_OBLIQUE_NEG);
        sprite_overlay_fast_grass_side = event.getMap().getSprite(RESOURCE_OVERLAY_FAST_GRASS_SIDE);
        sprite_overlay_hay_side = event.getMap().getSprite(RESOURCE_OVERLAY_HAY_SIDE);
        sprite_overlay_snow_side = event.getMap().getSprite(RESOURCE_OVERLAY_SNOW_SIDE);
        sprite_overlay_mycelium_side = event.getMap().getSprite(RESOURCE_OVERLAY_MYCELIUM_SIDE);
        sprite_tile_blank = event.getMap().getSprite(RESOURCE_TILE_BLANK);
        sprite_grass_tinted_side = () -> getTintedGrassSprite(Direction.NORTH);
        sprite_grass_top = () -> getTintedGrassSprite(Direction.UP);
        sprite_debug_one = event.getMap().getSprite(RESOURCE_DEBUG_ONE);
        sprite_debug_two = event.getMap().getSprite(RESOURCE_DEBUG_TWO);
    }
    
    /**
     * Helper method to gather tinted grass texture atlas
     * sprite for given direction.
     * 
     * @param direction the direction
     * @return a texture atlas sprite, or <code>null</code> if none found
     */
    private static TextureAtlasSprite getTintedGrassSprite(Direction direction) {
    	BlockState blockState = Blocks.GRASS_BLOCK.defaultBlockState();
    	return Minecraft
				.getInstance()
				.getBlockRenderer()
				.getBlockModel(blockState)
				.getQuads(blockState, direction, new Random())
				.stream()
				.filter(q -> q.getTintIndex() == 0)
				.findFirst()
				.get()
				.getSprite();
    }

}
