package com.carpentersblocks.util;

import org.apache.logging.log4j.Level;

import com.carpentersblocks.ModLogger;
import com.carpentersblocks.api.IWrappableBlock;
import com.carpentersblocks.block.AbstractCoverableBlock;
import com.carpentersblocks.config.Configuration;
import com.carpentersblocks.nbt.AttributeHelper;
import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.nbt.attribute.AbstractAttribute;
import com.carpentersblocks.nbt.attribute.AttributeItemStack;
import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.nbt.attribute.EnumAttributeType;
import com.carpentersblocks.util.handler.DyeHandler;
import com.carpentersblocks.util.handler.OverlayHandler;
import com.carpentersblocks.util.handler.OverlayHandler.Overlay;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class BlockUtil {

    /**
     * Takes an ItemStack and returns block, or air block if ItemStack
     * does not contain a block.
     */
    public static Block toBlock(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof BlockItem) {
            return Block.byItem(itemStack.getItem());
        } else {
            return Blocks.AIR;
        }
    }
    
    /**
     * Returns {@link CbTileEntity} if one exists and the block at coordinates
     * matches passed in {@link Block}.
     *
     * @param block the {@link Block} to match against
     * @param world the {@link World}
     * @param blockPos the block position
     * @return a {@link CbTileEntity}
     */
    public static CbTileEntity getTileEntity(Block block, World world, BlockPos blockPos) {
        TileEntity tileEntity = world.getBlockEntity(blockPos);
        if (tileEntity != null && tileEntity instanceof CbTileEntity && world.getBlockState(blockPos).getBlock().equals(block)) {
            return (CbTileEntity) tileEntity;
        }
        return null;
    }
    
    public static BlockState getAttributeBlockState(CbTileEntity cbTileEntity, AbstractAttribute<?> attribute) {
    	return getAttributeBlockState(cbTileEntity, attribute.getLocation(), attribute.getType());
    }
    
    public static BlockState getAttributeBlockState(CbTileEntity cbTileEntity, EnumAttributeLocation location, EnumAttributeType type) {
    	return getAttributeBlockState(cbTileEntity.getAttributeHelper(), location, type);
    }
    
    public static BlockState getAttributeBlockState(AttributeHelper attributeHelper, EnumAttributeLocation location, EnumAttributeType type) {
    	if (attributeHelper.hasAttribute(location, type)) {
    		AbstractAttribute<?> attribute = attributeHelper.getAttribute(location, type);
    		if (attribute instanceof AttributeItemStack) {
	    		ItemStack itemStack = ((AttributeItemStack)attribute).getModel();
	    		return getBlockState(itemStack);
    		}
    	}
    	return null;
    }
    
    public static BlockState getBlockState(ItemStack itemStack) {
		return ((BlockItem)itemStack.getItem()).getBlock().defaultBlockState();
    }
    
    /**
     * Filters the {@link ItemStack} to a form that is safe for standard
     * block calls.  This is necessary for ItemStacks that contain {@link NBTTagCompounds}
     * or otherwise produce a block that has a {@link TileEntity}.
     *
     * @param itemStack the {@link ItemStack}
     * @return an {@link ItemStack} that is safe from throwing casting crashes during {@link Block} calls
     */
    public static ItemStack getCallableItemStack(ItemStack itemStack) {
    	BlockState blockState = getBlockState(itemStack);
        if (blockState.getBlock() instanceof AbstractCoverableBlock || blockState.getBlock() instanceof IWrappableBlock) {
            return itemStack;
        } else {
            return blockState.getBlock().hasTileEntity(blockState) ? new ItemStack(Blocks.OAK_PLANKS) : itemStack;
        }
    }
    
    /**
     * Returns cover {@link ItemStack}, or instance of {@link AbstractCoverableBlock}
     * if no cover exists on side.
     *
     * @param cbTileEntity the Carpenter's Blocks tile entity
     * @param location the attribute location
     * @return an {@link ItemStack}
     */
    public static ItemStack getCover(CbTileEntity cbTileEntity, EnumAttributeLocation location) {
        return getCover(cbTileEntity.getAttributeHelper(), cbTileEntity.getBlockState(), location);
    }
    
    public static ItemStack getCover(AttributeHelper attributeHelper, BlockState blockState, EnumAttributeLocation location) {
    	ItemStack itemStack = getCoverSafe(attributeHelper, blockState, location);
    	return getCallableItemStack(itemStack);
    }
    
    /**
     * Returns the cover, or if no cover exists, will return the calling block type.
     *
     * @param cbTileEntity the Carpenter's Blocks tile entity
     * @param location the attribute location
     * @return the {@link ItemStack}
     */
    public static ItemStack getCoverSafe(CbTileEntity cbTileEntity, EnumAttributeLocation location) {
        return getCoverSafe(cbTileEntity.getAttributeHelper(), cbTileEntity.getBlockState(), location);
    }
    
    public static ItemStack getCoverSafe(AttributeHelper attributeHelper, BlockState blockState, EnumAttributeLocation location) {
        ItemStack itemStack = new ItemStack(blockState.getBlock());
    	if (attributeHelper.hasAttribute(location, EnumAttributeType.COVER)) {
        	itemStack = (ItemStack) attributeHelper.getAttribute(location, EnumAttributeType.COVER).getModel();
        }
        return itemStack;
    }
    
    public static EnumAttributeLocation getAttributeLocationForFacing(CbTileEntity cbTileEntity, Direction facing) {
    	return cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.valueOf(facing.ordinal()), EnumAttributeType.COVER) ? EnumAttributeLocation.valueOf(facing.ordinal()) : EnumAttributeLocation.HOST;
    }
    
    /**
     * Gets an {@link ItemStack} that best represents the surface
     * side of a Carpenter's Block.
     * <p>
     * The side covers and any overlays are taken into consideration.
     *
     * @param cbTileEntity the Carpenter's Blocks tile entity
     * @param facing the facing
     * @return
     */
    public static ItemStack getFeatureSensitiveSideItemStack(CbTileEntity cbTileEntity, Direction facing) {
        ItemStack itemStack = null;
        EnumAttributeLocation location = cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.valueOf(facing.ordinal()), EnumAttributeType.COVER) ? EnumAttributeLocation.valueOf(facing.ordinal()) : EnumAttributeLocation.HOST;
        
        // Check for overlay
        if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.valueOf(facing.ordinal()), EnumAttributeType.OVERLAY)) {
            Overlay overlay = OverlayHandler.getOverlayType(((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(location, EnumAttributeType.OVERLAY)).getModel());
            if (OverlayHandler.coversFullSide(overlay, facing)) {
                itemStack = overlay.getItemStack();
            }
        }
        
        // Check for side cover
        if (itemStack == null) {
            itemStack = getCover(cbTileEntity, location);
        }

        return itemStack;
    }
    
    public static TextureAtlasSprite getParticleTexture(ItemStack itemStack) {
    	BlockState blockState = getBlockState(itemStack);
    	IBakedModel itemModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState);
    	return itemModel.getParticleIcon();
    }
    
    /**
     * Returns whether block is a cover.
     */
    public static boolean isCover(ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem && !isOverlay(itemStack)) {
            Block block = toBlock(itemStack);
            BlockState blockState = block.defaultBlockState();
            return !(block instanceof AbstractCoverableBlock) && (
                   blockState.canOcclude() ||
                   block instanceof SlabBlock ||
                   block instanceof PaneBlock ||
                   block instanceof BreakableBlock ||
                   Configuration.getCoverBlockExceptions().contains(itemStack.getItem().getRegistryName().toString()));
        }
        return false;
    }
    
    /**
     * Returns whether ItemStack contains glowstone dust.
     *
     * @return <code>true</code> if {@link ItemStack} contains glowstone dust
     */
    public static boolean isIlluminator(ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
        	return Items.GLOWSTONE_DUST.equals(itemStack.getItem());
        }
        return false;
    }

    /**
     * Returns true if ItemStack is a dye.
     */
    public static boolean isDye(ItemStack itemStack, boolean allowWhite) {
        return !itemStack.isEmpty() &&
               DyeHandler.isDye(itemStack, allowWhite);
    }

    /**
     * Returns whether ItemStack contains a valid overlay item or block.
     */
    public static boolean isOverlay(ItemStack itemStack) {
        return OverlayHandler.overlayMap.containsKey(itemStack.getItem().getRegistryName().toString());
    }
 
    public static boolean isValidTileEntity(IWorld world, BlockPos blockPos) {
		TileEntity tileEntity = world.getBlockEntity(blockPos);
    	return tileEntity != null && tileEntity instanceof CbTileEntity;
	}

	public static boolean isAreaLoaded(ServerWorld serverWorld, BlockPos blockPos) {
		if (!serverWorld.isAreaLoaded(blockPos, 0)) {
    		ModLogger.log(Level.WARN, "Server ignoring block interact packet for unloaded block position {}", blockPos.toShortString());
    		return false;
    	}
		return true;
	}
    
}