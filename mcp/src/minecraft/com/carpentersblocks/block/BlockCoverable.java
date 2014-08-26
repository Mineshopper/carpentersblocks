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
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import com.carpentersblocks.api.ICarpentersChisel;
import com.carpentersblocks.api.ICarpentersHammer;
import com.carpentersblocks.renderer.helper.ParticleHelper;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;
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
    public BlockCoverable(int blockID, Material material)
    {
        super(blockID, material);
        setBurnProperties(blockID, 5, 20);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister) { }

    @SideOnly(Side.CLIENT)
    /**
     * Returns a base icon that doesn't rely on blockIcon, which
     * is set prior to texture stitch events.
     *
     * @return default icon
     */
    public Icon getIcon()
    {
        return IconRegistry.icon_uncovered_solid;
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Returns the icon on the side given the block metadata.
     * <p>
     * Due to the amount of control needed over this, vanilla calls will always return icon_blank.
     */
    public Icon getIcon(int side, int metadata)
    {
        if (BlockProperties.isMetadataDefaultIcon(metadata)) {
            return getIcon();
        }

        return BlockRedstoneWire.getRedstoneWireIcon("cross_overlay");
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        ItemStack itemStack = BlockProperties.getCover(TE, 6);
        Block block = BlockProperties.toBlock(itemStack);

        return block instanceof BlockCoverable ? getIcon() : block.getIcon(side, itemStack.getItemDamage());
    }

    /**
     * Returns adjacent, similar tile entities that can be used for duplicating
     * block properties like dye color, pattern, style, etc.
     *
     * @param  world  the world reference
     * @param  x      the x coordinate
     * @param  y      the y coordinate
     * @param  z      the z coordinate
     * @return           an array of adjacent, similar tile entities
     * @see           TEBase
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
    protected TEBase getSimpleTileEntity(IBlockAccess world, int x, int y, int z)
    {
        TileEntity TE = world.getBlockTileEntity(x, y, z);
        return (TE instanceof TEBase) ? (TEBase) TE : null;
    }

    /**
     * Returns tile entity if block tile entity is instanceof TEBase and
     * also belongs to this block type.
     */
    protected TEBase getTileEntity(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getSimpleTileEntity(world, x, y, z);
        return TE != null && TE.getBlockType().equals(this) ? TE : null;
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

                        int effectiveSide = BlockProperties.hasCover(TE, EventHandler.eventFace) ? EventHandler.eventFace : 6;
                        Item item = itemStack.getItem();

                        if (item instanceof ICarpentersHammer && ((ICarpentersHammer)item).canUseHammer(world, entityPlayer)) {

                            preOnBlockClicked(TE, world, x, y, z, entityPlayer, actionResult);

                            if (!actionResult.altered) {

                                if (entityPlayer.isSneaking()) {

                                    if (BlockProperties.hasOverlay(TE, effectiveSide)) {
                                        BlockProperties.setOverlay(TE, effectiveSide, (ItemStack)null);
                                        actionResult.setAltered();
                                    } else if (BlockProperties.hasDye(TE, effectiveSide)) {
                                        BlockProperties.setDye(TE, effectiveSide, (ItemStack)null);
                                        actionResult.setAltered();
                                    } else if (BlockProperties.hasCover(TE, effectiveSide)) {
                                        BlockProperties.setCover(TE, effectiveSide, (ItemStack)null);
                                        BlockProperties.setChiselDesign(TE, effectiveSide, "");
                                        actionResult.setAltered();
                                    }

                                } else {

                                    onHammerLeftClick(TE, entityPlayer);
                                    actionResult.setAltered();

                                }

                            }

                            if (actionResult.altered) {
                                onNeighborBlockChange(world, x, y, z, blockID);
                                world.notifyBlocksOfNeighborChange(x, y, z, blockID);
                            }

                        } else if (item instanceof ICarpentersChisel && ((ICarpentersChisel)item).canUseChisel(world, entityPlayer)) {

                            if (entityPlayer.isSneaking()) {

                                if (BlockProperties.hasChiselDesign(TE, effectiveSide)) {
                                    BlockProperties.setChiselDesign(TE, effectiveSide, "");
                                }

                            } else if (BlockProperties.hasCover(TE, effectiveSide)) {

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

                    /* Sides 0-5 are side covers, and 6 is the base block. */
                    int effectiveSide = BlockProperties.hasCover(TE, side) ? side : 6;

                    preOnBlockActivated(TE, entityPlayer, side, hitX, hitY, hitZ, actionResult);

                    if (PlayerPermissions.canPlayerEdit(TE, TE.xCoord, TE.yCoord, TE.zCoord, entityPlayer)) {

                        if (!actionResult.altered) {

                            if (itemStack != null) {

                                if (itemStack.getItem() instanceof ICarpentersHammer && ((ICarpentersHammer)itemStack.getItem()).canUseHammer(world, entityPlayer)) {

                                    if (onHammerRightClick(TE, entityPlayer)) {
                                        actionResult.setAltered();
                                    }

                                } else if (ItemRegistry.enableChisel && itemStack.getItem() instanceof ICarpentersChisel && ((ICarpentersChisel)itemStack.getItem()).canUseChisel(world, entityPlayer)) {

                                    if (BlockProperties.hasCover(TE, effectiveSide)) {
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
                                        int facing = BlockProperties.getOppositeFacing(EventHandler.eventEntityPlayer);
                                        int side_interpolated = entityPlayer.rotationPitch < -45.0F ? 0 : entityPlayer.rotationPitch > 45 ? 1 : facing == 0 ? 3 : facing == 1 ? 4 : facing == 2 ? 2 : 5;
                                        metadata = block.onBlockPlaced(world, x, y, z, side_interpolated, hitX, hitY, hitZ, metadata);
                                    }

                                    ItemStack tempStack = itemStack.copy();
                                    tempStack.setItemDamage(metadata);

                                    /* Base cover should always be checked. */

                                    if (effectiveSide == 6 && (!canCoverSide(TE, world, x, y, z, 6) || BlockProperties.hasCover(TE, 6))) {
                                        effectiveSide = side;
                                    }

                                    if (canCoverSide(TE, world, x, y, z, effectiveSide) && !BlockProperties.hasCover(TE, effectiveSide)) {
                                        if (BlockProperties.setCover(TE, effectiveSide, tempStack)) {
                                            actionResult.setAltered().decInventory();
                                        }
                                    }

                                } else if (entityPlayer.isSneaking()) {

                                    if (FeatureRegistry.enableOverlays && BlockProperties.isOverlay(itemStack)) {
                                        if (!BlockProperties.hasOverlay(TE, effectiveSide) && (effectiveSide < 6 && BlockProperties.hasCover(TE, effectiveSide) || effectiveSide == 6)) {
                                            if (BlockProperties.setOverlay(TE, effectiveSide, itemStack)) {
                                                actionResult.setAltered().decInventory().setSoundSource(itemStack);
                                            }
                                        }
                                    } else if (FeatureRegistry.enableDyeColors && BlockProperties.isDye(itemStack, false)) {
                                        if (!BlockProperties.hasDye(TE, effectiveSide)) {
                                            if (BlockProperties.setDye(TE, effectiveSide, itemStack)) {
                                                actionResult.setAltered().decInventory().setSoundSource(itemStack);
                                            }
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
                        onNeighborBlockChange(world, x, y, z, blockID);
                        world.notifyBlocksOfNeighborChange(x, y, z, blockID);

                    }

                    if (actionResult.playSound) {
                        BlockProperties.playBlockSound(TE.getWorldObj(), actionResult.itemStack, TE.xCoord, TE.yCoord, TE.zCoord, false);
                    }

                    if (actionResult.decInv) {
                        if (!entityPlayer.capabilities.isCreativeMode && --itemStack.stackSize <= 0) {
                            entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, (ItemStack)null);
                        }
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
        String design = BlockProperties.getChiselDesign(TE, side);
        String designAdj = "";

        if (design.equals("")) {

            World world = TE.getWorldObj();

            /* Match pattern with adjacent pattern if possible. */

            TEBase[] TE_list = getAdjacentTileEntities(world, TE.xCoord, TE.yCoord, TE.zCoord);

            for (TEBase TE_current : TE_list) {
                if (TE_current != null) {
                    TE_current.getBlockType();
                    if (BlockProperties.hasChiselDesign(TE_current, side)) {
                        design = BlockProperties.getChiselDesign(TE_current, side);
                        designAdj = design;
                    }
                }
            }

        }

        if (designAdj.equals("")) {
            design = leftClick ? DesignHandler.getPrev("chisel", design) : DesignHandler.getNext("chisel", design);
        }

        if (!design.equals("")) {
            BlockProperties.setChiselDesign(TE, side, design);
        }

        return true;
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        if (!world.isRemote) {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {

                /*
                 * Check for and eject side covers that are blocked by a
                 * solid adjacent block.
                 */
                if (BlockProperties.hasSideCovers(TE))
                {
                    for (int side = 0; side < 6; ++side)
                    {
                        if (BlockProperties.hasCover(TE, side))
                        {
                            /*
                             * If block state no longer allows a side cover on
                             * side, eject side cover.
                             */
                            if (!canCoverSide(TE, world, x, y, z, side))
                            {
                                BlockProperties.ejectAttributes(TE, side);
                                return;
                            }

                            float sideCoverDepth = BlockProperties.getSideCoverDepth(TE, side);

                            /*
                             * If side is obstructed, eject side cover.
                             * Currently does not check for side depth.
                             */
                            ForgeDirection dir = ForgeDirection.getOrientation(side);
                            int x_offset = x + dir.offsetX;
                            int y_offset = y + dir.offsetY;
                            int z_offset = z + dir.offsetZ;
                            Block adjBlock = Block.blocksList[world.getBlockId(x_offset, y_offset, z_offset)];

                            if (adjBlock != null)
                            {
                                ForgeDirection adj_side = ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[side]);

                                if (adjBlock.isBlockSolidOnSide(world, x_offset, y_offset, z_offset, adj_side)) {

                                    switch (adj_side) {
                                        case DOWN:
                                            if (adjBlock.getBlockBoundsMinY() < sideCoverDepth) {
                                                BlockProperties.ejectAttributes(TE, side);
                                            }
                                            break;
                                        case UP:
                                            if (adjBlock.getBlockBoundsMaxY() > 1.0F - sideCoverDepth) {
                                                BlockProperties.ejectAttributes(TE, side);
                                            }
                                            break;
                                        case NORTH:
                                            if (adjBlock.getBlockBoundsMinZ() < sideCoverDepth) {
                                                BlockProperties.ejectAttributes(TE, side);
                                            }
                                            break;
                                        case SOUTH:
                                            if (adjBlock.getBlockBoundsMaxZ() > 1.0F - sideCoverDepth) {
                                                BlockProperties.ejectAttributes(TE, side);
                                            }
                                            break;
                                        case WEST:
                                            if (adjBlock.getBlockBoundsMinX() < sideCoverDepth) {
                                                BlockProperties.ejectAttributes(TE, side);
                                            }
                                            break;
                                        case EAST:
                                            if (adjBlock.getBlockBoundsMaxX() > 1.0F - sideCoverDepth) {
                                                BlockProperties.ejectAttributes(TE, side);
                                            }
                                            break;
                                        default: {}
                                    }
                                }
                            }
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
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            if (BlockProperties.hasCover(TE, 6)) {

                int effectiveSide = ForgeDirection.OPPOSITES[side];
                int power = BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).isProvidingWeakPower(world, x, y, z, side);
                int power_side = BlockProperties.hasCover(TE, effectiveSide) ? BlockProperties.toBlock(BlockProperties.getCover(TE, effectiveSide)).isProvidingWeakPower(world, x, y, z, side) : 0;

                return power_side > power ? power_side : power;

            }

        }

        return 0;
    }

    @Override
    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            if (BlockProperties.hasCover(TE, 6)) {

                int effectiveSide = ForgeDirection.OPPOSITES[side];
                int power = BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).isProvidingStrongPower(world, x, y, z, side);
                int power_side = BlockProperties.hasCover(TE, effectiveSide) ? BlockProperties.toBlock(BlockProperties.getCover(TE, effectiveSide)).isProvidingStrongPower(world, x, y, z, side) : 0;

                return power_side > power ? power_side : power;

            }

        }

        return 0;
    }

    /**
     * Indicates whether block destruction should be suppressed when block is clicked.
     * Will return true if player is holding a Carpenter's tool in creative mode.
     */
    private boolean suppressDestroyBlock(EntityPlayer entityPlayer)
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
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        if (!suppressDestroyBlock(player)) {
            return super.removeBlockByPlayer(world, player, x, y, z);
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
    public boolean addBlockHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
        TEBase TE = getTileEntity(world, target.blockX, target.blockY, target.blockZ);

        if (TE != null) {

            int effectiveSide = BlockProperties.hasCover(TE, target.sideHit) ? target.sideHit : 6;
            ItemStack itemStack = BlockProperties.getCover(TE, effectiveSide);

            if (BlockProperties.hasOverlay(TE, effectiveSide)) {
                Overlay overlay = OverlayHandler.getOverlayType(BlockProperties.getOverlay(TE, effectiveSide));
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

        return super.addBlockHitEffects(world, target, effectRenderer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Renders block destruction effects.
     * This is controlled to prevent block destroy effects if left-clicked with a Carpenter's Hammer while player is in creative mode.
     *
     * Returns false to display effects.  True suppresses them (backwards).
     */
    public boolean addBlockDestroyEffects(World world, int x, int y, int z, int metadata, EffectRenderer effectRenderer)
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

        int lightValue = Block.lightValue[block.blockID];

        /* Try grabbing more accurate lighting using metadata */

        if (lightValue == 0) {
            BlockProperties.setHostMetadata(TE, metadata);
            lightValue = block.getLightValue(TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord);
            BlockProperties.resetHostMetadata(TE);
        }

        return lightValue;
    }

    @Override
    /**
     * Returns light value based on cover or side covers.
     */
    public int getLightValue(IBlockAccess world, int x, int y, int z)
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

        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            for (int side = 0; side < 7; ++side) {
                if (BlockProperties.hasCover(TE, side)) {
                    ItemStack itemStack = BlockProperties.getCover(TE, side);
                    int tempLight = getLightValue(TE, BlockProperties.toBlock(itemStack), itemStack.getItemDamage());
                    if (tempLight > lightValue) {
                        lightValue = tempLight;
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
            if (BlockProperties.hasCover(TE, 6)) {
                return BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).getBlockHardness(world, x, y, z);
            }
        }

        return blockHardness;
    }

    @Override
    /**
     * Chance that fire will spread and consume this block.
     */
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            return blockFlammability[BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).blockID];
        }

        return super.getFlammability(world, x, y, z, metadata, face);
    }

    @Override
    /**
     * Called when fire is updating on a neighbor block.
     */
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection dir)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            return blockFlammability[BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).blockID];
        }

        return super.getFireSpreadSpeed(world, x, y, z, metadata, dir);
    }

    @Override
    /**
     * Currently only called by fire when it is on top of this block.
     * Returning true will prevent the fire from naturally dying during updating.
     * Also prevents fire from dying from rain.
     */
    public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            if (BlockProperties.hasCover(TE, 6)) {
                ItemStack itemStack = BlockProperties.getCover(TE, 6);
                return BlockProperties.toBlock(itemStack).isFireSource(world, x, y, z, itemStack.getItemDamage(), side);
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
            if (BlockProperties.hasCover(TE, 6)) {
                return BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).getExplosionResistance(entity);
            }
        }

        return this.getExplosionResistance(entity);
    }

    @Override
    /**
     * Returns whether block is wood
     */
    public boolean isWood(World world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            if (BlockProperties.hasCover(TE, 6)) {
                return BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).isWood(world,  x,  y,  z);
            }
        }

        return super.isWood(world, x, y, z);
    }

    /**
     * Determines if this block is can be destroyed by the specified entities normal behavior.
     */
    @Override
    public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            if (BlockProperties.hasCover(TE, 6)) {

                Block block = BlockProperties.toBlock(BlockProperties.getCover(TE, 6));

                if (entity instanceof EntityWither) {
                    return !block.equals(Block.bedrock) && !block.equals(Block.endPortal) && !block.equals(Block.endPortalFrame);
                } else if (entity instanceof EntityDragon) {
                    return canDragonDestroy(world, x, y, z);
                }

            }

        }

        return super.canEntityDestroy(world, x, y, z, entity);
    }

    @Override
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            if (BlockProperties.hasCover(TE, 6)) {
                BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).onEntityCollidedWithBlock(world, x, y, z, entity);
            }
        }
    }

    @Override
    /**
     * Ejects contained items into the world, and notifies neighbors of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, int blockID, int metadata)
    {
        if (!world.isRemote) {

            TEBase TE = getSimpleTileEntity(world, x, y, z);

            if (TE != null) {
                for (int side = 0; side < 7; ++side) {
                    BlockProperties.ejectAttributes(TE, side);
                }
            }

        }

        super.breakBlock(world, x, y, z, blockID, metadata);
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

            if (BlockProperties.hasCover(TE, 6)) {
                BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).randomDisplayTick(world, x, y, z, random);
            }

            if (BlockProperties.hasOverlay(TE, 6)) {
                if (OverlayHandler.getOverlayType(BlockProperties.getOverlay(TE, 6)).equals(Overlay.MYCELIUM)) {
                    Block.mycelium.randomDisplayTick(world, x, y, z, random);
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
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @param side The direction relative to the given position the plant wants to be, typically its UP
     * @param plantable The plant that wants to check
     * @return True to allow the plant to be planted/stay.
     */
    @Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection dir, IPlantable plantable)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            /* If side is not solid, it can't sustain a plant. */

            if (!isBlockSolidOnSide(world, x, y, z, dir)) {
                return false;
            }

            /*
             * Add base block, top block, and both of their associated
             * overlays to judge whether plants can be supported on block.
             */

            List<Integer> blockIDs = new ArrayList<Integer>();

            for (int side = 1; side < 7; side += 5) {
                if (BlockProperties.hasCover(TE, side)) {
                    blockIDs.add(BlockProperties.toBlock(BlockProperties.getCover(TE, side)).blockID);
                }
                if (BlockProperties.hasOverlay(TE, side)) {
                    blockIDs.add(BlockProperties.toBlock(OverlayHandler.getOverlayType(BlockProperties.getOverlay(TE, side)).getItemStack()).blockID);
                }
            }

            /* Add types using cover material */

            Material material = BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).blockMaterial;
            if (material.equals(Material.grass)) {
                blockIDs.add(Block.grass.blockID);
            } else if (material.equals(Material.ground)) {
                blockIDs.add(Block.dirt.blockID);
            } else if (material.equals(Material.sand)) {
                blockIDs.add(Block.sand.blockID);
            }

            switch (plantable.getPlantType(world, x, y + 1, z))
            {
                case Desert: return blockIDs.contains(sand.blockID);
                case Nether: return blockIDs.contains(slowSand.blockID);
                case Plains: return blockIDs.contains(grass.blockID) || blockIDs.contains(dirt.blockID);
                case Beach:
                    boolean isBeach = blockIDs.contains(Block.grass.blockID) || blockIDs.contains(Block.dirt.blockID) || blockIDs.contains(Block.sand.blockID);
                    boolean hasWater = world.getBlockMaterial(x - 1, y, z    ).equals(Material.water) ||
                                       world.getBlockMaterial(x + 1, y, z    ).equals(Material.water) ||
                                       world.getBlockMaterial(x,     y, z - 1).equals(Material.water) ||
                                       world.getBlockMaterial(x,     y, z + 1).equals(Material.water);
                    return isBeach && hasWater;
                default: {}
            }
        }

        return super.canSustainPlant(world, x, y, z, dir, plantable);
    }

    /**
     * Returns whether this block is considered solid.
     */
    protected boolean isBlockSolid(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            return !BlockProperties.hasCover(TE, 6) || BlockProperties.toBlock(BlockProperties.getCover(TE, 6)).isOpaqueCube();
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
                TE.setOwner(((EntityPlayer)entityLiving).getDisplayName());
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
                return -1.0F;
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
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            ForgeDirection side_src = ForgeDirection.getOrientation(side);
            ForgeDirection side_adj = ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[side]);

            TEBase TE_adj = (TEBase) world.getBlockTileEntity(x, y, z);
            TEBase TE_src = (TEBase) world.getBlockTileEntity(x + side_adj.offsetX, y + side_adj.offsetY, z + side_adj.offsetZ);

            if (TE_adj.getBlockType().isBlockSolidOnSide(TE.worldObj, x, y, z, side_adj) == TE_src.getBlockType().isBlockSolidOnSide(TE.worldObj, x + side_adj.offsetX, y + side_adj.offsetY, z + side_adj.offsetZ, ForgeDirection.getOrientation(side))) {

                if (shareFaces(TE_adj, TE_src, side_adj, side_src)) {

                    if (shareFaces(TE_adj, TE_src, side_adj, side_src)) {

                        Block block_adj = BlockProperties.toBlock(BlockProperties.getCover(TE_adj, 6));
                        Block block_src = BlockProperties.toBlock(BlockProperties.getCover(TE_src, 6));

                        if (!BlockProperties.hasCover(TE_adj, 6)) {
                            return BlockProperties.hasCover(TE_src, 6);
                        } else {
                            if (!BlockProperties.hasCover(TE_src, 6) && block_adj.getRenderBlockPass() == 0) {
                                return !block_adj.isOpaqueCube();
                            } else if (BlockProperties.hasCover(TE_src, 6) && block_src.isOpaqueCube() == block_adj.isOpaqueCube() && block_src.getRenderBlockPass() == block_adj.getRenderBlockPass()) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }

                }

            }

        }

        return super.shouldSideBeRendered(world, x, y, z, side);
    }

    /**
     * Returns whether two blocks share faces.
     * Primarily for slopes, stairs and slabs.
     */
    protected boolean shareFaces(TEBase TE_adj, TEBase TE_src, ForgeDirection side_adj, ForgeDirection side_src)
    {
        return TE_adj.getBlockType().isBlockSolidOnSide(TE_adj.getWorldObj(), TE_adj.xCoord, TE_adj.yCoord, TE_adj.zCoord, side_adj) &&
               TE_src.getBlockType().isBlockSolidOnSide(TE_src.getWorldObj(), TE_src.xCoord, TE_src.yCoord, TE_src.zCoord, side_src);
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
        return 1;
    }

    @Override
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
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

    @Override
    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        world.setBlockTileEntity(x, y, z, createNewTileEntity(world));
    }

    @Override
    public TileEntity createNewTileEntity(World world)
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

}
