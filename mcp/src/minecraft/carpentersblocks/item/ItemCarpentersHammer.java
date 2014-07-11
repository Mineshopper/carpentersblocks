package carpentersblocks.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.api.ICarpentersHammer;
import carpentersblocks.block.BlockCoverable;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCarpentersHammer extends Item implements ICarpentersHammer {

    public ItemCarpentersHammer(int itemID)
    {
        super(itemID);
        setMaxStackSize(1);
        setCreativeTab(CarpentersBlocks.creativeTab);

        if (ItemRegistry.itemCarpentersToolsDamageable) {
            setMaxDamage(ItemRegistry.itemCarpentersToolsUses);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "hammer");
    }

    @Override
    public void onHammerUse(World world, EntityPlayer entityPlayer)
    {
        entityPlayer.getCurrentEquippedItem().damageItem(1, entityPlayer);
    }

    @Override
    public boolean canUseHammer(World world, EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    public boolean canHarvestBlock(Block blockToBeHarvested)
    {
        return blockToBeHarvested instanceof BlockCoverable ? true : false;
    }

}
