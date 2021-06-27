package com.carpentersblocks.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.ModLogger;
import com.carpentersblocks.item.CbItems;
import com.google.common.collect.ImmutableMap;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

public class Configuration {
	
	private static final CommonConfig COMMON_CONFIG;
    public static final ForgeConfigSpec COMMON_SPEC;

	static {
		Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		COMMON_CONFIG = specPair.getLeft();
		COMMON_SPEC = specPair.getRight();
	}
	
	private static boolean enableApplicator;
	private static boolean enableBarrier;
    private static boolean enableBed;
    private static boolean enableBlock;
    private static boolean enableButton;
    private static boolean enableChisel;
    private static boolean enableCollapsibleBlock;
    private static boolean enableDaylightSensor;
    private static boolean enableDoor;
    private static boolean enableFlowerPot;
    private static boolean enableGarageDoor;
    private static boolean enableGate;
    private static boolean enableHammer;
    private static boolean enableHatch;
    private static boolean enableLadder;
    private static boolean enableLever;
    private static boolean enablePressurePlate;
    private static boolean enableSafe;
    private static boolean enableSlope;
    private static boolean enableStairs;
    private static boolean enableTile;
    private static boolean enableTorch;
    
    private static int itemCarpentersToolsUses;
    private static boolean itemCarpentersToolsDamageable;
    private static double itemHammerDamageChanceFromSlopes;
    private static double itemHammerDamageChanceFromStairs;
    private static double itemHammerDamageChanceFromCollapsible;
    private static boolean enableCovers;
    private static boolean enableOverlays;
    private static boolean enableSideCovers;
    private static boolean enableDyeColors;
    private static boolean enableChiselDesigns;
    private static boolean enableTorchWeatherEffects;
    private static boolean enableOwnership;
    private static boolean enableIllumination;
    private static boolean enableRailSlopeFill;
    private static boolean enableGarageDoorFill;
    private static boolean enableFreeStandingLadders;
    private static boolean enableSmoothSlopes;
    private static int multiBlockSizeLimit;
    private static List<String> overlayItems;
    private static List<String> coverBlockExceptions;
	
    public static boolean isApplicatorEnabled() {
    	return enableApplicator;
    }
    
    public static boolean isBarrierEnabled() {
    	return enableBarrier;
    }

    public static boolean isBedEnabled() {
    	return enableBed;
    }
    
    public static boolean isBlockEnabled() {
    	return enableBlock;
    }

    public static boolean isButtonEnabled() {
    	return enableButton;
    }

    public static boolean isChiselEnabled() {
    	return enableChisel;
    }
    
    public static boolean isCollapsibleBlockEnabled() {
    	return enableCollapsibleBlock;
    }

    public static boolean isDaylightSensorEnabled() {
    	return enableDaylightSensor;
    }

    public static boolean isDoorEnabled() {
    	return enableDoor;
    }

    public static boolean isFlowerPotEnabled() {
    	return enableFlowerPot;
    }

    public static boolean isGarageDoorEnabled() {
    	return enableGarageDoor;
    }

    public static boolean isGateEnabled() {
    	return enableGate;
    }

    public static boolean isHammerEnabled() {
    	return enableHammer;
    }
    
    public static boolean isHatchEnabled() {
    	return enableHatch;
    }

    public static boolean isLadderEnabled() {
    	return enableLadder;
    }

    public static boolean isLeverEnabled() {
    	return enableLever;
    }

    public static boolean isPressurePlateEnabled() {
    	return enablePressurePlate;
    }

    public static boolean isSafeEnabled() {
    	return enableSafe;
    }

    public static boolean isSlopeEnabled() {
    	return enableSlope;
    }

    public static boolean isStairsEnabled() {
    	return enableStairs;
    }
    
    public static boolean isTileEnabled() {
    	return enableTile;
    }

    public static boolean isTorchEnabled() {
    	return enableTorch;
    }
    
    public static int getItemCarpentersToolsUses() {
    	return itemCarpentersToolsUses;
    }

    public static boolean isItemCarpentersToolsDamageable() {
    	return itemCarpentersToolsDamageable;
    }

