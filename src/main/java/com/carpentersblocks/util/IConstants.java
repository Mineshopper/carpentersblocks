package com.carpentersblocks.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;

public interface IConstants {
	
	public static final int DEFAULT_BRIGHTNESS = 0xffffff;
	public static final Set<EnumFacing> DEFAULT_RENDER_FACES = new HashSet<EnumFacing>(Arrays.asList(EnumFacing.VALUES));
	public static final BlockRenderLayer DEFAULT_RENDER_LAYER = BlockRenderLayer.CUTOUT_MIPPED;
	public static final int DEFAULT_RGB = 0xffffff;
	public static final String EMPTY_STRING = "";
	
}
