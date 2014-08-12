package com.carpentersblocks.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.data.FlowerPot;
import com.carpentersblocks.network.PacketEnrichPlant;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.tileentity.TECarpentersFlowerPot;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.flowerpot.FlowerPotHandler;
import com.carpentersblocks.util.flowerpot.FlowerPotHandler.Profile;
import com.carpentersblocks.util.flowerpot.FlowerPotProperties;
import com.carpentersblocks.util.handler.EventHandler;
import com.carpentersblocks.util.handler.PacketHandler;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersFlowerPot extends BlockCoverable {

    public BlockCarpentersFlowerPot(Material material)
    {
        super(material);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        IconRegistry.icon_flower_pot       = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "flowerpot/flower_pot");
        IconRegistry.icon_flower_pot_glass = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "flowerpot/flower_pot_glass");
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Returns the icon on the side given the block metadata.
     */
    public IIcon getIcon(int side, int metadata)
    {
        /*
         * This doesn't work perfectly, but it's necessary to render
         * the pot as an Item in the inventory.  Block destruction will
         * spawn cover and block icons as a result.
         */
        if (side == 1 && metadata == 0) {
            return IconRegistry.icon_flower_pot;
        } else {
            return super.getIcon(side, metadata);
        }
    }

    @Override
    /**
     * Cycle backward through bed designs.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        BlockProperties.setPrevDesign(TE);

        if (BlockProperties.hasCover(TE, 6)) {
            BlockProperties.setCover(TE, 6, null);
        }

        return true;
    }

    @Override
    /**
     * Cycle forward through designs or set to no design.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        if (entityPlayer.isSneaking()) {
            BlockProperties.clearDesign(TE);
        } else {
            BlockProperties.setNextDesign(TE);
        }

        if (BlockProperties.hasCover(TE, 6)) {
            BlockProperties.setCover(TE, 6, null);
        }

        return true;
    }

    @Override
    /**
     * Sneak-click removes plant and/or soil.
     */
    protected void preOnBlockClicked(TEBase TE, World world, int x, int y, int z, EntityPlayer entityPlayer, ActionResult actionResult)
    {
        if (entityPlayer.isSneaking()) {

            if (EventHandler.hitY > 0.375F) {

                if (FlowerPot.isEnriched(TE)) {
                    actionResult.setSoundSource(new ItemStack(Blocks.sand));
                    FlowerPot.setEnrichment(TE, false);
                    actionResult.setAltered();
                }

                if (FlowerPotProperties.hasPlant(TE)) {
                    actionResult.setSoundSource(FlowerPotProperties.getPlant(TE));
                    FlowerPotProperties.setPlant(TE, (ItemStack)null);
                    actionResult.setAltered();
                }

            } else if (FlowerPotProperties.hasSoil(TE)) {

                if (EventHandler.eventFace == 1 && EventHandler.hitX > 0.375F && EventHandler.hitX < 0.625F && EventHandler.hitZ > 0.375F && EventHandler.hitZ < 0.625F) {
                    actionResult.setSoundSource(FlowerPotProperties.getSoil(TE));
                    FlowerPotProperties.setSoil(TE, (ItemStack)null);
                    actionResult.setAltered();
                }

            }

        }
    }

    @Override
    /**
     * Everything contained in this will run before default onBlockActivated events take place,
     * but after the player has been verified to have permission to edit block.
     */
    protected void preOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, ActionResult actionResult)
    {
        ItemStack itemStack = entityPlayer.getHeldItem();

        if (itemStack != null) {

            boolean hasCover = BlockProperties.hasCover(TE, 6);
            boolean hasOverlay = BlockProperties.hasOverlay(TE, 6);
            boolean soilAreaClicked = side == 1 && hitX > 0.375F && hitX < 0.625F && hitZ > 0.375F && hitZ < 0.625F;

            if (FlowerPotProperties.hasSoil(TE)) {

                /*
                 * Leaf blocks can be plants or covers.  We need to differentiate
                 * it based on where the block is clicked, and whether it already
                 * has a cover.
                 */
                if (!soilAreaClicked) {
                    if (!hasCover && BlockProperties.isCover(itemStack) || !hasOverlay && BlockProperties.isOverlay(itemStack)) {
                        return;
                    }
                }

                if (!FlowerPotProperties.hasPlant(TE)) {

                    if (FlowerPotProperties.isPlant(itemStack)) {
                        int angle = MathHelper.floor_double((entityPlayer.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 15;
                        FlowerPot.setAngle(TE, angle);
                        FlowerPotProperties.setPlant(TE, itemStack);
                        actionResult.setAltered().setSoundSource(itemStack).decInventory();
                    }

                }

            } else {

                if (FlowerPotProperties.isSoil(itemStack)) {

                    if (hasCover || soilAreaClicked) {
                        FlowerPotProperties.setSoil(TE, itemStack);
                        actionResult.setAltered().setSoundSource(itemStack).decInventory();
                    }

                }

            }

        }
    }

    @Override
    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        /*
         * Need to handle plant enrichment here since the properties
         * needing to be compared against are client-side only.
         *
         * Client will send relevant properties to the server using a packet,
         * and from there the server will determine if plant should be affected.
         */

        if (world.isRemote) {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {

                ItemStack itemStack = entityPlayer.getCurrentEquippedItem();

                if (itemStack != null && itemStack.getItem().equals(Items.dye) && itemStack.getItemDamage() == 15) {
                    if (!FlowerPot.isEnriched(TE) && FlowerPotProperties.isPlantColorable(TE)) {
                        PacketHandler.sendPacketToServer(new PacketEnrichPlant(x, y, z, FlowerPotProperties.getPlantColor(TE)));
                        return true;
                    }
                }

            }

        }

        return super.onBlockActivated(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (!world.isRemote) {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {

                if (!canPlaceBlockOnSide(world, x, y, z, 1)) {
                    dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                    world.setBlockToAir(x, y, z);
                }

                /* Eject double tall plant if obstructed. */

                if (FlowerPotProperties.hasPlant(TE)) {

                    Profile profile = FlowerPotHandler.getPlantProfile(TE);

                    if (profile.equals(Profile.DOUBLEPLANT) || profile.equals(Profile.THIN_DOUBLEPLANT)) {
                        if (world.getBlock(x, y + 1, z).isSideSolid(world, x, y + 1, z, ForgeDirection.DOWN)) {
                            FlowerPotProperties.setPlant(TE, (ItemStack)null);
                        }
                    }

                }

            }

        }

        super.onNeighborBlockChange(world, x, y, z, block);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        Block block_YN = world.getBlock(x, y - 1, z);
        return block_YN.isSideSolid(world, x, y - 1, z, ForgeDirection.UP) || block_YN.canPlaceTorchOnTop(world, x, y - 1, z);
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null && TE instanceof TECarpentersFlowerPot) {

            if (FlowerPotProperties.hasPlant(TE)) {

                switch (FlowerPotHandler.getPlantProfile(TE)) {
                    case CACTUS:
                    case LEAVES:
                        setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.99F, 0.6875F);
                        break;
                    default:
                        setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.75F, 0.6875F);
                }

            } else {

                setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.375F, 0.6875F);

            }

        }
    }

    @Override
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null && TE instanceof TECarpentersFlowerPot) {

            AxisAlignedBB axisAlignedBB = AxisAlignedBB.getBoundingBox(x + 0.3125F, y, z + 0.3125F, x + 0.6875F, y + 0.375F, z + 0.6875F);

            if (FlowerPotProperties.hasPlant(TE)) {

                switch (FlowerPotHandler.getPlantProfile(TE)) {
                    case CACTUS:
                    case LEAVES:
                        axisAlignedBB = AxisAlignedBB.getBoundingBox(x + 0.3125F, y, z + 0.3125F, x + 0.6875F, y + 0.99F, z + 0.6875F);
                        break;
                    default: {}
                }

            }

            return axisAlignedBB;

        }

        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    /**
     * Returns light value based on plant and soil in pot.
     */
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null && TE instanceof TECarpentersFlowerPot) {

            int coverLight = super.getLightValue(world, x, y, z);
            int potLight = getLightValue();

            if (FlowerPotProperties.hasSoil(TE)) {

                int soil_lightValue = BlockProperties.toBlock(FlowerPotProperties.getSoil(TE)).getLightValue();

                if (soil_lightValue > potLight) {
                    potLight = soil_lightValue;
                }

            }

            if (FlowerPotProperties.hasPlant(TE)) {

                int plant_lightValue = FlowerPotProperties.toBlock(FlowerPotProperties.getPlant(TE)).getLightValue();

                if (plant_lightValue > potLight) {
                    potLight = plant_lightValue;
                }

            }

            return coverLight > potLight ? coverLight : potLight;

        }

        return getLightValue();
    }

    @Override
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null && TE instanceof TECarpentersFlowerPot) {

            if (FlowerPotProperties.hasPlant(TE)) {

                ItemStack itemStack = FlowerPotProperties.getPlant(TE);

                BlockProperties.setHostMetadata(TE, itemStack.getItemDamage());
                FlowerPotProperties.toBlock(itemStack).onEntityCollidedWithBlock(world, x, y, z, entity);
                BlockProperties.resetHostMetadata(TE);

            }

        }

        super.onEntityCollidedWithBlock(world, x, y, z, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns true only if block is flowerPot
     */
    public boolean isFlowerPot()
    {
        return true;
    }

    @Override
    protected boolean canCoverSide(TEBase TE, World world, int x, int y, int z, int side)
    {
        return side == 6 ? !BlockProperties.hasDesign(TE) : false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null && TE instanceof TECarpentersFlowerPot) {

            /*
             * Metadata at coordinates are for the base cover only.
             * We need to set it for appropriate attributes in order
             * to get accurate results.
             */

            if (FlowerPotProperties.hasPlant(TE)) {

                ItemStack itemStack = FlowerPotProperties.getPlant(TE);

                BlockProperties.setHostMetadata(TE, itemStack.getItemDamage());
                FlowerPotProperties.toBlock(itemStack).randomDisplayTick(world, x, y, z, random);
                BlockProperties.resetHostMetadata(TE);

            }

            if (FlowerPotProperties.hasSoil(TE)) {

                ItemStack itemStack = FlowerPotProperties.getSoil(TE);

                BlockProperties.setHostMetadata(TE, itemStack.getItemDamage());
                BlockProperties.toBlock(itemStack).randomDisplayTick(world, x, y, z, random);
                BlockProperties.resetHostMetadata(TE);

            }

        }

        super.randomDisplayTick(world, x, y, z, random);
    }

    @Override
    /**
     * Ejects contained items into the world, and notifies neighbors of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        TEBase TE = getSimpleTileEntity(world, x, y, z);

        if (TE != null && TE instanceof TECarpentersFlowerPot) {

            if (FlowerPot.isEnriched(TE)) {
                FlowerPot.setEnrichment(TE, false);
            }
            if (FlowerPotProperties.hasPlant(TE)) {
                FlowerPotProperties.setPlant(TE, (ItemStack)null);
            }
            if (FlowerPotProperties.hasSoil(TE)) {
                FlowerPotProperties.setSoil(TE, (ItemStack)null);
            }

        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TECarpentersFlowerPot();
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersFlowerPotRenderID;
    }

}
