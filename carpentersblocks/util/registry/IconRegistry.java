package carpentersblocks.util.registry;

import java.util.logging.Level;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import carpentersblocks.util.ModLogger;
import carpentersblocks.util.bed.BedDesignHandler;
import carpentersblocks.util.flowerpot.FlowerPotDesignHandler;
import carpentersblocks.util.handler.PatternHandler;

public class IconRegistry {

	public static Icon icon_missing;
	public static Icon icon_blank;
	public static Icon icon_solid;
	public static Icon icon_slope_oblique_pt_high;
	public static Icon icon_slope_oblique_pt_low;
	public static Icon icon_overlay_fast_grass_side;
	public static Icon icon_overlay_hay_side;
	public static Icon icon_overlay_snow_side;
	public static Icon icon_overlay_mycelium_side;
	public static Icon icon_full_frame;
	public static Icon icon_quartered_frame;
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
	public static Icon icon_bed_pillow;
	public static Icon icon_daylight_sensor_glass_top;
	public static Icon icon_safe_light;
	public static Icon icon_flower_pot;
	public static Icon icon_flower_pot_glass;

	public static Icon[] icon_pattern = new Icon[PatternHandler.maxNum];
	public static Icon[] icon_bed_pillow_custom = new Icon[BedDesignHandler.maxNum];
	public static Icon[] icon_flower_pot_design = new Icon[FlowerPotDesignHandler.maxNum];

	@ForgeSubscribe
	/**
	 * This will load all icons that are used universally for all blocks.
	 */
	public void loadTextures(TextureStitchEvent.Pre event)
	{
		if (event.map.textureType == 0) {
			icon_missing = event.map.getAtlasSprite("missingno");
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
	public static void registerIcons(IconRegister iconRegister)
	{
		// General icons
		icon_blank = iconRegister.registerIcon("carpentersblocks:general/blank");
		icon_solid = iconRegister.registerIcon("carpentersblocks:general/solid");
		icon_full_frame = iconRegister.registerIcon("carpentersblocks:general/full_frame");
		icon_quartered_frame = iconRegister.registerIcon("carpentersblocks:general/quartered_frame");

		// Overlay icons
		icon_overlay_fast_grass_side = iconRegister.registerIcon("carpentersblocks:overlay/overlay_fast_grass_side");
		icon_overlay_hay_side = iconRegister.registerIcon("carpentersblocks:overlay/overlay_hay_side");
		icon_overlay_snow_side = iconRegister.registerIcon("carpentersblocks:overlay/overlay_snow_side");
		icon_overlay_mycelium_side = iconRegister.registerIcon("carpentersblocks:overlay/overlay_mycelium_side");

		// Pattern icons
		for (int numIcon = 0; numIcon < PatternHandler.maxNum; ++numIcon) {
			if (PatternHandler.hasPattern[numIcon]) {
				icon_pattern[numIcon] = iconRegister.registerIcon("carpentersblocks:pattern/pattern_" + numIcon);
			}
		}
	}

}
