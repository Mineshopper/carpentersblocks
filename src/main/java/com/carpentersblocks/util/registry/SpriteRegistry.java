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

    @SubscribeEvent
    /**
     * This will load all icons that are used universally for all blocks.
     */
    public void registerSprites(TextureStitchEvent.Pre event) {
        if (event.getMap().equals(Minecraft.getMinecraft().getTextureMapBlocks())) {
            CarpentersBlocksCachedResources.INSTANCE.rebuildCache();
            sprite_uncovered_solid         = event.getMap().registerSprite(new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/general/solid"));
            sprite_uncovered_full          = event.getMap().registerSprite(new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/general/full_frame"));
            sprite_uncovered_quartered     = event.getMap().registerSprite(new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/general/quartered_frame"));
            sprite_overlay_fast_grass_side = event.getMap().registerSprite(new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/overlay/overlay_fast_grass_side"));
            sprite_overlay_hay_side        = event.getMap().registerSprite(new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/overlay/overlay_hay_side"));
            sprite_overlay_snow_side       = event.getMap().registerSprite(new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/overlay/overlay_snow_side"));
            sprite_overlay_mycelium_side   = event.getMap().registerSprite(new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/overlay/overlay_mycelium_side"));
            sprite_tile_blank              = event.getMap().registerSprite(new ResourceLocation(CarpentersBlocks.MOD_ID, "blocks/tile/blank"));
            sprite_design_chisel.clear();
            sprite_design_bed.clear();
            sprite_design_flower_pot.clear();
            sprite_design_tile.clear();
            DesignHandler.registerSprites(event.getMap());
        }
    }

}
