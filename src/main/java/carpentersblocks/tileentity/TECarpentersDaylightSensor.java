package carpentersblocks.tileentity;

import carpentersblocks.block.BlockCarpentersDaylightSensor;

public class TECarpentersDaylightSensor extends TEBase {

    @Override
    /**
     * Determines if this TileEntity requires update calls.
     * @return True if you want updateEntity() to be called, false if not
     */
    public boolean canUpdate()
    {
        return true;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    @Override
    public void updateEntity()
    {
        if (worldObj != null && !worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0L)
        {
            if (getBlockType() != null) {
                if (blockType instanceof BlockCarpentersDaylightSensor) {
                    ((BlockCarpentersDaylightSensor) blockType).updateLightLevel(worldObj, xCoord, yCoord, zCoord);
                }
            }
        }
    }

}
