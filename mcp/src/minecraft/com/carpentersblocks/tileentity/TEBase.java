package com.carpentersblocks.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import com.carpentersblocks.util.protection.IProtected;

public class TEBase extends TileEntity implements IProtected {

    private final String TAG_COVER         = "cover";
    private final String TAG_DYE           = "dye";
    private final String TAG_OVERLAY       = "overlay";
    private final String TAG_ITEMSTACKS    = "itemstacks";
    private final String TAG_METADATA      = "metadata";
    private final String TAG_OWNER         = "owner";
    private final String TAG_CHISEL_DESIGN = "chiselDesign";
    private final String TAG_DESIGN        = "design";

    public ItemStack[] cover   = new ItemStack[7];
    public ItemStack[] dye     = new ItemStack[7];
    public ItemStack[] overlay = new ItemStack[7];

    /** Chisel design for each side and base block. */
    public String[] chiselDesign = { "", "", "", "", "", "", "" };

    /** Holds specific block information like facing, states, etc. */
    public short metadata;

    /** Design name. */
    public String design = "";

    /** Owner of tile entity. */
    private String owner = "";

    /** Used to convert old data types to new format. */
    protected MigrationHelper migrationHelper = new MigrationHelper();

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        if (nbt.hasKey("data")) {
            migrationHelper.cacheNBT(nbt);
        }

        NBTTagList itemstack_list = nbt.getTagList(TAG_ITEMSTACKS);

        cover = new ItemStack[7];
        dye = new ItemStack[7];
        overlay = new ItemStack[7];

        for (int idx = 0; idx < itemstack_list.tagCount(); ++idx)
        {
            NBTTagCompound nbt1 = (NBTTagCompound)itemstack_list.tagAt(idx);

            if (((NBTTagCompound)itemstack_list.tagAt(idx)).hasKey(TAG_COVER)) {
                cover[nbt1.getByte(TAG_COVER)] = ItemStack.loadItemStackFromNBT(nbt1);
            } else if (((NBTTagCompound)itemstack_list.tagAt(idx)).hasKey(TAG_DYE)) {
                dye[nbt1.getByte(TAG_DYE)] = ItemStack.loadItemStackFromNBT(nbt1);
            } else if (((NBTTagCompound)itemstack_list.tagAt(idx)).hasKey(TAG_OVERLAY)) {
                overlay[nbt1.getByte(TAG_OVERLAY)] = ItemStack.loadItemStackFromNBT(nbt1);
            }
        }

        for (int idx = 0; idx < 7; ++idx) {
            chiselDesign[idx] = nbt.getString(TAG_CHISEL_DESIGN + "_" + idx);
        }

        metadata = nbt.getShort(TAG_METADATA);
        design = nbt.getString(TAG_DESIGN);
        owner = nbt.getString(TAG_OWNER);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        if (migrationHelper.containsCache) {

            migrationHelper.writeToNBT(this, nbt);
            migrationHelper.containsCache = false;
            getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);

        } else {

            NBTTagList list = new NBTTagList();

            for (byte side = 0; side < 7; ++side)
            {
                if (cover[side] != null) {
                    NBTTagCompound nbt1 = new NBTTagCompound();
                    nbt1.setByte(TAG_COVER, side);
                    cover[side].writeToNBT(nbt1);
                    list.appendTag(nbt1);
                }
                if (dye[side] != null) {
                    NBTTagCompound nbt1 = new NBTTagCompound();
                    nbt1.setByte(TAG_DYE, side);
                    dye[side].writeToNBT(nbt1);
                    list.appendTag(nbt1);
                }
                if (overlay[side] != null) {
                    NBTTagCompound nbt1 = new NBTTagCompound();
                    nbt1.setByte(TAG_OVERLAY, side);
                    overlay[side].writeToNBT(nbt1);
                    list.appendTag(nbt1);
                }
            }

            nbt.setTag(TAG_ITEMSTACKS, list);

            for (int idx = 0; idx < 7; ++idx) {
                nbt.setString(TAG_CHISEL_DESIGN + "_" + idx, chiselDesign[idx]);
            }

            nbt.setShort(TAG_METADATA, metadata);
        }

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
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
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
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        readFromNBT(pkt.data);

        if (worldObj.isRemote) {
            Minecraft.getMinecraft().renderGlobal.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
            worldObj.updateAllLightTypes(xCoord, yCoord, zCoord);
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
    public boolean shouldRefresh(int oldID, int newID, int oldMeta, int newMeta, World world, int x, int y, int z)
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
        return oldID != newID;
    }

    /**
     * Sets owner of tile entity.
     */
    @Override
    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    /**
     * Returns owner of tile entity.
     */
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
