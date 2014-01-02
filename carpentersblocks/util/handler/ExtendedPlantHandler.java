package carpentersblocks.util.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import net.minecraft.block.BlockFlower;
import carpentersblocks.util.ModLogger;
import carpentersblocks.util.registry.FeatureRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ExtendedPlantHandler {

	private static Method canThisPlantGrowOnThisBlockID;

	/**
	 * Initializes extended plant support.
	 * If reflection fails, will return false.
	 */
	public static boolean init()
	{
		try {
			canThisPlantGrowOnThisBlockID = ReflectionHelper.findMethod(BlockFlower.class, null, new String[] { "func_72263_d_" }, int.class);
			ModLogger.log(Level.INFO, "Extended plant support integration successful.");
			return true;
		} catch (Exception e) {
			ModLogger.log(Level.WARNING, "Extended plant support integration failed: " + e.getMessage());
			return false;
		}
	}

	public static boolean canThisPlantGrowOnThisBlockID(int blockID)
	{
		boolean canSupportPlant = false;

		try {
			boolean tempCanSupportPlant = (Boolean) canThisPlantGrowOnThisBlockID.invoke(null, blockID);
			canSupportPlant = tempCanSupportPlant;
		} catch (InvocationTargetException e) {
			ModLogger.log(Level.WARNING, "Extended plant compatibility failed, disabling plant support integration: " + e.getMessage());
			FeatureRegistry.enableExtendedPlantSupport = false;
		} catch (Exception e) {}

		return canSupportPlant;
	}

}
