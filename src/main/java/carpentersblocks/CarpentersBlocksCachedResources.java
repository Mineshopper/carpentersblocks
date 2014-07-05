package carpentersblocks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;

import carpentersblocks.util.ModLogger;
import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.client.FMLFolderResourcePack;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CarpentersBlocksCachedResources extends DummyModContainer {

    public static final CarpentersBlocksCachedResources instance = new CarpentersBlocksCachedResources();
    public static String MODID = "CarpentersBlocksCachedResources";
    public static String resourceDir = FilenameUtils.normalizeNoEndSeparator(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()) + "\\mods\\" + CarpentersBlocks.MODID.toLowerCase();

    private CarpentersBlocksCachedResources()
    {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = MODID;
        meta.name = "Carpenter's Blocks Cached Resources";
        meta.description = "Holds dynamically-created resources used with Carpenter's Blocks.";
    }

    @Override
    public File getSource()
    {
        return new File(resourceDir, MODID + ".zip");
    }

    @Override
    public Class<?> getCustomResourcePackClass()
    {
        return getSource().isDirectory() ? FMLFolderResourcePack.class : FMLFileResourcePack.class;
    }

    public void clearResources()
    {
        image.clear();
        entry.clear();
    }

    private ArrayList<BufferedImage> image = new ArrayList<BufferedImage>();
    private ArrayList<String> entry = new ArrayList<String>();
    private ArrayList<String> path = new ArrayList<String>();

    public void addResource(String path, String entry, BufferedImage bufferedImage)
    {
        this.path.add(path);
        this.entry.add(entry);
        image.add(bufferedImage);
    }

    /**
     * Creates resource file, loads it, and refreshes resources.
     */
    public void createResources()
    {
        if (!image.isEmpty()) {
            try {
                if (createDirectory()) {
                    createZip(CarpentersBlocksCachedResources.resourceDir, CarpentersBlocksCachedResources.MODID + ".zip");
                    ModLogger.log(Level.INFO, "Cached " + image.size() + (image.size() != 1 ? " resources" : " resource"));
                }
            } catch (Exception e) {
                ModLogger.log(Level.WARN, "Resource caching failed: " + e.getMessage());
            }
        }
    }

    private boolean createDirectory() throws Exception
    {
        File dir = new File(CarpentersBlocksCachedResources.resourceDir);

        if (!dir.exists()) {
            dir.mkdir();
        }

        return dir.exists();
    }

    private void createZip(String dir, String fileName) throws Exception
    {
        File file = new File(dir, fileName);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));

        for (BufferedImage bufferedImage : image) {
            int idx = image.indexOf(bufferedImage);
            out.putNextEntry(new ZipEntry("assets/" + CarpentersBlocksCachedResources.MODID.toLowerCase() + path.get(idx) + "/" + entry.get(idx) + ".png"));
            ImageIO.write(bufferedImage, "png", out);
            out.closeEntry();
        }

        out.flush();
        out.close();
    }

}
