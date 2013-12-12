package carpentersblocks.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import carpentersblocks.data.Safe;

public class TECarpentersSafe extends TEBase implements ISidedInventory {

	/** Holds contents of block. */
	private ItemStack[] inventoryContents = new ItemStack[54];

	/** Holds size of inventory. */
	private int inventorySize = 27;

	private static final int[] accessibleSlots =
		{
		0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10,
		11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
		22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32,
		33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
		44, 45, 46, 47, 48, 49, 50, 51, 52, 53
		};

	/**
	 * Returns whether safe has 54 item slots.
	 */
	public boolean hasUpgrade()
	{
		return inventorySize > 27;
	}

	/**
	 * Sets inventory size.
	 * Returns false if size cannot be increased.
	 */
	public boolean incSizeInventory()
	{
		if (hasUpgrade()) {
			return false;
		}

		inventorySize = 54;
		return true;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return inventorySize;
	}

	/**
	 * Returns the stack in slot
	 */
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (slot < inventoryContents.length) {
			return inventoryContents[slot];
		} else {
			return null;
		}
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
	 * new stack.
	 */
	@Override
	public ItemStack decrStackSize(int slot, int numItems)
	{
		if (inventoryContents[slot] != null)
		{
			ItemStack itemStack;

			if (inventoryContents[slot].stackSize <= numItems) {
				itemStack = inventoryContents[slot];
				inventoryContents[slot] = null;
				onInventoryChanged();
				return itemStack;
			} else {
				itemStack = inventoryContents[slot].splitStack(numItems);

				if (inventoryContents[slot].stackSize == 0) {
					inventoryContents[slot] = null;
				}

				onInventoryChanged();
				return itemStack;
			}
		} else {
			return null;
		}
	}

	/**
	 * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
	 * like when you close a workbench GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if (inventoryContents[slot] != null) {
			ItemStack itemstack = inventoryContents[slot];
			inventoryContents[slot] = null;
			return itemstack;
		} else {
			return null;
		}
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

		onInventoryChanged();
	}

	@Override
	/**
	 * Called when an the contents of an Inventory change, usually
	 */
	public void onInventoryChanged()
	{
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		super.onInventoryChanged();
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList nbttaglist = nbt.getTagList("Items");

		inventorySize = nbt.getInteger("inventorySize");
		inventoryContents = new ItemStack[getSizeInventory()];

		for (int count = 0; count < nbttaglist.tagCount(); ++count)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(count);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 0 && j < inventoryContents.length) {
				inventoryContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
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
		NBTTagList nbttaglist = new NBTTagList();

		for (int idx = 0; idx < inventoryContents.length; ++idx)
		{
			if (inventoryContents[idx] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) idx);
				inventoryContents[idx].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setInteger("inventorySize", inventorySize);
		nbt.setTag("Items", nbttaglist);
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer)
	{
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this) {
			return entityPlayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
		}

		return false;
	}

	@Override
	public void openChest()
	{
		Safe.setState(this, Safe.STATE_OPEN);
	}

	@Override
	public void closeChest()
	{
		Safe.setState(this, Safe.STATE_CLOSED);
	}

	@Override
	public String getInvName()
	{
		return "tile.blockCarpentersSafe.name";
	}

	@Override
	public boolean isInvNameLocalized()
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
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}

}
