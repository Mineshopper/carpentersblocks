package carpentersblocks.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Door;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCarpentersDoor extends Item {
    
    public ItemCarpentersDoor()
    {
        setMaxStackSize(64);
        setCreativeTab(CarpentersBlocks.creativeTab);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door");
    }
    
    @Override
    /**
     * Callback for item usage. If the item does something special on right clicking, it will have one of these. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (side == 1) {
            
            ++y;
            
            if (
                    y < 255                                                              &&
                    entityPlayer.canPlayerEdit(x, y, z, side, itemStack)                 &&
                    entityPlayer.canPlayerEdit(x, y + 1, z, side, itemStack)             &&
                    world.isAirBlock(x, y, z)                                            &&
                    world.isAirBlock(x, y + 1, z)                                        &&
                    World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)               &&
                    world.setBlock(x, y, z, BlockRegistry.blockCarpentersDoor, 0, 4)     &&
                    world.setBlock(x, y + 1, z, BlockRegistry.blockCarpentersDoor, 0, 4)
                    )
            {
                int facing = MathHelper.floor_double((entityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F - 0.5D) & 3;
                
                BlockProperties.playBlockSound(world, new ItemStack(BlockRegistry.blockCarpentersDoor), x, y, z);
                
                /* Create bottom door piece. */
                
                TEBase TE = (TEBase) world.getTileEntity(x, y, z);
                Door.setFacing(TE, facing);
                Door.setHingeSide(TE, getHingePoint(TE, BlockRegistry.blockCarpentersDoor));
                Door.setPiece(TE, Door.PIECE_BOTTOM);
                
                /* Match door type and rigidity with adjacent type if possible. */
                
                TEBase TE_XN = world.getBlock(x - 1, y, z).equals(BlockRegistry.blockCarpentersDoor) ? (TEBase) world.getTileEntity(x - 1, y, z) : null;
                TEBase TE_XP = world.getBlock(x + 1, y, z).equals(BlockRegistry.blockCarpentersDoor) ? (TEBase) world.getTileEntity(x + 1, y, z) : null;
                TEBase TE_ZN = world.getBlock(x, y, z - 1).equals(BlockRegistry.blockCarpentersDoor) ? (TEBase) world.getTileEntity(x, y, z - 1) : null;
                TEBase TE_ZP = world.getBlock(x, y, z + 1).equals(BlockRegistry.blockCarpentersDoor) ? (TEBase) world.getTileEntity(x, y, z + 1) : null;
                
                int type = 0;
                if (TE_XN != null) {
                    Door.setType(TE, Door.getType(TE_XN));
                    Door.setRigidity(TE, Door.getRigidity(TE_XN));
                    type = Door.getType(TE_XN);
                } else if (TE_XP != null) {
                    Door.setType(TE, Door.getType(TE_XP));
                    Door.setRigidity(TE, Door.getRigidity(TE_XP));
                    type = Door.getType(TE_XP);
                } else if (TE_ZN != null) {
                    Door.setType(TE, Door.getType(TE_ZN));
                    Door.setRigidity(TE, Door.getRigidity(TE_ZN));
                    type = Door.getType(TE_ZN);
                } else if (TE_ZP != null) {
                    Door.setType(TE, Door.getType(TE_ZP));
                    Door.setRigidity(TE, Door.getRigidity(TE_ZP));
                    type = Door.getType(TE_ZP);
                }
                
                /* Create top door piece. */
                
                TEBase TE_YP = (TEBase) world.getTileEntity(x, y + 1, z);
                Door.setFacing(TE_YP, facing);
                Door.setType(TE_YP, type);
                Door.setHingeSide(TE_YP, Door.getHinge(TE));
                Door.setPiece(TE_YP, Door.PIECE_TOP);
                Door.setRigidity(TE_YP, Door.getRigidity(TE));
                
                --itemStack.stackSize;
                return true;
            }
            
        }
        
        return false;
    }
    
    /**
     * Returns a hinge point allowing double-doors if a matching neighboring door is found.
     * It returns the default hinge point if no neighboring doors are found.
     */
    private int getHingePoint(TEBase TE, Block block)
    {
        int facing = Door.getFacing(TE);
        Door.getHinge(TE);
        Door.getState(TE);
        int piece = Door.getPiece(TE);
        
        World world = TE.getWorldObj();
        
        TEBase TE_ZN = world.getBlock(TE.xCoord, TE.yCoord, TE.zCoord - 1).equals(block) ? (TEBase) world.getTileEntity(TE.xCoord, TE.yCoord, TE.zCoord - 1) : null;
        TEBase TE_ZP = world.getBlock(TE.xCoord, TE.yCoord, TE.zCoord + 1).equals(block) ? (TEBase) world.getTileEntity(TE.xCoord, TE.yCoord, TE.zCoord + 1) : null;
        TEBase TE_XN = world.getBlock(TE.xCoord - 1, TE.yCoord, TE.zCoord).equals(block) ? (TEBase) world.getTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord) : null;
        TEBase TE_XP = world.getBlock(TE.xCoord + 1, TE.yCoord, TE.zCoord).equals(block) ? (TEBase) world.getTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord) : null;
        
        switch (facing)
        {
            case Door.FACING_XN:
                
                if (TE_ZP != null) {
                    if (piece == Door.getPiece(TE_ZP) && facing == Door.getFacing(TE_ZP) && Door.getHinge(TE_ZP) == Door.HINGE_LEFT) {
                        return Door.HINGE_RIGHT;
                    }
                }
                
                break;
            case Door.FACING_XP:
                
                if (TE_ZN != null) {
                    if (piece == Door.getPiece(TE_ZN) && facing == Door.getFacing(TE_ZN) && Door.getHinge(TE_ZN) == Door.HINGE_LEFT) {
                        return Door.HINGE_RIGHT;
                    }
                }
                
                break;
            case Door.FACING_ZN:
                
                if (TE_XN != null) {
                    if (piece == Door.getPiece(TE_XN) && facing == Door.getFacing(TE_XN) && Door.getHinge(TE_XN) == Door.HINGE_LEFT) {
                        return Door.HINGE_RIGHT;
                    }
                }
                
                break;
            case Door.FACING_ZP:
                
                if (TE_XP != null) {
                    if (piece == Door.getPiece(TE_XP) && facing == Door.getFacing(TE_XP) && Door.getHinge(TE_XP) == Door.HINGE_LEFT) {
                        return Door.HINGE_RIGHT;
                    }
                }
                
                break;
        }
        
        return Door.HINGE_LEFT;
    }
    
}
