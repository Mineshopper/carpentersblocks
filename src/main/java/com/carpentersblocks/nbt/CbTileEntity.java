package com.carpentersblocks.nbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.carpentersblocks.CommonEventSubscriber;
import com.carpentersblocks.block.AbstractCoverableBlock;
import com.carpentersblocks.config.Configuration;
import com.carpentersblocks.nbt.attribute.AbstractAttribute;
import com.carpentersblocks.nbt.attribute.AbstractAttribute.Key;
import com.carpentersblocks.nbt.attribute.AttributeItemStack;
import com.carpentersblocks.nbt.attribute.AttributeString;
import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.nbt.attribute.EnumAttributeType;
import com.carpentersblocks.util.protection.IProtected;
import com.carpentersblocks.util.protection.ProtectedObject;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.extensions.IForgeTileEntity;

public class CbTileEntity extends TileEntity implements IForgeTileEntity, IProtected {
		
	public static final String TAG_ATTR_LIST = "cbAttrList";
    public static final String TAG_METADATA  = "cbMetadata";
    public static final String TAG_OWNER     = "cbOwner";
    
    /** For passing block properties to renderer */
    public static final ModelProperty<Map<Key, AbstractAttribute<?>>> MODEL_ATTRIBUTES = new ModelProperty<>();
    
    public static final ModelProperty<Integer> MODEL_METADATA = new ModelProperty<>();
    
    public static final ModelProperty<BlockPos> MODEL_BLOCK_POS = new ModelProperty<>();
    
    /** Holds runtime miscellaneous per-block properties */
    private transient Map<String, Object> _properties;
    
    /** Flag indicating lighting calculations are underway */
    private static boolean _flagLightingCalcs = false;
    
    /** The most recent cached light value of block */
    private int _lightValue = -1;
    
    /** Holds specific block information like facing, states, etc */
    private int _cbMetadata;

    /** Owner of tile entity */
    private String _cbOwner = "";
    
    /** Holds last stored block state */
    private BlockState tempBlockState;
    
    /** Accessor for block attributes */
    private AttributeHelper _cbAttrHelper = new AttributeHelper(new LinkedHashMap<AbstractAttribute.Key, AbstractAttribute<?>>());
    
    public CbTileEntity() {
		super(CommonEventSubscriber.tileEntityType);
	}
    
    /**
     * Loads data from compound NBT.
     * 
     * @param nbt the compound NBT
     */
    @Override
    public void load(BlockState blockState, CompoundNBT nbt) {
        super.load(blockState, nbt);
        _cbAttrHelper.clear();
        ListNBT nbtTagList = nbt.getList(TAG_ATTR_LIST, 10);
        for (int idx = 0; idx < nbtTagList.size(); ++idx) {
            CompoundNBT nbt1 = nbtTagList.getCompound(idx);
            String identifier = nbt1.getString(AbstractAttribute.TAG_ATTR_IDENT);
            AbstractAttribute<?> attribute = null;
            if (identifier.equalsIgnoreCase(AbstractAttribute.IDENT_ATTR_ITEMSTACK)) {
            	attribute = new AttributeItemStack();
            } else if (identifier.equalsIgnoreCase(AbstractAttribute.IDENT_ATTR_STRING)) {
            	attribute = new AttributeString();
            }
            if (attribute != null) {
            	attribute.readFromNBT(nbt1);
            	_cbAttrHelper.put(attribute.getKey(), attribute);
            }
        }
        _cbMetadata = nbt.getInt(TAG_METADATA);
        _cbOwner = nbt.getString(TAG_OWNER);
        if (getLevel() != null) {
        	ModelDataManager.requestModelDataRefresh(this);
        }
        update(false);
    }

