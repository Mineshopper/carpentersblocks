package com.carpentersblocks.nbt.attribute;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Stores attribute and unique identifier for validation purposes.
 */
public class AttributeItemStack extends AbstractAttribute<ItemStack> {

    public AttributeItemStack() {
    	super(null, null, null);
    }
    
    public AttributeItemStack(EnumAttributeLocation location, EnumAttributeType type, ItemStack itemStack) {
    	super(location, type, itemStack);
	}

	@Override
	public void writeModelToNBT(CompoundNBT nbt) {
		getModel().save(nbt);
	}

	@Override
	public void readModelFromNBT(CompoundNBT nbt) {
        ItemStack itemStack = ItemStack.of(nbt);
        setModel(itemStack);
	}

	@Override
	public String getModelIdentifier() {
		return IDENT_ATTR_ITEMSTACK;
	}

	@Override
	public AbstractAttribute<ItemStack> copy() {
		return new AttributeItemStack(_location, _type, _model.copy());
	}

}