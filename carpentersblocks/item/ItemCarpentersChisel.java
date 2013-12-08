package carpentersblocks.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.api.ICarpentersChisel;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCarpentersChisel extends Item implements ICarpentersChisel {

	public ItemCarpentersChisel(int itemID)
	{
		super(itemID);
		setUnlocalizedName("itemCarpentersChisel");

		if (ItemRegistry.itemCarpentersToolsDamageable) {
			setMaxDamage(300);
			canRepair = true;
		}

		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
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
        ItemStack equippedItem = entityPlayer.getCurrentEquippedItem();
        equippedItem.damageItem(1, entityPlayer);
        if(equippedItem.getItemDamage() >= equippedItem.getMaxDamage())
            equippedItem.stackSize--;
	}

	@Override
	public boolean canUseChisel(World world, EntityPlayer entityPlayer)
	{
		return true;
	}

}
