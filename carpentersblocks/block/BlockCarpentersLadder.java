package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.data.Ladder;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;

public class BlockCarpentersLadder extends BlockCoverable {
    
    public BlockCarpentersLadder(Material material)
    {
        super(material);
    }
    
    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = (TEBase) world.getTileEntity(x, y, z);
        int data = BlockProperties.getMetadata(TE);
        
        switch (data) {
            case Ladder.FACING_NORTH: // Ladder on +Z
                setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
                break;
            case Ladder.FACING_SOUTH: // Ladder on -Z
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
                break;
            case Ladder.FACING_WEST: // Ladder on +X
                setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;
            case Ladder.FACING_EAST: // Ladder on -X
                setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
                break;
            case Ladder.FACING_ON_X:
                setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
                break;
            default: // Ladder.FACING_ON_Z
                setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
                break;
        }
    }
    
    @Override
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
    {
        setBlockBoundsBasedOnState(world, x, y, z);
        super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
    }
    
    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        Block block;
        
        switch (ForgeDirection.getOrientation(side)) {
            case DOWN:
                
                block = world.getBlock(x, y + 1, z);
                
                if (block != null) {
                    return block.isSideSolid(world, x, y + 1, z, ForgeDirection.UP) || block.equals(this) && Ladder.isFreestanding((TEBase) world.getTileEntity(x, y + 1, z));
                }
                
            case UP:
                
                block = world.getBlock(x, y - 1, z);
                
                if (block != null) {
                    return block.isSideSolid(world, x, y - 1, z, ForgeDirection.DOWN) || block.equals(this) && Ladder.isFreestanding((TEBase) world.getTileEntity(x, y - 1, z));
                }
                
            case NORTH:
                
                block = world.getBlock(x, y, z + 1);
                
                if (block != null) {
                    return block.isSideSolid(world, x, y, z + 1, ForgeDirection.SOUTH);
                }
                
            case SOUTH:
                
                block = world.getBlock(x, y, z - 1);
                
                if (block != null) {
                    return block.isSideSolid(world, x, y, z - 1, ForgeDirection.NORTH);
                }
                
            case WEST:
                
                block = world.getBlock(x + 1, y, z);
                
                if (block != null) {
                    return block.isSideSolid(world, x + 1, y, z, ForgeDirection.EAST);
                }
                
            case EAST:
                
                block = world.getBlock(x - 1, y, z);
                
                if (block != null) {
                    return block.isSideSolid(world, x - 1, y, z, ForgeDirection.WEST);
                }
                
            default:
                return false;
        }
    }
    
    @Override
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int initData)
    {
        return side;
    }
    
    @Override
    /**
     * Called when the block is placed in the world.
     * Uses cardinal direction to adjust metadata if player clicks top or bottom face of block.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = (TEBase) world.getTileEntity(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        
        if (metadata < 2) {
            int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
            BlockProperties.setMetadata(TE, facing % 2 == 0 ? Ladder.FACING_ON_X : Ladder.FACING_ON_Z);
        } else {
            BlockProperties.setMetadata(TE, metadata);
        }
        
        if (!entityLiving.isSneaking())
        {
            TEBase TE_adj = null;
            
            Block block_YN = world.getBlock(x, y - 1, z);
            Block block_YP = world.getBlock(x, y + 1, z);
            
            if (block_YN != null && block_YN.equals(this)) {
                TE_adj = (TEBase) world.getTileEntity(x, y - 1, z);
            } else if (block_YP != null && block_YN.equals(this)) {
                TE_adj = (TEBase) world.getTileEntity(x, y + 1, z);
            }
            
            if (TE_adj != null) {
                BlockProperties.setMetadata(TE, BlockProperties.getMetadata(TE_adj));
            }
        }
        
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }
    
    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (!world.isRemote && TE != null) {
            
            int data = BlockProperties.getMetadata(TE);
            boolean dropBlock = false;
            
            switch (data) {
                case 2:
                    if (!world.getBlock(x, y, z + 1).isSideSolid(world, x, y, z + 1, ForgeDirection.NORTH)) {
                        dropBlock = true;
                    }
                    break;
                case 3:
                    if (!world.getBlock(x, y, z - 1).isSideSolid(world, x, y, z - 1, ForgeDirection.SOUTH)) {
                        dropBlock = true;
                    }
                    break;
                case 4:
                    if (!world.getBlock(x + 1, y, z).isSideSolid(world, x + 1, y, z, ForgeDirection.WEST)) {
                        dropBlock = true;
                    }
                    break;
                case 5:
                    if (!world.getBlock(x - 1, y, z).isSideSolid(world, x - 1, y, z, ForgeDirection.EAST)) {
                        dropBlock = true;
                    }
                    break;
            }
            
            if (dropBlock) {
                dropBlockAsItem(world, x, y, z, data, 0);
                world.setBlockToAir(x, y, z);
            }
            
        }
        
        super.onNeighborBlockChange(world, x, y, z, block);
    }
    
    @Override
    public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entityLiving)
    {
        return true;
    }
    
    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersLadderRenderID;
    }
    
}
