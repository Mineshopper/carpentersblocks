package carpentersblocks.util.handler;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.Level;

import carpentersblocks.util.ModLogger;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class TileHandler {
    
    public static List<String> tileList = new ArrayList<String>();

    /**
     * Initializes tiles.
     */
    public static boolean init(FMLPreInitializationEvent event)
    {
        try
        {
            ZipFile mod = new ZipFile(event.getSourceFile());
            Enumeration enumeration = mod.entries();

            while (enumeration.hasMoreElements())
            {
                ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
                
                if (zipentry.getName().contains("/tile/") && zipentry.getName().endsWith(".png"))
                {
                    int begin = zipentry.getName().indexOf("/tile/") + 6;
                    int end = zipentry.getName().indexOf(".png");
                    
                    String iconName = zipentry.getName().substring(begin, end);
                    
                    if (iconName != "blank") {
                        tileList.add(iconName);
                    }
                }
            }
            
            if (!tileList.isEmpty()) {
                ModLogger.log(Level.INFO, "Successfully loaded " + tileList.size() + " tile design" + (tileList.size() > 1 ? "s." : "."));
            }
            
            mod.close();
        }
        catch (Exception e)
        {
            ModLogger.log(Level.WARN, "Encountered a problem while initializing tile designs: " + e.getMessage());
        }
        
        return true;
    }

    /**
     * Returns name of next tile in list.
     */
    public static String getNext(String tile)
    {
        if (!tileList.isEmpty()) {
            int idx = tileList.indexOf(tile) + 1;
            return tileList.get(idx >= tileList.size() ? 0 : idx);
        }
        
        return tile;
    }
    
    /**
     * Returns name of previous tile in list.
     */
    public static String getPrev(String tile)
    {
        if (!tileList.isEmpty()) {
            int idx = tileList.indexOf(tile) - 1;
            return tileList.get(idx < 0 ? tileList.size() - 1 : idx);
        }
        
        return tile;
    }

}
