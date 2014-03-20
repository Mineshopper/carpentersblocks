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
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersFlowerPot;
import carpentersblocks.util.handler.DyeHandler;
import carpentersblocks.util.handler.OverlayHandler;

public class BlockProperties {
    
    // TODO: Change to custom sound for PlaySoundEvent overrides when available
    public final static SoundType stepSound = Blocks.planks.stepSound;//new SoundType(CarpentersBlocks.MODID, 1.0F, 1.0F);
    
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
            return Block.getBlockFromItem(itemStack.getItem());
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
            
            if (block.equals(Blocks.snow) || block.equals(Blocks.snow_layer)) {
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
    
    /**
     * Ejects an item at given coordinates.
     */
    public static void ejectEntity(TEBase TE, ItemStack itemStack)
    {
        World world = TE.getWorldObj();

        if (!world.isRemote) {
            
            itemStack.stackSize = 1;
            
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
    public static boolean blockRotates(ItemStack itemStack)
    {
        Block block = toBlock(itemStack);
        
        return block instanceof BlockQuartz ||
               block instanceof BlockRotatedPillar;
    }
    
    /**
     * Plays block sound.
     */
    public static void playBlockSound(TEBase TE, ItemStack itemStack)
    {
        playBlockSound(TE.getWorldObj(), itemStack, TE.xCoord, TE.yCoord, TE.zCoord);
    }
    
    /**
     * Plays block break sound.
     */
    public static void playBlockSound(World world, ItemStack itemStack, int x, int y, int z)
    {
        Block block = toBlock(itemStack);
        
        world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
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
        setPattern(TE, side, 0);
        
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

    /**
     * Returns cover ItemStack for side.
     */
    public static ItemStack getCover(TEBase TE, int side)
    {
        ItemStack itemStack = new ItemStack(TE.getBlockType());
        
        if (TE.cover[side] != null) {
            itemStack = TE.cover[side];
        }

        return itemStack;
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
     * Converts ItemStack damage to correct value.
     * Will correct log drop rotation, among other things.
     */
    public static ItemStack getFilteredBlock(World world, ItemStack itemStack)
    {
        if (itemStack != null) {

            Block block = Block.getBlockFromItem(itemStack.getItem());
            
            Item itemDropped = block.getItemDropped(itemStack.getItemDamage(), world.rand, /* Fortune */ 0);
            int damageDropped = block.damageDropped(itemStack.getItemDamage());
            
            /*
             * Check if block drops itself, and, if so, correct the damage value
             * to the block's default.
             */
            
            if (itemDropped.equals(itemStack.getItem()) && damageDropped != itemStack.getItemDamage()) {
                itemStack.setItemDamage(damageDropped);
            }
            
        }

        return itemStack;
    }
    
    /**
     * Sets cover block.
     */
    public static boolean setCover(TEBase TE, int side, ItemStack itemStack)
    {
        World world = TE.getWorldObj();

        if (hasCover(TE, side)) {
            ejectEntity(TE, getFilteredBlock(world, TE.cover[side]));
        }

        TE.cover[side] = itemStack;

        Block block = itemStack == null ? TE.getBlockType() : Block.getBlockFromItem(itemStack.getItem());
        int metadata = itemStack == null ? 0 : itemStack.getItemDamage();
        
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
    public final static int getMetadata(TEBase TE)
    {
        return TE.metadata & 0xffff;
    }
    
    /**
     * Set block data.
     * Will do nothing if data is not altered.
     */
    public static void setMetadata(TEBase TE, int data)
    {
        /* No need to update if data hasn't changed. */
        
        if (data != getMetadata(TE))
        {
            TE.metadata = (short) data;
            
            if (!suppressUpdate) {
                TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
            }
        }
    }
    
    /**
     * Returns true if ItemStack is a dye.
     */
    public static boolean isDye(ItemStack itemStack)
    {
        return DyeHandler.getDyeObject(itemStack) != null;
    }
    
    /**
     * Returns whether side has cover.
     */
    public static boolean hasDye(TEBase TE, int side)
    {
        return TE.dye[side] != null;
    }
    
    /**
     * Sets color for side.
     */
    public static boolean setDye(TEBase TE, int side, ItemStack itemStack)
    {
        if (TE.dye[side] != null) {
            ejectEntity(TE, TE.dye[side]);
        }
        
        TE.dye[side] = itemStack;
        
        if (!suppressUpdate) {
            TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
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
        if (hasOverlay(TE, side)) {
            ejectEntity(TE, TE.overlay[side]);
        }
        
        TE.overlay[side] = itemStack;
        
        if (!suppressUpdate) {
            TE.getWorldObj().markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
        }
        
        return true;
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
        return TE.overlay[side] != null;
    }
    
    /**
     * Returns whether ItemStack contains a valid overlay item or block.
     */
    public static boolean isOverlay(ItemStack itemStack)
    {
        return OverlayHandler.overlayMap.containsKey(itemStack.getUnlocalizedName());
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
    
}
