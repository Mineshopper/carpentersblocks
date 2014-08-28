package com.carpentersblocks.util.registry;

import java.util.ArrayList;
import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.CarpentersBlocksCachedResources;
import com.carpentersblocks.util.handler.DesignHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IconRegistry {

    public static Icon icon_uncovered_full;
    public static Icon icon_uncovered_quartered;
    public static Icon icon_uncovered_solid;
    public static Icon icon_uncovered_oblique_pos;
    public static Icon icon_uncovered_oblique_neg;
    public static Icon icon_overlay_fast_grass_side;
    public static Icon icon_overlay_hay_side;
    public static Icon icon_overlay_snow_side;
    public static Icon icon_overlay_mycelium_side;
    public static Icon icon_lever;
    public static Icon icon_torch_lit;
    public static Icon icon_torch_head_smoldering;
    public static Icon icon_torch_head_unlit;
    public static Icon icon_door_screen_tall;
    public static Icon icon_door_glass_tall_top;
    public static Icon icon_door_glass_tall_bottom;
    public static Icon icon_door_glass_top;
    public static Icon icon_door_french_glass_top;
    public static Icon icon_door_french_glass_bottom;
    public static Icon icon_hatch_french_glass;
    public static Icon icon_hatch_glass;
    public static Icon icon_hatch_screen;
    public static Icon icon_daylight_sensor_glass_top;
    public static Icon icon_safe_light;
    public static Icon icon_flower_pot;
    public static Icon icon_flower_pot_glass;
    public static Icon icon_tile_blank;
    public static Icon icon_bed_pillow;

    public static ArrayList<Icon>   icon_design_chisel     = new ArrayList<Icon>();
    public static ArrayList<Icon[]> icon_design_bed        = new ArrayList<Icon[]>();
    public static ArrayList<Icon>   icon_design_flower_pot = new ArrayList<Icon>();
    public static ArrayList<Icon>   icon_design_tile       = new ArrayList<Icon>();

    @ForgeSubscribe
    /**
     * This will load all icons that are used universally for all blocks.
     */
    public void iconRegistration(TextureStitchEvent.Pre event)
    {
        /** 0 = terrain.png, 1 = items.png */
        if (event.map.getTextureType() == 0) {

            CarpentersBlocksCachedResources.INSTANCE.rebuildCache();

            icon_uncovered_solid         = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "general/solid");
            icon_uncovered_full          = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "general/full_frame");
            icon_uncovered_quartered     = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "general/quartered_frame");
            icon_overlay_fast_grass_side = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_fast_grass_side");
            icon_overlay_hay_side        = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_hay_side");
            icon_overlay_snow_side       = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_snow_side");
            icon_overlay_mycelium_side   = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_mycelium_side");
            icon_tile_blank              = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "tile/blank");

            icon_design_chisel.clear();
            icon_design_bed.clear();
            icon_design_flower_pot.clear();
            icon_design_tile.clear();

            DesignHandler.registerIcons(event.map);

        }
    }

}
