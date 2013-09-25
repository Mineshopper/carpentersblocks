package carpentersblocks.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class CompatibilityHelper
{

	/**
	 * Converts old data types to new ones.
	 */
	public static void convertData(TECarpentersBlock TE, NBTTagCompound nbt)
	{
		/*
		 * Set temporary variables
		 */
		
		short[] cover = new short[7];
		byte pattern = 0;
		byte color = 0;
		byte overlay = 0;
		short data = 0;
		
		/*
		 * Convert miscellaneous data
		 */
		
		int old_data = nbt.getInteger("data");
		byte old_color = (byte) ((old_data & 0x00f00000) >> 20);
		byte old_overlay = (byte) ((old_data & 0x0000f000) >> 12);

		cover[6] = (short) (old_data & 0x00000fff);
		pattern = nbt.getByte("pattern");
		color = old_color;
		overlay = old_overlay;
		data = (short) ((old_data & 0xff000000) >>> 24);
		
		/*
		 * Convert side covers
		 */
		
		int[] old_array = nbt.getIntArray("sideCover");
		
		cover[0] = (short) (((old_array[0] & 0x0fff0000) >> 16) + ((old_array[0] & 0xf0000000) >>> 16));
		cover[1] = (short) ((old_array[0] & 0xfff) + (old_array[0] & 0xf000));
		cover[2] = (short) (((old_array[1] & 0x0fff0000) >> 16) + ((old_array[1] & 0xf0000000) >>> 16));
		cover[3] = (short) ((old_array[1] & 0xfff) + (old_array[1] & 0xf000));
		cover[4] = (short) (((old_array[2] & 0x0fff0000) >> 16) + ((old_array[2] & 0xf0000000) >>> 16));
		cover[5] = (short) ((old_array[2] & 0xfff) + (old_array[2] & 0xf000));
		
		/*
		 * Update TE
		 */
		
		for (int side = 0; side < 7; ++side)
			TE.cover[side] = cover[side];
		
		TE.pattern[6] = pattern;
		TE.color[6] = color;
		TE.overlay[6] = overlay;
		TE.data = data;
	}
	
}
