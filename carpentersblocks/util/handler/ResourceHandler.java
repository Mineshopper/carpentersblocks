package carpentersblocks.util.handler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;

import org.apache.logging.log4j.Level;

import carpentersblocks.CarpentersBlocksCachedResources;
import carpentersblocks.util.ModLogger;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourceHandler {

    private static final CarpentersBlocksCachedResources modContainer = new CarpentersBlocksCachedResources();
    private static ArrayList<BufferedImage> images  = new ArrayList<BufferedImage>();
    private static ArrayList<String> entries = new ArrayList<String>();

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
            try {
                if (createDirectory()) {
                    createZip(CarpentersBlocksCachedResources.resourceDir, CarpentersBlocksCachedResources.MODID + ".zip");
                    ModLogger.log(Level.INFO, "Cached " + images.size() + " resource" + (images.size() != 1 ? "s." : "."));
                    FMLClientHandler.instance().addModAsResource(modContainer);
                    Minecraft.getMinecraft().refreshResources();
                }
            } catch (Exception e) {
                ModLogger.log(Level.WARN, "Resource caching failed: " + e.getMessage());
            }
        }
    }

    private static boolean createDirectory() throws Exception
    {
        File dir = new File(CarpentersBlocksCachedResources.resourceDir);

        if (!dir.exists()) {
            dir.mkdir();
        }

        return dir.exists();
    }

    private static void createZip(String dir, String fileName) throws Exception
    {
        File file = new File(dir, fileName);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));

        for (BufferedImage image : images) {
            out.putNextEntry(new ZipEntry("assets/" + CarpentersBlocksCachedResources.MODID.toLowerCase() + "/textures/blocks/designs/bed/cache/" + entries.get(images.indexOf(image)) + ".png"));
            ImageIO.write(image, "png", out);
            out.closeEntry();
        }

        out.flush();
        out.close();
    }

}
