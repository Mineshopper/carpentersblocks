package carpentersblocks.util.registry;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import org.apache.logging.log4j.Level;

import carpentersblocks.CarpentersBlocks;
import carpentersblocks.util.ModLogger;
import carpentersblocks.util.bed.BedDesignHandler;
import carpentersblocks.util.flowerpot.FlowerPotDesignHandler;
import carpentersblocks.util.handler.PatternHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class IconRegistry {
    
    public static IIcon icon_blank;
    public static IIcon icon_solid;
    public static IIcon icon_oblique_int_pos;
    public static IIcon icon_oblique_int_neg;
    public static IIcon icon_oblique_ext_pos;
    public static IIcon icon_oblique_ext_neg;
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
    
    public static IIcon[] icon_pattern           = new IIcon[PatternHandler.maxNum];
    public static IIcon[] icon_bed_pillow_custom = new IIcon[BedDesignHandler.maxNum];
    public static IIcon[] icon_flower_pot_design = new IIcon[FlowerPotDesignHandler.maxNum];
    
    @SubscribeEvent
    /**
     * This will load all icons that are used universally for all blocks.
     */
    public void loadTextures(TextureStitchEvent.Pre event)
    {
        if (event.map.getTextureType() == 0) {
            if (!FeatureRegistry.enableMCPatcherCompatibility) {
                registerIcons(event.map);
            } else {
                ModLogger.log(Level.INFO, "MCPatcher icon registration compatibility enabled.");
            }
        }
    }
    
    /**
     * Registers non-specific icons.
     */
    public static void registerIcons(IIconRegister iconRegister)
    {
        icon_blank                   = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "general/blank");
        icon_solid                   = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "general/solid");
        icon_full_frame              = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "general/full_frame");
        icon_quartered_frame         = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "general/quartered_frame");
        icon_overlay_fast_grass_side = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_fast_grass_side");
        icon_overlay_hay_side        = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_hay_side");
        icon_overlay_snow_side       = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_snow_side");
        icon_overlay_mycelium_side   = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "overlay/overlay_mycelium_side");
        
        // Pattern icons
        for (int numIcon = 0; numIcon < PatternHandler.maxNum; ++numIcon) {
            if (PatternHandler.hasPattern[numIcon]) {
                icon_pattern[numIcon] = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "pattern/pattern_" + numIcon);
            }
        }
    }
    
}
