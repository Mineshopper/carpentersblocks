package com.carpentersblocks.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.api.ICarpentersHammer;
import com.carpentersblocks.block.BlockCoverable;
import com.carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCarpentersHammer extends Item implements ICarpentersHammer {

    public ItemCarpentersHammer()
    {
        setMaxStackSize(1);
        setCreativeTab(CarpentersBlocks.creativeTab);

        if (ItemRegistry.itemCarpentersToolsDamageable) {
            setMaxDamage(ItemRegistry.itemCarpentersToolsUses);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister)
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
    public boolean func_150897_b(Block blockToBeHarvested)
    {
        return blockToBeHarvested instanceof BlockCoverable ? true : false;
    }

}
