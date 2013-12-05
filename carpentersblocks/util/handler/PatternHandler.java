package carpentersblocks.util.handler;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import carpentersblocks.util.ModLogger;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class PatternHandler
{
	
	public static int maxNum = 256;
	public static boolean[] hasPattern = new boolean[maxNum];

	/**
	 * Initializes pattern detection.
	 */
    public static boolean init(FMLPreInitializationEvent event)
    {
        try
        {
        	ZipFile mod = new ZipFile(event.getSourceFile());
            Enumeration enumeration = mod.entries();
            
            int numPatterns = 0;

            while (enumeration.hasMoreElements())
            {
                ZipEntry zipentry = (ZipEntry)enumeration.nextElement();

                if (zipentry.getName().contains("/pattern/pattern_") && zipentry.getName().endsWith(".png"))
                {
                	int numStart = zipentry.getName().indexOf("/pattern/pattern_") + 17;
                	int numEnd = zipentry.getName().indexOf(".png");
                	
                	int numPattern = Integer.parseInt(zipentry.getName().substring(numStart, numEnd));
                	
                	if (numPattern > (maxNum - 1) || numPattern < 1) {
            			ModLogger.log(Level.WARNING, "Encountered out of range chisel pattern " + zipentry.getName() + ". This file will be ignored.");
                	} else {
	                	hasPattern[numPattern] = true;
	                	++numPatterns;
                	}
                }
            }
            
            if (numPatterns > 0)
            	ModLogger.log(Level.INFO, "Successfully loaded " + numPatterns + " chisel pattern" + (numPatterns > 1 ? "s." : "."));
            
            mod.close();
        }
        catch (Exception exception)
        {
			ModLogger.log(Level.WARNING, "Encountered a problem while initializing pattern icons.  See trace below.");
			exception.printStackTrace();
        }
        
        return true;
    }
    
    /**
     * Returns the next pattern in number sequence.
     */
    public static int getNext(int inPattern)
    {
    	for (int outPattern = (inPattern + 1); outPattern < maxNum; ++outPattern)
        	if (hasPattern[outPattern])
        		return outPattern;
    	
		return 0;
    }
    
    /**
     * Returns the previous pattern in number sequence.
     */
    public static int getPrev(int inPattern)
    {
    	if (inPattern == 0)
    		inPattern = maxNum;
    	
    	for (int outPattern = (inPattern - 1); outPattern > 0; --outPattern)
        	if (hasPattern[outPattern])
        		return outPattern;
    	
		return 0;
    }
    	
}
