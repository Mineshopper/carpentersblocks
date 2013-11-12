package carpentersblocks.util.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import carpentersblocks.block.BlockBase;
import carpentersblocks.tileentity.TEBase;

public class BlockEventHandler
{

	public static int eventFace;
	public static Entity eventEntity;
	
	@ForgeSubscribe
	/**
	 * Used to store side clicked and also forces onBlockActivated
	 * event when entityPlayer is sneaking and activates block with the
	 * Carpenter's Hammer.
	 */
	public void playerInteractEvent(PlayerInteractEvent event)
	{
		int blockID = event.entity.worldObj.getBlockId(event.x, event.y, event.z);
		
		if (blockID > 0 && Block.blocksList[blockID] instanceof BlockBase)
		{
			BlockBase block = (BlockBase) Block.blocksList[blockID];
			
			eventEntity = event.entity;

			if (event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {

				eventFace = event.face;

			} else if (event.entityPlayer.isSneaking()) {
				
				ItemStack itemStack = event.entityPlayer.getHeldItem();
				
				/*
				 * Call onBlockActivated() if player is holding the Carpenter's Hammer
				 * and sneaking.
				 */
				if (itemStack != null && itemStack.itemID == ItemHandler.itemCarpentersHammerID)
					block.onBlockActivated(event.entity.worldObj, event.x, event.y, event.z, event.entityPlayer, event.face, 1.0F, 1.0F, 1.0F);	
			}
		}
	}

	@ForgeSubscribe
	public void livingUpdateEvent(LivingUpdateEvent event)
	{
		EntityLiving entity = event.entityLiving;

		if (entity.isSprinting() && !entity.isInWater())
		{
			int x = MathHelper.floor_double(entity.posX);
			int y = MathHelper.floor_double(entity.posY - 0.20000000298023224D - (double) entity.yOffset);
			int z = MathHelper.floor_double(entity.posZ);
			int blockID = entity.worldObj.getBlockId(x, y, z);
			
			if (Block.blocksList[blockID] instanceof BlockBase)
			{
				TEBase TE = (TEBase) entity.worldObj.getBlockTileEntity(x, y, z);
				((BlockBase) Block.blocksList[blockID]).setBlockIcon(Block.blocksList[blockID].getBlockTexture(entity.worldObj, x, y, z, 0));
			}
		}
	}
	
}
