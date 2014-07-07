package carpentersblocks.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.client.resources.ResourcePackFileNotFoundException;
import carpentersblocks.CarpentersBlocksCachedResources;

import com.google.common.base.Charsets;

import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.common.ModContainer;

public class DynamicFileResourcePack extends FMLFileResourcePack {

    public DynamicFileResourcePack(ModContainer container)
    {
        super(container);
    }

    private ZipFile getResourcePackZipFile()
    {
        return CarpentersBlocksCachedResources.INSTANCE.getZipFile();
    }

    @Override
    protected InputStream getInputStreamByName(String resourceName) throws IOException
    {
        ZipFile zipfile = getResourcePackZipFile();
        ZipEntry zipentry = zipfile.getEntry(resourceName);

        try {
            if ("pack.mcmeta".equals(resourceName)) {
                return new ByteArrayInputStream(("{\n" +
                        " \"pack\": {\n"+
                        "   \"description\": \"dummy FML pack for "+CarpentersBlocksCachedResources.INSTANCE.getName()+"\",\n"+
                        "   \"pack_format\": 1\n"+
                        "}\n" +
                        "}").getBytes(Charsets.UTF_8));
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
