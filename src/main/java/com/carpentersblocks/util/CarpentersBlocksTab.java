package com.carpentersblocks.util;

import com.carpentersblocks.util.registry.ItemRegistry;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CarpentersBlocksTab extends CreativeTabs {

    public CarpentersBlocksTab(String label) {
        super(label);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemRegistry.itemCarpentersHammer);
    }

    @Override
    public String getTranslatedTabLabel() {
        return I18n.format("itemGroup.carpentersBlocks.name", new Object[0]);
    }

}
