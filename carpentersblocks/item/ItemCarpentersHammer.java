package carpentersblocks.item;

import carpentersblocks.CarpentersBlocks;
import carpentersblocks.api.ICarpentersHammer;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ItemCarpentersHammer extends Item implements ICarpentersHammer
{

	public ItemCarpentersHammer(int itemID)
	{
		super(itemID);
		setUnlocalizedName("itemCarpentersHammer");

		if (ItemRegistry.itemCarpentersToolsDamageable)
			setMaxDamage(300);

		canRepair = true;
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("carpentersblocks:hammer");
	}

    @Override
    public void onHammerUse(World world, EntityPlayer player) {
        player.getCurrentEquippedItem().damageItem(1, player);
    }

    @Override
    public boolean canUseHammer(World world, EntityPlayer player) {
        return true;
    }
}
