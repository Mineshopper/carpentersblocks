package com.carpentersblocks.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ICarpentersChisel {

    public void onChiselUse(World world, EntityPlayer entityPlayer);

    public boolean canUseChisel(World world, EntityPlayer entityPlayer);

}
