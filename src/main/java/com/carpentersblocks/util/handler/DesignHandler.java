package com.carpentersblocks.util.handler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.ModLogger;
import com.carpentersblocks.client.TextureAtlasSprites;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.resources.FilePack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.TextureStitchEvent.Pre;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@EventBusSubscriber(modid = CarpentersBlocks.MOD_ID, bus = Bus.MOD)
public class DesignHandler {
	
    public static List<String> listChisel = new ArrayList<>();
    public static List<String> listBed = new ArrayList<>();
    public static List<String> listFlowerPot = new ArrayList<>();
    public static List<String> listTile = new ArrayList<>();

    private static final String PATH_CHISEL     = "assets/carpentersblocks/textures/block/designs/chisel/";
    private static final String PATH_BED        = "assets/carpentersblocks/textures/block/designs/bed/";
    private static final String PATH_FLOWER_POT = "assets/carpentersblocks/textures/block/designs/flowerpot/";
    private static final String PATH_TILE       = "assets/carpentersblocks/textures/block/designs/tile/";
    
    public enum DesignType {
    	CHISEL,
    	BED,
    	FLOWERPOT,
    	TILE
    }
    
    /**
     * Gathers filenames without a file extension for given path.
     * 
     * @param path the path
     * @return a list of filenames without a file extension
     */
    private static List<String> listFileNames(String path) {
    	try (Stream<Path> stream = Files.list(Paths.get(DesignHandler.class.getClassLoader().getResource(path).toURI()))) {
    		return stream
    				.filter(f -> !Files.isDirectory(f))
    				.map(Path::getFileName)
    				.map(Path::toString)
    				.map(s -> s.substring(0, s.lastIndexOf(".")))
    				.collect(Collectors.toList());
    	} catch (Exception ex) {
			ex.printStackTrace();
		}
    	return Collections.emptyList();
    }
    
    /**
     * Processes design files on both {@link Dist distributions}.
     */
    @SubscribeEvent
    public static void onFMLConstructModEvent(FMLConstructModEvent event) {
    	listChisel.addAll(listFileNames(PATH_CHISEL));
    	listBed.addAll(listFileNames(PATH_BED));
    	listFlowerPot.addAll(listFileNames(PATH_FLOWER_POT));
    	listTile.addAll(listFileNames(PATH_TILE));
        ModLogger.log(Level.INFO, "Designs found: Bed[{}], Chisel[{}], FlowerPot[{}], Tile[{}]", listBed.size(), listChisel.size(), listFlowerPot.size(), listTile.size());
    }
    
    /**
     * Gets list of design names for input design type.
     * 
     * @param type the design type
     * @return a list of design names
     */
    private static List<String> getListForType(DesignType type) {
        return type.equals(DesignType.CHISEL) ? new ArrayList<String>(listChisel) :
               type.equals(DesignType.BED) ? new ArrayList<String>(listBed) :
               type.equals(DesignType.FLOWERPOT) ? new ArrayList<String>(listFlowerPot) :
               type.equals(DesignType.TILE) ? new ArrayList<String>(listTile) : null;
    }

    /**
     * Returns name of next tile in list for design type.
     * 
     * @param type the design type
     * @param designName the current design name
     * @return the next design name
     */
    public static String getNext(DesignType type, String designName) {
        List<String> list = getListForType(type);
        if (list.isEmpty()) {
            return designName;
        } else {
            int idx = list.indexOf(designName) + 1;
            return list.get(idx >= list.size() ? 0 : idx);
        }
    }

    /**
     * Returns name of previous tile in list for design type.
     * 
     * @param type the design type
     * @param designName the current design name
     * @return the previous design name
     */
    public static String getPrev(DesignType type, String designName) {
        List<String> list = getListForType(type);
        if (list.isEmpty()) {
            return designName;
        } else {
            int idx = "".equals(designName) ? list.size() - 1 : list.indexOf(designName) - 1;
            return list.get(idx < 0 ? list.size() - 1 : idx);
        }
    }
    
