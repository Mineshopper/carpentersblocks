package carpentersblocks.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TECarpentersFlowerPot extends TEBase {

    private final String TAG_PLANT_ITEMSTACKS = "pot_property";
    private final String TAG_SOIL  = "soil";
    private final String TAG_PLANT = "plant";

    public ItemStack soil;
    public ItemStack plant;

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        if (nbt.hasKey("data")) {
            migrationHelper.cacheFlowerPotNBT(nbt);
        }

        NBTTagList list = nbt.getTagList(TAG_PLANT_ITEMSTACKS);

        soil = null;
        plant = null;

        for (int idx = 0; idx < list.tagCount(); ++idx)
        {
            if (((NBTTagCompound)list.tagAt(idx)).hasKey(TAG_SOIL)) {
                soil = ItemStack.loadItemStackFromNBT((NBTTagCompound)list.tagAt(idx));
            } else if (((NBTTagCompound)list.tagAt(idx)).hasKey(TAG_PLANT)) {
                plant = ItemStack.loadItemStackFromNBT((NBTTagCompound)list.tagAt(idx));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        if (migrationHelper.containsFlowerPotCache) {

            migrationHelper.writeFlowerPotToNBT(this, nbt);
            migrationHelper.containsFlowerPotCache = false;
            getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);

        } else {

            NBTTagList list = new NBTTagList();

            if (soil != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_SOIL, (byte) 0);
                soil.writeToNBT(nbt1);
                list.appendTag(nbt1);
            }
            if (plant != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_PLANT, (byte) 0);
                plant.writeToNBT(nbt1);
                list.appendTag(nbt1);
            }

            nbt.setTag(TAG_PLANT_ITEMSTACKS, list);

        }
    }

}
