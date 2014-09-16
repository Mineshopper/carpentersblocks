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
        IconRegistry.icon_torch_lit             = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "torch/torch_lit");
        IconRegistry.icon_torch_head_smoldering = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "torch/torch_head_smoldering");
        IconRegistry.icon_torch_head_unlit      = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "torch/torch_head_unlit");
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Returns the icon on the side given the block metadata.
     */
    public IIcon getIcon(int side, int metadata)
    {
        return IconRegistry.icon_torch_lit;
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

    @Override
    /**
     * Returns light value based on cover or side covers.
     */
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            int coverLight = super.getLightValue(world, x, y, z);
            int torchLight = 0;

            switch (data.getState(TE)) {
                case LIT:
                    torchLight = 15;
                    break;
                case SMOLDERING:
                    torchLight = 10;
                    break;
                default: {}
            }

            return coverLight > torchLight ? coverLight : torchLight;

        }

        return super.getLightValue();
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
            switch (side) {
                case UP:
                    setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.6F, 0.6F);
                    break;
                case NORTH:
                    setBlockBounds(0.35F, 0.2F, 0.7F, 0.65F, 0.8F, 1.0F);
                    break;
                case SOUTH:
                    setBlockBounds(0.35F, 0.2F, 0.0F, 0.65F, 0.8F, 0.3F);
                    break;
                case WEST:
                    setBlockBounds(0.7F, 0.2F, 0.35F, 1.0F, 0.8F, 0.65F);
                    break;
                case EAST:
                    setBlockBounds(0.0F, 0.2F, 0.35F, 0.3F, 0.8F, 0.65F);
                    break;
                default: {}
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

            if (TE != null) {

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

        if (TE != null) {

            State state = data.getState(TE);

            if (!state.equals(State.UNLIT)) {

                double[] headCoords = data.getHeadCoordinates(TE);

                world.spawnParticle("smoke", headCoords[0], headCoords[1], headCoords[2], 0.0D, 0.0D, 0.0D);

                if (state.equals(State.LIT)) {
                    world.spawnParticle("flame", headCoords[0], headCoords[1], headCoords[2], 0.0D, 0.0D, 0.0D);
                }

            }

        }
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
