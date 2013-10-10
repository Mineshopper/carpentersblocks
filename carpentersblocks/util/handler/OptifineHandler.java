package carpentersblocks.util.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import carpentersblocks.util.ModLogger;

public class OptifineHandler
{

	private static Method getColorMultiplier;
	
	/**
	 * Initializes Optifine integration.
	 * If reflection fails, will return false.
	 */
	public static boolean init()
	{
		try {
			Class<?> CustomColorizer = Class.forName("CustomColorizer");
			getColorMultiplier = CustomColorizer.getMethod("getColorMultiplier", Block.class, IBlockAccess.class, int.class, int.class, int.class);
			ModLogger.log(Level.INFO, "Optifine integration successful.");
			return true;
		} catch (Exception e) {
			ModLogger.log(Level.WARNING, "Optifine integration failed.");
			return false;
		}
	}
	
	public static int getColorMultiplier(Block block, IBlockAccess blockAccess, int x, int y, int z)
	{
		int colorMultiplier = block.colorMultiplier(blockAccess, x, y, z);
		try {
			int tempColorMultiplier = (Integer) getColorMultiplier.invoke(null, block, blockAccess, x, y, z);
			colorMultiplier = tempColorMultiplier;
		} catch (InvocationTargetException E) {
			ModLogger.log(Level.WARNING, "Block custom coloring failed, disabling Optifine integration.");
			FeatureHandler.enableOptifineIntegration = false;
		} catch (Exception E) {}

		return colorMultiplier;
	}

}
