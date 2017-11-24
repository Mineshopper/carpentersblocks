package com.carpentersblocks.util.block;

import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.attribute.EnumAttributeLocation;
import com.carpentersblocks.util.attribute.EnumAttributeType;
import com.carpentersblocks.util.states.factory.AbstractState;
import com.carpentersblocks.util.states.factory.PressurePlateState;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class PressurePlateUtil extends DataUtil implements IDataFacing {

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

    public PressurePlateUtil(CbTileEntity cbTileEntity) {
    	super(cbTileEntity.getData());
    }
    
    public PressurePlateUtil(int cbMetadata) {
    	super(cbMetadata);
    }
    
	@Override
	public boolean setFacing(CbTileEntity cbTileEntity, EnumFacing facing) {
        int temp = (cbTileEntity.getData() & ~0x7) | facing.ordinal();
        return cbTileEntity.setData(temp);
	}

	@Override
	public EnumFacing getFacing() {
		return EnumFacing.getFront(getData() & 0x7);
	}

    /**
     * Returns state.
     */
    public int getState(CbTileEntity cbTileEntity) {
        return (cbTileEntity.getData() & 0x8) >> 3;
    }

    /**
     * Sets state.
     */
    public void setState(CbTileEntity cbTileEntity, int state, boolean playSound) {
        int temp = (cbTileEntity.getData() & ~0x8) | (state << 3);
        World world = cbTileEntity.getWorld();
        if (!world.isRemote) {
        	IBlockState blockState = BlockUtil.getAttributeBlockState(cbTileEntity.getAttributeHelper(), EnumAttributeLocation.HOST, EnumAttributeType.COVER);
        	if (blockState != null) {
	        	if (!Material.CLOTH.equals(blockState.getMaterial()) && playSound && getState(cbTileEntity) != state) {
		            world.playSound(null, cbTileEntity.getPos().add(0.05D, 0.5D, 0.5D), Blocks.WOODEN_BUTTON.getSoundType().getBreakSound(), SoundCategory.BLOCKS, 0.3F, getState(cbTileEntity) == STATE_DEPRESSED ? 0.5F : 0.6F);
		        }
        	}
        }
        cbTileEntity.setData(temp);
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
        cbTileEntity.setData(temp);
    }

    /**
     * Returns trigger entity.
     */
    public int getTriggerEntity(CbTileEntity cbTileEntity) {
        return (cbTileEntity.getData() & 0x60) >> 5;
    }

    /**
     * Sets trigger entity.
     */
    public void setTriggerEntity(CbTileEntity cbTileEntity, int trigger) {
        int temp = (cbTileEntity.getData() & ~0x60) | (trigger << 5);
        cbTileEntity.setData(temp);
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
