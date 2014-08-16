package com.carpentersblocks.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ICarpentersHammer {

    public void onHammerUse(World world, EntityPlayer player);

    public boolean canUseHammer(World world, EntityPlayer player);

}
