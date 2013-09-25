package carpentersblocks.block;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Lever;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import carpentersblocks.util.handler.IconHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersLever extends BlockBase
{

    public BlockCarpentersLever(int blockID)
    {
        super(blockID, Material.circuits);
        this.setHardness(0.2F);
        this.setUnlocalizedName("blockCarpentersLever");
		this.setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister)
    {
		this.blockIcon = IconHandler.icon_lever;
    }

    @Override
	/**
	 * Alters polarity.
	 * Handled differently for Levers since type is split into sub-components.
	 */
	protected boolean onHammerLeftClick(TECarpentersBlock TE, EntityPlayer entityPlayer)
	{
    	int data = BlockProperties.getData(TE);
		int polarity = Lever.getPolarity(data) == Lever.POLARITY_POSITIVE ? Lever.POLARITY_NEGATIVE : Lever.POLARITY_POSITIVE;
		
		if (!TE.worldObj.isRemote) {
	    	Lever.setPolarity(TE, polarity);
	    	this.notifySideNeighbor(TE.worldObj, TE.xCoord, TE.yCoord, TE.zCoord, Lever.getType(data));
		} else {
	    	switch (polarity) {
	    	case Lever.POLARITY_POSITIVE:
	    		entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.polarity_pos.name"));
	    		break;
	    	case Lever.POLARITY_NEGATIVE:
	    		entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.polarity_neg.name"));
	    	}
    	}

		return true;
	}
    
    @Override
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int metadata)
    {
    	ForgeDirection dir = ForgeDirection.getOrientation(metadata);
    	
    	return 	dir == DOWN  && world.isBlockSolidOnSide(x, y + 1, z, DOWN ) ||
    			dir == UP    && world.isBlockSolidOnSide(x, y - 1, z, UP   ) ||
    			dir == NORTH && world.isBlockSolidOnSide(x, y, z + 1, NORTH) ||
    			dir == SOUTH && world.isBlockSolidOnSide(x, y, z - 1, SOUTH) ||
    			dir == WEST  && world.isBlockSolidOnSide(x + 1, y, z, WEST ) ||
    			dir == EAST  && world.isBlockSolidOnSide(x - 1, y, z, EAST );
    }

    @Override
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return world.isBlockSolidOnSide(x - 1, y, z, EAST ) ||
               world.isBlockSolidOnSide(x + 1, y, z, WEST ) ||
               world.isBlockSolidOnSide(x, y, z - 1, SOUTH) ||
               world.isBlockSolidOnSide(x, y, z + 1, NORTH) ||
               world.isBlockSolidOnSide(x, y - 1, z, UP   ) ||
               world.isBlockSolidOnSide(x, y + 1, z, DOWN );
    }

    @Override
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {    	
        int type = -1;

        if (side == 0 && world.isBlockSolidOnSide(x, y + 1, z, DOWN))
            type = 0;

        if (side == 1 && world.isBlockSolidOnSide(x, y - 1, z, UP))
            type = 5;

        if (side == 2 && world.isBlockSolidOnSide(x, y, z + 1, NORTH))
            type = 4;

        if (side == 3 && world.isBlockSolidOnSide(x, y, z - 1, SOUTH))
            type = 3;

        if (side == 4 && world.isBlockSolidOnSide(x + 1, y, z, WEST))
            type = 2;

        if (side == 5 && world.isBlockSolidOnSide(x - 1, y, z, EAST))
            type = 1;

        return type;
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
	public void auxiliaryOnBlockPlacedBy(TECarpentersBlock TE, World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
    {
    	BlockProperties.setData(TE, world.getBlockMetadata(x, y, z));
    	
    	int data = BlockProperties.getData(TE);
        int type = Lever.getType(data);

        if (type == invertType(1))
        {
            if ((MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 1) == 0)
            	Lever.setType(TE, 5);
            else
            	Lever.setType(TE, 6);
        }
        else if (type == invertType(0))
        {
            if ((MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 1) == 0)
            	Lever.setType(TE, 7);
            else
            	Lever.setType(TE, 0);
        }
    }

    /**
     * only used in ComponentScatteredFeatureJunglePyramid.addComponentParts"
     */
    public static int invertType(int type)
    {
        switch (type)
        {
            case 0:
                return 0;
            case 1:
                return 5;
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 2;
            case 5:
                return 1;
            default:
                return -1;
        }
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
	protected void auxiliaryOnNeighborBlockChange(TECarpentersBlock TE, World world, int x, int y, int z, int blockID)
    {
    	if (this.checkIfAttachedToBlock(world, x, y, z))
    	{
    		int data = BlockProperties.getData(TE);
    		int type = Lever.getType(data);

    		boolean ejectLever =	!world.isBlockSolidOnSide(x - 1, y, z, EAST) && type == 1 ||
				    				!world.isBlockSolidOnSide(x + 1, y, z, WEST) && type == 2 ||
				    				!world.isBlockSolidOnSide(x, y, z - 1, SOUTH) && type == 3 ||
				    				!world.isBlockSolidOnSide(x, y, z + 1, NORTH) && type == 4 ||
				    				!world.isBlockSolidOnSide(x, y - 1, z, UP) && type == 5 ||
				    				!world.isBlockSolidOnSide(x, y - 1, z, UP) && type == 6 ||
				    				!world.isBlockSolidOnSide(x, y + 1, z, DOWN) && type == 0 ||
				    				!world.isBlockSolidOnSide(x, y + 1, z, DOWN) && type == 7;

    		if (ejectLever)
    		{
    			this.dropBlockAsItem(world, x, y, z, type, 0);
    			world.setBlockToAir(x, y, z);
    		}
    	}
    }

    /**
     * Checks if the block is attached to another block. If it is not, it returns false and drops the block as an item.
     * If it is it returns true.
     */
    private boolean checkIfAttachedToBlock(World world, int x, int y, int z)
    {
        if (!this.canPlaceBlockAt(world, x, y, z)) {
        	TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);
        	
        	int data = BlockProperties.getData(TE);
        	int type = Lever.getType(data);

            this.dropBlockAsItem(world, x, y, z, type, 0);
            world.setBlockToAir(x, y, z);
            return false;
        } else {
            return true;
        }
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
    	TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);
    	
    	int data = BlockProperties.getData(TE);
    	int type = Lever.getType(data);

        float offset = 0.1875F;
        
        switch (type) {
        case 0:
        	offset = 0.25F;
        	this.setBlockBounds(0.5F - offset, 0.4F, 0.5F - offset, 0.5F + offset, 1.0F, 0.5F + offset);
        	break;
        case 1:
        	this.setBlockBounds(0.0F, 0.2F, 0.5F - offset, offset * 2.0F, 0.8F, 0.5F + offset);
        	break;
        case 2:
        	this.setBlockBounds(1.0F - offset * 2.0F, 0.2F, 0.5F - offset, 1.0F, 0.8F, 0.5F + offset);
        	break;
        case 3:
        	this.setBlockBounds(0.5F - offset, 0.2F, 0.0F, 0.5F + offset, 0.8F, offset * 2.0F);
        	break;
        case 4:
        	this.setBlockBounds(0.5F - offset, 0.2F, 1.0F - offset * 2.0F, 0.5F + offset, 0.8F, 1.0F);
        	break;
        case 7:
        	offset = 0.25F;
        	this.setBlockBounds(0.5F - offset, 0.4F, 0.5F - offset, 0.5F + offset, 1.0F, 0.5F + offset);
        	break;
        default:
        	offset = 0.25F;
        	this.setBlockBounds(0.5F - offset, 0.0F, 0.5F - offset, 0.5F + offset, 0.6F, 0.5F + offset);
        	break;
        }
        
    }

    @Override
    /**
     * Called upon block activation.
     */
    public boolean auxiliaryOnBlockActivated(TECarpentersBlock TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
    	int data = BlockProperties.getData(TE);
    	int type = Lever.getType(data);

    	Lever.setState(TE, isActive(TE) ? Lever.STATE_OFF : Lever.STATE_ON, true);
    	world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);

    	switch (type) {
    	case 0:
    		world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
    		break;
    	case 1:
    		world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
    		break;
    	case 2:
    		world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
    		break;
    	case 3:
    		world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
    		break;
    	case 4:
    		world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
    		break;
    	case 7:
    		world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
    		break;
    	default:
    		world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
    		break;
    	}

    	return true;
    }
    
    /**
     * Returns whether lever is in active state
     */
    private boolean isActive(TECarpentersBlock TE)
    {
    	int data = BlockProperties.getData(TE);
    	return Lever.getState(data) == Lever.STATE_ON;
    }

    @Override
    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
    	TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);
    	
    	int data = BlockProperties.getData(TE);

        return this.getPowerSupply(TE, data);
    }

    @Override
    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
    	TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);
    	
    	int data = BlockProperties.getData(TE);
    	int type = Lever.getType(data);

    	if (
    			type == 0 && side == 0 ||
    			type == 7 && side == 0 ||
    			type == 6 && side == 1 ||
    			type == 5 && side == 1 ||
    			type == 4 && side == 2 ||
    			type == 3 && side == 3 ||
    			type == 2 && side == 4 ||
    			type == 1 && side == 5
    		)
    		return this.getPowerSupply(TE, data);
    	else
    		return 0;
    }
    
    /**
     * Returns power level (0 or 15)
     */
    private int getPowerSupply(TECarpentersBlock TE, int data)
    {
    	int polarity = Lever.getPolarity(data);

    	if (isActive(TE)) {
    		return polarity == Lever.POLARITY_POSITIVE ? 15 : 0;
    	} else {
    		return polarity == Lever.POLARITY_NEGATIVE ? 15 : 0;
    	}
    }
    
    private void notifySideNeighbor(World world, int x, int y, int z, int side)
    {
    	world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);

    	switch (side) {
    	case 0:
    		world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
    		break;
    	case 1:
    		world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
    		break;
    	case 2:
    		world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
    		break;
    	case 3:
    		world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
    		break;
    	case 4:
    		world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
    		break;
    	default:
    		world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
    		break;
    	}
    }
    
    @Override
    /**
     * Ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void auxiliaryBreakBlock(TECarpentersBlock TE, World world, int x, int y, int z, int par5, int metadata)
    {
        if (isActive(TE))
        {
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            
            int data = BlockProperties.getData(TE);
            int type = Lever.getType(data);

            switch (type) {
            case 0:
            	world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
            	break;
            case 1:
            	world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
            	break;
            case 2:
            	world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
            	break;
            case 3:
            	world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
            	break;
            case 4:
            	world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
            	break;
            case 7:
            	world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
            	break;
            default:
            	world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            	break;
            }
        }
    }

    @Override
    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
	public boolean canProvidePower()
    {
        return true;
    }
    
    @Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockHandler.carpentersLeverRenderID;
	}
    
}
