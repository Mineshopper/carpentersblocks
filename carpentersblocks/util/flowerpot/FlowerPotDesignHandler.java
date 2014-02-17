package carpentersblocks.util.flowerpot;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.Level;

import carpentersblocks.util.ModLogger;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class FlowerPotDesignHandler {
    
    public static int maxNum = 256;
    public static boolean[] hasDesign = new boolean[maxNum];
    
    /**
     * Initializes pattern detection.
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
                
                if (zipentry.getName().contains("/flowerpot/design/design_") && zipentry.getName().endsWith(".png"))
                {
                    int numStart = zipentry.getName().indexOf("/flowerpot/design/design_") + 25;
                    int numEnd = zipentry.getName().indexOf(".png");
                    
                    int numPattern = Integer.parseInt(zipentry.getName().substring(numStart, numEnd));
                    
                    if (numPattern > maxNum - 1 || numPattern < 1) {
                        ModLogger.log(Level.WARN, "Encountered out of range flower pot design " + zipentry.getName() + ". This file will be ignored.");
                    } else {
                        hasDesign[numPattern] = true;
                        ++numDesigns;
                    }
                }
            }
            
            if (numDesigns > 0) {
                ModLogger.log(Level.INFO, "Successfully loaded " + numDesigns + " flower pot design" + (numDesigns > 1 ? "s." : "."));
            }
            
            mod.close();
        }
        catch (Exception e)
        {
            ModLogger.log(Level.WARN, "Encountered a problem while initializing flower pot designs: " + e.getMessage());
        }
        
        return true;
    }
    
    /**
     * Returns the next design in number sequence.
     */
    public static int getNext(int inDesign)
    {
        for (int outDesign = inDesign + 1; outDesign < maxNum; ++outDesign) {
            if (hasDesign[outDesign]) {
                return outDesign;
            }
        }
        
        return 0;
    }
    
    /**
     * Returns the previous design in number sequence.
     */
    public static int getPrev(int inDesign)
    {
        if (inDesign == 0) {
            inDesign = maxNum;
        }
        
        for (int outDesign = inDesign - 1; outDesign > 0; --outDesign) {
            if (hasDesign[outDesign]) {
                return outDesign;
            }
        }
        
        return 0;
    }
    
}
