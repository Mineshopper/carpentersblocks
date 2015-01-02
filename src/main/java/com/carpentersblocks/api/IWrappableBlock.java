package com.carpentersblocks.api;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Implement this on your block class to gain full control over the way it behaves
 * as used as a Carpenter's Blocks cover, or to add compatibility that is not
 * otherwise possible.
 */
public interface IWrappableBlock {

    /**
     * Effectively overrides Block.colorMultiplier
     */
    @SideOnly(Side.CLIENT)
    public int getColorMultiplier(IBlockAccess iba, int x, int y, int z, Block b, int meta);

    /**
     * Effectively overrides Block.getIcon
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess iba, int x, int y, int z, int side, Block b, int meta);

    /**
     * Effectively overrides Block.isProvidingWeakPower
     */
    public int getWeakRedstone(World world, int x, int y, int z, Block b, int meta);

    /**
     * Effectively overrides Block.isProvidingStrongPower
     */
    public int getStrongRedstone(World world, int x, int y, int z, Block b, int meta);

    /**
     * Effectively overrides Block.getHardness
     */
    public float getHardness(World world, int x, int y, int z, Block b, int meta);

    /**
     * Effectively overrides Block.getExplosionResistance
     */
    public float getBlastResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ, Block b, int meta);

    /**
     * Effectively overrides Block.getFlammability
     */
    public int getFlammability(IBlockAccess iba, int x, int y, int z, ForgeDirection side, Block b, int meta);

    /**
     * Effectively overrides Block.getFireSpreadSpeed
     */
    public int getFireSpread(IBlockAccess iba, int x, int y, int z, ForgeDirection side, Block b, int meta);

    /**
     * Effectively overrides Block.isFireSource
     */
    public boolean sustainsFire(IBlockAccess iba, int x, int y, int z, ForgeDirection side, Block b, int meta);

    /**
     * Effectively overrides Block.isWood
     */
    public boolean isLog(IBlockAccess iba, int x, int y, int z, Block b, int meta);

    /**
     * Effectively overrides Block.canEntityDestroy
     */
    public boolean canEntityDestroy(IBlockAccess iba, int x, int y, int z, Entity e, Block b, int meta);

}
