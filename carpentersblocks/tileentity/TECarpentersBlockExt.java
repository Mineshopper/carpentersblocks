package carpentersblocks.tileentity;

import carpentersblocks.block.BlockCarpentersDaylightSensor;

public class TECarpentersBlockExt extends TECarpentersBlock
{
	
    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    @Override
	public void updateEntity()
    {
        if (this.worldObj != null && !this.worldObj.isRemote && this.worldObj.getTotalWorldTime() % 20L == 0L)
        {
            this.blockType = this.getBlockType();

            if (this.blockType != null)
            {            	
            	/*
            	 * Update daylight sensor light level once per second.
            	 */
            	if (this.blockType instanceof BlockCarpentersDaylightSensor)
        			((BlockCarpentersDaylightSensor) this.getBlockType()).updateLightLevel(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }

}
