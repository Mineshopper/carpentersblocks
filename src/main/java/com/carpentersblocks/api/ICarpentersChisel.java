package com.carpentersblocks.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface ICarpentersChisel {

    public void onChiselUse(World world, PlayerEntity playerEntity, Hand hand);

    public boolean canUseChisel(World world, PlayerEntity playerEntity, Hand hand);

}
