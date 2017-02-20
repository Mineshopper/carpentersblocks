package com.carpentersblocks.util.attribute;

import net.minecraft.nbt.NBTTagCompound;

public class AttributeString extends AbstractAttribute<String> {

	private static final String TAG_ATTR_STRING = "cbAttrString";
	
	public AttributeString() {
		super(null, null, null);
	}
	
	public AttributeString(EnumAttributeLocation location, EnumAttributeType type, String model) {
		super(location, type, model);
	}

	@Override
	public void writeModelToNBT(NBTTagCompound nbt) {
		nbt.setString(TAG_ATTR_STRING, getModel());
	}

	@Override
	public void readModelFromNBT(NBTTagCompound nbt) {
		setModel(nbt.getString(TAG_ATTR_STRING));
	}

	@Override
	public String getModelIdentifier() {
		return IDENT_ATTR_STRING;
	}

	@Override
	public AbstractAttribute copy() {
		return new AttributeString(_location, _type, new String(_model));
	}
	
}
