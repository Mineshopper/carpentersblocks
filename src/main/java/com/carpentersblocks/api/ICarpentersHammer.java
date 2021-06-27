package com.carpentersblocks.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface ICarpentersHammer {

    public void onHammerUse(World world, PlayerEntity player, Hand hand);

    public boolean canUseHammer(World world, PlayerEntity player, Hand hand);

}
