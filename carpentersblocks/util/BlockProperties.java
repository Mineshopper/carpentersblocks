package carpentersblocks.util;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockSlab;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.handler.OverlayHandler;

public class BlockProperties {
    
    public final static SoundType stepSound = new SoundType("carpentersblock", 1.0F, 1.0F);
    
    /**
     * Returns depth of side cover.
     */
    public static float getSideCoverDepth(TEBase TE, int side)
    {
        if (getOverlay(TE, side) == OverlayHandler.OVERLAY_SNOW) {
            return 0.125F;
        } else {
            return 0.0625F;
        }
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
     * Returns RGB of dye metadata.
     */
    public static float[] getDyeRGB(int metadata)
    {
        int color = ItemDye.field_150922_c[15 - metadata];
        
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        
        float[] rgb = { red, green, blue };
        
        return rgb;
    }
    
    /**
     * Will suppress block updates.
     */
    private static boolean suppressUpdate = false;
    
    /**
     * Ejects an item at given coordinates.
     */
    public static void ejectEntity(TEBase TE, ItemStack itemStack)
    {
        World world = TE.getWorldObj();
        
        if (!world.isRemote)
        {
            float offset = 0.7F;
            double xRand = world.rand.nextFloat() * offset + (1.0F - offset) * 0.5D;
            double yRand = world.rand.nextFloat() * offset + (1.0F - offset) * 0.2D + 0.6D;
            double zRand = world.rand.nextFloat() * offset + (1.0F - offset) * 0.5D;
            
            EntityItem entityEjectedItem = new EntityItem(world, TE.xCoord + xRand, TE.yCoord + yRand, TE.zCoord + zRand, itemStack);
            
            entityEjectedItem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityEjectedItem);
        }
    }
    
    /**
     * Returns whether block has an owner.
     * This is mainly for older blocks before ownership was implemented.
     */
    public static boolean hasOwner(TEBase TE)
    {
        return !TE.getOwner().equals("");
    }
    
    /**
     * Returns owner of block.
     */
    public static String getOwner(TEBase TE)
    {
        return TE.getOwner();
    }
    
    /**
     * Sets owner of block.
     */
    public static void setOwner(TEBase TE, EntityPlayer entityPlayer)
    {
        TE.setOwner(entityPlayer.getDisplayName());
        TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
    }
    
    /**
     * Returns whether block rotates based on placement conditions.
     * The blocks that utilize this property are mostly atypical, and
     * must be added manually.
     */
    public static boolean blockRotates(Block block)
    {
        return block instanceof BlockQuartz ||
                block instanceof BlockRotatedPillar;
    }
    
    /**
     * Plays block sound.
     */
    public static void playBlockSound(TEBase TE, Block block)
    {
        playBlockSound(TE.getWorldObj(), block, TE.xCoord, TE.yCoord, TE.zCoord);
    }
    
