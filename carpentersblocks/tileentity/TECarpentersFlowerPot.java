package carpentersblocks.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TECarpentersFlowerPot extends TEBase {

    public static final byte TAG_SOIL  = 0;
    public static final byte TAG_PLANT = 1;

    public ItemStack soil;
    public ItemStack plant;

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        NBTTagList list = nbt.getTagList("pot_property", 10);

        soil = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(TAG_SOIL));
        plant = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(TAG_PLANT));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        NBTTagList list = new NBTTagList();

        if (soil != null) {
            NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setByte("soil", TAG_SOIL);
            soil.writeToNBT(nbt1);
            list.appendTag(nbt1);
        }
        if (plant != null) {
            NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setByte("plant", TAG_PLANT);
            plant.writeToNBT(nbt1);
            list.appendTag(nbt1);
        }

        nbt.setTag("pot_property", list);
    }

}
