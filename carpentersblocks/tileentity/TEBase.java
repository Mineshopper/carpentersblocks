package carpentersblocks.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.OverlayHandler;

public class TEBase extends TileEntity {

	/* MC 1.7+ keys */

    private final String TAG_COVER      = "cover";
    private final String TAG_DYE        = "dye";
    private final String TAG_OVERLAY    = "overlay";
    private final String TAG_ITEMSTACKS = "itemstacks";
    private final String TAG_METADATA   = "metadata";

    /* MC 1.7+ fields */

    public ItemStack[] newCover   = new ItemStack[7];
    public ItemStack[] newDye     = new ItemStack[7];
    public ItemStack[] newOverlay = new ItemStack[7];

	/* MC 1.6+ fields */

    public short[] cover  = new short[7];
    public byte[] pattern = new byte[7];
    public byte[] color   = new byte[7];
    public byte[] overlay = new byte[7];

    /** Holds information like direction, block type, etc. */
    public short data;

    /** Holds name of player that created tile entity. */
    private String owner = "";

    private ItemStack[] getOverlaysAsItemStacks()
    {
    	ItemStack[] itemStack = new ItemStack[7];
    	for (int idx = 0; idx < 7; ++idx) {
    		if (BlockProperties.hasOverlay(this, idx)) {
    			itemStack[idx] = OverlayHandler.getItemStack(overlay[idx]);
    		}
    	}
    	return itemStack;
    }

    private ItemStack[] getCoversAsItemStacks()
    {
    	ItemStack[] itemStack = new ItemStack[7];
    	for (int idx = 0; idx < 7; ++idx) {
    		if (BlockProperties.hasCover(this, idx)) {
    			itemStack[idx] = new ItemStack(BlockProperties.getCoverID(this, idx), 1, BlockProperties.getCoverMetadata(this, idx));
    		}
    	}
    	return itemStack;
    }

    private ItemStack[] getColorAsItemStacks()
    {
    	ItemStack[] itemStack = new ItemStack[7];
    	for (int idx = 0; idx < 7; ++idx) {
    		if (BlockProperties.hasDyeColor(this, idx)) {
    			itemStack[idx] = new ItemStack(Item.dyePowder, 1, 15 - color[idx]);
    		}
    	}
    	return itemStack;
    }

    /**
     * Create new NBT tags that are compatible with 1.7.2 branch.
     */
    private void writeToNBTMigrate(NBTTagCompound nbt)
    {
        NBTTagList itemstack_list = new NBTTagList();

        newCover = getCoversAsItemStacks();
        newDye = getColorAsItemStacks();
        newOverlay = getOverlaysAsItemStacks();

        for (byte side = 0; side < 7; ++side)
        {
            if (newCover[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_COVER, side);
                newCover[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
            if (newDye[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_DYE, side);
                newDye[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
            if (newOverlay[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(TAG_OVERLAY, side);
                newOverlay[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
        }

        nbt.setTag(TAG_ITEMSTACKS, itemstack_list);
        nbt.setShort(TAG_METADATA, data);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        for (int count = 0; count < 7; ++count) {
            cover[count] = nbt.getShort("cover_" + count);
        }

        pattern = nbt.getByteArray("pattern");
        color = nbt.getByteArray("color");
        overlay = nbt.getByteArray("overlay");
        data = nbt.getShort("data");

        /* For compatibility with versions prior to 1.9.7 */

        if (nbt.hasKey("owner")) {
            owner = nbt.getString("owner");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        for (int count = 0; count < 7; ++count) {
            nbt.setShort("cover_" + count, cover[count]);
        }

        nbt.setByteArray("pattern", pattern);
        nbt.setByteArray("color", color);
        nbt.setByteArray("overlay", overlay);
        nbt.setShort("data", data);
        nbt.setString("owner", owner);

        /* For compatibility when migrating to Carpenter's Blocks for MC 1.7+ */

        writeToNBTMigrate(nbt);
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
     * @param x X Postion
     * @param y Y Position
     * @param z Z Position
     * @return True to remove the old tile entity, false to keep it in tact {and create a new one if the new values specify to}
     */
    @Override
    public boolean shouldRefresh(int oldID, int newID, int oldMeta, int newMeta, World world, int x, int y, int z)
    {
        return oldID != newID;
    }

    /**
     * Returns true if entityPlayer is owner of tile entity.
     */
    public boolean isOwner(EntityLivingBase entityLiving)
    {
        return owner.equals(entityLiving.getEntityName()) || owner.equals("");
    }

    /**
     * Sets owner of tile entity.
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    /**
     * Returns owner of tile entity.
     */
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
