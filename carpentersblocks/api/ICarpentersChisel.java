package carpentersblocks.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ICarpentersChisel {

    public void onChiselUse(World world, EntityPlayer player);

    public boolean canUseChisel(World world, EntityPlayer player);
}