    public static double getItemHammerDamageChanceFromSlopes() {
    	return itemHammerDamageChanceFromSlopes;
    }

    public static double getItemHammerDamageChanceFromStairs() {
    	return itemHammerDamageChanceFromStairs;
    }

    public static double getItemHammerDamageChanceFromCollapsible() {
    	return itemHammerDamageChanceFromCollapsible;
    }

    public static boolean isCoversEnabled() {
    	return enableCovers;
    }

    public static boolean isOverlaysEnabled() {
    	return enableOverlays;
    }

    public static boolean isSideCoversEnabled() {
    	return enableSideCovers;
    }

    public static boolean isDyeColorsEnabled() {
    	return enableDyeColors;
    }

    public static boolean isChiselDesignsEnabled() {
    	return enableChiselDesigns;
    }

    public static boolean isTorchWeatherEffectsEnabled() {
    	return enableTorchWeatherEffects;
    }

    public static boolean isOwnershipEnabled() {
    	return enableOwnership;
    }

    public static boolean isIlluminationEnabled() {
    	return enableIllumination;
    }

    public static boolean isRailSlopeFillEnabled() {
    	return enableRailSlopeFill;
    }

    public static boolean isGarageDoorFillEnabled() {
    	return enableGarageDoorFill;
    }

    public static boolean isFreeStandingLaddersEnabled() {
    	return enableFreeStandingLadders;
    }

    public static boolean isSmoothSlopesEnabled() {
    	return enableSmoothSlopes;
    }

    public static int getMultiBlockSizeLimit() {
    	return multiBlockSizeLimit;
    }

    public static List<String> getOverlayItems() {
    	return overlayItems;
    }

    public static List<String> getCoverBlockExceptions() {
    	return coverBlockExceptions;
    }
    
    @Mod.EventBusSubscriber(modid = CarpentersBlocks.MOD_ID, bus = Bus.MOD)
    static class CommonConfig {
    	
		private static BooleanValue enableApplicator;
		private static BooleanValue enableBarrier;
		private static BooleanValue enableBed;
		private static BooleanValue enableBlock;
		private static BooleanValue enableButton;
		private static BooleanValue enableChisel;
	    private static BooleanValue enableCollapsibleBlock;
	    private static BooleanValue enableDaylightSensor;
	    private static BooleanValue enableDoor;
	    private static BooleanValue enableFlowerPot;
	    private static BooleanValue enableGarageDoor;
	    private static BooleanValue enableGate;
	    private static BooleanValue enableHammer;
	    private static BooleanValue enableHatch;
	    private static BooleanValue enableLadder;
	    private static BooleanValue enableLever;
	    private static BooleanValue enablePressurePlate;
	    private static BooleanValue enableSafe;
	    private static BooleanValue enableSlope;
	    private static BooleanValue enableStairs;
	    private static BooleanValue enableTile;
	    private static BooleanValue enableTorch;
	    
	    private static IntValue itemCarpentersToolsUses;
	    private static BooleanValue itemCarpentersToolsDamageable;
	    private static DoubleValue itemHammerDamageChanceFromSlopes;
	    private static DoubleValue itemHammerDamageChanceFromStairs;
	    private static DoubleValue itemHammerDamageChanceFromCollapsible;
	    private static BooleanValue enableCovers;
	    private static BooleanValue enableOverlays;
	    private static BooleanValue enableSideCovers;
	    private static BooleanValue enableDyeColors;
	    private static BooleanValue enableChiselDesigns;
	    private static BooleanValue enableTorchWeatherEffects;
	    private static BooleanValue enableOwnership;
	    private static BooleanValue enableIllumination;
	    private static BooleanValue enableRailSlopeFill;
	    private static BooleanValue enableGarageDoorFill;
	    private static BooleanValue enableFreeStandingLadders;
	    private static BooleanValue enableSmoothSlopes;
	    private static IntValue multiBlockSizeLimit;
	    private static ConfigValue<List<String>> overlayItemList;
	    private static ConfigValue<List<String>> coverBlockExceptionList;
	
