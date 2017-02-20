package com.carpentersblocks.util.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.carpentersblocks.util.attribute.AbstractAttribute.Key;
import com.carpentersblocks.util.block.BlockUtil;

import net.minecraft.block.state.IBlockState;

public class AttributeHelper {
	
	private Map<Key, AbstractAttribute> _map;
	
	public AttributeHelper(Map<Key, AbstractAttribute> map) {
		_map = map;
	}
	
	public boolean hasAttribute(Key key) {
		return _map.containsKey(key);
	}
	
    public boolean hasAttribute(EnumAttributeLocation location, EnumAttributeType type) {
        return this.hasAttribute(AbstractAttribute.generateKey(location, type));
    }

    public AbstractAttribute getAttribute(Key key) {
    	return _map.get(key);
    }
    
    public AbstractAttribute getLastAttribute(EnumAttributeLocation location) {
    	AbstractAttribute attribute = null;
    	for (Key key : _map.keySet()) {
    		if (location.equals(key.getLocation())) {
    			attribute = _map.get(key);
    		}
    	}
    	return attribute;
    }
    
    public AbstractAttribute getAttribute(EnumAttributeLocation location, EnumAttributeType type) {
        return this.getAttribute(AbstractAttribute.generateKey(location, type));
    }
    
    public Map<Key, AbstractAttribute> copyMap() {
    	Map<Key, AbstractAttribute> map = new HashMap<Key, AbstractAttribute>();
    	Iterator iter = _map.entrySet().iterator();
	    while (iter.hasNext()) {
	        Map.Entry pair = (Map.Entry) iter.next();
	        map.put((Key) pair.getKey(), ((AbstractAttribute)pair.getValue()).copy());
	    }	    	
    	return map;
    }
    
    public List<IBlockState> getAttributeBlockStates() {
    	List<IBlockState> list = new ArrayList<IBlockState>();
    	Iterator iter = _map.entrySet().iterator();
	    while (iter.hasNext()) {
	        Map.Entry pair = (Map.Entry) iter.next();
	        if (pair.getValue() instanceof AttributeItemStack) {
		        IBlockState blockState = BlockUtil.getBlockState(((AttributeItemStack)pair.getValue()).getModel());
		        if (blockState != null) {
		        	list.add(blockState);
		        }
	        }
	    }	    	
    	return list;
    }
    
}
