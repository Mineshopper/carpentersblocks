package com.carpentersblocks.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public interface ICarpentersHammer {

    public void onHammerUse(World world, EntityPlayer player, EnumHand hand);

    public boolean canUseHammer(World world, EntityPlayer player, EnumHand hand);

}
