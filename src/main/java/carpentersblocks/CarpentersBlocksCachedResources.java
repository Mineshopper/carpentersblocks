package carpentersblocks;

import java.io.File;

import net.minecraft.client.Minecraft;

import org.apache.commons.io.FilenameUtils;

import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.client.FMLFolderResourcePack;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CarpentersBlocksCachedResources extends DummyModContainer {

    public static String MODID = "CarpentersBlocksCachedResources";
    public static String resourceDir = FilenameUtils.getFullPathNoEndSeparator(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()) + "\\mods\\" + CarpentersBlocks.MODID.toLowerCase();

    public CarpentersBlocksCachedResources()
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

}
