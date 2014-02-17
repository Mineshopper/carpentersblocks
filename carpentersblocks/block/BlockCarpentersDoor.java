package carpentersblocks.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Door;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.handler.ChatHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersDoor extends BlockCoverable {
    
    public BlockCarpentersDoor(Material material)
    {
        super(material);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        IconRegistry.icon_door_screen_tall         = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_screen_tall"        );
        IconRegistry.icon_door_glass_tall_top      = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_glass_tall_top"     );
        IconRegistry.icon_door_glass_tall_bottom   = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_glass_tall_bottom"  );
        IconRegistry.icon_door_glass_top           = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_glass_top"          );
        IconRegistry.icon_door_french_glass_top    = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_french_glass_top"   );
        IconRegistry.icon_door_french_glass_bottom = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_french_glass_bottom");
        
        super.registerBlockIcons(iconRegister);
    }
    
    @Override
    /**
     * Alters hinge side.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int hinge = Door.getHinge(TE);
        
        setDoorHinge(TE, hinge == Door.HINGE_LEFT ? Door.HINGE_RIGHT : Door.HINGE_LEFT);
        
        return true;
    }
    
    @Override
    /**
     * Alters door type and redstone behavior.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        if (!entityPlayer.isSneaking()) {
            
            int type = Door.getType(TE);
            
            if (++type > 5) {
                type = 0;
            }
            
            setDoorType(TE, type);
            
        } else {
            
            int rigidity = Door.getRigidity(TE) == Door.HINGED_NONRIGID ? Door.HINGED_RIGID : Door.HINGED_NONRIGID;
            
            setDoorRigidity(TE, rigidity);
            
            switch (rigidity) {
                case Door.HINGED_NONRIGID:
                    ChatHandler.sendMessageToPlayer("message.activation_wood.name", entityPlayer);
                    break;
                case Door.HINGED_RIGID:
                    ChatHandler.sendMessageToPlayer("message.activation_iron.name", entityPlayer);
            }
            
        }
        
        return true;
    }
    
    @Override
    /**
     * Opens or closes door on right click.
     */
    public boolean[] postOnBlockActivated(TEBase TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        if (!activationRequiresRedstone(TE)) {
            setDoorState(TE, Door.getState(TE) == Door.STATE_OPEN ? Door.STATE_CLOSED : Door.STATE_OPEN);
            boolean[] result = { true, false };
            return result;
        }
        
        return super.postOnBlockActivated(TE, world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
    }
    
    /**
     * Returns whether door requires redstone activation.
     */
    private boolean activationRequiresRedstone(TEBase TE)
    {
        return Door.getRigidity(TE) == Door.HINGED_RIGID;
    }
    
    /**
     * Returns a list of door tile entities that make up either a single door or two connected double doors.
     */
    private List<TEBase> getDoorPieces(TEBase TE)
    {
        List<TEBase> list = new ArrayList<TEBase>();
        
        World world = TE.getWorldObj();
        
        int facing = Door.getFacing(TE);
        int hinge = Door.getHinge(TE);
        int piece = Door.getPiece(TE);
        
        boolean isTop = piece == Door.PIECE_TOP;
        
        /*
         * Add calling TE and its matching piece
         */
        list.add(TE);
        list.add((TEBase) world.getTileEntity(TE.xCoord, TE.yCoord - (isTop ? 1 : -1), TE.zCoord));
        
        /*
         * Begin searching for and adding other neighboring pieces
         */
        TEBase TE_ZN = world.getBlock(TE.xCoord, TE.yCoord, TE.zCoord - 1).equals(this) ? (TEBase) world.getTileEntity(TE.xCoord, TE.yCoord, TE.zCoord - 1) : null;
        TEBase TE_ZP = world.getBlock(TE.xCoord, TE.yCoord, TE.zCoord + 1).equals(this) ? (TEBase) world.getTileEntity(TE.xCoord, TE.yCoord, TE.zCoord + 1) : null;
        TEBase TE_XN = world.getBlock(TE.xCoord - 1, TE.yCoord, TE.zCoord).equals(this) ? (TEBase) world.getTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord) : null;
        TEBase TE_XP = world.getBlock(TE.xCoord + 1, TE.yCoord, TE.zCoord).equals(this) ? (TEBase) world.getTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord) : null;
        
        switch (facing)
        {
            case Door.FACING_XN:
                
                if (TE_ZN != null) {
                    if (piece == Door.getPiece(TE_ZN) && facing == Door.getFacing(TE_ZN) && hinge == Door.HINGE_LEFT && Door.getHinge(TE_ZN) == Door.HINGE_RIGHT)
                    {
                        list.add(TE_ZN);
                        list.add((TEBase) world.getTileEntity(TE.xCoord, TE.yCoord - (isTop ? 1 : -1), TE.zCoord - 1));
                    }
                }
                if (TE_ZP != null) {
                    if (piece == Door.getPiece(TE_ZP) && facing == Door.getFacing(TE_ZP) && hinge == Door.HINGE_RIGHT && Door.getHinge(TE_ZP) == Door.HINGE_LEFT)
                    {
                        list.add(TE_ZP);
                        list.add((TEBase) world.getTileEntity(TE.xCoord, TE.yCoord - (isTop ? 1 : -1), TE.zCoord + 1));
                    }
                }
                break;
                
            case Door.FACING_XP:
                
                if (TE_ZN != null) {
                    if (piece == Door.getPiece(TE_ZN) && facing == Door.getFacing(TE_ZN) && hinge == Door.HINGE_RIGHT && Door.getHinge(TE_ZN) == Door.HINGE_LEFT)
                    {
                        list.add(TE_ZN);
                        list.add((TEBase) world.getTileEntity(TE.xCoord, TE.yCoord - (isTop ? 1 : -1), TE.zCoord - 1));
                    }
                }
                if (TE_ZP != null) {
                    if (piece == Door.getPiece(TE_ZP) && facing == Door.getFacing(TE_ZP) && hinge == Door.HINGE_LEFT && Door.getHinge(TE_ZP) == Door.HINGE_RIGHT)
                    {
                        list.add(TE_ZP);
                        list.add((TEBase) world.getTileEntity(TE.xCoord, TE.yCoord - (isTop ? 1 : -1), TE.zCoord + 1));
                    }
                }
                break;
                
            case Door.FACING_ZN:
            {
                
                if (TE_XN != null) {
                    if (piece == Door.getPiece(TE_XN) && facing == Door.getFacing(TE_XN) && hinge == Door.HINGE_RIGHT && Door.getHinge(TE_XN) == Door.HINGE_LEFT)
                    {
                        list.add(TE_XN);
                        list.add((TEBase) world.getTileEntity(TE.xCoord - 1, TE.yCoord - (isTop ? 1 : -1), TE.zCoord));
                    }
                }
                if (TE_XP != null) {
                    if (piece == Door.getPiece(TE_XP) && facing == Door.getFacing(TE_XP) && hinge == Door.HINGE_LEFT && Door.getHinge(TE_XP) == Door.HINGE_RIGHT)
                    {
                        list.add(TE_XP);
                        list.add((TEBase) world.getTileEntity(TE.xCoord + 1, TE.yCoord - (isTop ? 1 : -1), TE.zCoord));
                    }
                }
                break;
            }
            case Door.FACING_ZP:
                
                if (TE_XN != null) {
                    if (piece == Door.getPiece(TE_XN) && facing == Door.getFacing(TE_XN) && hinge == Door.HINGE_LEFT && Door.getHinge(TE_XN) == Door.HINGE_RIGHT)
                    {
                        list.add(TE_XN);
                        list.add((TEBase) world.getTileEntity(TE.xCoord - 1, TE.yCoord - (isTop ? 1 : -1), TE.zCoord));
                    }
                }
                if (TE_XP != null) {
                    if (piece == Door.getPiece(TE_XP) && facing == Door.getFacing(TE_XP) && hinge == Door.HINGE_RIGHT && Door.getHinge(TE_XP) == Door.HINGE_LEFT)
                    {
                        list.add(TE_XP);
                        list.add((TEBase) world.getTileEntity(TE.xCoord + 1, TE.yCoord - (isTop ? 1 : -1), TE.zCoord));
                    }
                }
                break;
                
        }
        
        return list;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z).equals(this)) {
            setBlockBoundsBasedOnState(world, x, y, z);
        }
        
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }
    
    @Override
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z).equals(this)) {
            setBlockBoundsBasedOnState(world, x, y, z);
        }
        
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
    
    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = (TEBase) world.getTileEntity(x, y, z);
        int facing = Door.getFacing(TE);
        int hinge = Door.getHinge(TE);
        boolean isOpen = Door.getState(TE) == Door.STATE_OPEN;
        
        float    x_low = 0.0F,
                z_low = 0.0F,
                x_high = 1.0F,
                z_high = 1.0F;
        
        switch (facing)
        {
            case Door.FACING_XN:
                if (!isOpen) {
                    x_low = 0.8125F;
                } else if (hinge == Door.HINGE_RIGHT) {
                    z_high = 0.1875F;
                } else {
                    z_low = 0.8125F;
                }
                break;
            case Door.FACING_XP:
                if (!isOpen) {
                    x_high = 0.1875F;
                } else if (hinge == Door.HINGE_RIGHT) {
                    z_low = 0.8125F;
                } else {
                    z_high = 0.1875F;
                }
                break;
            case Door.FACING_ZN:
                if (!isOpen) {
                    z_low = 0.8125F;
                } else if (hinge == Door.HINGE_RIGHT) {
                    x_low = 0.8125F;
                } else {
                    x_high = 0.1875F;
                }
                break;
            case Door.FACING_ZP:
                if (!isOpen) {
                    z_high = 0.1875F;
                } else if (hinge == Door.HINGE_RIGHT) {
                    x_high = 0.1875F;
                } else {
                    x_low = 0.8125F;
                }
                break;
        }
        
        setBlockBounds(x_low, 0.0F, z_low, x_high, 1.0F, z_high);
    }
    
    /**
     * Cycle door state.
     * Will update all connecting door pieces.
     */
    public void setDoorState(TEBase TE, int state)
    {
        List<TEBase> doorPieces = getDoorPieces(TE);
        for (TEBase piece : doorPieces) {
            Door.setState(piece, state, piece == TE ? true : false);
        }
    }
    
    /**
     * Updates door type.
     * Will also update adjoining door piece.
     */
    public void setDoorType(TEBase TE, int type)
    {
        Door.setType(TE, type);
        updateAdjoiningDoorPiece(TE);
    }
    
    /**
     * Set door rigidity.
     * Will update all connecting door pieces.
     */
    public void setDoorRigidity(TEBase TE, int rigidity)
    {
        List<TEBase> doorPieces = getDoorPieces(TE);
        for (TEBase piece : doorPieces) {
            Door.setRigidity(piece, rigidity);
        }
    }
    
    /**
     * Updates door hinge side.
     * Will also update adjoining door piece.
     */
    public void setDoorHinge(TEBase TE, int hinge)
    {
        Door.setHingeSide(TE, hinge);
        updateAdjoiningDoorPiece(TE);
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
            
            boolean isOpen = Door.getState(TE) == Door.STATE_OPEN;
            
            /*
             * Check if door piece is orphaned.
             */
            if (Door.getPiece(TE) == Door.PIECE_BOTTOM) {
                if (!world.getBlock(x, y + 1, z).equals(this)) {
                    world.setBlockToAir(x, y, z);
                    return;
                } else if (!world.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
                    world.setBlockToAir(x, y + 1, z);
                    dropBlockAsItem(world, x, y, z, 0, 0);
                    return;
                }
            } else if (!world.getBlock(x, y - 1, z).equals(this)) {
                world.setBlockToAir(x, y, z);
                return;
            }
            
            /*
             * Create list of door pieces and check state of each so
             * that they act as a single entity regardless of which
             * door piece receives this event.
             */
            boolean isPowered = false;
            List<TEBase> doorPieces = getDoorPieces(TE);
            for (TEBase piece : doorPieces) {
                if (world.isBlockIndirectlyGettingPowered(piece.xCoord, piece.yCoord, piece.zCoord)) {
                    isPowered = true;
                }
            }
            
            /*
             * Set block open or closed
             */
            if (block != null && block.canProvidePower() && isPowered != isOpen) {
                setDoorState(TE, isOpen ? Door.STATE_CLOSED : Door.STATE_OPEN);
            }
        }
        
        super.onNeighborBlockChange(world, x, y, z, block);
    }
    
    @Override
    /**
     * Returns the ID of the items to drop on destruction.
     */
    public Item getItemDropped(int metadata, Random random, int par3)
    {
        return ItemRegistry.itemCarpentersDoor;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public Item getItem(World world, int x, int y, int z)
    {
        return ItemRegistry.itemCarpentersDoor;
    }
    
    /**
     * Updates state, hinge and type for adjoining door piece.
     */
    private void updateAdjoiningDoorPiece(TEBase TE)
    {
        int state = Door.getState(TE);
        int hinge = Door.getHinge(TE);
        int type = Door.getType(TE);
        int rigidity = Door.getRigidity(TE);
        
        boolean isTop = Door.getPiece(TE) == Door.PIECE_TOP;
        
        World world = TE.getWorldObj();
        
        TEBase TE_adj;
        if (isTop) {
            TE_adj = (TEBase) world.getTileEntity(TE.xCoord, TE.yCoord - 1, TE.zCoord);
        } else {
            TE_adj = (TEBase) world.getTileEntity(TE.xCoord, TE.yCoord + 1, TE.zCoord);
        }
        
        Door.setState(TE_adj, state, false);
        Door.setHingeSide(TE_adj, hinge);
        Door.setType(TE_adj, type);
        Door.setRigidity(TE_adj, rigidity);
    }
    
    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersDoorRenderID;
    }
    
}
