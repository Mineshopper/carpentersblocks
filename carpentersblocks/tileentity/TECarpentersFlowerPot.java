package carpentersblocks.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TECarpentersFlowerPot extends TEBase {

    public short soil;
    public short plant;

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        soil = nbt.getShort("soil");
        plant = nbt.getShort("plant");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setShort("soil", soil);
        nbt.setShort("plant", plant);
    }

}