    /**
     * Saves data to compound NBT.
     * 
     * @param nbt the compound NBT
     */
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        ListNBT nbtTagList = new ListNBT();
        Iterator<Entry<Key, AbstractAttribute<?>>> iterator = _cbAttrHelper.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Key, AbstractAttribute<?>> entry = (Entry<Key, AbstractAttribute<?>>) iterator.next();
            CompoundNBT nbt1 = new CompoundNBT();
            AbstractAttribute<?> attribute = entry.getValue();
            attribute.writeToNBT(nbt1);
            nbtTagList.add(nbt1);
        }        
        nbt.put(TAG_ATTR_LIST, nbtTagList);
        nbt.putInt(TAG_METADATA, _cbMetadata);
        nbt.putString(TAG_OWNER, _cbOwner);	        
        return nbt;
    }
    
    
    
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), -1, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
    	return save(new CompoundNBT());
    }
    
    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, SUpdateTileEntityPacket pkt) {
    	load(getBlockState(), pkt.getTag());
    }
    
    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT nbt) {
    	load(blockState, nbt);
    }

    /**
     * Copies owner from CbTileEntity object.
     */
    public void copyOwner(CbTileEntity cbTileEntity) {
    	_cbOwner = cbTileEntity.getOwner();
        setChanged();
    }

    /**
     * Sets owner of tile entity.
     */
    @Override
    public void setOwner(ProtectedObject obj) {
        _cbOwner = obj.toString();
        setChanged();
    }

    @Override
    public String getOwner() {
        return _cbOwner;
    }
    
    public boolean addAttribute(EnumAttributeLocation location, EnumAttributeType type, Object model) {
        if (_cbAttrHelper.hasAttribute(location, type) || model == null) {
            return false;
        }
        if (model instanceof ItemStack) {
	        ItemStack reducedStack = ((ItemStack)model).copy();
	        reducedStack.setCount(1);
	        _cbAttrHelper.put(AbstractAttribute.generateKey(location, type), new AttributeItemStack(location, type, reducedStack));
	        //World world = getWorld();
	        switch (type) {
		        case COVER:
		        	//world.setBlockState(pos, this.getBlockState().getBlock().getDefaultState());
		        case ILLUMINATOR:
		        case DYE:
		        case PLANT:
		        case SOIL:
		        	//world.markBlockRangeForRenderUpdate(pos.add(-1, -1, -1), pos.add(1, 1, 1));
		        	break;
		        case FERTILIZER:
		        	// getWorld().playAuxSFX(2005, xCoord, yCoord, zCoord, 0);
		        	break;
	        	default: {}
	        }
        } else if (model instanceof String) {
        	_cbAttrHelper.put(AbstractAttribute.generateKey(location, type), new AttributeString(location, type, (String)model));
        } else {
        	return false;
        }
        //updateWorldAndLighting(true);
        return true;
    }
    
    /**
     * Gets block-specific data.
     *
     * @return the data
     */
    public int getCbMetadata() {
        return _cbMetadata;
    }

    /**
     * Sets block-specific data.
     * 
     * @param cbMetadata the block data
     * @return <code>true</code> if data was updated, or <code>false</code> if no change
     */
    public boolean setCbMetadata(int cbMetadata) {
    	if (!getLevel().isClientSide()) {
	    	if (cbMetadata == getCbMetadata()) {
	    		return false;
	    	}
            _cbMetadata = cbMetadata;
            this.update(true);
    	}
        return true;
    }
    
    /**
     * Sets block state without causing a render update.
     * <p>
     * As part of mimicking a cover block, the block state must
     * be changed to better represent the cover properties.
     */
    public void setBlockState(BlockState blockState) {
        tempBlockState = getLevel().getBlockState(getBlockPos());
        getLevel().setBlock(getBlockPos(), blockState, 4);
    }

    /**
     * Restores default block state from host cover.
     */
    public void restoreBlockState() {
    	getLevel().setBlock(getBlockPos(), this.tempBlockState, 4);
    }
    
    /**
     * Retrieves attribute helper for use in handling block
     * attributes.
     * 
     * @return the attribute helper
     */
    public AttributeHelper getAttributeHelper() {
    	return _cbAttrHelper;
    }
    
    //////////////////////////////
    // Properties section below //
    //////////////////////////////
    
    public Object getProperty(String key) {
    	if (_properties == null) {
    		return null;
    	}
    	return _properties.get(key);
    }
    
    public void setProperty(String key, Object value) {
    	if (_properties == null) {
    		_properties = new HashMap<String, Object>();
    	}
    	_properties.put(key, value);
    }
    
    public void removeProperty(String key) {
    	if (_properties != null) {
    		_properties.remove(key);
    	}
    }
    
    ////////////////////////////////////
    // Code below handles block drops //
    ////////////////////////////////////
    
    /**
     * Gets all attributes of type ItemStack.
     * 
     * @return a list of ItemStacks
     */
    public List<ItemStack> getAllDroppableAttributes() {
    	List<ItemStack> list = new ArrayList<ItemStack>();
    	Iterator<Entry<Key, AbstractAttribute<?>>> it = _cbAttrHelper.iterator();
        while (it.hasNext()) {
            Map.Entry<Key, AbstractAttribute<?>> pair = (Entry<Key, AbstractAttribute<?>>) it.next();
            if (pair.getValue() instanceof AttributeItemStack) {
            	list.add(((AttributeItemStack)pair.getValue()).getModel());
            }
        }
    	return list;
    }
    
    /**
     * &lt;Step 3&gt; in block drop process
     * <p>
     * Removes attribute from map prior to block drop.
     * <p>
     * Should only be called externally by {@link AbstractCoverableBlock#eventReceived}.
     *
     * @param param the attribute key
     */
    public boolean eventReceived(Key key) {
    	if (_cbAttrHelper.remove(key) == null) {
    		return false;
    	}
        update(true);
        return true;
    }
    
    public ItemStack getDroppableItemStack(Key key) {
        AbstractAttribute<?> attribute = _cbAttrHelper.getAttribute(key);
        if (attribute instanceof AttributeItemStack) {
	        return (ItemStack) attribute.getModel();
        }
        return null;
    }
    
    /**
     * Removes last added droppable attribute for location.
     * 
     * @param location the location
     * @return
     */
    public boolean removeLastAddedDroppableAttribute(EnumAttributeLocation location) {
    	AbstractAttribute<?> attribute = _cbAttrHelper.getLastAddedDroppableAttribute(location);
    	if (attribute instanceof AttributeItemStack) {
    		createBlockDropEvent((AttributeItemStack)attribute);
    		return true;
    	}
    	return false;
    }
    
    /**
     * Removes attribute from host location.
     * 
     * @param attribute the attribute
     */
    public boolean removeAttribute(EnumAttributeType type) {
    	return this.removeAttribute(EnumAttributeLocation.HOST, type);
    }
    
    /**
     * Removes attribute from block.
     * 
     * @param attribute the attribute
     */
    public boolean removeAttribute(EnumAttributeLocation location, EnumAttributeType type) {
    	if (_cbAttrHelper.hasAttribute(location, type)) {
    		AbstractAttribute<?> attribute = _cbAttrHelper.getAttribute(location, type);
	    	if (attribute instanceof AttributeItemStack) {
	    		createBlockDropEvent((AttributeItemStack)attribute);
	    		return true;
	    	} else {
	    		return _cbAttrHelper.remove(attribute.getKey()) != null;
	    	}
    	}
    	return false;
    }
    
    /**
     * Removes all attributes from block for attribute location.
     * 
     * @param location the attribute location
     */
    public void removeAttributes(EnumAttributeLocation location) {
    	Iterator<Entry<Key, AbstractAttribute<?>>> it = _cbAttrHelper.iterator();
        while (it.hasNext()) {
            Map.Entry<Key, AbstractAttribute<?>> pair = (Map.Entry<Key, AbstractAttribute<?>>)it.next();
            AbstractAttribute<?> attribute = (AbstractAttribute<?>) pair.getValue();
            if (attribute.getLocation().equals(location)) {
    	    	if (attribute instanceof AttributeItemStack) {
    	    		createBlockDropEvent((AttributeItemStack)attribute);
    	    	}
            	_cbAttrHelper.remove(attribute.getKey());
            }
        }
    }
    
    /**
     * &lt;Step 1&gt; in block drop process
     * <p>
     * Initiates block drop event.
     *
     * @param  attribute the attribute
     */
    private void createBlockDropEvent(AttributeItemStack attribute) {
        getLevel().blockEvent(getBlockPos(), getBlockState().getBlock(), AbstractCoverableBlock.EVENT_ID_DROP_ATTR , attribute.getKey().hashCode());
    }

    ///////////////////////////////////////////////////////
    // Code below implemented strictly for light updates //
    ///////////////////////////////////////////////////////

    /**
     * Grabs light value from cache.
     * <p>
     * If not cached, will calculate new cached value.
     * 
     * @return the light value
     */
    public int getLightValue() {
        if (_lightValue == -1 && !_flagLightingCalcs) {
            updateCachedLighting();
        }
        return _lightValue;
    }
    
    /**
     * Returns the dynamic block light value from block attributes.
     * 
     * @return a value from 0 to 15
     */
    protected int getDynamicLightValue() {
    	int value = 0;
    	if (Configuration.isIlluminationEnabled() && _cbAttrHelper.hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.ILLUMINATOR)) {
    		return 15;
    	} else {
    		_flagLightingCalcs = true;
    		for (BlockState blockState : _cbAttrHelper.getAttributeBlockStates()) {
				// Determine state-sensitive light value (usually recursive, and not useful)
				int posLightValue = blockState.getBlock().getLightValue(blockState, getLevel(), getBlockPos());
				if (posLightValue > 0) {
					value = Math.max(value, posLightValue);
				} else {
					value = Math.max(value, blockState.getBlock().getLightValue(blockState, getLevel(), getBlockPos()));
				}
    		}
    		_flagLightingCalcs = false;
    	}
    	return value;
    }
    
    /**
     * Updates light value and world lightmap.
     */
    private void updateCachedLighting() {
        _lightValue = getDynamicLightValue();
        getLevel().getLightEmission(getBlockPos());
    }
    
    /**
     * Performs world update and refreshes lighting.
     */
    public void update(boolean isChanged) {
        World world = getLevel();
        if (world != null) {
            BlockState blockState = world.getBlockState(getBlockPos());
            world.sendBlockUpdated(getBlockPos(), blockState, blockState, 4);
            if (blockState.hasAnalogOutputSignal()) {
            	world.updateNeighborsAt(getBlockPos(), blockState.getBlock());
            }
            updateCachedLighting();
            if (isChanged) {
            	setChanged();
            }
        }
    }
    
    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(MODEL_ATTRIBUTES, _cbAttrHelper.copyMap())
                .withInitial(MODEL_METADATA, _cbMetadata)
                .withInitial(MODEL_BLOCK_POS, getBlockPos())
                .build();
    }
    
}
