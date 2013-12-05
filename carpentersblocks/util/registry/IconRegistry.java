package carpentersblocks.util.registry;

import carpentersblocks.util.handler.BedDesignHandler;
import carpentersblocks.util.handler.PatternHandler;
import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IconRegistry
{
	
	@SideOnly(Side.CLIENT)
	public static Icon icon_generic;
	public static Icon icon_slope_oblique_pt_high;
	public static Icon icon_slope_oblique_pt_low;
	public static Icon icon_overlay_fast_grass_side;
	public static Icon icon_overlay_hay_side;
	public static Icon icon_overlay_snow_side;
	public static Icon icon_overlay_mycelium_side;
	public static Icon icon_slope;
	public static Icon icon_stairs;
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
	
	@SideOnly(Side.CLIENT)
	public static Icon[] icon_pattern = new Icon[PatternHandler.maxNum];

	@SideOnly(Side.CLIENT)
	public static Icon[] icon_bed_pillow_custom = new Icon[BedDesignHandler.maxNum];
	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void loadTextures(TextureStitchEvent.Pre event)
	{
		if (event.map.textureType == 0)
		{
			// General icons
			icon_generic = event.map.registerIcon("carpentersblocks:general/generic");
			icon_slope_oblique_pt_low = event.map.registerIcon("carpentersblocks:slope/oblique_pt_low");
			icon_slope_oblique_pt_high = event.map.registerIcon("carpentersblocks:slope/oblique_pt_high");
			icon_slope = event.map.registerIcon("carpentersblocks:slope/slope");
			icon_stairs = event.map.registerIcon("carpentersblocks:stairs/stairs");
			icon_lever = event.map.registerIcon("carpentersblocks:lever/lever");
			icon_torch_lit = event.map.registerIcon("carpentersblocks:torch/torch_lit");
			icon_torch_head_smoldering = event.map.registerIcon("carpentersblocks:torch/torch_head_smoldering");
			icon_torch_head_unlit = event.map.registerIcon("carpentersblocks:torch/torch_head_unlit");
			
			// Overlay icons
			icon_overlay_fast_grass_side = event.map.registerIcon("carpentersblocks:overlay/overlay_fast_grass_side");
			icon_overlay_hay_side = event.map.registerIcon("carpentersblocks:overlay/overlay_hay_side");
			icon_overlay_snow_side = event.map.registerIcon("carpentersblocks:overlay/overlay_snow_side");
			icon_overlay_mycelium_side = event.map.registerIcon("carpentersblocks:overlay/overlay_mycelium_side");
			
			// Pattern icons
			for (int numIcon = 0; numIcon < PatternHandler.maxNum; ++numIcon)
				if (PatternHandler.hasPattern[numIcon])
					icon_pattern[numIcon] = event.map.registerIcon("carpentersblocks:pattern/pattern_" + numIcon);
			
			// Bed design icons
			for (int numIcon = 0; numIcon < BedDesignHandler.maxNum; ++numIcon)
				if (BedDesignHandler.hasPillow[numIcon])
					icon_bed_pillow_custom[numIcon] = event.map.registerIcon("carpentersblocks:bed/design_" + numIcon + "/pillow");

			// Door icons
			icon_door_screen_tall = event.map.registerIcon("carpentersblocks:door/door_screen_tall");
			icon_door_glass_tall_top = event.map.registerIcon("carpentersblocks:door/door_glass_tall_top");
			icon_door_glass_tall_bottom = event.map.registerIcon("carpentersblocks:door/door_glass_tall_bottom");
			icon_door_glass_top = event.map.registerIcon("carpentersblocks:door/door_glass_top");
			icon_door_french_glass_top = event.map.registerIcon("carpentersblocks:door/door_french_glass_top");
			icon_door_french_glass_bottom = event.map.registerIcon("carpentersblocks:door/door_french_glass_bottom");
			
			// Hatch icons
			icon_hatch_glass = event.map.registerIcon("carpentersblocks:hatch/hatch_glass");
			icon_hatch_french_glass = event.map.registerIcon("carpentersblocks:hatch/hatch_french_glass");
			icon_hatch_screen = event.map.registerIcon("carpentersblocks:hatch/hatch_screen");
			
			// Bed icons
			icon_bed_pillow = event.map.registerIcon("carpentersblocks:bed/bed_pillow");
		}
	}

}
