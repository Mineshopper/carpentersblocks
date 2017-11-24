package com.carpentersblocks.tileentity;

	import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.carpentersblocks.block.BlockCoverable;
import com.carpentersblocks.util.attribute.AbstractAttribute;
import com.carpentersblocks.util.attribute.AbstractAttribute.Key;
import com.carpentersblocks.util.attribute.AttributeHelper;
import com.carpentersblocks.util.attribute.AttributeItemStack;
import com.carpentersblocks.util.attribute.AttributeString;
import com.carpentersblocks.util.attribute.EnumAttributeLocation;
import com.carpentersblocks.util.attribute.EnumAttributeType;
import com.carpentersblocks.util.block.BlockUtil;
import com.carpentersblocks.util.protection.IProtected;
import com.carpentersblocks.util.protection.ProtectedObject;
import com.carpentersblocks.util.registry.ConfigRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

	public class CbTileEntity extends TileEntity implements IProtected {

		public static final String TAG_ATTR_LIST = "cbAttrList";
	    public static final String TAG_METADATA  = "cbMetadata";
	    public static final String TAG_OWNER     = "cbOwner";
	    
	    // Holds temporary miscellaneous per-block properties
	    private Map<String, Object> _properties;
	    
	    // Map holding all block attributes
	    private Map<AbstractAttribute.Key, AbstractAttribute> _cbAttrMap = new LinkedHashMap<AbstractAttribute.Key, AbstractAttribute>();

	    private AttributeHelper _cbAttrHelper = new AttributeHelper(_cbAttrMap);
	    
	    // Holds specific block information like facing, states, etc
	    private int _cbMetadata;

	    // Owner of tile entity
	    private String _cbOwner = "";

	    // Indicates lighting calculations are underway
	    private static boolean _calcLighting = false;
	    
	    // The most recent light value of block
	    private int _lightValue = -1;

	    /**
	     * Reads data from NBT tag compound.
	     * 
	     * @param nbt the NBT tag compound
	     */
	    @Override
	    public void readFromNBT(NBTTagCompound nbt) {
	        super.readFromNBT(nbt);
	        _cbAttrMap.clear();
            NBTTagList nbtTagList = nbt.getTagList(TAG_ATTR_LIST, 10);
            for (int idx = 0; idx < nbtTagList.tagCount(); ++idx) {
                NBTTagCompound nbt1 = nbtTagList.getCompoundTagAt(idx);
                String identifier = nbt1.getString(AbstractAttribute.TAG_ATTR_IDENT);
                AbstractAttribute attribute = null;
                if (identifier.equalsIgnoreCase(AbstractAttribute.IDENT_ATTR_ITEMSTACK)) {
                	attribute = new AttributeItemStack();
                } else if (identifier.equalsIgnoreCase(AbstractAttribute.IDENT_ATTR_STRING)) {
                	attribute = new AttributeString();
                }
                if (attribute != null) {
                	attribute.readFromNBT(nbt1);
                	_cbAttrMap.put(attribute.getKey(), attribute);
                }
            }
            _cbMetadata = nbt.getInteger(TAG_METADATA);
            _cbOwner = nbt.getString(TAG_OWNER);
	        update(false);
	    }

	    /**
	     * Writes data to NBT tag compound.
	     * 
	     * @param nbt the NBT tag compound
	     */
	    @Override
	    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
	        super.writeToNBT(nbt);
	        NBTTagList nbtTagList = new NBTTagList();
	        Iterator iterator = _cbAttrMap.entrySet().iterator();
	        while (iterator.hasNext()) {
	            Map.Entry<Key, AbstractAttribute> entry = (Entry<Key, AbstractAttribute>) iterator.next();
	            NBTTagCompound nbt1 = new NBTTagCompound();
	            AbstractAttribute attribute = entry.getValue();
	            attribute.writeToNBT(nbt1);
	            nbtTagList.appendTag(nbt1);
	        }        
	        nbt.setTag(TAG_ATTR_LIST, nbtTagList);
	        nbt.setInteger(TAG_METADATA, _cbMetadata);
	        nbt.setString(TAG_OWNER, _cbOwner);	        
	        return nbt;
	    }
	    
	    @Nullable
	    public SPacketUpdateTileEntity getUpdatePacket() {
	        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	    }

	    @Override
	    public NBTTagCompound getUpdateTag() {
	    	return writeToNBT(super.getUpdateTag());
	    }
	    
	    @Override
	    public void onDataPacket(net.minecraft.network.NetworkManager net, SPacketUpdateTileEntity pkt) {
	    	readFromNBT(pkt.getNbtCompound());
	    }

	    /**
	     * Copies owner from CbTileEntity object.
	     */
	    public void copyOwner(final CbTileEntity TE) {
	        _cbOwner = TE.getOwner();
	        markDirty();
	    }

	    /**
	     * Sets owner of tile entity.
	     */
	    @Override
	    public void setOwner(ProtectedObject obj) {
	        _cbOwner = obj.toString();
	        markDirty();
	    }

	    @Override
	    public String getOwner() {
	        return _cbOwner;
	    }
	    
	    public ItemStack getDroppedItemStack(Key key) {
	        AbstractAttribute attribute = _cbAttrHelper.getAttribute(key);
	        if (attribute instanceof AttributeItemStack) {
	        	ItemStack itemStack = (ItemStack) attribute.getModel();
		        if (EnumAttributeType.COVER.equals(key.getType())) {
		        	IBlockState blockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, key.getLocation(), key.getType());
		        	int metadata = blockState.getBlock().getMetaFromState(blockState);
		        	itemStack.setItemDamage(metadata);
		        }
		        return itemStack;
	        }
	        return null;
	    }

	    public boolean addAttribute(EnumAttributeLocation location, EnumAttributeType type, Object model) {
	        if (_cbAttrHelper.hasAttribute(location, type) || model == null) {
	            return false;
	        }
	        if (model instanceof ItemStack) {
		        ItemStack reducedStack = ((ItemStack)model).copy();
		        reducedStack.setCount(1);
		        _cbAttrMap.put(AbstractAttribute.generateKey(location, type), new AttributeItemStack(location, type, reducedStack));
		        World world = getWorld();
		        switch (type) {
			        case COVER:
			        	//world.setBlockState(pos, this.getBlockType().getDefaultState());
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
	        	_cbAttrMap.put(AbstractAttribute.generateKey(location, type), new AttributeString(location, type, (String)model));
	        } else {
	        	return false;
	        }
	        //updateWorldAndLighting(true);
	        return true;
	    }

	    /**
	     * Will remove the attribute from map once block drop is complete.
	     * <p>
	     * Should only be called externally by {@link BlockCoverable#onBlockEventReceived}.
	     *
	     * @param key the attribute key
	     */
	    public boolean onAttrDropped(Key key) {
	    	if (_cbAttrMap.remove(key) == null) {
	    		return false;
	    	}
	        update(true);
	        return true;
	    }
	    
	    /**
	     * Initiates block drop event, which will remove attribute from tile entity.
	     *
	     * @param  attrId the attribute ID
	     */
	    private boolean createBlockDropEvent(AttributeItemStack attribute) {
	        getWorld().addBlockEvent(getPos(), getBlockType(), BlockCoverable.EVENT_ID_DROP_ATTR , attribute.getKey().hashCode());
	        return true;
	    }

	    /**
	     * Removes attribute from block.
	     * 
	     * @param attribute the attribute
	     */
	    public boolean removeAttribute(EnumAttributeLocation location, EnumAttributeType type) {
	    	if (_cbAttrHelper.hasAttribute(location, type)) {
		    	AbstractAttribute attribute = _cbAttrHelper.getAttribute(location, type);
		    	return removeAttribute(attribute);
	    	}
	    	return false;
	    }
	    
	    private boolean removeAttribute(AbstractAttribute attribute) {
	    	return _cbAttrMap.remove(attribute.getKey()) != null;
	    }
	    
	    public boolean removeLastAddedDroppableAttribute(EnumAttributeLocation location) {
	    	AbstractAttribute attribute = _cbAttrHelper.getLastAddedDroppableAttribute(location);
	    	if (attribute instanceof AttributeItemStack) {
	    		return createBlockDropEvent((AttributeItemStack)attribute);
	    	}
	    	return false;
	    }
	    
	    /**
	     * Removes all attributes from block based on attribute location.
	     * 
	     * @param location the attribute location
	     */
	    public void removeAttributes(EnumAttributeLocation location) {
	    	Iterator it = _cbAttrMap.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry pair = (Map.Entry)it.next();
	            AbstractAttribute attribute = (AbstractAttribute) pair.getValue();
	            if (attribute.getLocation().equals(location)) {
	            	removeAttribute(attribute);
	            }
	        }
	    }

	    /**
	     * Gets block-specific data.
	     *
	     * @return the data
	     */
	    public int getData() {
	        return _cbMetadata;
	    }

	    /**
	     * Sets block-specific data.
	     * 
	     * @param data the block data
	     * @return <code>true</code> if data was updated, or <code>false</code> if no change
	     */
	    public boolean setData(int data) {
	    	if (!getWorld().isRemote) {
		    	if (data == getData()) {
		    		return false;
		    	}
	            _cbMetadata = data;
	            this.update(true);
	    	}
            return true;
	    }
	    
	    /**
	     * Gets all attributes of type ItemStack.
	     * 
	     * @return a list of ItemStacks
	     */
	    public List<ItemStack> getAllDroppableAttributes() {
	    	List<ItemStack> list = new ArrayList<ItemStack>();
	    	Iterator it = _cbAttrMap.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry pair = (Map.Entry)it.next();
	            if (pair.getValue() instanceof AttributeItemStack) {
	            	list.add(((AttributeItemStack)pair.getValue()).getModel());
	            }
	        }
	    	return list;
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

	    /**
	     * Called from Chunk.setBlockIDWithMetadata and Chunk.fillChunk, determines if this tile entity should be re-created when the ID, or Metadata changes.
	     * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
	     * <p>
	     * Super implementation treats entities as different if before
	     * and after blocks are different.  This is the desired effect in
	     * the context of this mod.
	     *
	     * @param world Current world
	     * @param pos Tile's world position
	     * @param oldState The old ID of the block
	     * @param newState The new ID of the block (May be the same)
	     * @return true forcing the invalidation of the existing TE, false not to invalidate the existing TE
	     */
	    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
	    	return oldState.getBlock() != newState.getBlock();
	    }
	    
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
	        if (_lightValue == -1 && !_calcLighting) {
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
	    	if (ConfigRegistry.enableIllumination && _cbAttrHelper.hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.ILLUMINATOR)) {
	    		return 15;
	    	} else {
	    		_calcLighting = true;
	    		for (IBlockState blockState : _cbAttrHelper.getAttributeBlockStates()) {
    				// Determine state-sensitive light value (usually recursive, and not useful)
    				int posLightValue = blockState.getBlock().getLightValue(blockState, getWorld(), getPos());
    				if (posLightValue > 0) {
    					value = Math.max(value, posLightValue);
    				} else {
    					value = Math.max(value, blockState.getBlock().getLightValue(blockState));
    				}
	    		}
	    		_calcLighting = false;
	    	}
	    	return value;
	    }
	    
	    /**
	     * Updates light value and world lightmap.
	     */
	    private void updateCachedLighting() {
	        _lightValue = getDynamicLightValue();
	        getWorld().checkLightFor(EnumSkyBlock.BLOCK, pos);
	    }
	    
	    /**
	     * Performs world update and refreshes lighting.
	     */
	    public void update(boolean markDirty) {
	        World world = getWorld();
	        if (world != null) {
	            IBlockState blockState = world.getBlockState(this.pos);
	            world.notifyBlockUpdate(pos, blockState, blockState, 4);
	            Block block = this.getBlockType();
	            if (block.canProvidePower(blockState)) {
	            	world.notifyNeighborsOfStateChange(pos, blockType, false);
	            }
	            updateCachedLighting();
	            if (markDirty) {
	            	this.markDirty();
	            }
	        }
	    }
	    
	}
