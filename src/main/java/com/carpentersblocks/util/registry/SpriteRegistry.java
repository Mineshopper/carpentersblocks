package com.carpentersblocks.util.registry;

import java.util.ArrayList;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.CarpentersBlocksCachedResources;
import com.carpentersblocks.util.handler.DesignHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpriteRegistry {

	public static final ResourceLocation RESOURCE_UNCOVERED_SOLID 			= new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/general/uncovered_solid");
	public static final ResourceLocation RESOURCE_UNCOVERED_FULL	 		= new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/general/uncovered_full_frame");
	public static final ResourceLocation RESOURCE_UNCOVERED_QUARTERED 		= new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/general/uncovered_quartered_frame");
	public static final ResourceLocation RESOURCE_UNCOVERED_OBLIQUE_POS     = new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/slope/uncovered_oblique_pos");
	public static final ResourceLocation RESOURCE_UNCOVERED_OBLIQUE_NEG     = new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/slope/uncovered_oblique_neg");
	public static final ResourceLocation RESOURCE_OVERLAY_FAST_GRASS_SIDE 	= new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/overlay/overlay_fast_grass_side");
	public static final ResourceLocation RESOURCE_OVERLAY_HAY_SIDE 			= new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/overlay/overlay_hay_side");
	public static final ResourceLocation RESOURCE_OVERLAY_SNOW_SIDE 		= new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/overlay/overlay_snow_side");
	public static final ResourceLocation RESOURCE_OVERLAY_MYCELIUM_SIDE 	= new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/overlay/overlay_mycelium_side");
	public static final ResourceLocation RESOURCE_TILE_BLANK 				= new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/tile/blank");
	
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

    public static ArrayList<TextureAtlasSprite>   sprite_design_chisel     = new ArrayList<TextureAtlasSprite>();
    public static ArrayList<TextureAtlasSprite[]> sprite_design_bed        = new ArrayList<TextureAtlasSprite[]>();
    public static ArrayList<TextureAtlasSprite>   sprite_design_flower_pot = new ArrayList<TextureAtlasSprite>();
    public static ArrayList<TextureAtlasSprite>   sprite_design_tile       = new ArrayList<TextureAtlasSprite>();
    
    /**
     * This will load all icons that are used universally for all blocks.
     */
    @SubscribeEvent
    public void registerSprites(TextureStitchEvent.Pre event) {
        if (event.getMap().equals(Minecraft.getMinecraft().getTextureMapBlocks())) {
            CarpentersBlocksCachedResources.INSTANCE.rebuildCache();
            sprite_uncovered_solid         = event.getMap().registerSprite(RESOURCE_UNCOVERED_SOLID);
            sprite_uncovered_full          = event.getMap().registerSprite(RESOURCE_UNCOVERED_FULL);
            sprite_uncovered_quartered     = event.getMap().registerSprite(RESOURCE_UNCOVERED_QUARTERED);
            sprite_uncovered_oblique_pos   = event.getMap().registerSprite(RESOURCE_UNCOVERED_OBLIQUE_POS);
            sprite_uncovered_oblique_neg   = event.getMap().registerSprite(RESOURCE_UNCOVERED_OBLIQUE_NEG);
            sprite_overlay_fast_grass_side = event.getMap().registerSprite(RESOURCE_OVERLAY_FAST_GRASS_SIDE);
            sprite_overlay_hay_side        = event.getMap().registerSprite(RESOURCE_OVERLAY_HAY_SIDE);
            sprite_overlay_snow_side       = event.getMap().registerSprite(RESOURCE_OVERLAY_SNOW_SIDE);
            sprite_overlay_mycelium_side   = event.getMap().registerSprite(RESOURCE_OVERLAY_MYCELIUM_SIDE);
            sprite_tile_blank              = event.getMap().registerSprite(RESOURCE_TILE_BLANK);
            sprite_design_chisel.clear();
            sprite_design_bed.clear();
            sprite_design_flower_pot.clear();
            sprite_design_tile.clear();
            DesignHandler.registerSprites(event.getMap());
        }
    }

}
