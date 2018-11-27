package com.carpentersblocks.block;

import com.carpentersblocks.block.data.SlopeData;
import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.RotationUtil.Rotation;
import com.carpentersblocks.util.handler.EventHandler;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCarpentersSlope extends BlockCoverable {
	
    public final static String slopeType[] = {
    	"wedge",
    	"wedgeInterior",
    	"wedgeExterior",
    	"obliqueInterior",
    	"obliqueExterior",
    	"prismWedge",
    	"prism",
    	"invertPrism" };
    
    private boolean rayTracing;

    public BlockCarpentersSlope(Material material) {
        super(material);
    }

    @Override
    /**
     * Alters block direction.
     */
    protected boolean onHammerLeftClick(CbTileEntity cbTileEntity, EntityPlayer entityPlayer) {
    	return rotateBlock(cbTileEntity.getWorld(), cbTileEntity.getPos(), EventHandler.eventFace);
    }

    @Override
    /**
     * Alters block type.
     */
    protected boolean onHammerRightClick(CbTileEntity cbTileEntity, EntityPlayer entityPlayer) {
    	new SlopeData().setNextType(cbTileEntity);
        return true;        
    }

/*    @Override
    *//**
     * Damages hammer with a chance to not damage.
     *//*
    protected void damageItemWithChance(World world, EntityPlayer entityPlayer) {
        if (world.rand.nextFloat() <= ItemRegistry.itemHammerDamageChanceFromSlopes) {
            super.damageItemWithChance(world, entityPlayer);
        }
    }*/

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
    public boolean isSideSolid(IBlockAccess blockAccess, BlockPos blockPos, EnumFacing side) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
            if (isBlockSolid(blockAccess, blockPos)) {
                return Slope.getSlope(cbTileEntity).isFaceFull(side);
            }
        }
        return false;
    }*/

    @Override
    /**
     * Returns whether sides share faces based on sloping property and face shape.
     */
    protected boolean shareFaces(CbTileEntity cbTileEntity_adj, CbTileEntity cbTileEntity_src, EnumFacing side_adj, EnumFacing side_src) {
/*        if (cbTileEntity_adj.getBlockType() == this) {
            Slope slope_src = Slope.getSlope(cbTileEntity_src);
            Slope slope_adj = Slope.getSlope(cbTileEntity_adj);
            if (!slope_adj.hasSide(side_adj)) {
                return false;
            } else if (slope_src.getFaceBias(side_src) == slope_adj.getFaceBias(side_adj)) {
                return true;
            } else {
                return false;
            }
        }
        return super.shareFaces(cbTileEntity_adj, cbTileEntity_src, side_adj, side_src);*/
    	return false;
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    	// IUnlistedProperty values are not retained between this method and onBlockPlacedBy(). Store with thread temporarily.
    	_threadLocalHitCoords.set(new Float[] { hitX, hitY, hitZ });
    	_threadLocalFacing.set(facing);
    	return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
    	super.onBlockPlacedBy(world, blockPos, blockState, entityLivingBase, itemStack);
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {

        	// Set type
        	switch (itemStack.getItemDamage()) {
	        	case 0:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.WEDGE);
	        		break;
	        	case 1:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.WEDGE_INTERIOR);
	        		break;
	        	case 2:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.WEDGE_EXTERIOR);
	        		break;
	        	case 3:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.OBLIQUE_INTERIOR);
	        		break;
	        	case 4:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.OBLIQUE_EXTERIOR);
	        		break;
	        	case 5:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.PRISM_WEDGE);
	        		break;
	        	case 6:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.PRISM);
	        		break;
        		default: // 7
        			SlopeData.setType(cbTileEntity, SlopeData.Type.INVERT_PRISM);
        			break;
        	}
        	
        	// Set rotation
        	float x = _threadLocalHitCoords.get()[0];
        	float y = _threadLocalHitCoords.get()[1];
        	float z = _threadLocalHitCoords.get()[2];
        	_threadLocalHitCoords.remove();
        	EnumFacing facing = _threadLocalFacing.get();
        	_threadLocalFacing.remove();
        	Rotation rotation = Rotation.X0_Y0_Z0;
        	EnumFacing cardinalFacing = entityLivingBase.getHorizontalFacing().getOpposite();
        	switch (facing) {
	        	case DOWN:
	        		if (x >= 0.2f && x <= 0.8f && z >= 0.2f && z <= 0.8f) {
	        			switch (entityLivingBase.getHorizontalFacing()) {
		        			case NORTH:
		        				rotation = Rotation.X0_Y0_Z180;
		        				break;
		        			case SOUTH:
		        				rotation = Rotation.X180_Y0_Z0;
		        				break;
		        			case WEST:
		        				rotation = Rotation.X180_Y90_Z0;
		        				break;
		        			case EAST:
		        				rotation = Rotation.X180_Y270_Z0;
		        				break;
							default:
								break;
	        			}
	        		} else if (1.0f - x > z && x <= 0.5f && x < z) {
	        			rotation = Rotation.X180_Y90_Z0;
        			} else if (z > x && z >= 0.5f) {
        				rotation = Rotation.X180_Y0_Z0;
        			} else if (x > 1.0f - z) {
        				rotation = Rotation.X180_Y270_Z0;
        			} else {
        				rotation = Rotation.X0_Y0_Z180;
        			}
	        		break;
	        	case UP:
	        		if (x >= 0.2f && x <= 0.8f && z >= 0.2f && z <= 0.8f) {
	        			switch (entityLivingBase.getHorizontalFacing()) {
		        			case NORTH:
		        				rotation = Rotation.X0_Y0_Z0;
		        				break;
		        			case SOUTH:
		        				rotation = Rotation.X0_Y180_Z0;
		        				break;
		        			case WEST:
		        				rotation = Rotation.X0_Y270_Z0;
		        				break;
		        			case EAST:
		        				rotation = Rotation.X0_Y90_Z0;
		        				break;
							default:
								break;
	        			}
	        		} else if (x < z && x <= 0.5f && 1.0f - x > z) {
	        			rotation = Rotation.X0_Y270_Z0;
	        		} else if (1.0f - z > x && z <= 0.5f) {
	        			rotation = Rotation.X0_Y0_Z0;
	        		} else if (x > z) {
	        			rotation = Rotation.X0_Y90_Z0;
	        		} else {
	        			rotation = Rotation.X0_Y180_Z0;
	        		}
	        		break;
	        	case NORTH:
	        		if (x >= 0.2f && x <= 0.8f) {
	        			if (y >= 0.5f) {
	        				rotation = Rotation.X90_Y0_Z180;
	        			} else {
	        				rotation = Rotation.X90_Y0_Z0;
	        			}
	        		} else if (x > y && x >= 0.5f && x > 1.0f - y) {
	        			rotation = Rotation.X90_Y0_Z270;
	        		} else if (y > 1.0f - x && y >= 0.5f) {
	        			rotation = Rotation.X90_Y0_Z180;
	        		} else if (x < y) {
	        			rotation = Rotation.X90_Y0_Z90;
	        		} else {
	        			rotation = Rotation.X90_Y0_Z0;
	        		}
	        		break;
	        	case SOUTH:
	        		if (x >= 0.2f && x <= 0.8f) {
	        			if (y >= 0.5f) {
	        				rotation = Rotation.X270_Y0_Z0;
	        			} else {
	        				rotation = Rotation.X90_Y180_Z0;
	        			}
	        		} else if (1.0f - x > y && x <= 0.5f && x < y) {
	        			rotation = Rotation.X90_Y180_Z90;
	        		} else if (y > x && y >= 0.5f) {
	        			rotation = Rotation.X270_Y0_Z0;
	        		} else if (x > 1.0f - y) {
	        			rotation = Rotation.X270_Y0_Z90;
	        		} else {
	        			rotation = Rotation.X90_Y180_Z0;
	        		}
	        		break;
	        	case WEST:
	        		if (z >= 0.2f && z <= 0.8f) {
	        			if (y >= 0.5f) {
	        				rotation = Rotation.X270_Y90_Z0;
	        			} else {
	        				rotation = Rotation.X90_Y270_Z0;
	        			}
	        		} else if (1.0f - z > y && z <= 0.5f && z < y) {
	        			rotation = Rotation.X0_Y0_Z270;
	        		} else if (y > z && y >= 0.5f) {
	        			rotation = Rotation.X270_Y90_Z0;
	        		} else if (z > 1.0f - y) {
	        			rotation = Rotation.X180_Y0_Z90;
	        		} else {
	        			rotation = Rotation.X90_Y270_Z0;
	        		}
	        		break;
	        	case EAST:
	        		if (z >= 0.2f && z <= 0.8f) {
	        			if (y >= 0.5f) {
	        				rotation = Rotation.X0_Y270_Z90;
	        			} else {
	        				rotation = Rotation.X90_Y90_Z0;
	        			}
	        		} else if (z > y && z >= 0.5f && z > 1.0f - y) {
	        			rotation = Rotation.X0_Y180_Z90;
	        		} else if (y > 1.0f - z && y >= 0.5f) {
	        			rotation = Rotation.X0_Y270_Z90;
	        		} else if (z < y) {
	        			rotation = Rotation.X0_Y0_Z90;
	        		} else {
	        			rotation = Rotation.X90_Y90_Z0;
	        		}
	        		break;
        	}
        	SlopeData.setRotation(cbTileEntity, rotation);
        }
    }

    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos blockPos) {
        return EnumFacing.VALUES;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos blockPos, EnumFacing facing) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {
        	SlopeData.rotate(cbTileEntity, facing.getAxis());
        	return true;
        }
        return false;
    }
    
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
	@Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }
    
    /**
     * Check if the face of a block should block rendering.
     *
     * Faces which are fully opaque should return true, faces with transparency
     * or faces which do not span the full size of the block should return false.
     *
     * @param state The current block state
     * @param world The current world
     * @param pos Block position in world
     * @param face The side to check
     * @return True if the block is opaque on the specified side.
     */
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

}
