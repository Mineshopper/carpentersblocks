package carpentersblocks.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TECarpentersFlowerPot extends TEBase {
    
    public String soil;
    public String plant;
    public byte soil_metadata;
    public byte plant_metadata;
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        soil = nbt.getString("soil");
        plant = nbt.getString("plant");
        soil_metadata = nbt.getByte("soil_metadata");
        plant_metadata = nbt.getByte("plant_metadata");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setString("soil", soil);
        nbt.setString("plant", plant);
        nbt.setByte("soil_metadata", soil_metadata);
        nbt.setByte("plant_metadata", plant_metadata);
    }
    
}
