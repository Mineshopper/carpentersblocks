package carpentersblocks.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.api.ICarpentersChisel;
import carpentersblocks.block.BlockCoverable;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCarpentersChisel extends Item implements ICarpentersChisel {

    public ItemCarpentersChisel(int itemID)
    {
        super(itemID);
        setUnlocalizedName("itemCarpentersChisel");
        setMaxStackSize(1);
        setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);

        if (ItemRegistry.itemCarpentersToolsDamageable) {
            setMaxDamage(ItemRegistry.itemCarpentersToolsUses);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("carpentersblocks:chisel");
    }

    @Override
    public void onChiselUse(World world, EntityPlayer entityPlayer)
    {
        entityPlayer.getCurrentEquippedItem().damageItem(1, entityPlayer);
    }

    @Override
    public boolean canUseChisel(World world, EntityPlayer entityPlayer)
    {
        return true;
    }
    
    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    @Override
    public float getStrVsBlock(ItemStack itemStack, Block block)
    {
        return block instanceof BlockCoverable ? 1.0001F : super.getStrVsBlock(itemStack, block);
    }

}
