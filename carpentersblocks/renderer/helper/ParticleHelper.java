package carpentersblocks.renderer.helper;

import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.handler.OverlayHandler;
import net.minecraft.block.Block;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;

public class ParticleHelper {

	/**
	 * Spawns a particle at the base of an entity
	 * 
	 * @param world the world to spawn the particle
	 * @param entity the entity at which feet the particles will spawn
	 * @param blockID the ID of the block to reference for the particle
	 * @param metadata the metadata of the block for the particle
	 */
	public static void spawnTileParticleAt(World world, Entity entity, int blockID, int metadata)
	{
		entity.worldObj.spawnParticle(
			"tilecrack_" + blockID + "_" + metadata,
			entity.posX + ((double) entity.worldObj.rand.nextFloat() - 0.5D) * (double) entity.width,
			entity.boundingBox.minY + 0.1D,
			entity.posZ + ((double) entity.worldObj.rand.nextFloat() - 0.5D) * (double) entity.width,
			-entity.motionX * 4.0D,
			1.5D,
			-entity.motionZ * 4.0D
		);
	}
	
	/**
	 * Produces block destruction particles at coordinates.
	 */
	public static void addDestroyEffect(World world, int x, int y, int z, Block block, int metadata, EffectRenderer effectRenderer)
	{
		byte factor = 4;

		for (int posX = 0; posX < factor; ++posX)
		{
			for (int posY = 0; posY < factor; ++posY)
			{
				for (int posZ = 0; posZ < factor; ++posZ)
				{
					double dirX = (double) x + ((double) posX + 0.5D) / (double) factor;
					double dirY = (double) y + ((double) posY + 0.5D) / (double) factor;
					double dirZ = (double) z + ((double) posZ + 0.5D) / (double) factor;

					EntityDiggingFX particle = new EntityDiggingFX(world, dirX, dirY, dirZ, dirX - (double) x - 0.5D, dirY - (double) y - 0.5D, dirZ - (double) z - 0.5D, block, metadata);
					effectRenderer.addEffect(particle.applyColourMultiplier(x, y, z));
				}
			}
		}
	}
	
	/**
	 * Produces block hit particles at coordinates.
	 */
	public static void addBlockHitEffect(TEBase TE, MovingObjectPosition target, double x, double y, double z, Block block, int metadata, EffectRenderer effectRenderer)
	{
		EntityDiggingFX particle = new EntityDiggingFX(TE.worldObj, x, y, z, 0.0D, 0.0D, 0.0D, block, metadata);
		effectRenderer.addEffect(particle.applyColourMultiplier(target.blockX, target.blockY, target.blockZ).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
	}
	
	/**
	 * Returns block with considerations for the overlay and side of block
	 * being interacted with.
	 */
	public static Block getParticleBlockFromOverlay(TEBase TE, int coverSide, Block block)
	{
		if (BlockProperties.hasOverlay(TE, coverSide))
		{
			switch (BlockProperties.getOverlay(TE, coverSide)) {
			case OverlayHandler.OVERLAY_GRASS:
				block = Block.grass;
				break;
			case OverlayHandler.OVERLAY_HAY:
				block = Block.hay;
				break;
			case OverlayHandler.OVERLAY_MYCELIUM:
				block = Block.mycelium;
				break;
			case OverlayHandler.OVERLAY_SNOW:
				block = Block.blockSnow;
				break;
			case OverlayHandler.OVERLAY_VINE:
				block = Block.vine;
				break;
			case OverlayHandler.OVERLAY_WEB:
				block = Block.web;
				break;
			}
		}
		
		return block;
	}
	
}
