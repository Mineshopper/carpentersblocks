package com.carpentersblocks.util.block;

import com.carpentersblocks.api.IWrappableBlock;
import com.carpentersblocks.block.BlockCoverable;
import com.carpentersblocks.block.state.Property;
import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.attribute.AbstractAttribute;
import com.carpentersblocks.util.attribute.AttributeHelper;
import com.carpentersblocks.util.attribute.AttributeItemStack;
import com.carpentersblocks.util.attribute.EnumAttributeLocation;
import com.carpentersblocks.util.attribute.EnumAttributeType;
import com.carpentersblocks.util.handler.ChatHandler;
import com.carpentersblocks.util.handler.DyeHandler;
import com.carpentersblocks.util.handler.OverlayHandler;
import com.carpentersblocks.util.handler.OverlayHandler.Overlay;
import com.carpentersblocks.util.registry.ConfigRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.oredict.OreDictionary;

public class BlockUtil {

    /**
     * Takes an ItemStack and returns block, or air block if ItemStack
     * does not contain a block.
     */
    public static Block toBlock(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            return Block.getBlockFromItem(itemStack.getItem());
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
    public static CbTileEntity getTileEntity(Block block, World world, BlockPos blockPos)
    {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity != null && tileEntity instanceof CbTileEntity && world.getBlockState(blockPos).getBlock().equals(block)) {
            return (CbTileEntity) tileEntity;
        }

        return null;
    }
    
    public static boolean validateBlockState(IBlockState blockState) {
    	return  blockState != null &&
    			blockState instanceof IExtendedBlockState &&
    			((IExtendedBlockState)blockState).getUnlistedProperties().containsKey(Property.CB_METADATA) &&
    			((IExtendedBlockState)blockState).getValue(Property.CB_METADATA) != null;
    }
    
    public static IBlockState getAttributeBlockState(AttributeHelper helper, AbstractAttribute attribute) {
    	return getAttributeBlockState(helper, attribute.getLocation(), attribute.getType());
    }
    
    public static IBlockState getAttributeBlockState(AttributeHelper helper, EnumAttributeLocation location, EnumAttributeType type) {
    	if (helper.hasAttribute(location, type)) {
    		AbstractAttribute attribute = helper.getAttribute(location, type);
    		if (attribute instanceof AttributeItemStack) {
	    		ItemStack itemStack = ((AttributeItemStack)attribute).getModel();
	    		return getBlockState(itemStack);
    		}
    	}
    	return null;
    }
    
    public static IBlockState getBlockState(ItemStack itemStack) {
		Block block = Block.getBlockFromItem(itemStack.getItem());
		return block != null ? block.getStateFromMeta(itemStack.getMetadata()) : null;
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
    	IBlockState blockState = getBlockState(itemStack);
        if (blockState.getBlock() instanceof BlockCoverable || blockState.getBlock() instanceof IWrappableBlock) {
            return itemStack;
        } else {
            return blockState.getBlock().hasTileEntity(blockState) ? new ItemStack(Blocks.PLANKS) : itemStack;
        }
    }
    
    /**
     * Returns cover {@link ItemStack}, or instance of {@link BlockCoverable}
     * if no cover exists on side.
     *
     * @param cbTileEntity the Carpenter's Blocks tile entity
     * @param location the attribute location
     * @return an {@link ItemStack}
     */
    public static ItemStack getCover(CbTileEntity cbTileEntity, EnumAttributeLocation location)
    {
        ItemStack itemStack = getCoverSafe(cbTileEntity, location);
        return getCallableItemStack(itemStack);
    }
    
    /**
     * Returns the cover, or if no cover exists, will return the calling block type.
     *
     * @param cbTileEntity the Carpenter's Blocks tile entity
     * @param location the attribute location
     * @return the {@link ItemStack}
     */
    public static ItemStack getCoverSafe(CbTileEntity cbTileEntity, EnumAttributeLocation location)
    {
        ItemStack itemStack = ((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(location, EnumAttributeType.COVER)).getModel();
        return itemStack != null ? itemStack : new ItemStack(cbTileEntity.getBlockType());
    }
    
    public static EnumAttributeLocation getAttributeLocationForFacing(CbTileEntity cbTileEntity, EnumFacing facing) {
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
    public static ItemStack getFeatureSensitiveSideItemStack(CbTileEntity cbTileEntity, EnumFacing facing) {
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
    	IBakedModel itemModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemStack);
    	return itemModel.getParticleTexture();
    }
    
    /**
     * Returns whether block is a cover.
     */
    public static boolean isCover(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemBlock && !isOverlay(itemStack)) {
            Block block = toBlock(itemStack);
            IBlockState blockState = block.getStateFromMeta(itemStack.getItemDamage());
            return block.isFullCube(blockState) ||
                   block instanceof BlockSlab ||
                   block instanceof BlockPane ||
                   block instanceof BlockBreakable ||
                   ConfigRegistry.coverExceptions.contains(itemStack.getDisplayName()) ||
                   ConfigRegistry.coverExceptions.contains(ChatHandler.getDefaultTranslation(itemStack));
        }
        return false;
    }
    
    /**
     * Checks {@link OreDictionary} to determine if {@link ItemStack} contains
     * a dustGlowstone ore name.
     *
     * @return <code>true</code> if {@link ItemStack} contains dustGlowstone ore name
     */
    public static boolean isIlluminator(ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            for (int Id : OreDictionary.getOreIDs(itemStack)) {
                if (OreDictionary.getOreName(Id).equals("dustGlowstone")) {
                    return true;
                }
            }
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
        return OverlayHandler.overlayMap.containsKey(itemStack.getDisplayName()) ||
               OverlayHandler.overlayMap.containsKey(ChatHandler.getDefaultTranslation(itemStack));
    }
    
    /**
     * Gets the first matching ore dictionary entry from the provided ore names.
     *
     * @param  itemStack the {@link ItemStack}
     * @param  name the OreDictionary name to check against
     * @return the first matching OreDictionary name, otherwise blank string
     */
    public static String getOreDictMatch(ItemStack itemStack, String ... name) {
        if (!itemStack.isEmpty()) {
            for (int Id : OreDictionary.getOreIDs(itemStack)) {
                for (String oreName : name) {
                    if (OreDictionary.getOreName(Id).equals(oreName)) {
                        return oreName;
                    }
                }
            }
        }
        return "";
    }
    
}