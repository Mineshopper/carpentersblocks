package com.carpentersblocks.nbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.carpentersblocks.nbt.attribute.AbstractAttribute;
import com.carpentersblocks.nbt.attribute.AbstractAttribute.Key;
import com.carpentersblocks.nbt.attribute.AttributeItemStack;
import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.nbt.attribute.EnumAttributeType;
import com.carpentersblocks.util.BlockUtil;

import net.minecraft.block.BlockState;

/**
 * A helper class for accessing and modifying a block's attribute map.
 */
public class AttributeHelper {
	
	private Map<Key, AbstractAttribute<?>> _map;
	
	public AttributeHelper(Map<Key, AbstractAttribute<?>> map) {
		_map = map;
	}
	
	public boolean hasAttribute(Key key) {
		return _map.containsKey(key);
	}
	
	public boolean hasAttribute(EnumAttributeType type) {
		return hasAttribute(EnumAttributeLocation.HOST, type);
	}
	
    public boolean hasAttribute(EnumAttributeLocation location, EnumAttributeType type) {
        return this.hasAttribute(AbstractAttribute.generateKey(location, type));
    }

    public AbstractAttribute<?> getAttribute(Key key) {
    	return _map.get(key);
    }
    
    public AbstractAttribute<?> getLastAddedDroppableAttribute(EnumAttributeLocation location) {
    	AbstractAttribute<?> attribute = null;
    	for (Key key : _map.keySet()) {
    		if (location.equals(key.getLocation()) && _map.get(key).isDroppable()) {
    			attribute = _map.get(key);
    		}
    	}
    	return attribute;
    }
    
    public AbstractAttribute<?> getAttribute(EnumAttributeLocation location, EnumAttributeType type) {
        return this.getAttribute(AbstractAttribute.generateKey(location, type));
    }
    
    public Map<Key, AbstractAttribute<?>> copyMap() {
    	Map<Key, AbstractAttribute<?>> map = new HashMap<Key, AbstractAttribute<?>>();
    	Iterator<Entry<Key, AbstractAttribute<?>>> iter = _map.entrySet().iterator();
	    while (iter.hasNext()) {
	    	Entry<Key, AbstractAttribute<?>> pair = (Entry<Key, AbstractAttribute<?>>) iter.next();
	        map.put((Key) pair.getKey(), ((AbstractAttribute<?>)pair.getValue()).copy());
	    }	    	
    	return map;
    }
    
    public List<BlockState> getAttributeBlockStates() {
    	List<BlockState> list = new ArrayList<BlockState>();
    	Iterator<Entry<Key, AbstractAttribute<?>>> iter = _map.entrySet().iterator();
	    while (iter.hasNext()) {
	    	Entry<Key, AbstractAttribute<?>> pair = (Entry<Key, AbstractAttribute<?>>) iter.next();
	        if (pair.getValue() instanceof AttributeItemStack) {
		        BlockState blockState = BlockUtil.getBlockState(((AttributeItemStack)pair.getValue()).getModel());
		        if (blockState != null) {
		        	list.add(blockState);
		        }
	        }
	    }	    	
    	return list;
    }
    
    ///////////////////////////////////////////////////////////////////////
    // Below methods are only meant to be accessed by CbTileEntity class //
    ///////////////////////////////////////////////////////////////////////
    
    protected AbstractAttribute<?> put(Key key, AbstractAttribute<?> value) {
    	return _map.put(key, value);
    }
    
    protected AbstractAttribute<?> remove(Key key) {
    	return _map.remove(key);
    }
    
    protected void clear() {
    	_map.clear();
    }
    
    protected Iterator<Entry<Key, AbstractAttribute<?>>> iterator() {
    	return _map.entrySet().iterator();
    }
    
}