	    /**
	     * Used to populate public static variables once configuration
	     * values are read from filesystem.
	     * 
	     * @param configEvent the event
	     */
		@SubscribeEvent
		public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
			if (COMMON_SPEC != configEvent.getConfig().getSpec()) {
				return;
			}
			Configuration.enableBarrier = enableBarrier.get();
			Configuration.enableApplicator = enableApplicator.get();
			Configuration.enableBed = enableBed.get();
			Configuration.enableBlock = enableBlock.get();
			Configuration.enableButton = enableButton.get();
			Configuration.enableChisel = enableChisel.get();
			Configuration.enableCollapsibleBlock = enableCollapsibleBlock.get();
			Configuration.enableDaylightSensor = enableDaylightSensor.get();
			Configuration.enableDoor = enableDoor.get();
			Configuration.enableFlowerPot = enableFlowerPot.get();
			Configuration.enableGarageDoor = enableGarageDoor.get();
			Configuration.enableGate = enableGate.get();
			Configuration.enableHammer = enableHammer.get();
			Configuration.enableHatch = enableHatch.get();
			Configuration.enableLadder = enableLadder.get();
			Configuration.enableLever = enableLever.get();
			Configuration.enablePressurePlate = enablePressurePlate.get();
			Configuration.enableSafe = enableSafe.get();
			Configuration.enableSlope = enableSlope.get();
			Configuration.enableStairs = enableStairs.get();
			Configuration.enableTile = enableTile.get();
			Configuration.enableTorch = enableTorch.get();
			Configuration.itemCarpentersToolsUses = itemCarpentersToolsUses.get();
			Configuration.itemCarpentersToolsDamageable = itemCarpentersToolsDamageable.get();
			Configuration.itemHammerDamageChanceFromSlopes = itemHammerDamageChanceFromSlopes.get();
			Configuration.itemHammerDamageChanceFromStairs = itemHammerDamageChanceFromStairs.get();
			Configuration.itemHammerDamageChanceFromCollapsible = itemHammerDamageChanceFromCollapsible.get();
			Configuration.enableCovers = enableCovers.get();
			Configuration.enableOverlays = enableOverlays.get();
			Configuration.enableSideCovers = enableSideCovers.get();
			Configuration.enableDyeColors = enableDyeColors.get();
			Configuration.enableChiselDesigns = enableChiselDesigns.get();
			Configuration.enableTorchWeatherEffects = enableTorchWeatherEffects.get();
			Configuration.enableOwnership = enableOwnership.get();
			Configuration.enableIllumination = enableIllumination.get();
			Configuration.enableRailSlopeFill = enableRailSlopeFill.get();
			Configuration.enableGarageDoorFill = enableGarageDoorFill.get();
			Configuration.enableFreeStandingLadders = enableFreeStandingLadders.get();
			Configuration.enableSmoothSlopes = enableSmoothSlopes.get();
			Configuration.multiBlockSizeLimit = multiBlockSizeLimit.get();
			Configuration.overlayItems = new ArrayList<>();
			for (String item : overlayItemList.get()) {
				Configuration.overlayItems.add(item);
	        }
			Configuration.coverBlockExceptions = new ArrayList<>();
			for (String item : coverBlockExceptionList.get()) {
				Configuration.coverBlockExceptions.add(item);
	        }
		}
		
