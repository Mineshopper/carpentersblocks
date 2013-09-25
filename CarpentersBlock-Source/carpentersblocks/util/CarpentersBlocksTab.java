package carpentersblocks.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import carpentersblocks.util.handler.ItemHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CarpentersBlocksTab extends CreativeTabs
{

	public CarpentersBlocksTab(String label)
	{
		super(label);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return ItemHandler.itemCarpentersHammer;
	}

	@Override
	public String getTranslatedTabLabel()
	{
		return LanguageRegistry.instance().getStringLocalization("itemGroup.carpentersBlocks.name");
	}

}
