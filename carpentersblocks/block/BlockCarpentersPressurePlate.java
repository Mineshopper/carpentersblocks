package carpentersblocks.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import carpentersblocks.data.PressurePlate;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.ChatHandler;
import carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersPressurePlate extends BlockCoverable {
    
    public BlockCarpentersPressurePlate(Material material)
    {
        super(material);
        setTickRandomly(true);
    }
    
    @Override
    /**
     * Alters polarity.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int polarity = PressurePlate.getPolarity(TE) == PressurePlate.POLARITY_POSITIVE ? PressurePlate.POLARITY_NEGATIVE : PressurePlate.POLARITY_POSITIVE;
        
        PressurePlate.setPolarity(TE, polarity);
        TE.getWorldObj().notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord - 1, TE.zCoord, this);
        
        switch (polarity) {
            case PressurePlate.POLARITY_POSITIVE:
                ChatHandler.sendMessageToPlayer("message.polarity_pos.name", entityPlayer);
                break;
            case PressurePlate.POLARITY_NEGATIVE:
                ChatHandler.sendMessageToPlayer("message.polarity_neg.name", entityPlayer);
        }
        
        return true;
    }
    
    @Override
    /**
     * Alters trigger behavior.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int trigger;
        
        switch (PressurePlate.getTriggerEntity(TE))
        {
            case PressurePlate.TRIGGER_PLAYER:
                trigger = PressurePlate.TRIGGER_MONSTER;
                break;
            case PressurePlate.TRIGGER_MONSTER:
                trigger = PressurePlate.TRIGGER_ANIMAL;
                break;
            case PressurePlate.TRIGGER_ANIMAL:
                trigger = PressurePlate.TRIGGER_ALL;
                break;
            default:
                trigger = PressurePlate.TRIGGER_PLAYER;
        }
        
        PressurePlate.setTriggerEntity(TE, trigger);
        
        switch (trigger) {
            case PressurePlate.TRIGGER_PLAYER:
                ChatHandler.sendMessageToPlayer("message.trigger_player.name", entityPlayer);
                break;
            case PressurePlate.TRIGGER_MONSTER:
                ChatHandler.sendMessageToPlayer("message.trigger_monster.name", entityPlayer);
                break;
            case PressurePlate.TRIGGER_ANIMAL:
                ChatHandler.sendMessageToPlayer("message.trigger_animal.name", entityPlayer);
                break;
            case PressurePlate.TRIGGER_ALL:
                ChatHandler.sendMessageToPlayer("message.trigger_all.name", entityPlayer);
        }
        
        return true;
    }
    
    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = (TEBase) world.getTileEntity(x, y, z);
        
        setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F - 0.0625F, isDepressed(TE) ? 0.03125F : 0.0625F, 1.0F - 0.0625F);
    }
    
    @Override
    /**
     * How many world ticks before ticking
     */
    public int tickRate(World world)
    {
        return 20;
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
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return world.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
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
            if (!world.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
                dropBlockAsItem(world, x, y, z, 0, 0);
                world.setBlockToAir(x, y, z);
            }
        }
        
        super.onNeighborBlockChange(world, x, y, z, block);
    }
    
    @Override
    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (TE != null) {
            
            List entityList = world.getEntitiesWithinAABB(Entity.class, getSensitiveAABB(x, y, z));
            
            boolean shouldActivate = false;
            if (!entityList.isEmpty()) {
                for (int count = 0; count < entityList.size() && !shouldActivate; ++count) {
                    if (shouldTrigger(TE, (Entity)entityList.get(count), world, x, y, z)) {
                        shouldActivate = true;
                    }
                }
            }
            
            if (!shouldActivate && isDepressed(TE)) {
                toggleOff(TE, world, x, y, z);
            } else {
                world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
            }
            
        }
    }
    
    @Override
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (TE != null) {
            if (shouldTrigger(TE, entity, world, x, y, z) && !isDepressed(TE)) {
                toggleOn(TE, world, x, y, z);
            }
        }
    }
    
    /**
     * Activates pressure plate.
     */
    private void toggleOn(TEBase TE, World world, int x, int y, int z)
    {
        PressurePlate.setState(TE, PressurePlate.STATE_ON, true);
        notifyNeighborsOfUpdate(world, x, y, z);
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    }
    
    /**
     * Deactivates pressure plate.
     */
    private void toggleOff(TEBase TE, World world, int x, int y, int z)
    {
        PressurePlate.setState(TE, PressurePlate.STATE_OFF, true);
        notifyNeighborsOfUpdate(world, x, y, z);
    }
    
    private AxisAlignedBB getSensitiveAABB(int x, int y, int z)
    {
        return AxisAlignedBB.getAABBPool().getAABB(x + 0.125F, y, z + 0.125F, x + 1.0F - 0.125F, y + 0.25D, z + 1.0F - 0.125F);
    }
    
    private void notifyNeighborsOfUpdate(World world, int x, int y, int z)
    {
        world.notifyBlocksOfNeighborChange(x, y, z, this);
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
    }
    
    /**
     * Returns whether pressure plate is in depressed state
     */
    private boolean isDepressed(TEBase TE)
    {
        return PressurePlate.getState(TE) == PressurePlate.STATE_ON;
    }
    
    @Override
    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (TE != null) {        
            return getPowerSupply(TE, BlockProperties.getData(TE));
        } else {
            return 0;
        }
    }
    
    @Override
    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (TE != null) {        
            return side == 1 ? getPowerSupply(TE, BlockProperties.getData(TE)) : 0;
        } else {
            return 0;
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
    
    /**
     * Returns power level (0 or 15)
     */
    private int getPowerSupply(TEBase TE, int data)
    {
        int polarity = PressurePlate.getPolarity(TE);
        
        if (isDepressed(TE)) {
            return polarity == PressurePlate.POLARITY_POSITIVE ? 15 : 0;
        } else {
            return polarity == PressurePlate.POLARITY_NEGATIVE ? 15 : 0;
        }
    }
    
    /**
     * Returns whether pressure plate should trigger based on entity colliding with it.
     */
    private boolean shouldTrigger(TEBase TE, Entity entity, World world, int x, int y, int z)
    {
        if (entity == null) {
            return false;
        }
        
        int trigger = PressurePlate.getTriggerEntity(TE);
        
        switch (trigger) {
            case PressurePlate.TRIGGER_PLAYER:
                return entity instanceof EntityPlayer;
            case PressurePlate.TRIGGER_MONSTER:
                return entity.isCreatureType(EnumCreatureType.monster, false);
            case PressurePlate.TRIGGER_ANIMAL:
                return entity.isCreatureType(EnumCreatureType.creature, false);
            default: // TRIGGER_EVERYTHING
                return true;
        }
    }
    
    @Override
    /**
     * Ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (TE != null) {
            if (isDepressed(TE)) {
                notifyNeighborsOfUpdate(world, x, y, z);
            }
        }
        
        super.breakBlock(world, x, y, z, block, metadata);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: world, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        return side != 0;
    }
    
    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersPressurePlateRenderID;
    }
    
}