		public CommonConfig(ForgeConfigSpec.Builder builder) {
			builder.push("blocks");
			enableBarrier = builder
					.comment("Enable Carpenter's Barrier")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableBarrier")
					.define("enableBarrier", false);
			enableBed = builder
					.comment("Enable Carpenter's Bed")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableBed")
					.define("enableBed", false);
			enableBlock = builder
					.comment("Enable Carpenter's Block", "Note: this block is required for most recipes")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableBlock")
					.define("enableBlock", true);
			enableButton = builder
					.comment("Enable Carpenter's Button")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableButton")
					.define("enableButton", false);
			enableCollapsibleBlock = builder
					.comment("Enable Carpenter's Collapsible Block")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableCollapsibleBlock")
					.define("enableCollapsibleBlock", false);
			enableDaylightSensor = builder
					.comment("Enable Carpenter's Daylight Sensor")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableDaylightSensor")
					.define("enableDaylightSensor", false);
			enableDoor = builder
					.comment("Enable Carpenter's Door")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableDoor")
					.define("enableDoor", false);
			enableFlowerPot = builder
					.comment("Enable Carpenter's Flower Pot")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableFlowerPot")
					.define("enableFlowerPot", false);
			enableGarageDoor = builder
					.comment("Enable Carpenter's Garage Door")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableGarageDoor")
					.define("enableGarageDoor", false);
			enableGate = builder
					.comment("Enable Carpenter's Gate")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableGate")
					.define("enableGate", false);
			enableHatch = builder
					.comment("Enable Carpenter's Hatch")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableHatch")
					.define("enableHatch", false);
			enableLadder = builder
					.comment("Enable Carpenter's Ladder")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableLadder")
					.define("enableLadder", false);
			enableLever = builder
					.comment("Enable Carpenter's Lever")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableLever")
					.define("enableLever", false);
			enablePressurePlate = builder
					.comment("Enable Carpenter's Pressure Plate")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enablePressurePlate")
					.define("enablePressurePlate", false);
			enableSafe = builder
					.comment("Enable Carpenter's Safe")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableSafe")
					.define("enableSafe", false);
			enableSlope = builder
					.comment("Enable Carpenter's Slope")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableSlope")
					.define("enableSlope", true);
			enableStairs = builder
					.comment("Enable Carpenter's Stairs")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableStairs")
					.define("enableStairs", false);
			enableTorch = builder
					.comment("Enable Carpenter's Torch")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableTorch")
					.define("enableTorch", false);
			
			builder.pop();
			builder.push("items");
			
			enableHammer = builder
					.comment("Enable Carpenter's Hammer")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableHammer")
					.define("enableHammer", true);
			
			enableChisel = builder
					.comment("Enable Carpenter's Chisel")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableChisel")
					.define("enableChisel", true);
			
			enableApplicator = builder
					.comment("Enable Carpenter's Applicator")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableApplicator")
					.define("enableApplicator", true);
			
			enableTile = builder
					.comment("Enable Carpenter's Tile")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableTile")
					.define("enableTile", true);
					
			builder.pop();
		    builder.push("tool properties");
		    
		    itemCarpentersToolsDamageable = builder
					.comment("Damage tools when used")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "itemCarpentersToolsDamageable")
					.define("itemCarpentersToolsDamageable", true);
		    
