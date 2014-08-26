package com.carpentersblocks.renderer.helper;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.carpentersblocks.data.Torch;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.tileentity.TECarpentersTorch;
import com.carpentersblocks.util.BlockProperties;

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
        TE.getWorldObj().spawnParticle("largesmoke", headCoords[0], headCoords[1], headCoords[2], 0.0D, 0.0D, 0.0D);
    }

    /**
     * Spawns a particle at the base of an entity.
     *
     * @param world the world to spawn the particle
     * @param entity the entity at which feet the particles will spawn
     * @param blockID the ID of the block to reference for the particle
     * @param metadata the metadata of the block for the particle
     */
    public static void spawnTileParticleAt(Entity entity, ItemStack itemStack)
    {
        BlockProperties.prepareItemStackForRendering(itemStack);

        entity.worldObj.spawnParticle
        (
                "tilecrack_" + itemStack.itemID + "_" + itemStack.getItemDamage(),
                entity.posX + (entity.worldObj.rand.nextFloat() - 0.5D) * entity.width, entity.boundingBox.minY + 0.1D,
                entity.posZ + (entity.worldObj.rand.nextFloat() - 0.5D) * entity.width,
                -entity.motionX * 4.0D,
                1.5D,
                -entity.motionZ * 4.0D
        );
    }

    /**
     * Produces block destruction particles at coordinates.
     */
    public static void addDestroyEffect(World world, int x, int y, int z, ItemStack itemStack, EffectRenderer effectRenderer)
    {
        BlockProperties.prepareItemStackForRendering(itemStack);
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

                    EntityDiggingFX particle = new EntityDiggingFX(world, dirX, dirY, dirZ, dirX - x - 0.5D, dirY - y - 0.5D, dirZ - z - 0.5D, BlockProperties.toBlock(itemStack), itemStack.getItemDamage());
                    effectRenderer.addEffect(particle.applyColourMultiplier(x, y, z));
                }
            }
        }
    }

    /**
     * Produces block hit particles at coordinates.
     */
    public static void addHitEffect(TEBase TE, MovingObjectPosition target, double x, double y, double z, ItemStack itemStack, EffectRenderer effectRenderer)
    {
        BlockProperties.prepareItemStackForRendering(itemStack);

        EntityDiggingFX particle = new EntityDiggingFX(TE.getWorldObj(), x, y, z, 0.0D, 0.0D, 0.0D, BlockProperties.toBlock(itemStack), itemStack.getItemDamage());
        effectRenderer.addEffect(particle.applyColourMultiplier(target.blockX, target.blockY, target.blockZ).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
    }

}
