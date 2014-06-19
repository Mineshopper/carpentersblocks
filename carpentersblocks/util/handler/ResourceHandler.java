package carpentersblocks.util.handler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;

import carpentersblocks.CarpentersBlocksCachedResources;
import carpentersblocks.util.ModLogger;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourceHandler {

    private static String modDir = "";
    private static ArrayList<BufferedImage> images  = new ArrayList<BufferedImage>();
    private static ArrayList<String>        entries = new ArrayList<String>();

    /**
     * Initializes resource handler.
     */
    public static void init(FMLPreInitializationEvent event)
    {
        modDir = FilenameUtils.getFullPathNoEndSeparator(event.getSourceFile().getAbsolutePath());
    }

    public static void addResource(String name, BufferedImage bufferedImage)
    {
        images.add(bufferedImage);
        entries.add(name);
    }

    /**
     * Creates resource file, loads it, and refreshes resources.
     */
    public static void addResources()
    {
        if (!images.isEmpty()) {
            createZip();
            FMLClientHandler.instance().addModAsResource(new CarpentersBlocksCachedResources(modDir));
            Minecraft.getMinecraft().refreshResources();
        }
    }

    private static void createZip()
    {
        File zipfile = new File(modDir, CarpentersBlocksCachedResources.fileName);

        try {

            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));

            for (BufferedImage image : images) {
                out.putNextEntry(new ZipEntry("assets/" + CarpentersBlocksCachedResources.MODID.toLowerCase() + "/textures/blocks/designs/bed/cache/" + entries.get(images.indexOf(image)) + ".png"));
                ImageIO.write(image, "png", out);
                out.closeEntry();
            }

            out.flush();
            out.close();

            ModLogger.log(Level.INFO, "Cached " + images.size() + " resource" + (images.size() != 1 ? "s." : "."));

        } catch (IOException e) {

            ModLogger.log(Level.WARN, "Resource caching failed: " + e.getMessage());

        }
    }

}
