package com.carpentersblocks.util.attribute;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Stores attribute and unique identifier for validation purposes.
 */
public class AttributeItemStack extends AbstractAttribute<ItemStack> {
    
	private static final String TAG_RESOURCE_LOC_ID = "cbResourceLoc";
    private ResourceLocation _resourceLoc;
    
    public AttributeItemStack() {
    	super(null, null, null);
    }
    
    public AttributeItemStack(EnumAttributeLocation location, EnumAttributeType type, ItemStack itemStack) {
    	super(location, type, itemStack);
        if (itemStack != null && itemStack.getItem() != null) {
            _resourceLoc = itemStack.getItem().getRegistryName();
        }
	}

    public ResourceLocation getResourceLocation() {
        return _resourceLoc;
    }

	@Override
	public void writeModelToNBT(NBTTagCompound nbt) {
		getModel().writeToNBT(nbt);
		nbt.setString(TAG_RESOURCE_LOC_ID, _resourceLoc.toString());
	}

	@Override
	public void readModelFromNBT(NBTTagCompound nbt) {
        ItemStack itemStack = new ItemStack(nbt);
        if (itemStack.getItem() == null) {
            ResourceLocation resourceLoc = new ResourceLocation(nbt.getString(TAG_RESOURCE_LOC_ID));
            itemStack = GameRegistry.makeItemStack(resourceLoc.toString(), 0, 1, null);
            if (itemStack != null) {
                int dmg = nbt.getShort("Damage");
                itemStack.setItemDamage(dmg);
                //ModLogger.log(Level.WARN, "Invalid Id for attribute '" + uniqueId.toString() + "' corrected.");
            } else {
                //ModLogger.log(Level.WARN, "Block attribute '" + uniqueId.toString() + "' was unable to be recovered. Was a mod removed?");
            }
        }   
        setModel(itemStack);
        _resourceLoc = new ResourceLocation(nbt.getString(TAG_RESOURCE_LOC_ID));
	}

	@Override
	public String getModelIdentifier() {
		return IDENT_ATTR_ITEMSTACK;
	}

	@Override
	public AbstractAttribute copy() {
		return new AttributeItemStack(_location, _type, _model.copy());
	}

}