		    itemCarpentersToolsUses = builder
					.comment("Damage (uses) a tool can sustain before breaking")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "itemCarpentersToolsUses")
					.defineInRange("itemCarpentersToolsUses", 400, 1, Integer.MAX_VALUE);
		    
		    itemHammerDamageChanceFromSlopes = builder
					.comment("Chance of damaging the Carpenter's Hammer when interacting with Carpenter's Slopes")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "itemHammerDamageChanceFromSlopes")
					.defineInRange("itemHammerDamageChanceFromSlopes", 0.75D, 0.0D, 1.0D);
		    
		    itemHammerDamageChanceFromStairs = builder
					.comment("Chance of damaging the Carpenter's Hammer when interacting with Carpenter's Stairs")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "itemHammerDamageChanceFromStairs")
					.defineInRange("itemHammerDamageChanceFromStairs", 1.0D, 0.0D, 1.0D);
		    
		    itemHammerDamageChanceFromCollapsible = builder
					.comment("Chance of damaging the Carpenter's Hammer when interacting with Carpenter's Collapsible Blocks")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "itemHammerDamageChanceFromCollapsible")
					.defineInRange("itemHammerDamageChanceFromCollapsible", 0.2D, 0.0D, 1.0D);
		    
			builder.pop();
		    builder.push("features");
		    
		    enableCovers = builder
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableCovers")
					.define("enableCovers", true);
		    
		    enableOverlays = builder
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableOverlays")
					.define("enableOverlays", true);
		    
		    enableSideCovers = builder
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableSideCovers")
					.define("enableSideCovers", true);
		    
		    enableDyeColors = builder
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableDyeColors")
					.define("enableDyeColors", true);
		    
		    enableChiselDesigns = builder
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableChiselDesigns")
					.define("enableChiselDesigns", true);
		    
		    enableFreeStandingLadders = builder
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableFreeStandingLadders")
					.define("enableFreeStandingLadders", true);
		    
		    enableIllumination = builder
		    		.comment("This will enable players to cover blocks with glowstone dust to make them illuminate.")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableIllumination")
					.define("enableIllumination", true);
		    
		    enableOwnership = builder
		    		.comment("This will prevent players besides you and server operators from editing your objects.", "Note: this does not protect objects against destruction (intentional), and may allow activation if appropriate. Also, the Carpenter's Safe is not affected by this.")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableOwnership")
					.define("enableOwnership", true);
		    
		    enableTorchWeatherEffects = builder
		    		.comment("This controls whether torches extinguish themselves when exposed to rain or snow.")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableTorchWeatherEffects")
					.define("enableTorchWeatherEffects", true);
		    
		    enableRailSlopeFill = builder
		    		.comment("This allows Carpenter's Blocks with solid top faces to create slopes above them when a sloping rail is above the block.")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableRailSlopeFill")
					.define("enableRailSlopeFill", true);
		    
		    enableGarageDoorFill = builder
		    		.comment("This allows garage doors to automatically fill in gaps when obstructions beneath them are destroyed.")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableGarageDoorFill")
					.define("enableGarageDoorFill", true);
		    
		    enableSmoothSlopes = builder
					.comment("This smoothes player movement over slopes.")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "enableSmoothSlopes")
					.define("enableSmoothSlopes", false);
		    
		    multiBlockSizeLimit = builder
					.comment("This controls how many blocks can be connected as a single entity.", "Note: this currently only applies to Garage Doors.")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "multiBlockSizeLimit")
					.defineInRange("multiBlockSizeLimit", 500, 0, 2000);
		    
		    overlayItemList = builder
					.comment("This maps items to overlays.", "Items are prefixed with display names (en_US only).", "Overlay suffixes are :grass, :snow, :web, :vine, :hay, :mycelium")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "overlayItemList")
					.define("overlayItemList", Arrays.asList(new String[] { "Seeds:grass", "Snowball:snow", "String:web", "Vines:vine", "Wheat:hay", "Mushroom:mycelium" }));
		    
		    builder.pop();
		    builder.push("compatibility");
		    
		    coverBlockExceptionList = builder
					.comment("This allows restricted blocks to be used as covers.", "Add your own by supplying the block's resource name (minecraft:block/grass_block, for example).")
					.translation(CarpentersBlocks.MOD_ID + ".config." + "coverBlockExceptionList")
					.define("coverBlockExceptionList", Arrays.asList("test"));
		    
		    builder.pop();
		}
		
	}
    
    @EventBusSubscriber(modid = CarpentersBlocks.MOD_ID, bus = Bus.FORGE)
    static class ForgeCommonEventHandler {
    	
    	private static Field itemGroupField;
    	
    	/**
    	 * Remove items from creative menu, and recipes for disabled items and blocks.
    	 * 
    	 * @param event the event
    	 */
    	@SubscribeEvent
    	public static void FMLServerAboutToStartEvent(FMLServerAboutToStartEvent event) {
    		Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes = null;
    		Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> updatedRecipes = new HashMap<>();
    		Field recipesField = null;
    		try {
    			itemGroupField = ObfuscationReflectionHelper.findField(Item.class, "field_77701_a");
    			recipesField = ObfuscationReflectionHelper.findField(RecipeManager.class, "field_199522_d");
    			recipes = (Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>) recipesField.get(event.getServer().getRecipeManager());
    			
    			// create mutable copy of map
    			for (Entry<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> entry : recipes.entrySet()) {
    				updatedRecipes.put(entry.getKey(), new HashMap<>());
    				for (Entry<ResourceLocation, IRecipe<?>> entry2 : entry.getValue().entrySet()) {
    					updatedRecipes.get(entry.getKey()).put(entry2.getKey(), entry2.getValue());
    				}
    			}
    			
    			// remove recipes and item group assignments
    			Map<ResourceLocation, IRecipe<?>> craftingRecipes = updatedRecipes.get(IRecipeType.CRAFTING);
    			if (!enableApplicator) {
    				removeItem(craftingRecipes, CbItems.itemApplicator);
        		}
        		if (!enableBarrier) {
        			removeItem(craftingRecipes, CbItems.blockItemBarrier);
        		}
        		if (!enableBed) {
        			removeItem(craftingRecipes, CbItems.itemBed);
        		}
        		if (!enableBlock) {
        			removeItem(craftingRecipes, CbItems.blockItemBlock);
        		}
        		if (!enableButton) {
        			removeItem(craftingRecipes, CbItems.blockItemButton);
        		}
        		if (!enableChisel) {
        			removeItem(craftingRecipes, CbItems.itemChisel);
        		}
        		if (!enableCollapsibleBlock) {
        			removeItem(craftingRecipes, CbItems.blockItemCollapsibleBlock);
        		}
        		if (!enableDaylightSensor) {
        			removeItem(craftingRecipes, CbItems.blockItemDaylightSensor);
        		}
        		if (!enableDoor) {
        			removeItem(craftingRecipes, CbItems.itemDoor);
        		}
        		if (!enableFlowerPot) {
        			removeItem(craftingRecipes, CbItems.blockItemFlowerPot);
        		}
        		if (!enableGarageDoor) {
        			removeItem(craftingRecipes, CbItems.blockItemGarageDoor);
        		}
        		if (!enableGate) {
        			removeItem(craftingRecipes, CbItems.blockItemGate);
        		}
        		if (!enableHammer) {
        			removeItem(craftingRecipes, CbItems.itemHammer);
        		}
        		if (!enableHatch) {
        			removeItem(craftingRecipes, CbItems.blockItemHatch);
        		}
        		if (!enableLadder) {
        			removeItem(craftingRecipes, CbItems.blockItemLadder);
        		}
        		if (!enableLever) {
        			removeItem(craftingRecipes, CbItems.blockItemLever);
        		}
        		if (!enablePressurePlate) {
        			removeItem(craftingRecipes, CbItems.blockItemPressurePlate);
        		}
        		if (!enableSafe) {
        			removeItem(craftingRecipes, CbItems.blockItemSafe);
        		}
        		if (!enableSlope) {
        			removeItem(craftingRecipes, CbItems.blockItemSlopeWedge);
        		}
        		// always remove slope selection subtypes
    			removeItem(craftingRecipes, CbItems.blockItemSlopeInvertedPrism);
    			removeItem(craftingRecipes, CbItems.blockItemSlopeObliqueExterior);
    			removeItem(craftingRecipes, CbItems.blockItemSlopeObliqueInterior);
    			removeItem(craftingRecipes, CbItems.blockItemSlopePrism);
    			removeItem(craftingRecipes, CbItems.blockItemSlopePrismWedge);
    			removeItem(craftingRecipes, CbItems.blockItemSlopeWedgeExterior);
    			removeItem(craftingRecipes, CbItems.blockItemSlopeWedgeInterior);
        		if (!enableStairs) {
        			removeItem(craftingRecipes, CbItems.blockItemStairs);
        		}
        		if (!enableTile) {
        			removeItem(craftingRecipes, CbItems.itemTile);
        		}
        		if (!enableTorch) {
        			removeItem(craftingRecipes, CbItems.blockItemTorch);
        		}
        		
        		// restore recipe list
        		recipesField.set(event.getServer().getRecipeManager(),
        				updatedRecipes.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, (r) -> ImmutableMap.copyOf(r.getValue()))));
    		} catch (Exception ex) {
    			ModLogger.log(Level.WARN, "Exception encountered while attempting to remove recipes and item group assignments");
    			return;
    		}
    	}
    	
    	/**
    	 * Removes item from map.
    	 * 
    	 * @param recipes the recipe map
    	 * @param item the item
    	 * @throws IllegalAccessException 
    	 * @throws IllegalArgumentException 
    	 */
    	private static void removeItem(Map<ResourceLocation, IRecipe<?>> recipes, Item item) throws IllegalArgumentException, IllegalAccessException {
    		itemGroupField.set(item, null);
			recipes.remove(item.getRegistryName());
			ModLogger.log(Level.INFO, "Removed recipe and item group assignment for {}", item.getRegistryName());
    	}
    	
    }
	
}
