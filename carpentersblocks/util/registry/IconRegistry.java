package carpentersblocks.util.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.util.bed.BedDesignHandler;
import carpentersblocks.util.flowerpot.FlowerPotDesignHandler;
import carpentersblocks.util.handler.PatternHandler;
import carpentersblocks.util.handler.TileHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IconRegistry {
    
    public static IIcon icon_blank;
    public static IIcon icon_solid;
    public static IIcon icon_oblique_pos;
    public static IIcon icon_oblique_neg;
    public static IIcon icon_overlay_fast_grass_side;
    public static IIcon icon_overlay_hay_side;
    public static IIcon icon_overlay_snow_side;
    public static IIcon icon_overlay_mycelium_side;
    public static IIcon icon_full_frame;
    public static IIcon icon_quartered_frame;
    public static IIcon icon_lever;
    public static IIcon icon_torch_lit;
    public static IIcon icon_torch_head_smoldering;
    public static IIcon icon_torch_head_unlit;
    public static IIcon icon_door_screen_tall;
    public static IIcon icon_door_glass_tall_top;
    public static IIcon icon_door_glass_tall_bottom;
    public static IIcon icon_door_glass_top;
    public static IIcon icon_door_french_glass_top;
    public static IIcon icon_door_french_glass_bottom;
    public static IIcon icon_hatch_french_glass;
    public static IIcon icon_hatch_glass;
    public static IIcon icon_hatch_screen;
    public static IIcon icon_bed_pillow;
    public static IIcon icon_daylight_sensor_glass_top;
    public static IIcon icon_safe_light;
    public static IIcon icon_flower_pot;
    public static IIcon icon_flower_pot_glass;
    public static IIcon icon_blank_tile;

    public static IIcon[] icon_pattern           = new IIcon[PatternHandler.maxNum];
    public static IIcon[] icon_bed_pillow_custom = new IIcon[BedDesignHandler.maxNum];
    public static IIcon[] icon_flower_pot_design = new IIcon[FlowerPotDesignHandler.maxNum];
    
    public static List<IIcon> icon_tile          = new ArrayList<IIcon>();
    
    @SubscribeEvent
    /**
     * This will load all icons that are used universally for all blocks.
     */
    public void iconRegistration(TextureStitchEvent.Pre event)
    {
        if (event.map.getTextureType() == 0) {

            icon_blank                   = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "general/blank");
            icon_solid                   = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "general/solid");
            icon_full_frame              = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "general/full_frame");
            icon_quartered_frame         = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "general/quartered_frame");
            icon_overlay_fast_grass_side = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_fast_grass_side");
            icon_overlay_hay_side        = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_hay_side");
            icon_overlay_snow_side       = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_snow_side");
            icon_overlay_mycelium_side   = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_mycelium_side");

            /* Chisel pattern icons */
            
            for (int numIcon = 0; numIcon < PatternHandler.maxNum; ++numIcon) {
                if (PatternHandler.hasPattern[numIcon]) {
                    icon_pattern[numIcon] = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "pattern/pattern_" + numIcon);
                }
            }

            /* Tile icons */
            
            icon_blank_tile = event.map.registerIcon(CarpentersBlocks.MODID + ":" + "tile/blank");
            for (String name : TileHandler.tileList) {
                icon_tile.add(event.map.registerIcon(CarpentersBlocks.MODID + ":" + "tile/" + name));
            }   
        	
        }
    }

}
