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
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.ItemRegistry;

public class EventHandler {

	/** Stores face for onBlockClicked(). */
	public static int eventFace;
	
	/** Stores entity that hit block. */
	public static Entity eventEntity;
	
	/** Stores metadata representing blockIcon in BlockBase. */
	public final static int BLOCKICON_META_ID = 1000;
	
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
            
            ItemStack itemStack = event.entityPlayer.getHeldItem();
            
            boolean isHammerEquipped = itemStack != null && itemStack.itemID == ItemRegistry.itemCarpentersHammerID;

			if (event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {

				eventFace = event.face;
			}
			else if (isHammerEquipped && event.entityPlayer.isSneaking())
			{
				/*
				 * Call onBlockActivated() if player is holding the Carpenter's Hammer
				 * and sneaking.
				 */
				block.onBlockActivated(event.entity.worldObj, event.x, event.y, event.z, event.entityPlayer, event.face, 1.0F, 1.0F, 1.0F);	
			}
		}
	}

	@ForgeSubscribe
	public void livingUpdateEvent(LivingUpdateEvent event)
	{
		EntityLivingBase entity = event.entityLiving;
		World world = entity.worldObj;

		if (world.isRemote && entity.isSprinting() && !entity.isInWater())
		{
			int x = MathHelper.floor_double(entity.posX);
			int y = MathHelper.floor_double(entity.posY - 0.20000000298023224D - (double) entity.yOffset);
			int z = MathHelper.floor_double(entity.posZ);
			
			/*
			 * Here we setup the details to be used to create the particles
			 * this is so the EntityDiggingFX parses the values that we want
			 * it to
			 * 
			 * Setting default metadata to BLOCKICON_META_ID states that this is
			 * BlockBase, if the Block is covered the necessary adjustments
			 * are made
			 */
			int blockId = world.getBlockId(x, y, z);
			int metadata = BLOCKICON_META_ID;
		
			if (blockId > 0 && Block.blocksList[blockId] instanceof BlockBase)
			{
				TileEntity TE_normal = world.getBlockTileEntity(x, y, z);
				
				if (TE_normal instanceof TEBase)
				{
					TEBase TE = (TEBase) TE_normal;
					
					if (BlockProperties.hasCover(TE, 6))
					{
						blockId = BlockProperties.getCoverID(TE, 6);
						metadata = BlockProperties.getCoverMetadata(TE, 6);
					}
				}
				
				/* Spawn sprint particles at the foot of the entity */
				RenderHelper.spawnTileParticleAt(entity.worldObj, entity, blockId, metadata);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void SoundEvent(PlaySoundEvent event)
	{
		if (event != null && event.name != null)
		{
			if (event.name.contains("carpentersblock"))
			{
				if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
				{
					World world = FMLClientHandler.instance().getClient().theWorld;
					int x = MathHelper.floor_float(event.x);
					int y = MathHelper.floor_float(event.y);
					int z = MathHelper.floor_float(event.z);
					int blockID = world.getBlockId(x, y, z);

					if (blockID > 0 && Block.blocksList[blockID] instanceof BlockBase) {

						Block block = BlockProperties.getCoverBlock((TEBase)world.getBlockTileEntity(x, y, z), 6);

						if (block instanceof BlockBase) {
							event.result = event.manager.soundPoolSounds.getRandomSoundFromSoundPool(event.name.startsWith("dig.") ? Block.soundWoodFootstep.getBreakSound() : event.name.startsWith("place.") ? Block.soundWoodFootstep.getPlaceSound() : Block.soundWoodFootstep.getStepSound());
						} else {
							event.result = event.manager.soundPoolSounds.getRandomSoundFromSoundPool(event.name.startsWith("dig.") ? block.stepSound.getBreakSound() : event.name.startsWith("place.") ? block.stepSound.getPlaceSound() : block.stepSound.getStepSound());
						}

					} else {

						event.result = event.manager.soundPoolSounds.getRandomSoundFromSoundPool(Block.soundWoodFootstep.getBreakSound());

					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void StepSoundInterrupt(PlaySoundAtEntityEvent event)
	{
		if (event != null && event.name != null)
		{
			if (event.name.startsWith("step.carpentersblock"))
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

}
