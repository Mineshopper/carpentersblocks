package com.carpentersblocks.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import com.carpentersblocks.data.Safe;

public class TECarpentersSafe extends TEBase implements ISidedInventory {

    private final String TAG_UPGRADE = "upgrade";
    private final String TAG_SLOT    = "Slot";
    private final String TAG_ITEMS   = "Items";

    /** Holds contents of block. */
    private ItemStack[] inventoryContents = new ItemStack[54];

    /** Whether safe holds 54 items opposed to only 27. */
    private boolean hasUpgrade = false;

    /** Counts ticks. */
    private int tickCount;

    /** Used to determine whether capacity strip requires a redraw. */
    private boolean contentsChanged;

    /** Indicates safe render update should occur next tick. */
    private boolean forceEntityUpdate;

    @Override
    /**
     * Determines if this TileEntity requires update calls.
     * @return True if you want updateEntity() to be called, false if not
     */
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (!worldObj.isRemote) {
            if (++tickCount >= 20 || forceEntityUpdate) {
                tickCount = 0;
                if (contentsChanged) {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    contentsChanged = forceEntityUpdate = false;
                }
            }
        }
    }

    /**
     * Returns whether safe is upgraded to 54 slots (gold).
     */
    public boolean hasUpgrade()
    {
        return hasUpgrade;
    }

    public void setUpgrade()
    {
        hasUpgrade = true;
        markDirty();
        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory()
    {
        return hasUpgrade ? 54 : 27;
    }

    /**
     * Returns the stack in slot
     */
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inventoryContents[slot];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
    public ItemStack decrStackSize(int slot, int size)
    {
        ItemStack itemStack = null;

        if (inventoryContents[slot] != null) {
            if (inventoryContents[slot].stackSize <= size) {
                itemStack = inventoryContents[slot];
                inventoryContents[slot] = null;
            } else {
                itemStack = inventoryContents[slot].splitStack(size);
                if (inventoryContents[slot].stackSize == 0) {
                    inventoryContents[slot] = null;
                }
            }
        }

        if (itemStack == null) {
            return null;
        } else {
            markDirty();
            return itemStack;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        return null;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        inventoryContents[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
            itemStack.stackSize = getInventoryStackLimit();
        }

        markDirty();
    }

    @Override
    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void markDirty()
    {
        contentsChanged = true;
        super.markDirty();
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        /* Compatibility code with older versions prior to v3.2.5 */
        if (nbt.hasKey("inventorySize")) {
            hasUpgrade = nbt.getInteger("inventorySize") > 27;
        } else {
            hasUpgrade = nbt.getBoolean(TAG_UPGRADE);
        }

        NBTTagList nbttaglist = nbt.getTagList(TAG_ITEMS, 10);
        inventoryContents = new ItemStack[54];

        for (int idx = 0; idx < nbttaglist.tagCount(); ++idx) {
            NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(idx);
            int j = nbt1.getByte(TAG_SLOT) & 255;
            if (j >= 0 && j < inventoryContents.length) {
                inventoryContents[j] = ItemStack.loadItemStackFromNBT(nbt1);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setBoolean(TAG_UPGRADE, hasUpgrade);

        NBTTagList nbttaglist = new NBTTagList();

        for (int idx = 0; idx < inventoryContents.length; ++idx) {
            if (inventoryContents[idx] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte(TAG_SLOT, (byte) idx);
                inventoryContents[idx].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt.setTag(TAG_ITEMS, nbttaglist);
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer)
    {
        if (worldObj.getTileEntity(xCoord, yCoord, zCoord).equals(this)) {
            return entityPlayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
        }

        return false;
    }

    @Override
    public void openInventory()
    {
        Safe.setState(this, Safe.STATE_OPEN);
    }

    @Override
    public void closeInventory()
    {
        Safe.setState(this, Safe.STATE_CLOSED);

        /* Make updateEntity() check contents immediately on close. */
        forceEntityUpdate = true;
    }

    @Override
    public String getInventoryName()
    {
        return "tile.blockCarpentersSafe.name";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        int sizeInventory = getSizeInventory();
        int[] accessibleSlots = new int[sizeInventory];

        for (int idx = 0; idx < sizeInventory; ++idx) {
            accessibleSlots[idx] = idx;
        }

        return accessibleSlots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side)
    {
        return Safe.allowsInsertion(this) && Safe.getFacing(this).ordinal() != side;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side)
    {
        return Safe.allowsExtraction(this) && Safe.getFacing(this).ordinal() != side;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return true;
    }

}
