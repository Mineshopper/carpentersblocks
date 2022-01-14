package com.carpentersblocks.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface ICarpentersGlue {

    public void onGlueUse(World world, PlayerEntity playerEntity, Hand hand);

    public boolean canUseGlue(World world, PlayerEntity playerEntity, Hand hand);

}
