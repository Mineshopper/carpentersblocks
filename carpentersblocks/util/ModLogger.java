package carpentersblocks.util;

import org.apache.logging.log4j.Level;

import carpentersblocks.CarpentersBlocks;
import cpw.mods.fml.common.FMLLog;

public class ModLogger {
    
    public static void log(Level level, String message)
    {
        FMLLog.log(level, "[" + CarpentersBlocks.MODID + "] " + message);
    }
    
}
