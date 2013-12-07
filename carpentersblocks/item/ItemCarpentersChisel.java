package carpentersblocks.item;

import carpentersblocks.api.ICarpentersChisel;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;

public class ItemCarpentersChisel extends Item implements ICarpentersChisel
{

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

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("carpentersblocks:chisel");
	}

    @Override
    public void onChiselUse(World world, EntityPlayer player) {
        player.getCurrentEquippedItem().damageItem(1, player);
    }

    @Override
    public boolean canUseChisel(World world, EntityPlayer player) {
        return true;
    }
}
