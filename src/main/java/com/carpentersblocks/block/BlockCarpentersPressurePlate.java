package com.carpentersblocks.block;

import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.util.metadata.PressurePlateMetadata;
import com.carpentersblocks.util.states.StateConstants;
import com.carpentersblocks.util.states.StateMap;

import net.minecraft.entity.Entity;

public class BlockCarpentersPressurePlate extends BlockFacing implements IStateImplementor {
	
	private static final String PROP_FULL_BOUNDS = "FULL_BOUNDS";
	
	private static StateMap _stateMap;
	
    public BlockCarpentersPressurePlate(Properties properties, StateMap stateMap) {
        super(properties);
        _stateMap = stateMap;
    }
    
    /**
     * Alters polarity.
     *
    @Override
    protected boolean onHammerLeftClick(CbTileEntity cbTileEntity, PlayerEntity playerEntity) {
    	PressurePlateMetadata util = new PressurePlateMetadata(cbTileEntity);
        util.togglePolarity(cbTileEntity);
        notifyBlocksOfPowerChange(cbTileEntity.getWorld(), getDefaultState(),cbTileEntity.getPos());
        if (util.isPositivePolarity(cbTileEntity)) {
        	ChatHandler.sendMessageToPlayer("message.polarity_neg.name", playerEntity);
        } else {
        	ChatHandler.sendMessageToPlayer("message.polarity_pos.name", playerEntity);
        }
        return true;
    }

    /**
     * Alters trigger behavior.
     *
    @Override
    protected boolean onHammerRightClick(CbTileEntity cbTileEntity, PlayerEntity playerEntity) {
    	PressurePlateMetadata util = new PressurePlateMetadata(cbTileEntity);
    	util.setNextTriggerType(cbTileEntity);
        notifyBlocksOfPowerChange(cbTileEntity.getWorld(), getDefaultState(), cbTileEntity.getPos());
        switch (util.getTriggerEntity(cbTileEntity)) {
	        case TRIGGER_ALL:
	        	ChatHandler.sendMessageToPlayer("message.trigger_all.name", playerEntity);
	        	break;
	        case TRIGGER_ANIMAL:
	        	ChatHandler.sendMessageToPlayer("message.trigger_animal.name", playerEntity);
	        	break;
	        case TRIGGER_MONSTER:
	        	ChatHandler.sendMessageToPlayer("message.trigger_monster.name", playerEntity);
	        	break;
	        case TRIGGER_PLAYER:
	        	ChatHandler.sendMessageToPlayer("message.trigger_player.name", playerEntity);
	        	break;
        }
        return true;
    }
    
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     *
    @Override
    public AxisAlignedBB getBoundingBox(BlockState blockState, IWorld blockAccess, BlockPos blockPos) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
        	if (Boolean.TRUE.equals(cbTileEntity.getProperty(PROP_FULL_BOUNDS))) {
        		return FULL_BLOCK_AABB;
        	} else {
        		return StateFactory.getState(cbTileEntity).getBoundingBox();
        	}
        }
        return super.getBoundingBox(blockState, blockAccess, blockPos);
    }
    
    @Deprecated
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IWorld blockAccess, BlockPos blockPos) {
    	return NULL_AABB;
    }
    
    /**
     * Ticks the block if it's been scheduled
     *
    @Override
    public void updateTick(World world, BlockPos blockPos, BlockState blockState, Random random) {
        if (!world.isRemote) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
            	PressurePlateMetadata util = new PressurePlateMetadata(cbTileEntity);
            	boolean depressed = false;
                if (util.isDepressed(cbTileEntity)) {
                    depressed = hasTriggerInBounds(cbTileEntity);
                }
                if (depressed) {
                    world.scheduleBlockUpdate(blockPos, this, tickRate(world), 0);
                } else if (util.isDepressed(cbTileEntity)) {
        	    	util.setState(cbTileEntity, util.STATE_NORMAL, true);
        	        notifyBlocksOfPowerChange(cbTileEntity.getWorld(), getDefaultState(), cbTileEntity.getPos());
                }
            }
        }
        super.updateTick(world, blockPos, blockState, random);
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     *
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (!world.isRemote) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
            	if (canEntityTrigger(cbTileEntity, entity)) {
                	PressurePlateMetadata util = new PressurePlateMetadata(cbTileEntity);
                	if (!util.isDepressed(cbTileEntity)) {
                    	util.setState(cbTileEntity, util.STATE_DEPRESSED, true);
                        notifyBlocksOfPowerChange(cbTileEntity.getWorld(), getDefaultState(), cbTileEntity.getPos());
                	}
                    cbTileEntity.getWorld().scheduleBlockUpdate(cbTileEntity.getPos(), this, 0, 0);
                }
            }
        }
    }

    /**
     * Returns whether sensitive area contains an entity that can
     * trigger a state change.
     *
     * @param  cbTileEntity the {@link CbTileEntity}
     * @return whether sensitive area contains valid {@link Entity}
     *
    private boolean hasTriggerInBounds(CbTileEntity cbTileEntity) {
    	cbTileEntity.setProperty(PROP_FULL_BOUNDS, Boolean.TRUE);
        AxisAlignedBB globalAABB = getGlobalBoundingBox(getDefaultState(), cbTileEntity.getWorld(), cbTileEntity.getPos());
        List entityList = cbTileEntity.getWorld().getEntitiesWithinAABB(Entity.class, globalAABB);
        cbTileEntity.removeProperty(PROP_FULL_BOUNDS);
        if (!entityList.isEmpty()) {
            for (Entity entity : (List<Entity>) entityList) {
                if (canEntityTrigger(cbTileEntity, entity)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     *
    @Override
    public boolean canProvidePower(BlockState blockState) {
        return true;
    }

    /**
     * Returns power level (0 or 15)
     *
    @Override
    public int getPowerOutput(CbTileEntity cbTileEntity) {
    	PressurePlateMetadata util = new PressurePlateMetadata(cbTileEntity);
    	return util.isEmitting(cbTileEntity) ? 15 : 0;
    }

    /**
     * Returns whether pressure plate should trigger based on entity colliding with it.
     *
    private boolean canEntityTrigger(CbTileEntity cbTileEntity, Entity entity) {
        if (entity == null) {
            return false;
        }
        PressurePlateMetadata util = new PressurePlateMetadata(cbTileEntity);
        switch (util.getTriggerEntity(cbTileEntity)) {
	        case TRIGGER_ANIMAL:
	        	return entity.isCreatureType(EnumCreatureType.CREATURE, false);
	        case TRIGGER_MONSTER:
	        	return entity.isCreatureType(EnumCreatureType.MONSTER, false);
	        case TRIGGER_PLAYER:
	        	return entity instanceof PlayerEntity;
	        default: // TRIGGER_ALL
	        	return true;
	    }
    }

	@Override
	public void setFacing(CbTileEntity cbTileEntity, Direction facing) {
		PressurePlateMetadata util = new PressurePlateMetadata(cbTileEntity);
		util.setFacing(cbTileEntity, facing);
	}

	@Override
	public Direction getFacing(CbTileEntity cbTileEntity) {
		PressurePlateMetadata util = new PressurePlateMetadata(cbTileEntity);
		return util.getFacing();
	}
	
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     *
	@Override
    public boolean isOpaqueCube(BlockState blockState) {
        return false;
    }

	@Override
    public boolean isFullCube(BlockState blockState) {
        return false;
    }

	@Override
    public boolean isPassable(IWorld blockAccess, BlockPos blockPos) {
        return true;
	}

    /**
     * Return true if an entity can be spawned inside the block (used to get the player's bed spawn location)
     *
	@Override
    public boolean canSpawnInBlock() {
        return true;
    }*/

	@Override
	public StateMap getStateMap() {
		return _stateMap;
	}

	@Override
	public String getStateDescriptor(CbTileEntity cbTileEntity) {
		PressurePlateMetadata util = new PressurePlateMetadata(cbTileEntity);
		if (util.isDepressed(cbTileEntity)) {
			return StateConstants.PRESSURE_PLATE_DEPRESSED_STATE;
		}
		return StateConstants.DEFAULT_STATE;
	}
	
}
