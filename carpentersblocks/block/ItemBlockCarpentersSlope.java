package carpentersblocks.block;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockCarpentersSlope extends ItemBlock {
    
    public ItemBlockCarpentersSlope(int blockID)
    {
        super(blockID);
        setHasSubtypes(true);
        setUnlocalizedName("blockCarpentersSlope");
    }
    
    @Override
    public int getMetadata(int damageValue)
    {
        return damageValue;
    }
    
    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return getUnlocalizedName() + "." + BlockCarpentersSlope.slopeType[itemstack.getItemDamage()];
    }
    
}
