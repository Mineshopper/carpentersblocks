package carpentersblocks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;

import carpentersblocks.util.ModLogger;
import carpentersblocks.util.handler.DesignHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CarpentersBlocksCachedResources extends DummyModContainer implements IResourceManagerReloadListener {

    private static final CarpentersBlocksCachedResources INSTANCE = new CarpentersBlocksCachedResources();
    public final static String MODID = "CarpentersBlocksCachedResources";
    public static String resourceDir = FilenameUtils.normalizeNoEndSeparator(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()) + "\\mods\\" + CarpentersBlocks.MODID.toLowerCase();

    private CarpentersBlocksCachedResources()
    {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = MODID;
        meta.name = "Carpenter's Blocks Cached Resources";
        meta.description = "Holds dynamically-created resources used with Carpenter's Blocks.";
    }

    /**
     * Initializes
     */
    public void init()
    {
        SimpleReloadableResourceManager resourceManager = (SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
        FMLClientHandler.instance().addModAsResource(this);
        resourceManager.reloadResourcePack(FMLClientHandler.instance().getResourcePackFor(MODID));
        resourceManager.registerReloadListener(this);
    }

    public static CarpentersBlocksCachedResources instance()
    {
        return INSTANCE;
    }

    @Override
    public File getSource()
    {
        return new File(resourceDir, MODID + ".zip");
    }

    @Override
    public Class<?> getCustomResourcePackClass()
    {
        return FMLFileResourcePack.class;
    }

    private ArrayList<BufferedImage> image = new ArrayList<BufferedImage>();
    private ArrayList<String> entry = new ArrayList<String>();
    private ArrayList<String> path = new ArrayList<String>();

    /**
     * Adds a resource to list to be added to resource pack.
     */
    public void addResource(String path, String entry, BufferedImage bufferedImage)
    {
        this.path.add(path);
        this.entry.add(entry);
        image.add(bufferedImage);
    }

    /**
     * Creates final resource pack.
     */
    public void createResourcePack()
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

    /**
     * Creates directory for resource file.
     */
    private boolean createDirectory() throws Exception
    {
        File dir = new File(CarpentersBlocksCachedResources.resourceDir);

        if (!dir.exists()) {
            dir.mkdir();
        }

        return dir.exists();
    }

    /**
     * Creates resource zip file.
     */
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

    /**
     * Refreshes dynamic resources and creates new resource pack.
     */
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        path.clear();
        entry.clear();
        image.clear();
        DesignHandler.addResources(resourceManager);
        createResourcePack();
    }

}
