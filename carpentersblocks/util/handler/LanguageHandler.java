package carpentersblocks.util.handler;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.Level;

import carpentersblocks.util.ModLogger;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class LanguageHandler {
    
    public static void init(FMLPreInitializationEvent event)
    {
        try
        {
            ZipFile zip = new ZipFile(event.getSourceFile());
            Enumeration enumeration = zip.entries();
            
            while (enumeration.hasMoreElements())
            {
                ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
                
                if (zipentry.getName().endsWith(".lang"))
                {
                    String lang = zipentry.getName().substring(zipentry.getName().indexOf("/lang/") + 6, zipentry.getName().indexOf(".lang"));
                    LanguageRegistry.instance().loadLocalization("/" + zipentry.getName(), lang, false);
                }
            }
            
            zip.close();
        }
        catch (Exception e)
        {
            ModLogger.log(Level.WARN, "Encountered a problem while loading language files: " + e.getMessage());
        }
    }
    
}
