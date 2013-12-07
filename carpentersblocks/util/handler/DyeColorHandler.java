package carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

public class DyeColorHandler {

	/*
	 * Color definitions
	 */
	public static final byte NO_COLOR = 0;
	
	public static Map colorMap;
	
	/**
	 * Initializes dye colors.
	 */
	public static void init()
	{
		colorMap = new HashMap();
		colorMap.put(0, 	new float[] {        1.0F,        1.0F,	       1.0F } );
		colorMap.put(1, 	new float[] {        1.0F, 0.59765625F,   0.328125F } );
		colorMap.put(2, 	new float[] { 	   0.785F, 0.39828125F, 0.82015625F } );
		colorMap.put(3, 	new float[] { 0.47859375F, 	 0.623125F, 0.91609375F } );
		colorMap.put(4, 	new float[] { 0.80859375F,  0.7578125F, 0.19140625F } );
		colorMap.put(5, 	new float[] { 0.37421875F, 0.81171875F, 0.32734375F } );
		colorMap.put(6, 	new float[] {        1.0F,  0.6171875F,  0.7265625F } );
		colorMap.put(7, 	new float[] {  0.2890625F,  0.2890625F,  0.2890625F } );
		colorMap.put(8, 	new float[] {   0.703125F,    0.71875F,    0.71875F } );
		colorMap.put(9, 	new float[] {  0.2109375F,        0.5F,  0.6171875F } );
		colorMap.put(10, 	new float[] { 0.56640625F,  0.3203125F,  0.7734375F } );
		colorMap.put(11, 	new float[] {  0.2109375F,       0.25F,   0.640625F } );
		colorMap.put(12, 	new float[] {   0.359375F,  0.2265625F,   0.140625F } );
		colorMap.put(13, 	new float[] { 0.23828125F,  0.3203125F,      0.125F } );
		colorMap.put(14, 	new float[] {  0.6796875F,  0.2421875F,    0.21875F } );
		colorMap.put(15, 	new float[] {   0.140625F,      0.125F,      0.125F } );
	}
	
	/**
	 * Returns dye color array RGB.
	 */
	public static float[] getDyeColorRGB(int dyeColor)
	{
		return (float[]) colorMap.get(dyeColor);
	}
		
}