    /**
     * Loads design resources.
     * 
     * @param event the texture stitch event
     */
    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent event) {
    	if (!PlayerContainer.BLOCK_ATLAS.equals(event.getMap().location())) {
    		return;
    	}
    	boolean pre = event instanceof Pre;
    	// bed designs
    	if (pre) {
    		for (String iconName : listBed) {
	            List<BufferedImage> tempList = getImageTilesFromBedDesign(iconName);
	            for (BufferedImage image : tempList) {
	            	DesignCache.getInstance().addImageResource(
	            			new StringBuilder("assets/minecraft/textures/block/carpentersblocks/bed/")
		            			.append(iconName)
		            			.append("_")
		            			.append(tempList.indexOf(image))
		            			.toString(), image);
	            }
	        }
    	}
		TextureAtlasSprites.sprite_design_bed.clear();
        for (String spriteName : listBed) {
            TextureAtlasSprite[] sprites = new TextureAtlasSprite[8];
            for (int count = 0; count < 8; ++count) {
            	ResourceLocation resourceLocation = new ResourceLocation("block/carpentersblocks/bed/" + spriteName + "_" + count);
            	if (pre) {
            		((TextureStitchEvent.Pre)event).addSprite(resourceLocation);
            	} else {
            		sprites[count] = ((TextureStitchEvent.Post)event).getMap().getSprite(resourceLocation);
            	}
            }
            TextureAtlasSprites.sprite_design_bed.add(sprites);
        }
        // chisel designs
    	TextureAtlasSprites.sprite_design_chisel.clear();
        for (String spriteName : listChisel) {
        	ResourceLocation resourceLocation = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/designs/chisel/" + spriteName);
        	if (pre) {
        		((TextureStitchEvent.Pre)event).addSprite(resourceLocation);
        	} else {
        		TextureAtlasSprites.sprite_design_chisel.add(((TextureStitchEvent.Post)event).getMap().getSprite(resourceLocation));
        	}
        }
        // flower pot designs
    	TextureAtlasSprites.sprite_design_flower_pot.clear();
        for (String spriteName : listFlowerPot) {
        	ResourceLocation resourceLocation = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/designs/flowerpot/" + spriteName);
        	if (pre) {
        		((TextureStitchEvent.Pre)event).addSprite(resourceLocation);
        	} else {
        		TextureAtlasSprites.sprite_design_flower_pot.add(((TextureStitchEvent.Post)event).getMap().getSprite(resourceLocation));
        	}
        }
        // tile designs
    	TextureAtlasSprites.sprite_design_tile.clear();
        for (String spriteName : listTile) {
        	ResourceLocation resourceLocation = new ResourceLocation(CarpentersBlocks.MOD_ID, "block/designs/tile/" + spriteName);
        	if (pre) {
        		((TextureStitchEvent.Pre)event).addSprite(resourceLocation);
        	} else {
        		TextureAtlasSprites.sprite_design_tile.add(((TextureStitchEvent.Post)event).getMap().getSprite(resourceLocation));
        	}
        }
        // update cache at end of pre work when all resources are staged
        if (pre) {
        	DesignCache.getInstance().createOrUpdateResourcePack((Pre) event);
        }
    }

    /**
     * Breaks down bed design into 16x16 tiles for saving into
     * the design cache.
     * 
     * @param fileName the file name
     * @return a list of buffered images
     */
    @OnlyIn(Dist.CLIENT)
    private static ArrayList<BufferedImage> getImageTilesFromBedDesign(String fileName) {
        ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
        ResourceLocation resourceLocation = new ResourceLocation(CarpentersBlocks.MOD_ID, new StringBuilder("textures/block/designs/bed/").append(fileName).append(".png").toString());
        try {
            BufferedImage image = ImageIO.read(Minecraft.getInstance().getResourceManager().getResource(resourceLocation).getInputStream());
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
        catch (Exception e) {
        	ModLogger.log(Level.ERROR, "Failed to get bed design icons: {}", e.getMessage());
        }
        return imageList;
    }
    
    /**
     * Caches resources that are created at runtime so they can
     * be stitched into the texture map and later retrieved like
     * any other sprite.
     */
    @OnlyIn(Dist.CLIENT)
    private static class DesignCache {
    	
    	private final static String PACK_FILENAME = CarpentersBlocks.MOD_ID + "-design-cache.zip";
    	private final static String PACK_NAME = "file/" + PACK_FILENAME;
    	
    	private static DesignCache _instance;
    	
    	private String _zipPath;
    	
    	private List<Pair<String, BufferedImage>> _stagedResources;
    	
        private DesignCache() {
        	_zipPath = new StringBuilder(Minecraft.getInstance().getResourcePackDirectory().getPath())
    			.append(File.separator)
    			.append(PACK_FILENAME)
    			.toString();
    		_stagedResources = new ArrayList<>();
    	}
        
        /**
         * Gets instance of class.
         * 
         * @return class instance
         */
        synchronized public static DesignCache getInstance() {
        	if (_instance == null) {
        		_instance = new DesignCache();
        	}
        	return _instance;
        }
        
        /**
         * Adds image resource for including in zip file.
         * 
         * @param path path to resource
         * @param bufferedImage the buffered image
         */
        public void addImageResource(String path, BufferedImage bufferedImage) {
        	this._stagedResources.add(Pair.of(path, bufferedImage));
        }
        
        /**
         * Makes modifications to immutable resource pack properties.
         * <p>
         * This should only be called once <em>all</em> staged resources
         * have been added as part of this event.
         * 
         * @param event the pre texture stitch event
         */
        public void createOrUpdateResourcePack(TextureStitchEvent.Pre event) {
        	this.createOrUpdateZip();
        	SimpleReloadableResourceManager resourceManager = (SimpleReloadableResourceManager) Minecraft.getInstance().getResourceManager();
        	ResourcePackList resourcePackList = Minecraft.getInstance().getResourcePackRepository();
        	if (!resourceManager.listPacks().anyMatch(r -> PACK_NAME.equals(r.getName()))) {
        		resourceManager.add(new FilePack(new File(_zipPath)));
        		resourcePackList.reload();
        	}
        	ResourcePackInfo resourcePackInfo = resourcePackList.getPack(PACK_NAME);
        	if (!resourcePackInfo.isRequired()) {
    			try {
    				// update 'title' field with friendly name
    				ObfuscationReflectionHelper.findField(ResourcePackInfo.class, "field_195802_d").set(resourcePackInfo, new StringTextComponent("Design Cache"));
    				// update 'required' field so pack cannot be disabled
    				ObfuscationReflectionHelper.findField(ResourcePackInfo.class, "field_195806_h").set(resourcePackInfo, true);
    			} catch (Exception ex) {
    				ModLogger.log(Level.ERROR, "Failed to modify resource pack info -- resource pack will have to be manually applied");
    			} finally {
    				// put enabled pack names into mutable list and manually add new resource pack if needed
    				Collection<String> enabledPacks = new ArrayList<>(resourcePackList.getSelectedIds());
    				if (!enabledPacks.contains(PACK_NAME)) {
	    				enabledPacks.add(resourcePackInfo.getTitle().getString());
	    				resourcePackList.setSelected(enabledPacks);
    				}
    			}
    		}
        }
        
        /**
         * Creates or updates resource pack zip file.
         */
        private void createOrUpdateZip() {
        	ResourceLocation packPngResourceLocation = new ResourceLocation(CarpentersBlocks.MOD_ID, "textures/block/designs/cache_logo.png");
        	IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        	try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(_zipPath)))) {
    	        // write staged resources
            	for (Pair<String, BufferedImage> pair : _stagedResources) {
    	            out.putNextEntry(new ZipEntry(pair.getLeft().toString() + ".png"));
    	            ImageIO.write(pair.getRight(), "png", out);
    	            out.closeEntry();
    	        }
            	// write logo
            	out.putNextEntry(new ZipEntry("pack.png"));
            	ImageIO.write(ImageIO.read(resourceManager.getResource(packPngResourceLocation).getInputStream()), "png", out);
            	out.closeEntry();
            	// write pack.mcmeta
            	out.putNextEntry(new ZipEntry("pack.mcmeta"));
            	out.write("{\"pack\":{\"pack_format\":6,\"description\":\"Carpenter's Blocks Design Cache\"}}".getBytes(StandardCharsets.UTF_8));
            	out.closeEntry();
            } catch (Exception e) {
            	ModLogger.log(Level.WARN, "Failed to create zip for design cache: {}", e.getMessage());
            } finally {
            	_stagedResources.clear();
            }
        }
        
    }
    
}