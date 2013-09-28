package carpentersblocks.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import carpentersblocks.block.BlockBase;

public class ItemBlockBase extends ItemBlock {

    public ItemBlockBase(int par1)
    {
        super(par1);      
        
    }
    
    public Icon getIconFromDamage(ItemStack par1ItemStack){
        return ((BlockBase)Block.blocksList[par1ItemStack.itemID]).getDefaultIconFromSide(0);
    }

    public Icon getIconFromDamage(int par1)
    {
    	return ((BlockBase)Block.blocksList[this.itemID]).getDefaultIconFromSide(0);
    }
}
