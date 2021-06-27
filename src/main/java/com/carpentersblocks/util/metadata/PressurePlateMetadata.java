package com.carpentersblocks.util.metadata;

import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.nbt.attribute.EnumAttributeType;
import com.carpentersblocks.util.BlockUtil;
import com.carpentersblocks.util.states.factory.AbstractState;
import com.carpentersblocks.util.states.factory.PressurePlateState;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class PressurePlateMetadata extends CommonMetadata implements IMetadataFacing {

    /**
     * 16-bit data components:
     *
     * [000000000] [00]    [0]      [0]   [000]
     * Unused      Trigger Polarity State Dir
     */

    public static final byte POLARITY_POS = 0;
    public static final byte POLARITY_NEG = 1;

    public static final byte STATE_NORMAL = 0;
    public static final byte STATE_DEPRESSED  = 1;

    public static final byte TRIGGER_PLAYER  = 0;
    public static final byte TRIGGER_MONSTER = 1;
    public static final byte TRIGGER_ANIMAL  = 2;
    public static final byte TRIGGER_ALL     = 3;

    public PressurePlateMetadata(CbTileEntity cbTileEntity) {
    	super(cbTileEntity.getCbMetadata());
    }
    
    public PressurePlateMetadata(int cbMetadata) {
    	super(cbMetadata);
    }
    
	@Override
	public boolean setFacing(CbTileEntity cbTileEntity, Direction facing) {
        int temp = (cbTileEntity.getCbMetadata() & ~0x7) | facing.ordinal();
        return cbTileEntity.setCbMetadata(temp);
	}

	@Override
	public Direction getFacing() {
		return Direction.from3DDataValue(getData() & 0x7);
	}

    /**
     * Returns state.
     */
    public int getState(CbTileEntity cbTileEntity) {
        return (cbTileEntity.getCbMetadata() & 0x8) >> 3;
    }

    /**
     * Sets state.
     */
    public void setState(CbTileEntity cbTileEntity, int state, boolean playSound) {
        int temp = (cbTileEntity.getCbMetadata() & ~0x8) | (state << 3);
        World world = cbTileEntity.getLevel();
        if (!world.isClientSide()) {
        	BlockState blockState = BlockUtil.getAttributeBlockState(cbTileEntity.getAttributeHelper(), EnumAttributeLocation.HOST, EnumAttributeType.COVER);
        	if (blockState != null) {
	        	if (!Material.WOOL.equals(blockState.getMaterial()) && playSound && getState(cbTileEntity) != state) {
		            world.playSound(null, cbTileEntity.getBlockPos().offset(0.05D, 0.5D, 0.5D), Blocks.ACACIA_BUTTON.getSoundType(blockState).getBreakSound(), SoundCategory.BLOCKS, 0.3F, getState(cbTileEntity) == STATE_DEPRESSED ? 0.5F : 0.6F);
		        }
        	}
        }
        cbTileEntity.setCbMetadata(temp);
    }

    /**
     * Returns polarity.
     */
    private int getPolarity(CbTileEntity cbTileEntity) {
        return (getData() & 0x10) >> 4;
    }

    /**
     * Sets polarity.
     */
    private void setPolarity(CbTileEntity cbTileEntity, int polarity) {
        int temp = (getData() & ~0x10) | (polarity << 4);
        cbTileEntity.setCbMetadata(temp);
    }

    /**
     * Returns trigger entity.
     */
    public int getTriggerEntity(CbTileEntity cbTileEntity) {
        return (cbTileEntity.getCbMetadata() & 0x60) >> 5;
    }

    /**
     * Sets trigger entity.
     */
    public void setTriggerEntity(CbTileEntity cbTileEntity, int trigger) {
        int temp = (cbTileEntity.getCbMetadata() & ~0x60) | (trigger << 5);
        cbTileEntity.setCbMetadata(temp);
    }
    
    public boolean isDepressed(CbTileEntity cbTileEntity) {
    	return getState(cbTileEntity) == STATE_DEPRESSED;
    }
    
    public boolean isEmitting(CbTileEntity cbTileEntity) {
    	return POLARITY_POS == getPolarity(cbTileEntity) && STATE_DEPRESSED == getState(cbTileEntity) ||
    			POLARITY_NEG == getPolarity(cbTileEntity) && STATE_NORMAL == getState(cbTileEntity);
    }
    
    public void togglePolarity(CbTileEntity cbTileEntity) {
    	if (isPositivePolarity(cbTileEntity)) {
    		setPolarity(cbTileEntity, POLARITY_NEG);
    	} else {
    		setPolarity(cbTileEntity, POLARITY_POS);
    	}
    }

    public boolean isPositivePolarity(CbTileEntity cbTileEntity) {
    	return getPolarity(cbTileEntity) == POLARITY_POS;
    }
    
    public void setNextTriggerType(CbTileEntity cbTileEntity) {
    	int trigger = getTriggerEntity(cbTileEntity);
        if (++trigger > 3) {
            trigger = 0;
        }
        setTriggerEntity(cbTileEntity, trigger);
    }
    
    public AbstractState getCbState(CbTileEntity cbTileEntity) {
    	return new PressurePlateState(cbTileEntity);
    }
    
}
