package carpentersblocks.renderer.helper;

import net.minecraft.block.Block;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import carpentersblocks.data.Torch;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersTorch;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.OverlayHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleHelper {

    /**
     * Spawns big smoke particle when torch lowers state.
     */
    public static void spawnTorchBigSmoke(TECarpentersTorch TE)
    {
        double[] headCoords = Torch.getHeadCoordinates(TE);
        TE.worldObj.spawnParticle("largesmoke", headCoords[0], headCoords[1], headCoords[2], 0.0D, 0.0D, 0.0D);
    }

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
                entity.posX + (entity.worldObj.rand.nextFloat() - 0.5D) * entity.width,
                entity.boundingBox.minY + 0.1D,
                entity.posZ + (entity.worldObj.rand.nextFloat() - 0.5D) * entity.width,
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
                    double dirX = x + (posX + 0.5D) / factor;
                    double dirY = y + (posY + 0.5D) / factor;
                    double dirZ = z + (posZ + 0.5D) / factor;

                    EntityDiggingFX particle = new EntityDiggingFX(world, dirX, dirY, dirZ, dirX - x - 0.5D, dirY - y - 0.5D, dirZ - z - 0.5D, block, metadata);
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
