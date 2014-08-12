package com.carpentersblocks.util.handler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.classloading.FMLForgePlugin;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.CarpentersBlocksCachedResources;
import com.carpentersblocks.util.ModLogger;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.FeatureRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DesignHandler {

    public static ArrayList<String> listChisel    = new ArrayList<String>();
    public static ArrayList<String> listBed       = new ArrayList<String>();
    public static ArrayList<String> listFlowerPot = new ArrayList<String>();
    public static ArrayList<String> listTile      = new ArrayList<String>();

    private static final String PATH_BASE       = "assets/carpentersblocks/textures/blocks/";
    private static final String PATH_EXEMPT     = "template/";
    private static final String PATH_CHISEL     = "designs/chisel/";
    private static final String PATH_BED        = "designs/bed/";
    private static final String PATH_FLOWER_POT = "designs/flowerpot/";
    private static final String PATH_TILE       = "designs/tile/";

    private static boolean isPathValid(String path)
    {
        return path.contains(PATH_BASE) &&
               path.endsWith(".png") &&
               !path.contains(PATH_EXEMPT);
    }

    /**
     * Processes design files.
     */
    public static void preInit(FMLPreInitializationEvent event)
    {
        if (FMLForgePlugin.RUNTIME_DEOBF) {

            try {
                ZipFile zipFile = new ZipFile(event.getSourceFile());
                Enumeration enumeration = zipFile.entries();
                while (enumeration.hasMoreElements()) {
                    processPath(((ZipEntry)enumeration.nextElement()).getName());
                }
                zipFile.close();
            } catch (Exception e) { }

        } else {

            File folder = new File(event.getSourceFile().getAbsolutePath());

            for (File file : FileUtils.listFiles(folder, new String[] { "png" }, true)) {
                processPath(file.getAbsolutePath().replace("\\", "/"));
            }
        }

        ModLogger.log(Level.INFO, "Designs found: Bed(" + listBed.size() + "), Chisel(" + listChisel.size() + "), FlowerPot(" + listFlowerPot.size() + "), Tile(" + listTile.size() + ")");
    }

    private static void processPath(String path)
    {
        if (isPathValid(path)) {

            String name = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));

            if (path.contains(PATH_CHISEL)) {
                listChisel.add(name);
            } else if (path.contains(PATH_BED)) {
                listBed.add(name);
            } else if (path.contains(PATH_FLOWER_POT)) {
                listFlowerPot.add(name);
            } else if (path.contains(PATH_TILE)) {
                listTile.add(name);
            }

        }
    }

    @SideOnly(Side.CLIENT)
    public static void addResources(IResourceManager resourceManager)
    {
        for (String iconName : listBed)
        {
            ArrayList<BufferedImage> tempList = getBedIcons(resourceManager, iconName);
            for (BufferedImage image : tempList) {
                CarpentersBlocksCachedResources.INSTANCE.addResource("/textures/blocks/designs/bed/cache/" + iconName + "_" + tempList.indexOf(image), image);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerIcons(IIconRegister iconRegister)
    {
        if (BlockRegistry.enableBed) {
            for (String iconName : listBed) {
                IIcon[] icons = new IIcon[8];
                for (int count = 0; count < 8; ++count) {
                    icons[count] = iconRegister.registerIcon(CarpentersBlocksCachedResources.INSTANCE.getModId() + ":" + PATH_BED + "cache/" + iconName + "_" + count);
                }
                IconRegistry.icon_design_bed.add(icons);
            }
        }
        if (FeatureRegistry.enableChiselDesigns) {
            for (String iconName : listChisel) {
                IconRegistry.icon_design_chisel.add(iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + PATH_CHISEL + iconName));
            }
        }
        if (BlockRegistry.enableFlowerPot) {
            for (String iconName : listFlowerPot) {
                IconRegistry.icon_design_flower_pot.add(iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + PATH_FLOWER_POT + iconName));
            }
        }
        if (FeatureRegistry.enableTile) {
            for (String iconName : listTile) {
                IconRegistry.icon_design_tile.add(iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + PATH_TILE + iconName));
            }
        }
    }

    public static ArrayList<String> getListForType(String type)
    {
        return (ArrayList<String>) (
                type.equals("chisel") ? listChisel.clone() :
                type.equals("bed") ? listBed.clone() :
                type.equals("flowerpot") ? listFlowerPot.clone() :
                type.equals("tile") ? listTile.clone() : null);
    }

    /**
     * Returns name of next tile in list.
     */
    public static String getNext(String type, String iconName)
    {
        ArrayList<String> tempList = getListForType(type);

        if (tempList.isEmpty()) {
            return iconName;
        } else {
            int idx = tempList.indexOf(iconName) + 1;
            return tempList.get(idx >= tempList.size() ? 0 : idx);
        }
    }

    /**
     * Returns name of previous tile in list.
     */
    public static String getPrev(String type, String iconName)
    {
        ArrayList<String> tempList = getListForType(type);

        if (tempList.isEmpty()) {
            return iconName;
        } else {
            int idx = iconName.equals("") ? tempList.size() - 1 : tempList.indexOf(iconName) - 1;
            return tempList.get(idx < 0 ? tempList.size() - 1 : idx);
        }
    }

    @SideOnly(Side.CLIENT)
    public static ArrayList<BufferedImage> getBedIcons(IResourceManager resourceManager, String atlas)
    {
        ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();

        try
        {
            ResourceLocation resourceLocation = new ResourceLocation(CarpentersBlocks.MODID + ":textures/blocks/designs/bed/" + atlas + ".png");
            BufferedImage image = ImageIO.read(resourceManager.getResource(resourceLocation).getInputStream());

            int size = image.getWidth() / 3;
            int rows = image.getHeight() / size;
            int cols = image.getWidth() / size;
            int count = -1;

            for (int x = 0; x < rows; x++)
            {
                for (int y = 0; y < cols; y++)
                {
                    ++count;
                    switch (count) {
                        case 0:
                        case 2:
                        case 9:
                        case 11:
                            continue;
                        default:
                            BufferedImage bufferedImage = new BufferedImage(size, size, image.getType());
                            Graphics2D gr = bufferedImage.createGraphics();

                            switch (count) {
                                case 3:
                                case 6:
                                    gr.rotate(Math.toRadians(270.0D), size/2, size/2);
                                    break;
                                case 5:
                                case 8:
                                    gr.rotate(Math.toRadians(90.0D), size/2, size/2);
                                    break;
                            }

                            gr.drawImage(image, 0, 0, size, size, size * y, size * x, size * y + size, size * x + size, null);
                            gr.dispose();

                            imageList.add(bufferedImage);
                    }
                }
            }
        }
        catch (Exception e) { }

        return imageList;
    }

}
