package com.carpentersblocks.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * An interface for an item that can be used as a carpenter's hammer
 *
 * @author Mineshopper
 */
public interface ICarpentersHammer {

    /**
     * Called when the hammer is used on a {@link net.minecraft.block.Block}
     *
     * @param world The {@link net.minecraft.world.World} the hammer is used in
     * @param player The {@link net.minecraft.entity.player.EntityPlayer} using the hammer
     */
    public void onHammerUse(World world, EntityPlayer player);

    /**
     * Checks to see if the specified {@link net.minecraft.entity.player.EntityPlayer} can use a hammer
     *
     * @param world The {@link net.minecraft.world.World} the {@link net.minecraft.entity.player.EntityPlayer} is in
     * @param player The {@link net.minecraft.entity.player.EntityPlayer} being tested against
     * @return {@code true} if the {@link net.minecraft.entity.player.EntityPlayer} is allowed to use a hammer, otherwise {@code false}
     */
    public boolean canUseHammer(World world, EntityPlayer player);

}
