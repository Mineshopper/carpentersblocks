package carpentersblocks.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Bed;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCarpentersBed extends Item {
    
    public ItemCarpentersBed()
    {
        setMaxStackSize(64);
        setCreativeTab(CarpentersBlocks.creativeTab);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "bed");
    }
    
    @Override
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (side == 1) {
            
            ++y;
            
            int facing = BlockProperties.getOppositeFacing(entityPlayer);
            ForgeDirection dir = BlockProperties.getDirectionFromFacing(facing);
            
            int x_offset = x - dir.offsetX;
            int z_offset = z - dir.offsetZ;
            
            if (
                    entityPlayer.canPlayerEdit(x, y, z, side, itemStack)                          &&
                    entityPlayer.canPlayerEdit(x_offset, y, z_offset, side, itemStack)            &&
                    world.isAirBlock(x, y, z)                                                     &&
                    world.isAirBlock(x_offset, y, z_offset)                                       &&
                    World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)                        &&
                    World.doesBlockHaveSolidTopSurface(world, x_offset, y - 1, z_offset)          &&
                    world.setBlock(x, y, z, BlockRegistry.blockCarpentersBed, 0, 4)               &&
                    world.setBlock(x_offset, y, z_offset, BlockRegistry.blockCarpentersBed, 0, 4)
                    )
            {
                BlockProperties.playBlockSound(world, new ItemStack(BlockRegistry.blockCarpentersBed), x, y, z, false);
                
                /* Foot of bed. */
                
                TEBase TE_foot = (TEBase) world.getTileEntity(x, y, z);
                Bed.setDirection(TE_foot, facing);
                
                /* Head of bed. */
                
                TEBase TE_head = (TEBase) world.getTileEntity(x_offset, y, z_offset);
                Bed.setHeadOfBed(TE_head);
                Bed.setDirection(TE_head, facing);
                
                --itemStack.stackSize;
                return true;
            }
            
        }
        
        return false;
    }
    
}
