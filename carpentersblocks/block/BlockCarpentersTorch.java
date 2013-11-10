package carpentersblocks.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Torch;
import carpentersblocks.data.Torch.State;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.handler.BlockHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersTorch extends BlockBase
{

	private float currentLightLevel = 1.0F;
	
    public BlockCarpentersTorch(int blockID)
    {
        super(blockID, Material.circuits);
        this.setTickRandomly(true);
		this.setUnlocalizedName("blockCarpentersTorch");
		this.setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		setTextureName("carpentersblocks:torch/torch_lit");
    }
    
	@Override
	/**
	 * Returns light value based on cover or side covers.
	 */
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		Block block = blocksList[world.getBlockId(x, y, z)];

		if (block != null && block.blockID == blockID)
		{
			TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);
			
			int coverLight = super.getLightValue(world, x, y, z);
			int torchLight = 0;
			
			switch (Torch.getState(TE)) {
			case LIT:
				torchLight = 15;
				break;
			case SMOLDERING:
				torchLight = 10;
				break;
			default: {}
			}
			
			return coverLight > torchLight ? coverLight : torchLight;
		}

		return lightValue[blockID];
	}
    
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

	@Override
	/**
	 * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
	 */
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
	{
		if (side > 0)
		{
			ForgeDirection dir = ForgeDirection.getOrientation(side);
			
            int blockID = world.getBlockId(x, y, z);
			boolean canPlaceOnTop = blockID > 0 && Block.blocksList[blockID].canPlaceTorchOnTop(world, x, y, z);
			
			return world.isBlockSolidOnSide(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ, dir) || (side == 1 && canPlaceOnTop);
		}
		
		return false;
	}

	@Override
	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		return side;
	}
	
	@Override
	/**
	 * Called when the block is placed in the world.
	 */
	public void auxiliaryOnBlockPlacedBy(TECarpentersBlock TE, World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		int facing = world.getBlockMetadata(x, y, z);
		
		Torch.setFacing(TE, facing);
		Torch.setReady(TE);
	}
	
	@Override
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	protected void auxiliaryOnNeighborBlockChange(TECarpentersBlock TE, World world, int x, int y, int z, int blockID)
	{
		if (Torch.isReady(TE))
		{
			ForgeDirection facing = Torch.getFacing(TE);
			
			if (!canPlaceBlockOnSide(world, x, y, z, facing.ordinal())) {
				dropBlockAsItem(world, x, y, z, 0, 0);
				world.setBlockToAir(x, y, z);
			}
		}
	}

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    @Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

		ForgeDirection facing = Torch.getFacing(TE);

        switch (facing) {
        case NORTH:
        	this.setBlockBounds(0.5F - 0.15F, 0.2F, 1.0F - 0.15F * 2.0F, 0.5F + 0.15F, 0.8F, 1.0F);
        	break;
        case SOUTH:
        	this.setBlockBounds(0.5F - 0.15F, 0.2F, 0.0F, 0.5F + 0.15F, 0.8F, 0.15F * 2.0F);
        	break;
        case WEST:
        	this.setBlockBounds(1.0F - 0.15F * 2.0F, 0.2F, 0.5F - 0.15F, 1.0F, 0.8F, 0.5F + 0.15F);
        	break;
        case EAST:
        	this.setBlockBounds(0.0F, 0.2F, 0.5F - 0.15F, 0.15F * 2.0F, 0.8F, 0.5F + 0.15F);
        	break;
        default:
            this.setBlockBounds(0.5F - 0.1F, 0.0F, 0.5F - 0.1F, 0.5F + 0.1F, 0.6F, 0.5F + 0.1F);
        	break;
        }

        return super.collisionRayTrace(world, x, y, z, startVec, endVec);
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    @Override
	public void updateTick(World world, int x, int y, int z, Random random)
    {
    	if (!world.isRemote)
    	{
	    	TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);
	
	    	double xOffset = x + 0.5F;
	    	double yOffset = y + 0.7F;
	    	double zOffset = z + 0.5F;
	    	double offset1 = 0.2199999988079071D;
	    	double offset2 = 0.27000001072883606D;
	    	
	    	boolean isWet = world.isRaining() && world.canBlockSeeTheSky(x, y, z);
	    	
	    	switch (Torch.getState(TE))
	    	{
		    	case LIT:
		    		if (isWet) {
		    			Torch.setState(TE, State.SMOLDERING);
		    			world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.fizz", 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
		    			world.spawnParticle("largesmoke", xOffset, yOffset + offset1, zOffset - offset2, 0.0D, 0.0D, 0.0D);
		    		}
		    		break;
		    	case SMOLDERING:
		    		if (isWet) {
		    			Torch.setState(TE, State.UNLIT);
			    		world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.fizz", 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
		    		} else {
			    		Torch.setState(TE, State.LIT);
		    		}
		    		break;
		    	case UNLIT:
		    		if (!isWet) {
		    			Torch.setState(TE, State.SMOLDERING);
		    		}
		    		break;
		    	default: {}
	    	}
    	}
    }

    @Override
	@SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
    	TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);
    	
    	State state = Torch.getState(TE);

    	if (!state.equals(State.UNLIT))
    	{
	    	double xOffset = x + 0.5F;
	    	double yOffset = y + 0.7F;
	    	double zOffset = z + 0.5F;
	    	double offset1 = 0.2199999988079071D;
	    	double offset2 = 0.27000001072883606D;

		    ForgeDirection facing = Torch.getFacing(TE);
				
	        switch (facing) {
	        case NORTH:
	            world.spawnParticle("smoke", xOffset, yOffset + offset1, zOffset + offset2, 0.0D, 0.0D, 0.0D);
	            if (state.equals(State.LIT)) {
	            	world.spawnParticle("flame", xOffset, yOffset + offset1, zOffset + offset2, 0.0D, 0.0D, 0.0D);
	            }
	        	break;
	        case SOUTH:
	            world.spawnParticle("smoke", xOffset, yOffset + offset1, zOffset - offset2, 0.0D, 0.0D, 0.0D);
	            if (state.equals(State.LIT)) {
	            	world.spawnParticle("flame", xOffset, yOffset + offset1, zOffset - offset2, 0.0D, 0.0D, 0.0D);
	            }
	        	break;
	        case WEST:
	            world.spawnParticle("smoke", xOffset + offset2, yOffset + offset1, zOffset, 0.0D, 0.0D, 0.0D);
	            if (state.equals(State.LIT)) {
	            	world.spawnParticle("flame", xOffset + offset2, yOffset + offset1, zOffset, 0.0D, 0.0D, 0.0D);
	            }
	        	break;
	        case EAST:
	            world.spawnParticle("smoke", xOffset - offset2, yOffset + offset1, zOffset, 0.0D, 0.0D, 0.0D);
	            if (state.equals(State.LIT)) {
	            	world.spawnParticle("flame", xOffset - offset2, yOffset + offset1, zOffset, 0.0D, 0.0D, 0.0D);
	            }
	        	break;
	        default:
	            world.spawnParticle("smoke", xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D);
	            if (state.equals(State.LIT)) {
	            	world.spawnParticle("flame", xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D);
	            }
	        	break;
	        }
    	}
    }
    
	@Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockHandler.carpentersTorchRenderID;
	}
    
}
