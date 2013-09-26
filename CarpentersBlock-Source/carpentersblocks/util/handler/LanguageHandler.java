package carpentersblocks.util.handler;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import carpentersblocks.util.ModLogger;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class LanguageHandler
{

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
                	String lang = zipentry.getName().substring((zipentry.getName().indexOf("/lang/") + 6), zipentry.getName().indexOf(".lang"));
                    LanguageRegistry.instance().loadLocalization("/" + zipentry.getName(), lang, false);
                }
            }

            zip.close();
        }
        catch (Exception exception)
        {
			ModLogger.log(Level.WARNING, "Encountered a problem while loading language files.");
        }
	}
	
}
