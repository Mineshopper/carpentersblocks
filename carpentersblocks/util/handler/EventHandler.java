package carpentersblocks.util.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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
import carpentersblocks.renderer.helper.ParticleHelper;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EventHandler {
	
	/** Stores face for onBlockClicked(). */
	public static int eventFace;

	/** Stores entity that hit block. */
	public static Entity eventEntity;

	/** This is an offset used for blockIcon. */
	public final static int BLOCKICON_BASE_ID = 1000;

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
				
				/*
				 * Creative mode won't call onBlockClicked() because it will try to destroy the block.
				 * We'll invoke it here if the hammer is equipped.
				 */
				if (event.entityPlayer.capabilities.isCreativeMode && isHammerEquipped && event.entityPlayer.isSneaking()) {
					block.onBlockClicked(event.entity.worldObj, event.x, event.y, event.z, event.entityPlayer);
				}
				
			} else { 
				
				/*
				 * onBlockActivated() isn't called if the player is sneaking.
				 * We'll invoke it here if the hammer is equipped.
				 */
				if (isHammerEquipped && event.entityPlayer.isSneaking()) {
					block.onBlockActivated(event.entity.worldObj, event.x, event.y, event.z, event.entityPlayer, event.face, 1.0F, 1.0F, 1.0F);
				}
				
			}
		}
	}

	@ForgeSubscribe
	public void livingUpdateEvent(LivingUpdateEvent event)
	{
		EntityLiving entity = event.entityLiving;
		World world = entity.worldObj;

		if (world.isRemote && entity.isSprinting() && !entity.isInWater())
		{
			int x = MathHelper.floor_double(entity.posX);
			int y = MathHelper.floor_double(entity.posY - 0.20000000298023224D - entity.yOffset);
			int z = MathHelper.floor_double(entity.posZ);

			int blockId = world.getBlockId(x, y, z);

			if (blockId > 0 && Block.blocksList[blockId] instanceof BlockBase)
			{
				TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);
				
				int effectiveSide = BlockProperties.hasCover(TE, 1) ? 1 : 6;

				Block block = BlockProperties.getCoverBlock(TE, effectiveSide);
				int metadata = block instanceof BlockBase ? BLOCKICON_BASE_ID : BlockProperties.getCoverMetadata(TE, effectiveSide);
								
				/* Check for overlays that influence particles */
				block = ParticleHelper.getParticleBlockFromOverlay(TE, effectiveSide, block);

				/* Spawn sprint particles at the foot of the entity */
				ParticleHelper.spawnTileParticleAt(world, entity, block.blockID, metadata);
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
				int y = MathHelper.floor_double(event.entity.posY - 0.20000000298023224D - event.entity.yOffset);
				int z = MathHelper.floor_double(event.entity.posZ);

				TileEntity TE = event.entity.worldObj.getBlockTileEntity(x, y, z);

				if (TE != null && TE instanceof TEBase)
				{
					Block block = BlockProperties.getCoverBlock((TEBase) TE, 6);

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