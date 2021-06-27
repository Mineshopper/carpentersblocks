package com.carpentersblocks;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLogger {

    private static Logger logger = LogManager.getLogger(CarpentersBlocks.MOD_ID);

    public static void log(Level level, String format, Object... data) {
        logger.log(level, format, data);
    }

}