    /**
     * Plays block sound.
     */
    public static void playBlockSound(World world, Block block, int x, int y, int z)
    {
        world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, block.stepSound.func_150496_b(), block.stepSound.getVolume() + 1.0F / 2.0F, block.stepSound.getPitch() * 0.8F);
    }
    
    /**
     * Returns whether block or side block has an attribute.
     * It checks for cover, dye color and overlay.
     */
    public static boolean hasAttribute(TEBase TE, int side)
    {
        return hasCover(TE, side) ||
                hasDyeColor(TE, side) ||
                hasOverlay(TE, side);
    }
    
    /**
     * Strips side of all properties.
     */
    public static void clearAttributes(TEBase TE, int side)
    {
        suppressUpdate = true;
        
        setDyeColor(TE, side, 0);
        setOverlay(TE, side, (ItemStack)null);
        setCover(TE, side, 0, (ItemStack)null);
        setPattern(TE, side, 0);
        
        suppressUpdate = false;
        
        TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
    }
    
    /**
     * Returns cover block metadata.
     */
    public static int getCoverMetadata(TEBase TE, int side)
    {
        return TE.metadata[side];
    }
    
    /**
     * Returns whether block has a cover.
     * Checks if block ID exists and whether it is a valid cover block.
     */
    public static boolean hasCover(TEBase TE, int side)
    {
        Block block = getCover(TE, side);
        int metadata = getCoverMetadata(TE, side);
        
        return block != null &&
                isCover(new ItemStack(block, 1, metadata));
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
    
    /**
     * Returns cover block.
     */
    public static Block getCover(IBlockAccess world, int side, int x, int y, int z)
    {
        TEBase TE = (TEBase) world.getTileEntity(x, y, z);
        
        return getCover(TE, side);
    }
    
    /**
     * Returns cover block.
     */
    public static Block getCover(TEBase TE, int side)
    {
        if (TE.cover[side] == null) {
            return TE.getBlockType();
        } else {
            Block block = Block.getBlockFromName(TE.cover[side]);
            
            if (block != null) {
                return block;
            } else {
                return TE.getBlockType();
            }
        }
    }
    
    /**
     * Returns whether block is a cover.
     */
    public static boolean isCover(ItemStack itemStack)
    {
        if (itemStack.getItem() instanceof ItemBlock && !isOverlay(itemStack))
        {
            Block block = Block.getBlockFromItem(itemStack.getItem());
            
            return !block.hasTileEntity(itemStack.getItemDamage()) &&
                    (
                            block.renderAsNormalBlock() ||
                            block instanceof BlockSlab ||
                            block instanceof BlockPane ||
                            block instanceof BlockBreakable
                            );
        }
        
        return false;
    }
    
    /**
     * Converts ItemStack block metadata to correct value.
     * Things like logs should always drop with metadata 0, even if
     * a rotation in metadata is set.
     */
    public static ItemStack getFilteredBlock(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            int damageDropped = Block.getBlockFromItem(itemStack.getItem()).damageDropped(itemStack.getItemDamage());
            
            if (damageDropped != itemStack.getItemDamage()) {
                itemStack.setItemDamage(damageDropped);
            }
        }
        
        return itemStack;
    }
    
    /**
     * Sets cover block.
     */
    public static boolean setCover(TEBase TE, int side, int metadata, ItemStack itemStack)
    {
        if (hasCover(TE, side)) {
            ejectEntity(TE, getFilteredBlock(new ItemStack(getCover(TE, side), 1, getCoverMetadata(TE, side))));
        }
        
        Block block = Block.getBlockFromItem(itemStack.getItem());
        
        TE.cover[side] = block.getUnlocalizedName();
        
        World world = TE.getWorldObj();
        
        if (side == 6) {
            world.setBlockMetadataWithNotify(TE.xCoord, TE.yCoord, TE.zCoord, metadata, 0);
        }
        
        world.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, block);
        world.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
        
        return true;
    }
    
    /**
     * Get block data.
     * Will handle signed data types automatically.
     */
    public final static int getData(TEBase TE)
    {
        return TE.data & 0xffff;
    }
    
    /**
     * Set block data.
     * Will do nothing if data is not altered.
     */
    public static void setData(TEBase TE, int data)
    {
        /* No need to update if data hasn't changed. */
        if (data != getData(TE))
        {
            TE.data = (short) data;
            
            if (!suppressUpdate) {
                TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
            }
        }
    }
    
    /**
     * Returns whether side has cover.
     */
    public static boolean hasDyeColor(TEBase TE, int side)
    {
        return TE.color[side] > 0;
    }
    
    /**
     * Sets color for side.
     */
    public static boolean setDyeColor(TEBase TE, int side, int metadata)
    {
        if (TE.color[side] > 0) {
            ejectEntity(TE, new ItemStack(Items.dye, 1, 15 - TE.color[side]));
        }
        
        TE.color[side] = (byte) metadata;
        
        if (!suppressUpdate) {
            TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
        }
        
        return true;
    }
    
    /**
     * Returns dye color for side.
     */
    public static int getDyeColor(TEBase TE, int side)
    {
        return TE.color[side];
    }
    
    /**
     * Sets overlay.
     */
    public static boolean setOverlay(TEBase TE, int side, ItemStack itemStack)
    {
        if (hasOverlay(TE, side)) {
            ejectEntity(TE, OverlayHandler.getItemStack(TE.overlay[side]));
        }
        
        TE.overlay[side] = (byte) OverlayHandler.getKey(itemStack);
        
        if (!suppressUpdate) {
            TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
        }
        
        return true;
    }
    
    /**
     * Returns overlay.
     */
    public static int getOverlay(TEBase TE, int side)
    {
        return TE.overlay[side];
    }
    
    /**
     * Returns whether block has overlay.
     */
    public static boolean hasOverlay(TEBase TE, int side)
    {
        return TE.overlay[side] > 0;
    }
    
    /**
     * Returns whether ItemStack contains a valid overlay item or block.
     */
    public static boolean isOverlay(ItemStack itemStack)
    {
        return OverlayHandler.overlayMap.containsValue(itemStack.getItem());
    }
    
    /**
     * Returns whether block has pattern.
     */
    public static boolean hasPattern(TEBase TE, int side)
    {
        return getPattern(TE, side) > 0;
    }
    
    /**
     * Returns pattern.
     */
    public static int getPattern(TEBase TE, int side)
    {
        return TE.pattern[side] & 0xffff;
    }
    
    /**
     * Sets pattern.
     */
    public static boolean setPattern(TEBase TE, int side, int pattern)
    {
        TE.pattern[side] = (byte) pattern;
        
        if (!suppressUpdate) {
            TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
        }
        
        return true;
    }
    
    /**
     * Returns whether side should render based on cover blocks
     * of both source and adjacent block.
     */
    public static boolean shouldRenderSharedFaceBasedOnCovers(TEBase TE_adj, TEBase TE_src)
    {
        Block block_adj= BlockProperties.getCover(TE_adj, 6);
        Block block_src = BlockProperties.getCover(TE_src, 6);
        
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
