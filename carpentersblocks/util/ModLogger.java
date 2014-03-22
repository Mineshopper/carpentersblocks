package carpentersblocks.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import carpentersblocks.CarpentersBlocks;

public class ModLogger {
    
    private static Logger logger = LogManager.getLogger(CarpentersBlocks.MODID);

    public static void log(Level level, String format, Object... data)
    {
        logger.log(level, format, data);
    }

    public static void severe(String format, Object... data)
    {
        log(Level.ERROR, format, data);
    }

    public static void warning(String format, Object... data)
    {
        log(Level.WARN, format, data);
    }

    public static void info(String format, Object... data)
    {
        log(Level.INFO, format, data);
    }

    public static void fine(String format, Object... data)
    {
        log(Level.DEBUG, format, data);
    }

    public static void finer(String format, Object... data)
    {
        log(Level.TRACE, format, data);
    }

    public static Logger getLogger()
    {
        return logger;
    }
    
}
