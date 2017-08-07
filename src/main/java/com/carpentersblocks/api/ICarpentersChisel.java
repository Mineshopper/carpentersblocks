package com.carpentersblocks.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public interface ICarpentersChisel {

    public void onChiselUse(World world, EntityPlayer entityPlayer, EnumHand hand);

    public boolean canUseChisel(World world, EntityPlayer entityPlayer, EnumHand hand);

}
