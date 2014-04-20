package carpentersblocks.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.api.ICarpentersHammer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCarpentersHammer extends ItemBase implements ICarpentersHammer {

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

}
