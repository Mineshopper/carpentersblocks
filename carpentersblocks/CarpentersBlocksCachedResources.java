package carpentersblocks;

import java.io.File;

import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.client.FMLFolderResourcePack;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;

public class CarpentersBlocksCachedResources extends DummyModContainer {

    private String modDir = "";
    public final static String MODID = "CarpentersBlocksCachedResources";
    public final static String fileName = MODID + ".zip";

    public CarpentersBlocksCachedResources(String modDir)
    {
        super(new ModMetadata());
        this.modDir = modDir;
        ModMetadata meta = getMetadata();
        meta.modId = MODID;
        meta.name = "Carpenter's Blocks Cached Resources";
        meta.description = "Holds dynamically-created resources used with Carpenter's Blocks.";
    }

    @Override
    public File getSource()
    {
        return new File(modDir, fileName);
    }

    @Override
    public Class<?> getCustomResourcePackClass()
    {
        return getSource().isDirectory() ? FMLFolderResourcePack.class : FMLFileResourcePack.class;
    }

}
