package com.carpentersblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.data.Lever;
import com.carpentersblocks.data.Lever.Axis;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.EntityLivingUtil;
import com.carpentersblocks.util.handler.ChatHandler;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersLever extends BlockSided {

    private static Lever data = new Lever();

    public BlockCarpentersLever(Material material)
    {
        super(material, data);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        IconRegistry.icon_lever = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "lever/lever");
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Returns the icon on the side given the block metadata.
     */
    public IIcon getIcon(int side, int metadata)
    {
        return IconRegistry.icon_lever;
    }

    @Override
    /**
     * Alters polarity.
     * Handled differently for Levers since type is split into sub-components.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        BlockProperties.getMetadata(TE);
        int polarity = data.getPolarity(TE) == data.POLARITY_POSITIVE ? data.POLARITY_NEGATIVE : data.POLARITY_POSITIVE;

        data.setPolarity(TE, polarity);
        notifyBlocksOfPowerChange(TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord);

        if (polarity == data.POLARITY_POSITIVE) {
            ChatHandler.sendMessageToPlayer("message.polarity_pos.name", entityPlayer);
        } else {
            ChatHandler.sendMessageToPlayer("message.polarity_neg.name", entityPlayer);
        }

        return true;
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta < 2) {
                ForgeDirection dir = EntityLivingUtil.getFacing(entityLiving);
                if (dir.equals(ForgeDirection.NORTH) || dir.equals(ForgeDirection.SOUTH)) {
                    data.setAxis(TE, Axis.Z);
                }
            } else {
                if (meta == ForgeDirection.NORTH.ordinal() || meta == ForgeDirection.SOUTH.ordinal()) {
                    data.setAxis(TE, Axis.Z);
                }
            }
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            ForgeDirection side = data.getDirection(TE);
            Axis axis = data.getAxis(TE);

            float offset = 0.1875F;

            switch (side) {
                case DOWN:
                    if (axis.equals(Axis.X)) {
                        setBlockBounds(0.2F, 1.0F - offset, 0.5F - offset, 0.8F, 1.0F, 0.5F + offset);
                    } else {
                        setBlockBounds(0.5F - offset, 1.0F - offset, 0.2F, 0.5F + offset, 1.0F, 0.8F);
                    }
                    break;
                case UP:
                    if (axis.equals(Axis.X)) {
                        setBlockBounds(0.2F, 0.0F, 0.5F - offset, 0.8F, offset, 0.5F + offset);
                    } else {
                        setBlockBounds(0.5F - offset, 0.0F, 0.2F, 0.5F + offset, offset, 0.8F);
                    }
                    break;
                default:
                    setBlockBounds(0.5F - offset, 0.2F, 0.0F, 0.5F + offset, 0.8F, offset, side);
                    break;
            }

        }
    }

    @Override
    /**
     * Called upon block activation.
     */
    protected void postOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, ActionResult actionResult)
    {
        data.setState(TE, isActive(TE) ? data.STATE_OFF : data.STATE_ON, true);

        World world = TE.getWorldObj();

        world.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, this);
        notifyBlocksOfPowerChange(world, TE.xCoord, TE.yCoord, TE.zCoord);

        actionResult.setAltered().setNoSound();
    }

    /**
     * Returns whether lever is in active state
     */
    private boolean isActive(TEBase TE)
    {
        return data.getState(TE) == data.STATE_ON;
    }

    /**
     * Returns power level (0 or 15)
     */
    @Override
    public int getPowerOutput(TEBase TE)
    {
        if (isActive(TE)) {
            return data.getPolarity(TE) == data.POLARITY_POSITIVE ? 15 : 0;
        } else {
            return data.getPolarity(TE) == data.POLARITY_NEGATIVE ? 15 : 0;
        }
    }

    @Override
    /**
     * Ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        TEBase TE = getSimpleTileEntity(world, x, y, z);

        if (TE != null) {
            if (isActive(TE)) {
                world.notifyBlocksOfNeighborChange(x, y, z, block);
                notifyBlocksOfPowerChange(world, x, y, z);
            }
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersLeverRenderID;
    }

}
