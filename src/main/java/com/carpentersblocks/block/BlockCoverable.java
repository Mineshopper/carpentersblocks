package com.carpentersblocks.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.api.ICarpentersChisel;
import com.carpentersblocks.api.ICarpentersHammer;
import com.carpentersblocks.renderer.helper.FancyFluidsHelper;
import com.carpentersblocks.renderer.helper.ParticleHelper;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.EntityLivingUtil;
import com.carpentersblocks.util.handler.DesignHandler;
import com.carpentersblocks.util.handler.EventHandler;
import com.carpentersblocks.util.handler.OverlayHandler;
import com.carpentersblocks.util.handler.OverlayHandler.Overlay;
import com.carpentersblocks.util.protection.PlayerPermissions;
import com.carpentersblocks.util.registry.FeatureRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import com.carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCoverable extends BlockContainer {

    /** Used when grabbing light value of covers. */
    protected boolean grabLightValue = false;

    /** Indicates during getDrops that block instance should not be dropped. */
    protected final int METADATA_DROP_ATTR_ONLY = 16;

    /** Whether breakBlock() should drop block attributes. */
    private boolean enableDrops = false;

    /**
     * Stores actions taken on a block in order to properly play sounds,
     * decrement player inventory, and to determine if a block was altered.
     */
    protected class ActionResult {

        public ItemStack itemStack;
        public boolean playSound = true;
        public boolean altered = false;
        public boolean decInv = false;

        public ActionResult setSoundSource(ItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        public ActionResult setNoSound() {
            playSound = false;
            return this;
        }

        public ActionResult setAltered() {
            altered = true;
            return this;
        }

        public ActionResult decInventory() {
            decInv = true;
            return this;
        }

    }

    /**
     * Class constructor.
     *
     * @param material
     */
    public BlockCoverable(Material material)
    {
        super(material);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerBlockIcons(IIconRegister iconRegister) { }

    @SideOnly(Side.CLIENT)
    /**
     * Returns a base icon that doesn't rely on blockIcon, which
     * is set prior to texture stitch events.
     *
     * @return default icon
     */
    public IIcon getIcon()
    {
        return IconRegistry.icon_uncovered_solid;
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Returns the icon on the side given the block metadata.
     * <p>
     * Due to the amount of control needed over this, vanilla calls will always return an invisible icon.
     */
    public IIcon getIcon(int side, int metadata)
    {
        if (BlockProperties.isMetadataDefaultIcon(metadata)) {
            return getIcon();
        }

        /*
         * This icon is a mask (or something) for redstone wire.
         * We use it here because it renders an invisible icon.
         *
         * Using an invisible icon is important because sprint particles are
         * hard-coded and will always grab particle icons using this method.
         * We'll throw our own sprint particles in EventHandler.class.
         */

        return BlockRedstoneWire.getRedstoneWireIcon("cross_overlay");
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);
        ItemStack itemStack = BlockProperties.getCover(TE, 6);
        Block block = BlockProperties.toBlock(itemStack);

        return block instanceof BlockCoverable ? getIcon() : block.getIcon(side, itemStack.getItemDamage());
    }

    /**
     * For South-sided blocks, rotates and sets the block bounds using
     * the provided ForgeDirection.
     *
     * @param  dir the rotated {@link ForgeDirection}
     */
    protected void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, ForgeDirection dir)
    {
        switch (dir) {
            case DOWN:
                setBlockBounds(minX, 1.0F - maxZ, minY, maxX, 1.0F - minZ, maxY);
                break;
            case UP:
                setBlockBounds(minX, minZ, minY, maxX, maxZ, maxY);
                break;
            case NORTH:
                setBlockBounds(1.0F - maxX, minY, 1.0F - maxZ, 1.0F - minX, maxY, 1.0F - minZ);
                break;
            case EAST:
                setBlockBounds(minZ, minY, 1.0F - maxX, maxZ, maxY, 1.0F - minX);
                break;
            case WEST:
                setBlockBounds(1.0F - maxZ, minY, minX, 1.0F - minZ, maxY, maxX);
                break;
            default:
                setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
                break;
        }
    }

    /**
     * Called when block event is received.
     *<p>
     * For the context of this mod, this is used for dropping block attributes
     * like covers, overlays, dyes, or any other ItemStack.
     *<p>
     * In order for external classes to call the protected method
     * {@link Block#dropBlockAsItem(World,int,int,int,ItemStack) dropBlockAsItem},
     * they create a block event with parameters itemId and metadata, allowing
     * the {@link ItemStack} to be recreated and dropped.
     *
     * @param  world the {@link World}
     * @param  x the x coordinate
     * @param  y the y coordinate
     * @param  z the z coordinate
     * @param  itemId the eventId, repurposed
     * @param  metadata the event parameter, repurposed
     * @return true if event was handled
     */
    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int itemId/*eventId*/, int metadata/*param*/)
    {
        ItemStack itemStack = new ItemStack(Item.getItemById(itemId), 1, metadata);

        if (itemStack != null) {
            dropBlockAsItem(world, x, y, z, itemStack);
        }

        return true;
    }

    /**
     * Drops block as {@link ItemStack} and notifies relevant systems of
     * block removal.  Block attributes will drop later in destruction.
     * <p>
     * This is usually called when a {@link #onNeighborBlockChange(World, int, int, int, Block) neighbor changes}.
     *
     * @param world the {@link World}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param dropBlock whether block {@link ItemStack} is dropped
     */
    protected void destroyBlock(World world, int x, int y, int z, boolean dropBlock)
    {
        if (dropBlock) {
            dropBlockAsItem(world, x, y, z, new ItemStack(getItemDropped(0, world.rand, 0)));
        }
        world.setBlockToAir(x, y, z);
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack getItemDrop(World world, int metadata)
    {
        int fortune = 1;
        return new ItemStack(getItemDropped(metadata, world.rand, fortune), 1, metadata);
    }

    /**
     * Returns adjacent, similar tile entities that can be used for duplicating
     * block properties like dye color, pattern, style, etc.
     *
     * @param  world the world reference
     * @param  x the x coordinate
     * @param  y the y coordinate
     * @param  z the z coordinate
     * @return an array of adjacent, similar tile entities
     * @see {@link TEBase}
     */
    protected TEBase[] getAdjacentTileEntities(World world, int x, int y, int z)
    {
        return new TEBase[] {
            getSimpleTileEntity(world, x, y - 1, z),
            getSimpleTileEntity(world, x, y + 1, z),
            getSimpleTileEntity(world, x, y, z - 1),
            getSimpleTileEntity(world, x, y, z + 1),
            getSimpleTileEntity(world, x - 1, y, z),
            getSimpleTileEntity(world, x + 1, y, z)
        };
    }

    /**
     * Returns tile entity if block tile entity is instanceof TEBase.
     *
     * Used for generic purposes such as getting pattern, dye color, or
     * cover of another Carpenter's block.  Is also used if block
     * no longer exists, such as when breaking a block and ejecting
     * attributes.
     */
    protected TEBase getSimpleTileEntity(IBlockAccess blockAccess, int x, int y, int z)
    {
        TileEntity TE = blockAccess.getTileEntity(x, y, z);
        return (TE instanceof TEBase) ? (TEBase) TE : null;
    }

    /**
     * Returns tile entity if block tile entity is instanceof TEBase and
     * also belongs to this block type.
     */
    protected TEBase getTileEntity(IBlockAccess blockAccess, int x, int y, int z)
    {
        TEBase TE = getSimpleTileEntity(blockAccess, x, y, z);
        return TE != null && blockAccess.getBlock(x, y, z).equals(this) ? TE : null;
    }

    /**
     * Returns whether player is allowed to activate this block.
     */
    protected boolean canPlayerActivate(TEBase TE, EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer)
    {
        if (!world.isRemote)
        {
            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null)
            {
                if (PlayerPermissions.canPlayerEdit(TE, TE.xCoord, TE.yCoord, TE.zCoord, entityPlayer))
                {
                    ActionResult actionResult = new ActionResult();
                    ItemStack itemStack = entityPlayer.getCurrentEquippedItem();

                    if (itemStack != null) {

                        int effectiveSide = TE.hasAttribute(TE.ATTR_COVER[EventHandler.eventFace]) ? EventHandler.eventFace : 6;
                        Item item = itemStack.getItem();

                        if (item instanceof ICarpentersHammer && ((ICarpentersHammer)item).canUseHammer(world, entityPlayer)) {

                            preOnBlockClicked(TE, world, x, y, z, entityPlayer, actionResult);

                            if (!actionResult.altered) {

                                if (entityPlayer.isSneaking()) {

                                    if (TE.hasAttribute(TE.ATTR_ILLUMINATOR)) {
                                        TE.removeAttribute(TE.ATTR_ILLUMINATOR);
                                        actionResult.setAltered();
                                    } else if (TE.hasAttribute(TE.ATTR_OVERLAY[effectiveSide])) {
                                        TE.removeAttribute(TE.ATTR_OVERLAY[effectiveSide]);
                                        actionResult.setAltered();
                                    } else if (TE.hasAttribute(TE.ATTR_DYE[effectiveSide])) {
                                        TE.removeAttribute(TE.ATTR_DYE[effectiveSide]);
                                        actionResult.setAltered();
                                    } else if (TE.hasAttribute(TE.ATTR_COVER[effectiveSide])) {
                                        TE.removeAttribute(TE.ATTR_COVER[effectiveSide]);
                                        TE.removeChiselDesign(effectiveSide);
                                        actionResult.setAltered();
                                    }

                                } else {

                                    onHammerLeftClick(TE, entityPlayer);
                                    actionResult.setAltered();

                                }

                            }

                            if (actionResult.altered) {
                                onNeighborBlockChange(world, x, y, z, this);
                                world.notifyBlocksOfNeighborChange(x, y, z, this);
                            }

                        } else if (item instanceof ICarpentersChisel && ((ICarpentersChisel)item).canUseChisel(world, entityPlayer)) {

                            if (entityPlayer.isSneaking()) {

                                if (TE.hasChiselDesign(effectiveSide)) {
                                    TE.removeChiselDesign(effectiveSide);
                                }

                            } else if (TE.hasAttribute(TE.ATTR_COVER[effectiveSide])) {

                                onChiselClick(TE, effectiveSide, true);

                            }
                        }
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
        ItemStack itemStack = entityPlayer.getCurrentEquippedItem();

        if (world.isRemote) {

            return true;

        } else {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {

                ActionResult actionResult = new ActionResult();

                if (canPlayerActivate(TE, entityPlayer)) {

                    // Allow block to change TE if needed before altering attributes
                    TE = getTileEntityForBlockActivation(TE);

                    preOnBlockActivated(TE, entityPlayer, side, hitX, hitY, hitZ, actionResult);

                    /* Sides 0-5 are side covers, and 6 is the base block. */
                    int effectiveSide = TE.hasAttribute(TE.ATTR_COVER[side]) ? side : 6;

                    if (PlayerPermissions.canPlayerEdit(TE, TE.xCoord, TE.yCoord, TE.zCoord, entityPlayer)) {

                        if (!actionResult.altered) {

                            if (itemStack != null) {

                                if (itemStack.getItem() instanceof ICarpentersHammer && ((ICarpentersHammer)itemStack.getItem()).canUseHammer(world, entityPlayer)) {

                                    if (onHammerRightClick(TE, entityPlayer)) {
                                        actionResult.setAltered();
                                    }

                                } else if (ItemRegistry.enableChisel && itemStack.getItem() instanceof ICarpentersChisel && ((ICarpentersChisel)itemStack.getItem()).canUseChisel(world, entityPlayer)) {

                                    if (TE.hasAttribute(TE.ATTR_COVER[effectiveSide])) {
                                        if (onChiselClick(TE, effectiveSide, false)) {
                                            actionResult.setAltered();
                                        }
                                    }

                                } else if (FeatureRegistry.enableCovers && BlockProperties.isCover(itemStack)) {

                                    Block block = BlockProperties.toBlock(itemStack);

                                    /* Will handle blocks that save directions using only y axis (pumpkin) */
                                    int metadata = block instanceof BlockDirectional ? MathHelper.floor_double(entityPlayer.rotationYaw * 4.0F / 360.0F + 2.5D) & 3 : itemStack.getItemDamage();

                                    /* Will handle blocks that save directions using all axes (logs, quartz) */
                                    if (BlockProperties.blockRotates(itemStack)) {
                                        int rot = Direction.rotateOpposite[EntityLivingUtil.getRotationValue(entityPlayer)];
                                        int side_interpolated = entityPlayer.rotationPitch < -45.0F ? 0 : entityPlayer.rotationPitch > 45 ? 1 : rot == 0 ? 3 : rot == 1 ? 4 : rot == 2 ? 2 : 5;
                                        metadata = block.onBlockPlaced(world, TE.xCoord, TE.yCoord, TE.zCoord, side_interpolated, hitX, hitY, hitZ, metadata);
                                    }

                                    ItemStack tempStack = itemStack.copy();
                                    tempStack.setItemDamage(metadata);

                                    /* Base cover should always be checked. */

                                    if (effectiveSide == 6 && (!canCoverSide(TE, world, TE.xCoord, TE.yCoord, TE.zCoord, 6) || TE.hasAttribute(TE.ATTR_COVER[6]))) {
                                        effectiveSide = side;
                                    }

                                    if (canCoverSide(TE, world, TE.xCoord, TE.yCoord, TE.zCoord, effectiveSide) && !TE.hasAttribute(TE.ATTR_COVER[effectiveSide])) {
                                        TE.addAttribute(TE.ATTR_COVER[effectiveSide], tempStack);
                                        actionResult.setAltered().decInventory().setSoundSource(itemStack);
                                    }

                                } else if (entityPlayer.isSneaking()) {

                                    if (FeatureRegistry.enableIllumination && BlockProperties.isIlluminator(itemStack)) {
                                        if (!TE.hasAttribute(TE.ATTR_ILLUMINATOR)) {
                                            TE.addAttribute(TE.ATTR_ILLUMINATOR, itemStack);
                                            actionResult.setAltered().decInventory().setSoundSource(itemStack);
                                        }
                                    } else if (FeatureRegistry.enableOverlays && BlockProperties.isOverlay(itemStack)) {
                                        if (!TE.hasAttribute(TE.ATTR_OVERLAY[effectiveSide]) && (effectiveSide < 6 && TE.hasAttribute(TE.ATTR_COVER[effectiveSide]) || effectiveSide == 6)) {
                                            TE.addAttribute(TE.ATTR_OVERLAY[effectiveSide], itemStack);
                                            actionResult.setAltered().decInventory().setSoundSource(itemStack);
                                        }
                                    } else if (FeatureRegistry.enableDyeColors && BlockProperties.isDye(itemStack, false)) {
                                        if (!TE.hasAttribute(TE.ATTR_DYE[effectiveSide])) {
                                            TE.addAttribute(TE.ATTR_DYE[effectiveSide], itemStack);
                                            actionResult.setAltered().decInventory().setSoundSource(itemStack);
                                        }
                                    }

                                }
                            }
                        }
                    }

                    if (!actionResult.altered) {

                        postOnBlockActivated(TE, entityPlayer, side, hitX, hitY, hitZ, actionResult);

                    } else {

                        if (actionResult.itemStack == null) {
                            actionResult.setSoundSource(BlockProperties.getCover(TE, 6));
                        }
                        damageItemWithChance(world, entityPlayer);
                        onNeighborBlockChange(world, TE.xCoord, TE.yCoord, TE.zCoord, this);
                        world.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, this);

                    }

                    if (actionResult.playSound) {
                        BlockProperties.playBlockSound(TE.getWorldObj(), actionResult.itemStack, TE.xCoord, TE.yCoord, TE.zCoord, false);
                    }

                    if (actionResult.decInv) {
                        EntityLivingUtil.decrementCurrentSlot(entityPlayer);
                    }

                }

                return actionResult.altered;

            }
        }

        return super.onBlockActivated(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
    }

    /**
     * Cycles through chisel patterns.
     */
    public boolean onChiselClick(TEBase TE, int side, boolean leftClick)
    {
        String design = TE.getChiselDesign(side);
        String designAdj = "";

        if (design.equals("")) {

            World world = TE.getWorldObj();

            /* Match pattern with adjacent pattern if possible. */

            TEBase[] TE_list = getAdjacentTileEntities(world, TE.xCoord, TE.yCoord, TE.zCoord);

            for (TEBase TE_current : TE_list) {
                if (TE_current != null) {
                    TE_current.getBlockType();
                    if (TE_current.hasChiselDesign(side)) {
                        design = TE_current.getChiselDesign(side);
                        designAdj = design;
                    }
                }
            }

        }

        if (designAdj.equals("")) {
            design = leftClick ? DesignHandler.getPrev("chisel", design) : DesignHandler.getNext("chisel", design);
        }

        if (!design.equals("")) {
            TE.setChiselDesign(side, design);
        }

        return true;
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        /* This will check for and eject side covers that are obstructed. */

        if (!world.isRemote) {
            TEBase TE = getTileEntity(world, x, y, z);
            if (TE != null) {
                for (int side = 0; side < 6; ++side) {
                    if (TE.hasAttribute(TE.ATTR_COVER[side])) {
                        if (!canCoverSide(TE, world, x, y, z, side)) {
                            TE.removeAttributes(side);
                            continue;
                        }
                        ForgeDirection dir = ForgeDirection.getOrientation(side);
                        if (world.isSideSolid(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite()) && isSideSolid(world, x, y, z, dir)) {
                            TE.removeAttributes(side);
                        }
                    }
                }
            }
        }
    }

    @Override
    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);
        int power = 0;

        /* Indirect power is provided by any cover. */

        if (TE != null) {
            for (int idx = 0; idx < 7; ++idx) {
                if (TE.hasAttribute(TE.ATTR_COVER[idx])) {
                    Block block = BlockProperties.toBlock(BlockProperties.getCover(TE, idx));
                    int tempPower = block.isProvidingWeakPower(blockAccess, x, y, z, side);
                    if (tempPower > power) {
                        power = tempPower;
                    }
                }
            }
        }

        return power;
    }

    @Override
    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);
        int power = 0;

        /* Strong power is provided by the base cover, or a side cover if one exists. */

        if (TE != null) {
            int effectiveSide = ForgeDirection.OPPOSITES[side];
            if (TE.hasAttribute(TE.ATTR_COVER[effectiveSide])) {
                Block block = BlockProperties.toBlock(BlockProperties.getCover(TE, effectiveSide));
                int tempPower = block.isProvidingWeakPower(blockAccess, x, y, z, side);
                if (tempPower > power) {
                    power = tempPower;
                }
            } else if (TE.hasAttribute(TE.ATTR_COVER[6])) {
                Block block = BlockProperties.toBlock(BlockProperties.getCover(TE, 6));
                int tempPower = block.isProvidingWeakPower(blockAccess, x, y, z, side);
                if (tempPower > power) {
                    power = tempPower;
                }
            }
        }

        return power;
    }

    /**
     * Indicates whether block destruction should be suppressed when block is clicked.
     * Will return true if player is holding a Carpenter's tool in creative mode.
     */
    protected boolean suppressDestroyBlock(EntityPlayer entityPlayer)
    {
        ItemStack itemStack = entityPlayer.getHeldItem();

        if (itemStack != null) {
            Item item = itemStack.getItem();
            return entityPlayer.capabilities.isCreativeMode && item != null && (item instanceof ICarpentersHammer || item instanceof ICarpentersChisel);
        }

        return false;
    }

    @Override
    /**
     * Called when a player removes a block.
     * This controls block break behavior when a player in creative mode left-clicks on block while holding a Carpenter's Hammer
     */
    public boolean removedByPlayer(World world, EntityPlayer entityPlayer, int x, int y, int z)
    {
        if (!suppressDestroyBlock(entityPlayer)) {
            return super.removedByPlayer(world, entityPlayer, x, y, z);
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Spawn a digging particle effect in the world, this is a wrapper
     * around EffectRenderer.addBlockHitEffects to allow the block more
     * control over the particles. Useful when you have entirely different
     * texture sheets for different sides/locations in the world.
     *
     * @param world The current world
     * @param target The target the player is looking at {x/y/z/side/sub}
     * @param effectRenderer A reference to the current effect renderer.
     * @return True to prevent vanilla digging particles form spawning.
     */
    public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
        TEBase TE = getTileEntity(world, target.blockX, target.blockY, target.blockZ);

        if (TE != null) {

            int effectiveSide = TE.hasAttribute(TE.ATTR_COVER[target.sideHit]) ? target.sideHit : 6;
            ItemStack itemStack = BlockProperties.getCover(TE, effectiveSide);

            if (TE.hasAttribute(TE.ATTR_OVERLAY[effectiveSide])) {
                Overlay overlay = OverlayHandler.getOverlayType(TE.getAttribute(TE.ATTR_OVERLAY[effectiveSide]));
                if (OverlayHandler.coversFullSide(overlay, target.sideHit)) {
                    itemStack = overlay.getItemStack();
                }
            }

            Block block = BlockProperties.toBlock(itemStack);

            double xOffset = target.blockX + world.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinX();
            double yOffset = target.blockY + world.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinY();
            double zOffset = target.blockZ + world.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinZ();

            switch (target.sideHit) {
                case 0:
                    yOffset = target.blockY + block.getBlockBoundsMinY() - 0.1D;
                    break;
                case 1:
                    yOffset = target.blockY + block.getBlockBoundsMaxY() + 0.1D;
                    break;
                case 2:
                    zOffset = target.blockZ + block.getBlockBoundsMinZ() - 0.1D;
                    break;
                case 3:
                    zOffset = target.blockZ + block.getBlockBoundsMaxZ() + 0.1D;
                    break;
                case 4:
                    xOffset = target.blockX + block.getBlockBoundsMinX() - 0.1D;
                    break;
                case 5:
                    xOffset = target.blockX + block.getBlockBoundsMaxX() + 0.1D;
                    break;
            }

            ParticleHelper.addHitEffect(TE, target, xOffset, yOffset, zOffset, itemStack, effectRenderer);

            return true;

        }

        return super.addHitEffects(world, target, effectRenderer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Renders block destruction effects.
     * This is controlled to prevent block destroy effects if left-clicked with a Carpenter's Hammer while player is in creative mode.
     *
     * Returns false to display effects.  True suppresses them (backwards).
     */
    public boolean addDestroyEffects(World world, int x, int y, int z, int metadata, EffectRenderer effectRenderer)
    {
        /*
         * We don't have the ability to accurately determine the entity that is
         * hitting the block. So, instead we're guessing based on who is
         * closest. This should be adequate most of the time.
         */

        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            EntityPlayer entityPlayer = world.getClosestPlayer(x, y, z, 6.5F);

            if (entityPlayer != null) {
                if (!suppressDestroyBlock(entityPlayer)) {
                    ParticleHelper.addDestroyEffect(world, x, y, z, BlockProperties.getCover(TE, 6), effectRenderer);
                } else {
                    return true;
                }
            }

        }

        return false;
    }

    /**
     * Returns light value for block using two methods.  First, it
     * checks if the static light value is not zero.  If zero, it checks
     * using the block metadata.
     *
     * @param itemStack
     * @return
     */
    protected int getLightValue(TEBase TE, Block block, int metadata)
    {
        /* Grab static light value */

        int lightValue = block.getLightValue();

        /* Try grabbing more accurate lighting using metadata */

        if (lightValue == 0) {
            TE.setMetadata(metadata);
            lightValue = block.getLightValue(TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord);
            TE.restoreMetadata();
        }

        return lightValue;
    }

    @Override
    /**
     * Returns light value based on cover or side covers.
     */
    public int getLightValue(IBlockAccess blockAccess, int x, int y, int z)
    {
        int lightValue = 0;

        /*
         * Block.class will call this method by default if the passed
         * in coordinates don't match the expected block type.  Because
         * we're passing in covers, it may recurse.
         *
         * Return 0 when this happens.
         */

        if (grabLightValue) {
            return 0;
        }
        grabLightValue = true;

        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {
            if (FeatureRegistry.enableIllumination && TE.hasAttribute(TE.ATTR_ILLUMINATOR)) {
                lightValue = 15;
            } else {
                for (int side = 0; side < 7; ++side) {
                    if (TE.hasAttribute(TE.ATTR_COVER[side])) {
                        ItemStack itemStack = BlockProperties.getCover(TE, side);
                        int tempLight = getLightValue(TE, BlockProperties.toBlock(itemStack), itemStack.getItemDamage());
                        if (tempLight > lightValue) {
                            lightValue = tempLight;
                        }
                    }
                }
            }
        }

        grabLightValue = false;
        return lightValue;
    }

    @Override
    /**
     * Returns the block hardness at a location. Args: world, x, y, z
     */
    public float getBlockHardness(World world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            if (TE.hasAttribute(TE.ATTR_COVER[6])) {
                return BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).getBlockHardness(world, x, y, z);
            }
        }

        return blockHardness;
    }

    @Override
    /**
     * Chance that fire will spread and consume this block.
     */
    public int getFlammability(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection face)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {
            return Blocks.fire.getFlammability(BlockProperties.toBlock(BlockProperties.getCover(TE, 6)));
        }

        return super.getFlammability(blockAccess, x, y, z, face);
    }

    @Override
    /**
     * Called when fire is updating on a neighbor block.
     */
    public int getFireSpreadSpeed(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection side)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {
            return Blocks.fire.getEncouragement(BlockProperties.toBlock(BlockProperties.getCover(TE, 6)));
        }

        return super.getFireSpreadSpeed(blockAccess, x, y, z, side);
    }

    @Override
    /**
     * Currently only called by fire when it is on top of this block.
     * Returning true will prevent the fire from naturally dying during updating.
     * Also prevents fire from dying from rain.
     */
    public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            if (TE.hasAttribute(TE.ATTR_COVER[6])) {
                return BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).isFireSource(world, x, y, z, side);
            }
        }

        return false;
    }

    @Override
    /**
     * Location sensitive version of getExplosionRestance
     */
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            if (TE.hasAttribute(TE.ATTR_COVER[6])) {
                return BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).getExplosionResistance(entity);
            }
        }

        return this.getExplosionResistance(entity);
    }

    @Override
    /**
     * Returns whether block is wood
     */
    public boolean isWood(IBlockAccess blockAccess, int x, int y, int z)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {
            if (TE.hasAttribute(TE.ATTR_COVER[6])) {
                return BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).isWood(blockAccess,  x,  y,  z);
            }
        }

        return super.isWood(blockAccess, x, y, z);
    }

    /**
     * Determines if this block is can be destroyed by the specified entities normal behavior.
     */
    @Override
    public boolean canEntityDestroy(IBlockAccess blockAccess, int x, int y, int z, Entity entity)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {
            if (TE.hasAttribute(TE.ATTR_COVER[6])) {
                Block block = BlockProperties.toBlock(BlockProperties.getCover(TE, 6));
                if (entity instanceof EntityWither) {
                    return !block.equals(Blocks.bedrock) && !block.equals(Blocks.end_portal) && !block.equals(Blocks.end_portal_frame) && !block.equals(Blocks.command_block);
                } else if (entity instanceof EntityDragon) {
                    return !block.equals(Blocks.obsidian) && !block.equals(Blocks.end_stone) && !block.equals(Blocks.bedrock);
                }
            }
        }

        return super.canEntityDestroy(blockAccess, x, y, z, entity);
    }

    @Override
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            if (TE.hasAttribute(TE.ATTR_COVER[6])) {
                BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).onEntityCollidedWithBlock(world, x, y, z, entity);
            }
        }
    }

    @Override
    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float harvestLevel, int fortune)
    {
        /*
         * When fluids break a block, they automatically call for blocks to drop items.
         * We only allow item drops in breakBlock() or when called elsewhere within this
         * mod, so check first.
         */
        if (enableDrops) {
            super.dropBlockAsItemWithChance(world, x, y, z, metadata, harvestLevel, fortune);
        } else {
            dropBlockAsItem(world, x, y, z, getItemDrop(world, metadata));
        }
    }

    @Override
    /**
     * Ejects contained items into the world, and notifies neighbors of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        /* Drop block instance. */

        for (ItemStack itemStack : getDrops(world, x, y, z, METADATA_DROP_ATTR_ONLY, 0)) {
            enableDrops = true;
            dropBlockAsItem(world, x, y, z, itemStack);
            enableDrops = false;
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    /**
     * Spawns EntityItem in the world for the given ItemStack if the world is not remote.
     */
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack itemStack)
    {
        Block block = BlockProperties.toBlock(itemStack);
        if (block instanceof BlockCoverable)
        {
            itemStack.setItemDamage(0);
            super.dropBlockAsItem(world, x, y, z, itemStack);
        }
    }

    /**
     * This returns a complete list of items dropped from this block.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     * @return A ArrayList containing all items this block drops
     */
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
        TEBase TE = getSimpleTileEntity(world, x, y, z);

        if (metadata == METADATA_DROP_ATTR_ONLY) {
            ret.clear(); // Remove block instance from drop list
        }

        if (TE != null) {
            for (int idx = 0; idx < 7; ++idx) {
                if (TE.hasAttribute(TE.ATTR_COVER[idx])) {
                    ret.add(BlockProperties.getCoverForDrop(TE, idx));
                }
                if (TE.hasAttribute(TE.ATTR_OVERLAY[idx])) {
                    ret.add(TE.getAttribute(TE.ATTR_OVERLAY[idx]));
                }
                if (TE.hasAttribute(TE.ATTR_DYE[idx])) {
                    ret.add(TE.getAttribute(TE.ATTR_DYE[idx]));
                }
            }
            if (TE.hasAttribute(TE.ATTR_ILLUMINATOR)) {
                ret.add(TE.getAttribute(TE.ATTR_ILLUMINATOR));
            }
        }

        return ret;
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

            if (TE.hasAttribute(TE.ATTR_COVER[6])) {
                BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).randomDisplayTick(world, x, y, z, random);
            }

            if (TE.hasAttribute(TE.ATTR_OVERLAY[6])) {
                if (OverlayHandler.getOverlayType(TE.getAttribute(TE.ATTR_OVERLAY[6])).equals(Overlay.MYCELIUM)) {
                    Blocks.mycelium.randomDisplayTick(world, x, y, z, random);
                }
            }

        }
    }

    /**
     * Determines if this block can support the passed in plant, allowing it to be planted and grow.
     * Some examples:
     *   Reeds check if its a reed, or if its sand/dirt/grass and adjacent to water
     *   Cacti checks if its a cacti, or if its sand
     *   Nether types check for soul sand
     *   Crops check for tilled soil
     *   Caves check if it's a solid surface
     *   Plains check if its grass or dirt
     *   Water check if its still water
     *
     * @param blockAccess The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @param side The direction relative to the given position the plant wants to be, typically its UP
     * @param plantable The plant that wants to check
     * @return True to allow the plant to be planted/stay.
     */
    @Override
    public boolean canSustainPlant(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection side, IPlantable plantable)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {

            /* If side is not solid, it can't sustain a plant. */

            if (!isSideSolid(blockAccess, x, y, z, side)) {
                return false;
            }

            /*
             * Add base block, top block, and both of their associated
             * overlays to judge whether plants can be supported on block.
             */

            List<Block> blocks = new ArrayList<Block>();

            for (int side1 = 1; side1 < 7; side1 += 5) {
                if (TE.hasAttribute(TE.ATTR_COVER[side1])) {
                    blocks.add(BlockProperties.toBlock(BlockProperties.getCover(TE, side1)));
                }
                if (TE.hasAttribute(TE.ATTR_OVERLAY[side1])) {
                    blocks.add(BlockProperties.toBlock(OverlayHandler.getOverlayType(TE.getAttribute(TE.ATTR_OVERLAY[side1])).getItemStack()));
                }
            }

            /* Add types using cover material */

            Material material = BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).getMaterial();
            if (material.equals(Material.grass)) {
                blocks.add(Blocks.grass);
            } else if (material.equals(Material.ground)) {
                blocks.add(Blocks.dirt);
            } else if (material.equals(Material.sand)) {
                blocks.add(Blocks.sand);
            }

            switch (plantable.getPlantType(blockAccess, x, y + 1, z))
            {
                case Desert: return blocks.contains(Blocks.sand);
                case Nether: return blocks.contains(Blocks.soul_sand);
                case Plains: return blocks.contains(Blocks.grass) || blocks.contains(Blocks.dirt);
                case Beach:
                    boolean isBeach = blocks.contains(Blocks.grass) || blocks.contains(Blocks.dirt) || blocks.contains(Blocks.sand);
                    boolean hasWater = blockAccess.getBlock(x - 1, y, z    ).getMaterial() == Material.water ||
                                       blockAccess.getBlock(x + 1, y, z    ).getMaterial() == Material.water ||
                                       blockAccess.getBlock(x,     y, z - 1).getMaterial() == Material.water ||
                                       blockAccess.getBlock(x,     y, z + 1).getMaterial() == Material.water;
                    return isBeach && hasWater;
                default:
                    break;
            }
        }

        return super.canSustainPlant(blockAccess, x, y, z, side, plantable);
    }

    /**
     * Returns whether this block is considered solid.
     */
    protected boolean isBlockSolid(IBlockAccess blockAccess, int x, int y, int z)
    {
        TEBase TE = getTileEntity(blockAccess, x, y, z);

        if (TE != null) {
            return !TE.hasAttribute(TE.ATTR_COVER[6]) || BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).isOpaqueCube();
        } else {
            return false;
        }
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        if (!world.isRemote) {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {
                TE.setOwner(entityLiving.getUniqueID());
            }

        }
    }

    @Override
    /**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * EntityPlayer.
     */
    public float getPlayerRelativeBlockHardness(EntityPlayer entityPlayer, World world, int x, int y, int z)
    {
        /* Don't damage block if holding Carpenter's tool. */

        ItemStack itemStack = entityPlayer.getHeldItem();

        if (itemStack != null) {
            Item item = itemStack.getItem();
            if (item instanceof ICarpentersHammer || item instanceof ICarpentersChisel) {
                return -1;
            }
        }

        /* Return block hardness of cover. */

        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            return ForgeHooks.blockStrength(BlockProperties.toBlock(BlockProperties.getCover(TE, 6)), entityPlayer, world, x, y, z);
        } else {
            return super.getPlayerRelativeBlockHardness(entityPlayer, world, x, y, z);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: world, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        // Side checks in out-of-range areas will crash
        if (y > 0 && y < blockAccess.getHeight())
        {
            TEBase TE = getTileEntity(blockAccess, x, y, z);
            if (TE != null) {
                ForgeDirection side_src = ForgeDirection.getOrientation(side);
                ForgeDirection side_adj = side_src.getOpposite();

                TEBase TE_adj = (TEBase) blockAccess.getTileEntity(x, y, z);
                TEBase TE_src = (TEBase) blockAccess.getTileEntity(x + side_adj.offsetX, y + side_adj.offsetY, z + side_adj.offsetZ);

                if (TE_adj.getBlockType().isSideSolid(blockAccess, x, y, z, side_adj) == TE_src.getBlockType().isSideSolid(blockAccess, x + side_adj.offsetX, y + side_adj.offsetY, z + side_adj.offsetZ, ForgeDirection.getOrientation(side))) {

                    if (shareFaces(TE_adj, TE_src, side_adj, side_src)) {

                        Block block_adj = BlockProperties.toBlock(BlockProperties.getCover(TE_adj, 6));
                        Block block_src = BlockProperties.toBlock(BlockProperties.getCover(TE_src, 6));

                        if (!TE_adj.hasAttribute(TE.ATTR_COVER[6])) {
                            return TE_src.hasAttribute(TE.ATTR_COVER[6]);
                        } else {
                            if (!TE_src.hasAttribute(TE.ATTR_COVER[6]) && block_adj.getRenderBlockPass() == 0) {
                                return !block_adj.isOpaqueCube();
                            } else if (TE_src.hasAttribute(TE.ATTR_COVER[6]) && block_src.isOpaqueCube() == block_adj.isOpaqueCube() && block_src.getRenderBlockPass() == block_adj.getRenderBlockPass()) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }

    @Override
    /**
     * Determines if this block should render in this pass.
     */
    public boolean canRenderInPass(int pass)
    {
        ForgeHooksClient.setRenderPass(pass);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns which pass this block be rendered on. 0 for solids and 1 for alpha.
     */
    public int getRenderBlockPass()
    {
        /*
         * Alpha properties of block or cover depend on this returning a value
         * of 1, so it's the default value.  However, when rendering in player
         * hand we'll encounter sorting artifacts, and thus need to enforce
         * opaque rendering, or 0.
         */

        if (ForgeHooksClient.getWorldRenderPass() < 0) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Returns whether two blocks share faces.
     * Primarily for slopes, stairs and slabs.
     */
    protected boolean shareFaces(TEBase TE_adj, TEBase TE_src, ForgeDirection side_adj, ForgeDirection side_src)
    {
        return TE_adj.getBlockType().isSideSolid(TE_adj.getWorldObj(), TE_adj.xCoord, TE_adj.yCoord, TE_adj.zCoord, side_adj) &&
               TE_src.getBlockType().isSideSolid(TE_src.getWorldObj(), TE_src.xCoord, TE_src.yCoord, TE_src.zCoord, side_src);
    }

    @Override
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        if (FeatureRegistry.enableRoutableFluids) {
            Class<?> clazz = FancyFluidsHelper.getCallerClass();
            if (clazz != null) {
                for (Class clazz1 : FancyFluidsHelper.liquidClasses) {
                    if (clazz.isAssignableFrom(clazz1)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Should block use the brightest neighbor light value as its own
     */
    @Override
    public boolean getUseNeighborBrightness()
    {
        return true;
    }

    @Override
    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        world.setTileEntity(x, y, z, createNewTileEntity(world, 0));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TEBase();
    }

    @Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    /**
     * This method is configured on as as-needed basis.
     * It's calling order is not guaranteed.
     */
    protected void preOnBlockClicked(TEBase TE, World world, int x, int y, int z, EntityPlayer entityPlayer, ActionResult actionResult) {}

    /**
     * Called before cover or decoration checks are performed.
     */
    protected void preOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, ActionResult actionResult) {}

    /**
     * Called if cover and decoration checks have been performed but
     * returned no changes.
     */
    protected void postOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, ActionResult actionResult) {}

    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        return false;
    }

    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        return false;
    }

    protected void damageItemWithChance(World world, EntityPlayer entityPlayer)
    {
        Item item = entityPlayer.getCurrentEquippedItem().getItem();

        if (item instanceof ICarpentersHammer) {
            ((ICarpentersHammer) item).onHammerUse(world, entityPlayer);
        } else if (item instanceof ICarpentersChisel) {
            ((ICarpentersChisel) item).onChiselUse(world, entityPlayer);
        }
    }

    /**
     * Returns whether side of block supports a cover.
     */
    protected boolean canCoverSide(TEBase TE, World world, int x, int y, int z, int side)
    {
        return side == 6;
    }

    /**
     * Allows a tile entity called during block activation to be changed before
     * altering attributes like cover, dye, overlay, etc.
     * <p>
     * Primarily offered for the garage door, when open, to swap the top piece
     * with the bottom piece for consistency.
     *
     * @param  TE the originating {@link TEBase}
     * @return a swapped in {@link TEBase}, or the passed in {@link TEBase}
     */
    protected TEBase getTileEntityForBlockActivation(TEBase TE)
    {
        return TE;
    }

}
