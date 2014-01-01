package carpentersblocks.tileentity;

import carpentersblocks.block.BlockCarpentersDaylightSensor;

public class TECarpentersDaylightSensor extends TEBase {

	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
	 * ticks and creates a new spawn inside its implementation.
	 */
	@Override
	public void updateEntity()
	{
		if (worldObj != null && !worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0L)
		{
			blockType = getBlockType();

			if (blockType != null) {
				if (getBlockType() instanceof BlockCarpentersDaylightSensor) {
					((BlockCarpentersDaylightSensor) getBlockType()).updateLightLevel(worldObj, xCoord, yCoord, zCoord);
				}
			}
		}
	}

}
