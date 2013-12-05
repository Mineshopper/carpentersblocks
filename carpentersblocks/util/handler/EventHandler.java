package carpentersblocks.util.handler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import carpentersblocks.block.BlockBase;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.ItemRegistry;

public class EventHandler
{

	/** Stores face for onBlockClicked() */
	public static int eventFace;

	/** Stores entity that hit block */
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

				/* Call onBlockActivated() if player is holding the Carpenter's Hammer and sneaking */
				
				if (itemStack != null && itemStack.itemID == ItemRegistry.itemCarpentersHammerID)
					block.onBlockActivated(event.entity.worldObj, event.x, event.y, event.z, event.entityPlayer, event.face, 1.0F, 1.0F, 1.0F);	
			}
		}
	}

	@ForgeSubscribe
	public void livingUpdateEvent(LivingUpdateEvent event)
	{
		EntityLivingBase entity = event.entityLiving;

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
	
	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void SoundEvent(PlaySoundEvent event)
	{
		if (event.name.contains("carpentermod"))
		{
			if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
			{
				World world = FMLClientHandler.instance().getClient().theWorld;
				int x = MathHelper.floor_float(event.x);
				int y = MathHelper.floor_float(event.y);
				int z = MathHelper.floor_float(event.z);
				int blockID = world.getBlockId(x, y, z);

				if (blockID > 0 && Block.blocksList[blockID] instanceof BlockBase)
				{
					TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

					Block block = BlockProperties.getCoverBlock(TE, 6);
					SoundPoolEntry sound;

					if (block instanceof BlockBase) {
						sound = event.manager.soundPoolSounds.getRandomSoundFromSoundPool(event.name.startsWith("dig.") ? Block.soundWoodFootstep.getBreakSound() : event.name.startsWith("place.") ? Block.soundWoodFootstep.getPlaceSound() : Block.soundWoodFootstep.getStepSound());
					} else {
						sound = event.manager.soundPoolSounds.getRandomSoundFromSoundPool(event.name.startsWith("dig.") ? block.stepSound.getBreakSound() : event.name.startsWith("place.") ? block.stepSound.getPlaceSound() : block.stepSound.getStepSound());
					}

					event.result = sound;
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void StepSoundInterrupt(PlaySoundAtEntityEvent event)
	{
		if (event != null && event.name != null && event.name.startsWith("step.carpentermod"))
		{
			int x = MathHelper.floor_double(event.entity.posX);
			int y = MathHelper.floor_double(event.entity.posY - 0.20000000298023224D - (double) event.entity.yOffset);
			int z = MathHelper.floor_double(event.entity.posZ);
			
			TileEntity TE = event.entity.worldObj.getBlockTileEntity(x, y, z);
			
			if (TE != null && TE instanceof TEBase)
			{
				Block block = BlockProperties.getCoverBlock(((TEBase) TE), 6);
				
				if (block instanceof BlockBase) {
					event.name = Block.soundWoodFootstep.getStepSound();
				} else if (block.stepSound != null) {
					event.name = block.stepSound.getStepSound();
				}
			}
		}
	}

}
