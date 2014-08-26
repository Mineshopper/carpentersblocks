package com.carpentersblocks;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackFileNotFoundException;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;
import com.carpentersblocks.util.ModLogger;
import com.carpentersblocks.util.handler.DesignHandler;
import com.google.common.base.Charsets;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CarpentersBlocksCachedResources extends DummyModContainer {

    public final static CarpentersBlocksCachedResources INSTANCE = new CarpentersBlocksCachedResources();
    private String MODID = "CarpentersBlocksCachedResources";
    private String resourceDir = FilenameUtils.normalizeNoEndSeparator(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()) + File.separator + "mods" + File.separator + CarpentersBlocks.MODID.toLowerCase();
    private static ZipFile resourcePackZipFile;
    private static ArrayList<Object[]> resources = new ArrayList<Object[]>();
    private final int RESOURCE_PATH  = 0;
    private final int RESOURCE_IMAGE = 1;

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
        FMLClientHandler.instance().addModAsResource(this);

        // Add resource pack to global list without triggering a full refresh
        ((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).reloadResourcePack(FMLClientHandler.instance().getResourcePackFor(MODID));
    }

    public static class DynamicFileResourcePack extends FMLFileResourcePack {

        public DynamicFileResourcePack(ModContainer container)
        {
            super(container);
        }

        public ZipFile getResourcePackZipFile()
        {
            if (resourcePackZipFile == null) {
                INSTANCE.rebuildCache();
            }
            return resourcePackZipFile;
        }

        @Override
        protected InputStream getInputStreamByName(String resourceName) throws IOException
        {
            ZipFile zipfile = getResourcePackZipFile();
            ZipEntry zipentry = zipfile.getEntry(resourceName);
            try {
                if ("pack.mcmeta".equals(resourceName)) {
                    return new ByteArrayInputStream(("{\n"+" \"pack\": {\n"+"   \"description\": \"dummy FML pack for "+getPackName()+"\",\n"+"   \"pack_format\": 1\n"+"}\n" + "}").getBytes(Charsets.UTF_8));
                } else {
                    return zipfile.getInputStream(zipentry);
                }
            } catch (IOException e) {
                throw new ResourcePackFileNotFoundException(resourcePackFile, resourceName);
            }
        }

        @Override
        public boolean hasResourceName(String resourceName)
        {
            return getResourcePackZipFile().getEntry(resourceName) != null;
        }

    }

    @Override
    public File getSource()
    {
        return new File(resourceDir, MODID + ".zip");
    }

    @Override
    public Class<?> getCustomResourcePackClass()
    {
        return DynamicFileResourcePack.class;
    }

    /**
     * Adds a resource to list to be added to resource pack.
     */
    public void addResource(String path, BufferedImage bufferedImage)
    {
        resources.add(new Object[] { path, bufferedImage });
    }

    /**
     * Creates final resource pack zip file.
     */
    private void createResourceZipFile()
    {
        try {
            if (createDirectory()) {
                createZip(resourceDir, MODID + ".zip");
                resourcePackZipFile = new ZipFile(getSource());
            }
        } catch (Exception e) {
            ModLogger.log(Level.WARN, "Cache rebuild failed: " + e.getMessage());
        }
    }

    /**
     * Creates directory for resource file.
     */
    private boolean createDirectory() throws Exception
    {
        File dir = new File(resourceDir);

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

        for (Object[] object : resources) {
            out.putNextEntry(new ZipEntry("assets/" + MODID.toLowerCase() + (String) object[RESOURCE_PATH] + ".png"));
            ImageIO.write((BufferedImage) object[RESOURCE_IMAGE], "png", out);
            out.closeEntry();
        }

        out.flush();
        out.close();
    }

    /**
     * Refreshes dynamic resources and creates new resource pack.
     */
    public void rebuildCache()
    {
        DesignHandler.addResources(Minecraft.getMinecraft().getResourceManager());
        createResourceZipFile();
        resources.clear();
    }

}
