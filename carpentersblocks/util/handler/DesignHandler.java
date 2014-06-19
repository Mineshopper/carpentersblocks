package carpentersblocks.util.handler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

import org.apache.logging.log4j.Level;

import carpentersblocks.CarpentersBlocks;
import carpentersblocks.CarpentersBlocksCachedResources;
import carpentersblocks.util.ModLogger;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.client.FMLClientHandler;
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
        return path.startsWith(PATH_BASE) &&
               path.endsWith(".png") &&
               !path.contains(PATH_EXEMPT);
    }

    /**
     * Builds file list.
     */
    public static void init(FMLPreInitializationEvent event)
    {
        try
        {
            ZipFile zipFile = new ZipFile(event.getSourceFile());
            Enumeration enumeration = zipFile.entries();
            int count = 0;

            while (enumeration.hasMoreElements())
            {
                ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
                String fileName = zipEntry.getName();

                if (isPathValid(fileName))
                {
                    ++count;
                    int idxStart = PATH_BASE.length();
                    int idxEnd = fileName.indexOf(".png");

                    if (fileName.contains(PATH_CHISEL)) {
                        listChisel.add(fileName.substring(idxStart + PATH_CHISEL.length(), idxEnd));
                    } else if (fileName.contains(PATH_BED)) {
                        listBed.add(fileName.substring(idxStart + PATH_BED.length(), idxEnd));
                    } else if (fileName.contains(PATH_FLOWER_POT)) {
                        listFlowerPot.add(fileName.substring(idxStart + PATH_FLOWER_POT.length(), idxEnd));
                    } else if (fileName.contains(PATH_TILE)) {
                        listTile.add(fileName.substring(idxStart + PATH_TILE.length(), idxEnd));
                    }
                }
            }

            zipFile.close();

            ModLogger.log(Level.INFO, "Loaded " + count + " design" + (count != 1 ? "s." : "."));
        }
        catch (Exception e)
        {
            ModLogger.log(Level.WARN, "Encountered a problem loading designs: " + e.getMessage());
        }

        if (event.getSide().equals(Side.CLIENT)) {

            /* Create bed resources. */
            for (String iconName : listBed)
            {
                ArrayList<BufferedImage> tempList = genBedIcons(iconName);
                for (BufferedImage image : tempList) {
                    ResourceHandler.addResource(iconName + "_" + tempList.indexOf(image), image);
                }
            }

            ResourceHandler.addResources();

        }
    }

    @SideOnly(Side.CLIENT)
    public static void initDesignIcons(TextureStitchEvent.Pre event)
    {
        for (String iconName : listChisel) {
            IconRegistry.icon_design_chisel.add(event.map.registerIcon(CarpentersBlocks.MODID + ":" + PATH_CHISEL + iconName));
        }
        for (String iconName : listBed) {
            IIcon[] icons = new IIcon[8];
            for (int count = 0; count < 8; ++count) {
                icons[count] = event.map.registerIcon(CarpentersBlocksCachedResources.MODID.toLowerCase() + ":" + PATH_BED + "cache/" + iconName + "_" + count);
            }
            IconRegistry.icon_design_bed.add(icons);
        }
        for (String iconName : listFlowerPot) {
            IconRegistry.icon_design_flower_pot.add(event.map.registerIcon(CarpentersBlocks.MODID + ":" + PATH_FLOWER_POT + iconName));
        }
        for (String iconName : listTile) {
            IconRegistry.icon_design_tile.add(event.map.registerIcon(CarpentersBlocks.MODID + ":" + PATH_TILE + iconName));
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

    public static ArrayList<BufferedImage> genBedIcons(String atlas)
    {
        ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();

        try
        {
            ResourceLocation resourceLocation = new ResourceLocation(CarpentersBlocks.MODID + ":textures/blocks/designs/bed/" + atlas + ".png");
            BufferedImage image = ImageIO.read(FMLClientHandler.instance().getResourcePackFor(CarpentersBlocks.MODID).getInputStream(resourceLocation));

            int size = image.getWidth() / 3;
            int rows = image.getHeight() / size;
            int cols = image.getWidth() / size;
            int count = -1;

            // Pillow [1] - Shift up 7px
            // Blanket-Head-Left [3] - Rotate 270
            // Blanket-Head-Top [4]
            // Blanket-Head-Right [5] - Rotate 90
            // Blanket-Foot-Left [6] - Rotate 270
            // Blanket-Foot-Top [7]
            // Blanket-Foot-Right [8] - Rotate 90
            // Blanket-Foot-End [10]

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
                                case 1:
                                    gr.translate(0, -7);
                                    break;
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
