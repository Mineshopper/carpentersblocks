package com.carpentersblocks.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.data.Torch;
import com.carpentersblocks.data.Torch.State;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.tileentity.TECarpentersTorch;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.FeatureRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersTorch extends BlockSided {

    private static final Torch data = new Torch();
    public final static String type[] = { "vanilla", "lantern" };

    public BlockCarpentersTorch(Material material)
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
        IconRegistry.icon_torch                 = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "torch/torch");
        IconRegistry.icon_torch_head_lit        = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "torch/torch_head_lit");
        IconRegistry.icon_torch_head_smoldering = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "torch/torch_head_smoldering");
        IconRegistry.icon_torch_head_unlit      = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "torch/torch_head_unlit");
        IconRegistry.icon_lantern_glass         = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "torch/lantern_glass");
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Returns the icon on the side given the block metadata.
     */
    public IIcon getIcon(int side, int metadata)
    {
        return IconRegistry.icon_torch;
    }


    @Override
    /**
     * Cycle forwards through types.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int temp = data.getType(TE);

        if (++temp > type.length - 1) {
            temp = 0;
        }

        data.setType(TE, temp);
        return true;
    }

    @Override
    /**
     * Cycle backwards through types.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int temp = data.getType(TE);

        if (--temp < 0) {
            temp = type.length - 1;
        }

        data.setType(TE, temp);
        return true;
    }

    /**
     * Called when block is activated (right-click), before normal processing resumes.
     */
    @Override
    protected void preOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, ActionResult actionResult)
    {
        ItemStack itemStack = entityPlayer.getHeldItem();

        if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            if (!data.getState(TE).equals(State.LIT)) {
                Block block = BlockProperties.toBlock(itemStack);
                if (block.equals(BlockRegistry.blockCarpentersTorch) || block.equals(Blocks.torch)) {
                    data.setState(TE, State.LIT);
                    actionResult.setAltered();
                }
            }
        }
    }

    /**
     * Gets the current light value based on covers and illumination.
     *
     * @param  blockAccess the {@link IBlockAccess} object
     * @param  x the x coordinate
     * @param  y the y coordinate
     * @param  z the z coordinate
     * @return a light value from 0 to 15
     */
    @Override
    protected int getCurrentLightValue(IBlockAccess blockAccess, int x, int y, int z)
    {
        int lightValue = super.getCurrentLightValue(blockAccess, x, y, z);
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null)
        {
            switch (data.getState(TE)) {
                case LIT:
                    lightValue = 15;
                    break;
                case SMOLDERING:
                    lightValue = Math.max(10, lightValue);
                    break;
                default: {}
            }
        }

        return lightValue;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null && data.getType(TE) == data.TYPE_LANTERN) {
            return super.getCollisionBoundingBoxFromPool(world, x, y, z);
        }

        return null;
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {

            ForgeDirection side = data.getDirection(TE);

            if (data.getType(TE) == data.TYPE_VANILLA) {
                switch (side) {
                    case UP:
                        setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.6F, 0.6F);
                        break;
                    default:
                        setBlockBounds(0.35F, 0.2F, 0.0F, 0.65F, 0.8F, 0.3F, side);
                        break;
                }
            } else {
                switch (side) {
                    case UP:
                        setBlockBounds(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.9375F, 0.8125F);
                        break;
                    default:
                        setBlockBounds(0.1875F, 0.25F, 0.0F, 0.8125F, 0.9375F, 0.8125F, side);
                        break;
                }
            }
        }
    }

    /**
     * Whether block can be attached to specified side of another block.
     *
     * @param  side the side
     * @return whether side is supported
     */
    @Override
    public boolean canAttachToSide(int side)
    {
        return side != 0;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (!world.isRemote) {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null && data.getType(TE) == data.TYPE_VANILLA) {

                boolean isWet = world.canLightningStrikeAt(x, y, z);
                boolean canDropState = FeatureRegistry.enableTorchWeatherEffects;

                switch (data.getState(TE)) {
                    case LIT:
                        if (canDropState && isWet) {
                            data.setState(TE, State.SMOLDERING);
                        }
                        break;
                    case SMOLDERING:
                        if (canDropState && isWet) {
                            data.setState(TE, State.UNLIT);
                        } else {
                            data.setState(TE, State.LIT);
                        }
                        break;
                    case UNLIT:
                        if (!canDropState || !isWet) {
                            data.setState(TE, State.SMOLDERING);
                        }
                        break;
                    default: {}
                }

            }

        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null)
        {
            State state = data.getState(TE);
            if (!state.equals(State.UNLIT))
            {
                double[] headCoords = data.getHeadCoordinates(TE);
                world.spawnParticle("smoke", headCoords[0], headCoords[1], headCoords[2], 0.0D, 0.0D, 0.0D);
                if (state.equals(State.LIT)) {
                    world.spawnParticle("flame", headCoords[0], headCoords[1], headCoords[2], 0.0D, 0.0D, 0.0D);
                }
            }
        }

        super.randomDisplayTick(world, x, y, z, random);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TECarpentersTorch();
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersTorchRenderID;
    }

}
