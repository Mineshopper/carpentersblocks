package com.carpentersblocks.util;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.StepSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.block.BlockCoverable;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.handler.ChatHandler;
import com.carpentersblocks.util.handler.DesignHandler;
import com.carpentersblocks.util.handler.DyeHandler;
import com.carpentersblocks.util.handler.OverlayHandler;
import com.carpentersblocks.util.registry.FeatureRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockProperties {

    public final static StepSound stepSound = new StepSound(CarpentersBlocks.MODID, 1.0F, 1.0F);
    public final static int       MASK_DEFAULT_ICON = 0x10;

    public static boolean isMetadataDefaultIcon(int metadata)
    {
        return (metadata & MASK_DEFAULT_ICON) > 0;
    }

    /**
     * Creates a block event that drops the provided ItemStack.
     * Used outside block methods since method required in Block.class is protected.
     * @param itemStack
     */
    public static void dropAttribute(TEBase TE, ItemStack itemStack)
    {
        TE.getWorldObj().addBlockEvent(TE.xCoord, TE.yCoord, TE.zCoord, TE.getBlockType().blockID, itemStack.itemID, itemStack.getItemDamage());
    }

    /**
     * Adds additional data to unused bits in ItemStack metadata to
     * identify special properties for ItemStack.
     *
     * Tells BlockCoverable class to retrieve block icon rather than
     * default blank icon.
     *
     * @param itemStack
     * @param mask
     */
    public static void prepareItemStackForRendering(ItemStack itemStack)
    {
        if (toBlock(itemStack) instanceof BlockCoverable) {
            itemStack.setItemDamage(itemStack.getItemDamage() | MASK_DEFAULT_ICON);
        }
    }

    /**
     * Sets host metadata to match ItemStack damage value.
     *
     * Most often used when retrieving properties for side covers,
     * or any ItemStack in general besides base cover (side == 6).
     */
    public static void setHostMetadata(TEBase TE, int metadata)
    {
        TE.getWorldObj().setBlockMetadataWithNotify(TE.xCoord, TE.yCoord, TE.zCoord, metadata, 4);
    }

    /**
     * Resets host block metadata.
     */
    public static void resetHostMetadata(TEBase TE)
    {
        TE.getWorldObj().setBlockMetadataWithNotify(TE.xCoord, TE.yCoord, TE.zCoord, BlockProperties.getCover(TE, 6).getItemDamage(), 4);
    }

    /**
     * Takes an ItemStack and returns block, or null if ItemStack
     * does not contain a block.
     */
    public static Block toBlock(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            return Block.blocksList[itemStack.itemID];
        } else {
            return null;
        }
    }

    /**
     * Returns depth of side cover.
     */
    public static float getSideCoverDepth(TEBase TE, int side)
    {
        if (side == 1 && hasCover(TE, side)) {

            Block block = toBlock(getCover(TE, side));

            if (block.equals(Block.snow) || block.equals(Block.blockSnow)) {
                return 0.125F;
            }

        }

        return 0.0625F;
    }

    /**
     * Returns opposite of entity facing.
     */
    public static int getOppositeFacing(EntityLivingBase entityLiving)
    {
        return MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
    }

    /**
     * Will return opposite direction from entity facing.
     */
    public static ForgeDirection getDirectionFromFacing(int facing)
    {
        switch (facing)
        {
            case 0:
                return ForgeDirection.NORTH;
            case 1:
                return ForgeDirection.EAST;
            case 2:
                return ForgeDirection.SOUTH;
            case 3:
                return ForgeDirection.WEST;
            default:
                return ForgeDirection.UNKNOWN;
        }
    }

    /**
     * Will suppress block updates.
     */
    private static boolean suppressUpdate = false;

    public static boolean hasDesign(TEBase TE)
    {
        return DesignHandler.getListForType(getBlockDesignType(TE)).contains(getDesign(TE));
    }

    public static String getDesign(TEBase TE)
    {
        return TE.design;
    }

    public static boolean setDesign(TEBase TE, String name)
    {
        if (!TE.design.equals(name)) {
            TE.design = name;
            if (!suppressUpdate) {
                TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
            }
            return true;
        }

        return false;
    }

    public static boolean clearDesign(TEBase TE)
    {
        return setDesign(TE, "");
    }

    public static String getBlockDesignType(TEBase TE)
    {
        String name = TE.getBlockType().getUnlocalizedName();
        return name.substring(new String("tile.blockCarpenters").length()).toLowerCase();
    }

    public static boolean setNextDesign(TEBase TE)
    {
        return setDesign(TE, DesignHandler.getNext(getBlockDesignType(TE), getDesign(TE)));
    }

    public static boolean setPrevDesign(TEBase TE)
    {
        return setDesign(TE, DesignHandler.getPrev(getBlockDesignType(TE), getDesign(TE)));
    }

    /**
     * Returns whether block rotates based on placement conditions.
     * The blocks that utilize this property are mostly atypical, and
     * must be added manually.
     */
    public static boolean blockRotates(ItemStack itemStack)
    {
        Block block = toBlock(itemStack);

        return block instanceof BlockQuartz ||
               block instanceof BlockRotatedPillar;
    }

    /**
     * Plays block sound.
     * Reduced volume is for damaging a block, versus full volume for placement or destruction.
     */
    public static void playBlockSound(World world, ItemStack itemStack, int x, int y, int z, boolean reducedVolume)
    {
        if (itemStack != null) {

            Block block;

            if (itemStack.getItem() instanceof ItemBlock) {
                block = toBlock(itemStack);
            } else {
                block = Block.sand;
            }

            StepSound stepSound = block.stepSound;
            float volume = (stepSound.getVolume() + 1.0F) / (reducedVolume ? 8.0F : 2.0F);
            float pitch = stepSound.getPitch() * 0.8F;

            world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, stepSound.getPlaceSound(), volume, pitch);
        }
    }

    /**
     * Returns whether block or side block has an attribute.
     * It checks for cover, dye color and overlay.
     */
    public static boolean hasAttribute(TEBase TE, int side)
    {
        return hasCover(TE, side) ||
               hasDye(TE, side) ||
               hasOverlay(TE, side);
    }

    /**
     * Strips side of all properties.
     */
    public static void ejectAttributes(TEBase TE, int side)
    {
        suppressUpdate = true;

        setCover(TE, side, (ItemStack)null);
        setDye(TE, side, (ItemStack)null);
        setOverlay(TE, side, (ItemStack)null);
        setChiselDesign(TE, side, "");

        suppressUpdate = false;

        TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
    }

    /**
     * Returns whether block has a cover.
     * Checks if block ID exists and whether it is a valid cover block.
     */
    public static boolean hasCover(TEBase TE, int side)
    {
        return TE.cover[side] != null &&
               isCover(TE.cover[side]);
    }

    /**
     * Returns whether block has side covers.
     */
    public static boolean hasSideCovers(TEBase TE)
    {
        for (int side = 0; side < 6; ++side) {
            if (hasCover(TE, side)) {
                return true;
            }
        }

        return false;
    }

    private static ItemStack getCoverUnfiltered(TEBase TE, int side)
    {
        return TE.cover[side] != null ? TE.cover[side] : new ItemStack(TE.getBlockType());
    }

    @SideOnly(Side.CLIENT)
    /**
     * Returns untouched cover ItemStack for rendering purposes.
     */
    public static ItemStack getCoverForRendering(TEBase TE, int side)
    {
        return getCoverUnfiltered(TE, side);
    }

    /**
     * Returns filtered cover ItemStack that is safe for calling block properties.
     *
     * This is needed to avoid calling properties for covers that have NBTTagCompounds,
     * which may rely on data that does not exist.
     */
    public static ItemStack getCover(TEBase TE, int side)
    {
        ItemStack itemStack = getCoverUnfiltered(TE, side);
        Block block = toBlock(itemStack);

        return block.hasTileEntity(itemStack.getItemDamage()) && !(block instanceof BlockCoverable) ? new ItemStack(Block.planks) : itemStack;
    }

    /**
     * Returns whether block is a cover.
     */
    public static boolean isCover(ItemStack itemStack)
    {
        if (itemStack.getItem() instanceof ItemBlock && !isOverlay(itemStack)) {

            Block block = Block.blocksList[itemStack.itemID];

            return block.renderAsNormalBlock() ||
                   block instanceof BlockHalfSlab ||
                   block instanceof BlockPane ||
                   block instanceof BlockBreakable ||
                   FeatureRegistry.coverExceptions.contains(itemStack.getDisplayName()) ||
                   FeatureRegistry.coverExceptions.contains(ChatHandler.getDefaultTranslation(itemStack));

        }

        return false;
    }

    /**
     * Returns cover ItemStack in its default state.
     * Will correct log drop rotation, among other things.
     */
    public static ItemStack getCoverForDrop(Random rand, ItemStack itemStack)
    {
        if (itemStack != null) {
            Block block = toBlock(itemStack);
            int damageDropped = block.damageDropped(itemStack.getItemDamage());
            Item itemDropped = Item.itemsList[block.idDropped(itemStack.getItemDamage(), rand, /* Fortune */ 0)];

            /* Check if block drops itself, and, if so, correct the damage value to the block's default. */

            if (itemDropped != null && itemDropped.equals(itemStack.getItem()) && damageDropped != itemStack.getItemDamage()) {
                itemStack.setItemDamage(damageDropped);
            }
        }

        return itemStack;
    }

    public static ItemStack getCoverForDrop(TEBase TE, int side)
    {
        return getCoverForDrop(TE.getWorldObj().rand, TE.cover[side]);
    }

    /**
     * Sets cover block.
     */
    public static boolean setCover(TEBase TE, int side, ItemStack itemStack)
    {
        World world = TE.getWorldObj();

        if (hasCover(TE, side)) {
            dropAttribute(TE, getCoverForDrop(TE, side));
        }

        TE.cover[side] = getReducedStack(itemStack);

        int blockID = itemStack == null ? 0 : itemStack.itemID;
        int metadata = itemStack == null ? 0 : itemStack.getItemDamage();

        if (side == 6) {
            world.setBlockMetadataWithNotify(TE.xCoord, TE.yCoord, TE.zCoord, metadata, 0);
        }

        world.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, blockID);
        world.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);

        return true;
    }

    /**
     * Get extended metadata for Carpenter's block.
     *
     * @deprecated  Provided until Smart Moving updates.
     *    Replaced by {@link #getMetadata(TEBase)}
     * @return extended metadata
     */
    @Deprecated
    public final static int getData(TEBase TE)
    {
        return getMetadata(TE);
    }

    /**
     * Get extended metadata for Carpenter's block.
     *
     * @return
     */
    public final static int getMetadata(TEBase TE)
    {
        return TE.metadata & 0xffff;
    }

    /**
     * Set block data.
     */
    public static boolean setMetadata(TEBase TE, int data)
    {
        if (data != getMetadata(TE)) {
            TE.metadata = (short) data;
            if (!suppressUpdate) {
                TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
            }
            return true;
        }

        return false;
    }

    /**
     * Returns true if ItemStack is a dye.
     */
    public static boolean isDye(ItemStack itemStack, boolean allowWhite)
    {
        return itemStack.getItem() != null &&
               DyeHandler.isDye(itemStack, allowWhite);
    }

    /**
     * Returns whether side has cover.
     */
    public static boolean hasDye(TEBase TE, int side)
    {
        return TE.dye[side] != null &&
               isDye(TE.dye[side], true);
    }

    /**
     * Sets color for side.
     */
    public static boolean setDye(TEBase TE, int side, ItemStack itemStack)
    {
        World world = TE.getWorldObj();

        if (hasDye(TE, side)) {
            dropAttribute(TE, getDye(TE, side));
        }

        TE.dye[side] = getReducedStack(itemStack);

        if (!suppressUpdate) {
            world.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
        }

        return true;
    }

    /**
     * Returns dye color for side.
     */
    public static ItemStack getDye(TEBase TE, int side)
    {
        return TE.dye[side];
    }

    /**
     * Sets overlay.
     */
    public static boolean setOverlay(TEBase TE, int side, ItemStack itemStack)
    {
        World world = TE.getWorldObj();

        if (hasOverlay(TE, side)) {
            dropAttribute(TE, getOverlay(TE, side));
        }

        TE.overlay[side] = getReducedStack(itemStack);

        if (!suppressUpdate) {
            world.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
        }

        return true;
    }

    /**
     * Returns a copy of ItemStack with stackSize set to 1.
     */
    public static ItemStack getReducedStack(ItemStack itemStack)
    {
        if (itemStack == null) {
            return itemStack;
        } else {
            ItemStack tempStack = itemStack.copy();
            tempStack.stackSize = 1;
            return tempStack;
        }
    }

    /**
     * Returns overlay.
     */
    public static ItemStack getOverlay(TEBase TE, int side)
    {
        return TE.overlay[side];
    }

    /**
     * Returns whether block has overlay.
     */
    public static boolean hasOverlay(TEBase TE, int side)
    {
        return TE.overlay[side] != null &&
               isOverlay(TE.overlay[side]);
    }

    /**
     * Returns whether ItemStack contains a valid overlay item or block.
     */
    public static boolean isOverlay(ItemStack itemStack)
    {
        return OverlayHandler.overlayMap.containsKey(itemStack.getDisplayName()) ||
               OverlayHandler.overlayMap.containsKey(ChatHandler.getDefaultTranslation(itemStack));
    }

    /**
     * Returns whether block has pattern.
     */
    public static boolean hasChiselDesign(TEBase TE, int side)
    {
        return DesignHandler.listChisel.contains(getChiselDesign(TE, side));
    }

    /**
     * Returns pattern.
     */
    public static String getChiselDesign(TEBase TE, int side)
    {
        return TE.chiselDesign[side];
    }

    /**
     * Sets pattern.
     */
    public static boolean setChiselDesign(TEBase TE, int side, String iconName)
    {
        if (!TE.chiselDesign.equals(iconName)) {
            TE.chiselDesign[side] = iconName;
            if (!suppressUpdate) {
                TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
            }
            return true;
        }

        return false;
    }

}
