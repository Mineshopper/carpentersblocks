package com.carpentersblocks.block;

import javax.annotation.Nullable;

import com.carpentersblocks.block.data.SlopeData;
import com.carpentersblocks.config.Configuration;
import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.util.EntityLivingUtil;
import com.carpentersblocks.util.RotationUtil.CbRotation;
import com.carpentersblocks.util.handler.EventHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCarpentersSlope extends AbstractWaterLoggableBlock {
	
    public BlockCarpentersSlope(Properties properties) {
        super(properties);
    }
    
    @Override
    /**
     * Alters block direction.
     */
    protected boolean onHammerLeftClick(CbTileEntity cbTileEntity, PlayerEntity playerEntity) {
        //SlopeData.rotate(cbTileEntity, EventHandler.getRayTraceResult().getDirection().getAxis());
        return true;
    }

    @Override
    /**
     * Alters block type.
     */
    protected boolean onHammerRightClick(CbTileEntity cbTileEntity, PlayerEntity playerEntity) {
    	SlopeData.setNextType(cbTileEntity);
        return true;        
    }

    @Override
    protected void damageItemWithChance(World world, PlayerEntity playerEntity, Hand hand) {
    	if (world.random.nextFloat() <= Configuration.getItemHammerDamageChanceFromSlopes()) {
            super.damageItemWithChance(world, playerEntity, hand);
        }
    }
    
    /**
     * Do not render ambient occlusion shadows around block.
     * <p>
     * This has the effect of darkening quads when block is not a cube.
     * 
     * @param blockState the block state
     * @param blockReader the block reader
     * @param blockPos the block position
     * @return a value between 0f and 1f
     */
	//@OnlyIn(Dist.CLIENT)
	//@Override
	//public float getAmbientOcclusionLightValue(BlockState blockState, IBlockReader blockReader, BlockPos blockPos) {
		// TODO: experiment with returning state-based values
    //	return 1.0F;
	//}

/*    @Override
    *//**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     *//*
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, BlockPos blockPos) {
        if (!rayTracing) {

            CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);

            if (cbTileEntity != null) {

                Slope slope = Slope.getSlope(cbTileEntity);

                switch (slope.getPrimaryType()) {
                    case PRISM:
                    case PRISM_1P:
                    case PRISM_2P:
                    case PRISM_3P:
                    case PRISM_4P:
                        if (slope.isPositive) {
                            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
                        } else {
                            setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
                        }
                        break;
                    default:
                        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                        break;
                }

            }

        }
    }*/

    /*@Override
    *//**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     *//*
    public MovingObjectPosition collisionRayTrace(World world, BlockPos blockPos, Vec3 startVec, Vec3 endVec) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        MovingObjectPosition finalTrace = null;

        if (cbTileEntity != null) {

            Slope slope = Slope.getSlope(cbTileEntity);
            SlopeUtil slopeUtil = new SlopeUtil();

            int numPasses = slopeUtil.getNumPasses(slope);
            int precision = slopeUtil.getNumBoxesPerPass(slope);

            rayTracing = true;

             Determine if ray trace is a hit on slope. 
            for (int pass = 0; pass < numPasses; ++pass)
            {
                for (int slice = 0; slice < precision && finalTrace == null; ++slice)
                {
                    float[] box = slopeUtil.genBounds(slope, slice, precision, pass);

                    if (box != null) {
                        setBlockBounds(box[0], box[1], box[2], box[3], box[4], box[5]);
                        finalTrace = super.collisionRayTrace(world, x, y, z, startVec, endVec);
                    }
                }
                if (slope.type.equals(Type.OBLIQUE_EXT)) {
                    --precision;
                }
            }

            rayTracing = false;

             Determine true face hit since sloped faces are two or more shared faces. 

            if (finalTrace != null) {
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                finalTrace = super.collisionRayTrace(world, x, y, z, startVec, endVec);
            }

        }

        return finalTrace;
    }*/

    /*@Override
    *//**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     *//*
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
        CbTileEntity cbTileEntity = getTileEntity(world, x, y, z);

        if (cbTileEntity != null) {

            AxisAlignedBB box = null;

            Slope slope = Slope.getSlope(cbTileEntity);
            SlopeUtil slopeUtil = new SlopeUtil();

            int precision = slopeUtil.getNumBoxesPerPass(slope);
            int numPasses = slopeUtil.getNumPasses(slope);

            for (int pass = 0; pass < numPasses; ++pass) {

                for (int slice = 0; slice < precision; ++slice)
                {
                    float[] dim = slopeUtil.genBounds(slope, slice, precision, pass);

                    if (dim != null) {
                        box = AxisAlignedBB.getBoundingBox(x + dim[0], y + dim[1], z + dim[2], x + dim[3], y + dim[4], z + dim[5]);
                    }

                    if (box != null && axisAlignedBB.intersectsWith(box)) {
                        list.add(box);
                    }
                }

                if (slope.type.equals(Type.OBLIQUE_EXT)) {
                    --precision;
                }

            }

        }
    }*/
    
/*    @Override
    *//**
     * Checks if the block is a solid face on the given side, used by placement logic.
     *//*
    public boolean isSideSolid(IBlockAccess blockAccess, BlockPos blockPos, Direction side) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
            if (isBlockSolid(blockAccess, blockPos)) {
                return Slope.getSlope(cbTileEntity).isFaceFull(side);
            }
        }
        return false;
    }*/
    
    @Override
    public void setPlacedBy(World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
    	super.setPlacedBy(world, blockPos, blockState, livingEntity, itemStack);
    	
    	CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
    	if (cbTileEntity == null) {
    		return;
    	}

    	// Set type
    	switch (itemStack.getItem().getRegistryName().toString()) {
        	case CbBlocks.REGISTRY_NAME_SLOPE_INVERTED_PRISM:
        		SlopeData.setType(cbTileEntity, SlopeData.Type.INVERTED_PRISM);
        		break;
        	case CbBlocks.REGISTRY_NAME_SLOPE_OBLIQUE_EXTERIOR:
        		SlopeData.setType(cbTileEntity, SlopeData.Type.OBLIQUE_EXTERIOR);
        		break;
        	case CbBlocks.REGISTRY_NAME_SLOPE_OBLIQUE_INTERIOR:
        		SlopeData.setType(cbTileEntity, SlopeData.Type.OBLIQUE_INTERIOR);
        		break;
        	case CbBlocks.REGISTRY_NAME_SLOPE_PRISM:
        		SlopeData.setType(cbTileEntity, SlopeData.Type.PRISM);
        		break;
        	case CbBlocks.REGISTRY_NAME_SLOPE_PRISM_WEDGE:
        		SlopeData.setType(cbTileEntity, SlopeData.Type.PRISM_WEDGE);
        		break;
        	case CbBlocks.REGISTRY_NAME_SLOPE_WEDGE:
        		SlopeData.setType(cbTileEntity, SlopeData.Type.WEDGE);
        		break;
        	case CbBlocks.REGISTRY_NAME_SLOPE_WEDGE_EXTERIOR:
        		SlopeData.setType(cbTileEntity, SlopeData.Type.WEDGE_EXTERIOR);
        		break;
        	case CbBlocks.REGISTRY_NAME_SLOPE_WEDGE_INTERIOR:
        		SlopeData.setType(cbTileEntity, SlopeData.Type.WEDGE_INTERIOR);
        		break;
    	}
    	
    	if (livingEntity == null) {
    		return;
    	}
    	
    	
    	////////////// DEBUG
    	
    	BlockRayTraceResult blockRayTraceResult = EntityLivingUtil.calculateBlockRayTraceResult(livingEntity);
    	
    	///////////// END DEBUG
    	
    	
    	// Set rotation
    	//BlockRayTraceResult blockRayTraceResult = EventHandler.getRayTraceResult();
    	double x = blockRayTraceResult.getLocation().x() - (int) blockRayTraceResult.getLocation().x();
    	if (x < 0.0d) {
    		x = 1 - (x * -1);
    	}
    	double y = blockRayTraceResult.getLocation().y() - (int) blockRayTraceResult.getLocation().y();
    	double z = blockRayTraceResult.getLocation().z() - (int) blockRayTraceResult.getLocation().z();
    	if (z < 0.0d) {
    		z = 1 - (z * -1);
    	}
    	Direction direction = blockRayTraceResult.getDirection();
    	CbRotation rotation = CbRotation.X0_Y0_Z0;
    	switch (direction) {
        	case DOWN:
        		if (x >= 0.2f && x <= 0.8f && z >= 0.2f && z <= 0.8f) {
        			switch (livingEntity.getDirection()) {
	        			case NORTH:
	        				rotation = CbRotation.X0_Y0_Z180;
	        				break;
	        			case SOUTH:
	        				rotation = CbRotation.X180_Y0_Z0;
	        				break;
	        			case WEST:
	        				rotation = CbRotation.X180_Y90_Z0;
	        				break;
	        			case EAST:
	        				rotation = CbRotation.X180_Y270_Z0;
	        				break;
						default:
							break;
        			}
        		} else if (1.0f - x > z && x <= 0.5f && x < z) {
        			rotation = CbRotation.X180_Y90_Z0;
    			} else if (z > x && z >= 0.5f) {
    				rotation = CbRotation.X180_Y0_Z0;
    			} else if (x > 1.0f - z) {
    				rotation = CbRotation.X180_Y270_Z0;
    			} else {
    				rotation = CbRotation.X0_Y0_Z180;
    			}
        		break;
        	case UP:
        		if (x >= 0.2f && x <= 0.8f && z >= 0.2f && z <= 0.8f) {
        			switch (livingEntity.getDirection()) {
	        			case NORTH:
	        				rotation = CbRotation.X0_Y0_Z0;
	        				break;
	        			case SOUTH:
	        				rotation = CbRotation.X0_Y180_Z0;
	        				break;
	        			case WEST:
	        				rotation = CbRotation.X0_Y270_Z0;
	        				break;
	        			case EAST:
	        				rotation = CbRotation.X0_Y90_Z0;
	        				break;
						default:
							break;
        			}
        		} else if (x < z && x <= 0.5f && 1.0f - x > z) {
        			rotation = CbRotation.X0_Y270_Z0;
        		} else if (1.0f - z > x && z <= 0.5f) {
        			rotation = CbRotation.X0_Y0_Z0;
        		} else if (x > z) {
        			rotation = CbRotation.X0_Y90_Z0;
        		} else {
        			rotation = CbRotation.X0_Y180_Z0;
        		}
        		break;
        	case NORTH:
        		if (x >= 0.2f && x <= 0.8f) {
        			if (y >= 0.5f) {
        				rotation = CbRotation.X90_Y0_Z180;
        			} else {
        				rotation = CbRotation.X90_Y0_Z0;
        			}
        		} else if (x > y && x >= 0.5f && x > 1.0f - y) {
        			rotation = CbRotation.X90_Y0_Z270;
        		} else if (y > 1.0f - x && y >= 0.5f) {
        			rotation = CbRotation.X90_Y0_Z180;
        		} else if (x < y) {
        			rotation = CbRotation.X90_Y0_Z90;
        		} else {
        			rotation = CbRotation.X90_Y0_Z0;
        		}
        		break;
        	case SOUTH:
        		if (x >= 0.2f && x <= 0.8f) {
        			if (y >= 0.5f) {
        				rotation = CbRotation.X270_Y0_Z0;
        			} else {
        				rotation = CbRotation.X90_Y180_Z0;
        			}
        		} else if (1.0f - x > y && x <= 0.5f && x < y) {
        			rotation = CbRotation.X90_Y180_Z90;
        		} else if (y > x && y >= 0.5f) {
        			rotation = CbRotation.X270_Y0_Z0;
        		} else if (x > 1.0f - y) {
        			rotation = CbRotation.X270_Y0_Z90;
        		} else {
        			rotation = CbRotation.X90_Y180_Z0;
        		}
        		break;
        	case WEST:
        		if (z >= 0.2f && z <= 0.8f) {
        			if (y >= 0.5f) {
        				rotation = CbRotation.X270_Y90_Z0;
        			} else {
        				rotation = CbRotation.X90_Y270_Z0;
        			}
        		} else if (1.0f - z > y && z <= 0.5f && z < y) {
        			rotation = CbRotation.X0_Y0_Z270;
        		} else if (y > z && y >= 0.5f) {
        			rotation = CbRotation.X270_Y90_Z0;
        		} else if (z > 1.0f - y) {
        			rotation = CbRotation.X180_Y0_Z90;
        		} else {
        			rotation = CbRotation.X90_Y270_Z0;
        		}
        		break;
        	case EAST:
        		if (z >= 0.2f && z <= 0.8f) {
        			if (y >= 0.5f) {
        				rotation = CbRotation.X0_Y270_Z90;
        			} else {
        				rotation = CbRotation.X90_Y90_Z0;
        			}
        		} else if (z > y && z >= 0.5f && z > 1.0f - y) {
        			rotation = CbRotation.X0_Y180_Z90;
        		} else if (y > 1.0f - z && y >= 0.5f) {
        			rotation = CbRotation.X0_Y270_Z90;
        		} else if (z < y) {
        			rotation = CbRotation.X0_Y0_Z90;
        		} else {
        			rotation = CbRotation.X90_Y90_Z0;
        		}
        		break;
    	}
    	SlopeData.setRotation(cbTileEntity, rotation);
    }
    
    /**
     * We want to reduce the shape by just a fraction so that
     * occlusion doesn't darken the interior faces of slopes.
     */
    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
    		ISelectionContext p_220053_4_) {
    	return Block.box(0.0001D, 0.0001D, 0.0001D, 15.9999D, 15.9999D, 15.9999D);
    }

}
