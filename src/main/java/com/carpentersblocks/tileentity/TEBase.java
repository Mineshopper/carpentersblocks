package com.carpentersblocks.tileentity;

import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import com.carpentersblocks.util.protection.IProtected;
import com.carpentersblocks.util.protection.ProtectedUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class TEBase extends TileEntity implements IProtected {

    private final String TAG_COVER         = "cover";
    private final String TAG_DYE           = "dye";
    private final String TAG_OVERLAY       = "overlay";
    private final String TAG_ILLUMINATOR   = "illuminator";
    private final String TAG_ITEMSTACKS    = "itemstacks";
    private final String TAG_METADATA      = "metadata";
    private final String TAG_OWNER         = "owner";
    private final String TAG_CHISEL_DESIGN = "chiselDesign";
    private final String TAG_DESIGN        = "design";

    public ItemStack[] cover   = new ItemStack[7];
    public ItemStack[] dye     = new ItemStack[7];
    public ItemStack[] overlay = new ItemStack[7];
    public ItemStack   illuminator;

    /** Chisel design for each side and base block. */
    public String[] chiselDesign = { "", "", "", "", "", "", "" };

    /** Holds specific block information like facing, states, etc. */
    public short metadata;

    /** Design name. */
    public String design = "";

    /** Owner of tile entity. */
    private String owner = "";

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        NBTTagList itemstack_list = nbt.getTagList(TAG_ITEMSTACKS, 10);

        cover = new ItemStack[7];
        dye = new ItemStack[7];
        overlay = new ItemStack[7];
        illuminator = null;

        for (int idx = 0; idx < itemstack_list.tagCount(); ++idx)
        {
            NBTTagCompound nbt1 = itemstack_list.getCompoundTagAt(idx);
            ItemStack tempStack = ItemStack.loadItemStackFromNBT(nbt1);

            /*
             * All ItemStacks pre-3.2.7 DEV R3 stored original
             * stack sizes, reduce them here.
             */
            tempStack.stackSize = 1;

            if (itemstack_list.getCompoundTagAt(idx).hasKey(TAG_COVER)) {
                cover[nbt1.getByte(TAG_COVER) & 255] = tempStack;
            } else if (itemstack_list.getCompoundTagAt(idx).hasKey(TAG_DYE)) {
                dye[nbt1.getByte(TAG_DYE) & 255] = tempStack;
            } else if (itemstack_list.getCompoundTagAt(idx).hasKey(TAG_OVERLAY)) {
                overlay[nbt1.getByte(TAG_OVERLAY) & 255] = tempStack;
            } else if (itemstack_list.getCompoundTagAt(idx).hasKey(TAG_ILLUMINATOR)) {
                illuminator = tempStack;
            }
        }

        for (int idx = 0; idx < 7; ++idx) {
            chiselDesign[idx] = nbt.getString(TAG_CHISEL_DESIGN + "_" + idx);
        }

        metadata = nbt.getShort(TAG_METADATA);
        design = nbt.getString(TAG_DESIGN);
        owner = nbt.getString(TAG_OWNER);

        /*
         * Attempt to update owner name to new UUID format.
         * TODO: Remove when player name-changing system is switched on
         */
        if (FMLCommonHandler.instance().getSide().equals(Side.SERVER)) {
            ProtectedUtil.updateOwnerUUID(this);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        NBTTagList itemstack_list = new NBTTagList();

        for (byte side = 0; side < 7; ++side)
        {
            if (cover[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_COVER, side);
                cover[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
            if (dye[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_DYE, side);
                dye[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
            if (overlay[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_OVERLAY, side);
                overlay[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
        }

        if (illuminator != null) {
            NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setByte(TAG_ILLUMINATOR, (byte) 6);
            illuminator.writeToNBT(nbt1);
            itemstack_list.appendTag(nbt1);
        }

        nbt.setTag(TAG_ITEMSTACKS, itemstack_list);

        for (int idx = 0; idx < 7; ++idx) {
            nbt.setString(TAG_CHISEL_DESIGN + "_" + idx, chiselDesign[idx]);
        }

        nbt.setShort(TAG_METADATA, metadata);
        nbt.setString(TAG_DESIGN, design);
        nbt.setString(TAG_OWNER, owner);
    }

    @Override
    /**
     * Overridden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
    }

    @Override
    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());

        if (worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            // TODO: Rename to updateAllLightByTypes
            worldObj.func_147451_t(xCoord, yCoord, zCoord);
        }
    }

    /**
     * Called from Chunk.setBlockIDWithMetadata, determines if this tile entity should be re-created when the ID, or Metadata changes.
     * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
     *
     * @param oldID The old ID of the block
     * @param newID The new ID of the block (May be the same)
     * @param oldMeta The old metadata of the block
     * @param newMeta The new metadata of the block (May be the same)
     * @param world Current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return True to remove the old tile entity, false to keep it in tact {and create a new one if the new values specify to}
     */
    @Override
    public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z)
    {
        /*
         * This is a curious method.
         *
         * Essentially, when doing most block logic server-side, changes
         * to blocks will momentarily "flash" to their default state
         * when rendering client-side.  This is most noticeable when adding
         * or removing covers for the first time.
         *
         * Making the tile entity refresh only when the block is first created
         * is not only reasonable, but fixes this behavior.
         */
        return oldBlock != newBlock;
    }

    /**
     * Sets owner of tile entity.
     */
    @Override
    public void setOwner(UUID uuid)
    {
        owner = uuid.toString();
        markDirty();
    }

    @Override
    public String getOwner()
    {
        return owner;
    }

    @Override
    /**
     * Determines if this TileEntity requires update calls.
     * @return True if you want updateEntity() to be called, false if not
     */
    public boolean canUpdate()
    {
        return false;
    }

}
