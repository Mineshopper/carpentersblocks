package com.carpentersblocks.nbt.attribute;

import net.minecraft.nbt.CompoundNBT;

public class AttributeString extends AbstractAttribute<String> {

	private static final String TAG_ATTR_STRING = "cbAttrString";
	
	public AttributeString() {
		super(null, null, null);
	}
	
	public AttributeString(EnumAttributeLocation location, EnumAttributeType type, String model) {
		super(location, type, model);
	}

	@Override
	public void writeModelToNBT(CompoundNBT nbt) {
		nbt.putString(TAG_ATTR_STRING, getModel());
	}

	@Override
	public void readModelFromNBT(CompoundNBT nbt) {
		setModel(nbt.getString(TAG_ATTR_STRING));
	}

	@Override
	public String getModelIdentifier() {
		return IDENT_ATTR_STRING;
	}

	@Override
	public AbstractAttribute<?> copy() {
		return new AttributeString(_location, _type, new String(_model));
	}
	
}
