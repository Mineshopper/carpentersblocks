package com.carpentersblocks.util.attribute;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Stores attribute and unique identifier for validation purposes.
 * @param <E>
 */
public abstract class AbstractAttribute<K> {

    public static final String TAG_ATTR_LOCATION = "cbAttrLocation";
    public static final String TAG_ATTR_TYPE     = "cbAttrType";
    public static final String TAG_ATTR_IDENT    = "cbAttrModel";
    public static final String IDENT_ATTR_ITEMSTACK   = "ItemStack";
    public static final String IDENT_ATTR_STRING      = "String";
    protected EnumAttributeType _type;
    protected EnumAttributeLocation _location;
    protected K _model;
    
    public AbstractAttribute(EnumAttributeLocation location, EnumAttributeType type, K model) {
    	_location = location;
    	_type = type;
    	_model = model;
    }
    
    public void setModel(K model) {
    	_model = model;
    }
    
    public K getModel() {
    	return _model;
    }
    
    public EnumAttributeType getType() {
    	return _type;
    }
    
    public EnumAttributeLocation getLocation() {
    	return _location;
    }

    public void writeToNBT(NBTTagCompound nbt) {
    	nbt.setInteger(TAG_ATTR_LOCATION, _location.ordinal());
    	nbt.setInteger(TAG_ATTR_TYPE, _type.ordinal());
    	nbt.setString(TAG_ATTR_IDENT, getModelIdentifier());
    	writeModelToNBT(nbt);
    }
    
    public void readFromNBT(NBTTagCompound nbt) {
    	_location = EnumAttributeLocation.valueOf(nbt.getInteger(TAG_ATTR_LOCATION) & 255);
    	_type = EnumAttributeType.valueOf(nbt.getInteger(TAG_ATTR_TYPE) & 255);
    	readModelFromNBT(nbt);
    }
    
    public abstract String getModelIdentifier();
    
    public abstract void writeModelToNBT(NBTTagCompound nbt);
    
    public abstract void readModelFromNBT(NBTTagCompound nbt);

    public static Key generateKey(EnumAttributeLocation location, EnumAttributeType type) {
    	return new Key(location, type);
    }
    
    public Key getKey() {
    	return new Key(_location, _type);
    }

    public static class Key {
    
    	private EnumAttributeLocation _location;
    	private EnumAttributeType _type;
    	
    	public Key(int hashCode) {
    		this(EnumAttributeLocation.valueOf(hashCode >>> 16), EnumAttributeType.valueOf(hashCode & 0xffff));
    	}
    	
    	public Key(EnumAttributeLocation _location, EnumAttributeType _type) {
    		this._location = _location;
    		this._type = _type;
    	}
    	
    	public EnumAttributeLocation getLocation() {
    		return _location;
    	}
    	
    	public EnumAttributeType getType() {
    		return _type;
    	}
    	
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (!(obj instanceof AbstractAttribute.Key)) return false;
			AbstractAttribute.Key attr = (AbstractAttribute.Key) obj;
			return _type == attr._type && _location == attr._location;
		}
	
		@Override
		public int hashCode() {
			return _location.ordinal() << 16 | _type.ordinal();
		}
	
    }
    
    public abstract AbstractAttribute copy();

}