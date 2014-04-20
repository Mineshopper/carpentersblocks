package carpentersblocks.item;

import carpentersblocks.CarpentersBlocks;
import carpentersblocks.block.BlockCoverable;
import carpentersblocks.util.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBase extends Item {

	public ItemBase()
	{
        setMaxStackSize(1);
        setCreativeTab(CarpentersBlocks.creativeTab);
        
        if (ItemRegistry.itemCarpentersToolsDamageable) {
            setMaxDamage(ItemRegistry.itemCarpentersToolsUses);
        }
	}
	
    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    @Override
    public float func_150893_a(ItemStack itemStack, Block block)
    {
        return block instanceof BlockCoverable ? 0.0F : super.func_150893_a(itemStack, block);
    }
    
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return false;
    }
	
}
