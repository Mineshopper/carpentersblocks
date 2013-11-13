package carpentersblocks.util.handler;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import carpentersblocks.block.BlockBase;
import carpentersblocks.tileentity.TEBase;
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
				int blockId = world.getBlockId(	x,
												y,
												z);
				if (blockId > 0
					&& world.getBlockTileEntity(x,
												y,
												z) instanceof TEBase) {
					TEBase tileentity = (TEBase) world.getBlockTileEntity(	x,
																			y,
																			z);
					if (tileentity != null) {
						Block block = BlockProperties.getCoverBlock(tileentity,
																	6);
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
	}

	@ForgeSubscribe
	public void StepSoundInterrupt(PlaySoundAtEntityEvent event) {
		if (event != null && event.name != null
			&& event.name.startsWith("step.carpentermod")) {
			TileEntity tileentity = event.entity.worldObj.getBlockTileEntity(	event.entity.chunkCoordX,
																				event.entity.chunkCoordY,
																				event.entity.chunkCoordZ);
			if (tileentity != null && tileentity instanceof TEBase) {
				StepSound stepsound = BlockProperties.getCoverBlock(((TEBase) tileentity),
																	6).stepSound;
				if (stepsound != null) {
					event.name = stepsound.getStepSound();
				}
			}
		}
	}
}
