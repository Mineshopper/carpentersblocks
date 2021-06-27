package com.carpentersblocks.api;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * Implement this on your block class to gain full control over the way it behaves
 * as used as a Carpenter's Blocks cover, or to add compatibility that is not
 * otherwise possible.
 */
public interface IWrappableBlock {

    /**
     * Effectively overrides Block.colorMultiplier
     */
    public int getColorMultiplier(IWorld world, BlockState state, BlockPos pos);

    /**
     * Effectively overrides Block.getIcon
     */
    //@SideOnly(Side.CLIENT)
    //public IIcon getIcon(IBlockAccess iba, int x, int y, int z, int side, Block b, int meta);

    /**
     * Effectively overrides Block.isProvidingWeakPower
     */
    public int getWeakRedstone(IWorld world, BlockState state, BlockPos pos);

    /**
     * Effectively overrides Block.isProvidingStrongPower
     */
    public int getStrongRedstone(IWorld world, BlockState state, BlockPos pos);

    /**
     * Effectively overrides Block.getHardness
     */
    public float getHardness(IWorld world, BlockState state, BlockPos pos);

    /**
     * Effectively overrides Block.getExplosionResistance
     */
    public float getBlastResistance(Entity entity, IWorld world, BlockState state, BlockPos pos, BlockPos explosionPos);

    /**
     * Effectively overrides Block.getFlammability
     */
    //public int getFlammability(IBlockAccess iba, int x, int y, int z, ForgeDirection side, Block b, int meta);

    /**
     * Effectively overrides Block.getFireSpreadSpeed
     */
    //public int getFireSpread(IBlockAccess iba, int x, int y, int z, ForgeDirection side, Block b, int meta);

    /**
     * Effectively overrides Block.isFireSource
     */
    //public boolean sustainsFire(IBlockAccess iba, int x, int y, int z, ForgeDirection side, Block b, int meta);

    /**
     * Effectively overrides Block.isWood
     */
    public boolean isLog(IWorld world, BlockState state, BlockPos pos);

    /**
     * Effectively overrides Block.canEntityDestroy
     */
    public boolean canEntityDestroy(IWorld world, BlockState state, BlockPos pos, Entity entity);

}
