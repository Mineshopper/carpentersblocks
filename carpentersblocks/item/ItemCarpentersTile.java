package carpentersblocks.item;

import static net.minecraftforge.common.util.ForgeDirection.UNKNOWN;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import static net.minecraftforge.common.util.ForgeDirection.EAST;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.entity.item.EntityCarpentersTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCarpentersTile extends Item {

    public ItemCarpentersTile()
    {
        setCreativeTab(CarpentersBlocks.creativeTab);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "tile");
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote) {
            
            return true;
            
        } else {
            
            ForgeDirection dir = ForgeDirection.getOrientation(side);
            int x_offset = x + dir.offsetX;
            int y_offset = y + dir.offsetY;
            int z_offset = z + dir.offsetZ;
    
            if (!entityPlayer.canPlayerEdit(x_offset, y_offset, z_offset, side, itemStack)) {
                
                return false;
                
            } else {
                
                ForgeDirection offset_side = getOffsetSide(ForgeDirection.getOrientation(side), hitX, hitY, hitZ);

                EntityCarpentersTile entity = new EntityCarpentersTile(entityPlayer, world, x_offset, y_offset, z_offset, ForgeDirection.getOrientation(side), offset_side, entityPlayer.isSneaking());
                
                if (entity != null && entity.onValidSurface()) {
                    world.spawnEntityInWorld(entity);
                    entity.playTileSound();
                    --itemStack.stackSize;
                }
                
                return true;
            }
        }
    }
    
    /**
     * Returns offset side relative to where on a block a player clicks.
     */
    private ForgeDirection getOffsetSide(ForgeDirection side, float hitX, float hitY, float hitZ)
    {
        ForgeDirection offset_side = UNKNOWN;
        
        float ratio = 0.20F;
        float invert_ratio = 1.0F - ratio;
        
        boolean center_clicked = hitX > ratio && hitX < invert_ratio && hitZ > ratio && hitZ < invert_ratio ||
                                 hitX > ratio && hitX < invert_ratio && hitY > ratio && hitY < invert_ratio ||
                                 hitZ > ratio && hitZ < invert_ratio && hitY > ratio && hitY < invert_ratio;

        if (!center_clicked) {
                                 
            switch (side) {
                case DOWN:
                case UP:
                    
                    if (hitZ < 1 - hitX && hitZ < hitX) {
                        offset_side = NORTH;
                    } else if (hitZ > 1 - hitX && hitZ > hitX) {
                        offset_side = SOUTH;
                    } else if (hitX < 1 - hitZ && hitX < hitZ) {
                        offset_side = WEST;
                    } else {
                        offset_side = EAST;
                    }
    
                    break;
                case NORTH:
                case SOUTH:
                    
                    if (hitX < 1 - hitY && hitX < hitY) {
                        offset_side = WEST;
                    } else if (hitX > 1 - hitY && hitX > hitY) {
                        offset_side = EAST;
                    } else if (hitY < 1 - hitX && hitY < hitX) {
                        offset_side = DOWN;
                    } else {
                        offset_side = UP;
                    }
                    
                    break;
                case WEST:
                case EAST:
                    
                    if (hitZ < 1 - hitY && hitZ < hitY) {
                        offset_side = NORTH;
                    } else if (hitZ > 1 - hitY && hitZ > hitY) {
                        offset_side = SOUTH;
                    } else if (hitY < 1 - hitZ && hitY < hitZ) {
                        offset_side = DOWN;
                    } else {
                        offset_side = UP;
                    }
                    
                    break;
                default:
                    
                    break;
            }
        
        }

        return offset_side;
    }

}