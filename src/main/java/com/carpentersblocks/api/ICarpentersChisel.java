package com.carpentersblocks.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * An interface for an item that can be used as a carpenter's chisel
 *
 * @author Mineshopper
 */
public interface ICarpentersChisel {

    /**
     * Called when the chisel is used on a {@link net.minecraft.block.Block}
     *
     * @param world The {@link net.minecraft.world.World} the chisel is used in
     * @param entityPlayer The {@link net.minecraft.entity.player.EntityPlayer} using the chisel
     */
    public void onChiselUse(World world, EntityPlayer entityPlayer);

    /**
     * Checks to see if the specified {@link net.minecraft.entity.player.EntityPlayer} can use a chisel
     *
     * @param world The {@link net.minecraft.world.World} the {@link net.minecraft.entity.player.EntityPlayer} is in
     * @param entityPlayer The {@link net.minecraft.entity.player.EntityPlayer} being tested against
     * @return {@code true} if the {@link net.minecraft.entity.player.EntityPlayer} is allowed to use a chisel, otherwise {@code false}
     */
    public boolean canUseChisel(World world, EntityPlayer entityPlayer);

}
