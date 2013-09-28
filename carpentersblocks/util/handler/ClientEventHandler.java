package carpentersblocks.util.handler;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.block.BlockBase;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ClientEventHandler {
    @ForgeSubscribe
    public void SoundEvent(PlaySoundEvent event) {
        if (event.name.startsWith("place.carpentermod")
            || event.name.startsWith("dig.carpentermod")
            || event.name.startsWith("step.carpentermod")) {
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                World world = FMLClientHandler.instance().getClient().theWorld;
                int x = MathHelper.floor_float(event.x), y = MathHelper.floor_float(event.y), z = MathHelper.floor_float(event.z);
                int blockId = world.getBlockId( x,
                                                y,
                                                z);
                if (blockId > 0 && world.getBlockTileEntity(
                        x,
                        y,
                        z) instanceof TECarpentersBlock) {
                    TECarpentersBlock tileentity = (TECarpentersBlock)  world.getBlockTileEntity(
                            x,
                            y,
                            z);
                    if (tileentity != null) {
                        Block block = BlockProperties.getCoverBlock(tileentity, 6);
                        SoundPoolEntry sound;
                        if (block instanceof BlockBase){
                            sound = event.manager.soundPoolSounds.getRandomSoundFromSoundPool(event.name.startsWith("dig.") ? Block.soundWoodFootstep.getBreakSound() : event.name.startsWith("place.") ? Block.soundWoodFootstep.getPlaceSound() : Block.soundWoodFootstep.getStepSound());  
                        }else{
                            sound = event.manager.soundPoolSounds.getRandomSoundFromSoundPool(event.name.startsWith("dig.") ? block.stepSound.getBreakSound() : event.name.startsWith("place.") ? block.stepSound.getPlaceSound() : block.stepSound.getStepSound());  
                        }
                        event.result = sound;
                    }
                        
                }
            }
        }
    }
    
    private static Random   rand    = new Random();

    @ForgeSubscribe
    public void onLivingUpdate(LivingUpdateEvent event) {
        EntityLivingBase entity = event.entityLiving;
        if (entity.isSprinting() && !entity.isInWater() && event.entity.worldObj.isRemote) {
            int x = MathHelper.floor_double(entity.posX);
            int y = MathHelper.floor_double(entity.posY - 0.20000000298023224D
                                            - (double) entity.yOffset);
            int z = MathHelper.floor_double(entity.posZ);
            int id = entity.worldObj.getBlockId(x,
                                                y,
                                                z);
            if(entity.worldObj.getBlockTileEntity(       x,
                    y,
                    z) instanceof TECarpentersBlock){
                TECarpentersBlock carpenterBlock = (TECarpentersBlock) entity.worldObj.getBlockTileEntity(       x,
                        y,
                        z); 
                if (carpenterBlock != null) {
                    
                    Block block = BlockProperties.getCoverBlock(carpenterBlock, 1);
                    int metadata;
                    if (block.blockID == carpenterBlock.worldObj.getBlockId(x,y,z)){
                        block = BlockProperties.getCoverBlock(carpenterBlock, 6);
                        metadata = BlockProperties.getCoverMetadata(carpenterBlock, 6);
                    }else{
                        metadata = BlockProperties.getCoverMetadata(carpenterBlock, 1);
                    }
                    EntityDiggingFX particle = new EntityDiggingFX(entity.worldObj, entity.posX
                            + ((double) rand.nextFloat() - 0.5D)
                            * (double) entity.width, entity.boundingBox.minY + 0.1D,                                                        entity.posZ
                            + ((double) rand.nextFloat() - 0.5D)
                            * (double) entity.width, -entity.motionX * 4.0D, 1.5D, -entity.motionZ * 4.0D, block, metadata);
                    if (block instanceof BlockBase){
                        particle.func_110125_a(((BlockBase)block).getDefaultIconFromSide(0));
                    }  
                    FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle.applyColourMultiplier(entity.chunkCoordX, entity.chunkCoordY, entity.chunkCoordZ));                       
                }
            }
        }
    }
    
    @ForgeSubscribe
    public void StepSoundInterrupt(PlaySoundAtEntityEvent event) {
        if (event != null && event.name != null
            && event.name.startsWith("step.carpentermod")) {
            TileEntity tileentity = event.entity.worldObj.getBlockTileEntity(event.entity.chunkCoordX, event.entity.chunkCoordY, event.entity.chunkCoordZ);
            if (tileentity != null
                && tileentity instanceof TECarpentersBlock) {
                StepSound stepsound =BlockProperties.getCoverBlock(((TECarpentersBlock) tileentity),6).stepSound;
                if (stepsound != null) {
                    event.name = stepsound.getStepSound();
                }
            }
        }
    }
}
