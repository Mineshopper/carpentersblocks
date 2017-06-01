package com.carpentersblocks.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Level;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

/**
 * Stores attribute and unique identifier for validation purposes.
 */
public class Attribute {
    
    private static final String TAG_UNIQUE_ID = "cbUniqueId";
    private ItemStack _itemStack;
    private String _uniqueId;
    private boolean _error;
    
    public Attribute(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() != null) {
            this._itemStack = itemStack;
            _uniqueId = GameRegistry.findUniqueIdentifierFor(itemStack.getItem()).toString();
        }
    }
    
    public ItemStack getItemStack() {
        return _itemStack;
    }

    public String getUniqueId() {
        return _uniqueId;
    }
    
    /**
     * Write the stack fields to a NBT object. Return the new NBT object.
     */
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        _itemStack.writeToNBT(nbt);
        nbt.setString(TAG_UNIQUE_ID, _uniqueId);
        return nbt;
    }
    
    public static Attribute loadAttributeFromNBT(NBTTagCompound nbt)
    {
        ItemStack itemStack = ItemStack.loadItemStackFromNBT(nbt);
        if (itemStack == null) {
            String uuid = nbt.getString(TAG_UNIQUE_ID);
            if (uuid.contains(":")) {
                UniqueIdentifier uniqueId = new UniqueIdentifier(uuid);
                itemStack = GameRegistry.findItemStack(uniqueId.modId, uniqueId.name, 1);
                if (itemStack != null) {
                    int dmg = nbt.getShort("Damage");
                    itemStack.setItemDamage(dmg);
                    ModLogger.log(Level.WARN, "Invalid Id for attribute '" + uniqueId.toString() + "' corrected.");
                } else {
                    ModLogger.log(Level.WARN, "Block attribute '" + uniqueId.toString() + "' was unable to be recovered. Was a mod removed?");
                }
            } else {
                ModLogger.log(Level.WARN, "Unable to resolve attribute '" + uuid + "'");
            }
        }        
        return new Attribute(itemStack);
    }
    
}