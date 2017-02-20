package com.carpentersblocks.util.handler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.CarpentersBlocksCachedResources;
import com.carpentersblocks.util.ModLogger;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.FeatureRegistry;
import com.carpentersblocks.util.registry.ItemRegistry;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DesignHandler {

    public static List<String> listChisel    = new ArrayList<String>();
    public static List<String> listBed       = new ArrayList<String>();
    public static List<String> listFlowerPot = new ArrayList<String>();
    public static List<String> listTile      = new ArrayList<String>();

    private static final String PATH_BASE       = "assets/carpentersblocks/textures/blocks/";
    private static final String PATH_EXEMPT     = "template/";
    private static final String PATH_CHISEL     = "blocks/designs/chisel/";
    private static final String PATH_BED        = "blocks/designs/bed/";
    private static final String PATH_FLOWER_POT = "blocks/designs/flowerpot/";
    private static final String PATH_TILE       = "blocks/designs/tile/";

    private static boolean isPathValid(String path) {
        return path.contains(PATH_BASE) &&
               path.endsWith(".png") &&
               !path.contains(PATH_EXEMPT);
    }

    /**
     * Processes design files.
     */
    public static void preInit(FMLPreInitializationEvent event) {
        File filePath = new File(event.getSourceFile().getAbsolutePath());
        if (filePath.isDirectory()) {
            for (File file : FileUtils.listFiles(filePath, new String[] { "png" }, true)) {
                processPath(file.getAbsolutePath().replace("\\", "/"));
            }
        } else {
        	JarFile jarFile = null;
            try {
                jarFile = new JarFile(event.getSourceFile());
                Enumeration enumeration = jarFile.entries();
                while (enumeration.hasMoreElements()) {
                    processPath(((ZipEntry)enumeration.nextElement()).getName());
                }
            } catch (Exception ex) {
            	ex.printStackTrace();
            } finally {
            	if (jarFile != null) {
            		try {
            			jarFile.close();
            		} catch (Exception ex1) {}
            	}
            }
        }
        ModLogger.log(Level.INFO, String.format("Designs found: Bed(%s), Chisel(%s), FlowerPot(%s), Tile(%s)", listBed.size(), listChisel.size(), listFlowerPot.size(), listTile.size()));
    }

    private static void processPath(String path) {
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
    public static void addResources(IResourceManager resourceManager) {
        for (String iconName : listBed) {
            List<BufferedImage> tempList = getBedIcons(resourceManager, iconName);
            for (BufferedImage image : tempList) {
                CarpentersBlocksCachedResources.INSTANCE.addResource("/textures/blocks/designs/bed/cache/" + iconName + "_" + tempList.indexOf(image), image);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerSprites(TextureMap textureMap) {
        if (BlockRegistry.enableBed) {
            for (String spriteName : listBed) {
                TextureAtlasSprite[] sprites = new TextureAtlasSprite[8];
                for (int count = 0; count < 8; ++count) {
                	sprites[count] = textureMap.registerSprite(new ResourceLocation(CarpentersBlocksCachedResources.INSTANCE.getModId(), PATH_BED + "cache/" + spriteName + "_" + count));
                }
                SpriteRegistry.sprite_design_bed.add(sprites);
            }
        }
        if (FeatureRegistry.enableChiselDesigns) {
            for (String spriteName : listChisel) {
            	SpriteRegistry.sprite_design_chisel.add(textureMap.registerSprite(new ResourceLocation(CarpentersBlocks.MOD_ID, PATH_CHISEL + spriteName)));
            }
        }
        if (BlockRegistry.enableFlowerPot) {
            for (String spriteName : listFlowerPot) {
            	SpriteRegistry.sprite_design_flower_pot.add(textureMap.registerSprite(new ResourceLocation(CarpentersBlocks.MOD_ID + ":" + PATH_FLOWER_POT + spriteName)));
            }
        }
        if (ItemRegistry.enableTile) {
            for (String spriteName : listTile) {
            	SpriteRegistry.sprite_design_tile.add(textureMap.registerSprite(new ResourceLocation(CarpentersBlocks.MOD_ID + ":" + PATH_TILE + spriteName)));
            }
        }
    }

    public static List<String> getListForType(String type) {
        return type.equals("chisel") ? new ArrayList<String>(listChisel) :
               type.equals("bed") ? new ArrayList<String>(listBed) :
               type.equals("flowerpot") ? new ArrayList<String>(listFlowerPot) :
               type.equals("tile") ? new ArrayList<String>(listTile) : null;
    }

    /**
     * Returns name of next tile in list.
     */
    public static String getNext(String type, String iconName) {
        List<String> tempList = getListForType(type);
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
    public static String getPrev(String type, String iconName) {
        List<String> tempList = getListForType(type);
        if (tempList.isEmpty()) {
            return iconName;
        } else {
            int idx = iconName.equals("") ? tempList.size() - 1 : tempList.indexOf(iconName) - 1;
            return tempList.get(idx < 0 ? tempList.size() - 1 : idx);
        }
    }

    @SideOnly(Side.CLIENT)
    public static ArrayList<BufferedImage> getBedIcons(IResourceManager resourceManager, String atlas) {
        ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
        try {
            ResourceLocation resourceLocation = new ResourceLocation(CarpentersBlocks.MOD_ID + ":textures/blocks/designs/bed/" + atlas + ".png");
            BufferedImage image = ImageIO.read(resourceManager.getResource(resourceLocation).getInputStream());

            int size = image.getWidth() / 3;
            int rows = image.getHeight() / size;
            int cols = image.getWidth() / size;
            int count = -1;

            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {
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