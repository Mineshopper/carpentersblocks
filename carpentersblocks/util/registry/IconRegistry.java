package carpentersblocks.util.registry;

import java.util.logging.Level;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import carpentersblocks.util.ModLogger;
import carpentersblocks.util.handler.BedDesignHandler;
import carpentersblocks.util.handler.PatternHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IconRegistry {

	@SideOnly(Side.CLIENT)
	public static Icon	icon_missing,
						icon_blank,
						icon_solid,
						icon_slope_oblique_pt_high,
						icon_slope_oblique_pt_low,
						icon_overlay_fast_grass_side,
						icon_overlay_hay_side,
						icon_overlay_snow_side,
						icon_overlay_mycelium_side,
						icon_full_frame,
						icon_quartered_frame,
						icon_lever,
						icon_torch_lit,
						icon_torch_head_smoldering,
						icon_torch_head_unlit,
						icon_door_screen_tall,
						icon_door_glass_tall_top,
						icon_door_glass_tall_bottom,
						icon_door_glass_top,
						icon_door_french_glass_top,
						icon_door_french_glass_bottom,
						icon_hatch_french_glass,
						icon_hatch_glass,
						icon_hatch_screen,
						icon_bed_pillow,
						icon_daylight_sensor_glass_top,
						icon_safe_light;

	@SideOnly(Side.CLIENT)
	public static Icon[] icon_pattern = new Icon[PatternHandler.maxNum];

	@SideOnly(Side.CLIENT)
	public static Icon[] icon_bed_pillow_custom = new Icon[BedDesignHandler.maxNum];

	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	/**
	 * This will load all icons that are used universally for all blocks.
	 */
	public void loadTextures(TextureStitchEvent.Pre event)
	{		
		if (event.map.textureType == 0) {
			icon_missing = event.map.getAtlasSprite(null);
			if (!FeatureRegistry.enableMCPatcherCompatibility) {
				registerIcons(event.map);
			} else {
				ModLogger.log(Level.INFO, "MCPatcher icon registration compatibility being used.");
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
