package carpentersblocks.util.handler;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import carpentersblocks.util.ModLogger;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class BedDesignHandler
{
	
	public static int maxNum = 256;
	public static boolean[]	hasDesign = new boolean[maxNum];
	public static boolean[]	hasPillow = new boolean[maxNum];
	public static boolean[]	hasBlanket = new boolean[maxNum];

	/**
	 * Initializes design detection.
	 */
    public static boolean init(FMLPreInitializationEvent event)
    {
        try
        {
        	ZipFile mod = new ZipFile(event.getSourceFile());
            Enumeration enumeration = mod.entries();
            
            int numDesigns = 0;

            while (enumeration.hasMoreElements())
            {
                ZipEntry zipentry = (ZipEntry)enumeration.nextElement();

                if (zipentry.getName().contains("/bed/design_"))
                {
                	
                	int numStart = zipentry.getName().indexOf("/bed/design_") + 12;
                	int numEnd = zipentry.getName().indexOf("/", numStart);
                	int numDesign = Integer.parseInt(zipentry.getName().substring(numStart, numEnd));
                	                	
                	if (numDesign > (maxNum - 1) || numDesign < 1) {
                		
            			ModLogger.log(Level.WARNING, "Encountered out of range bed design " + zipentry.getName() + ". This file will be ignored.");
            			
                	} else {
                		
                    	if (zipentry.isDirectory()) {
    		               	hasDesign[numDesign] = true;
    		               	++numDesigns;
                    	}
                	
	                	if (zipentry.getName().endsWith("blanket.png"))
	                		hasBlanket[numDesign] = true;
	                    
	                    if (zipentry.getName().endsWith("pillow.png"))
	                    	hasPillow[numDesign] = true;
                	}
                }
            }
            
            if (numDesigns > 0)
            	ModLogger.log(Level.INFO, "Successfully loaded " + numDesigns + " bed design" + (numDesigns > 1 ? "s." : "."));
            
            mod.close();
        }
        catch (Exception exception)
        {
			ModLogger.log(Level.WARNING, "Encountered a problem while initializing bed designs.");
        }
        
        return true;
    }
    
    /**
     * Returns the next design in number sequence.
     */
    public static int getNext(int inDesign)
    {
    	for (int outDesign = ++inDesign; outDesign < maxNum; ++outDesign)
        	if (hasDesign[outDesign])
        		return outDesign;

		return 0;
    }
    
    /**
     * Returns the previous design in number sequence.
     */
    public static int getPrev(int inDesign)
    {
    	if (inDesign == 0)
    		inDesign = maxNum;
    	
    	for (int outDesign = (inDesign - 1); outDesign > 0; --outDesign)
        	if (hasDesign[outDesign])
        		return outDesign;
    	
		return 0;
    }
    	
}
