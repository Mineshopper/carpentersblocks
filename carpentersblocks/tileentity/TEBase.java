package carpentersblocks.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TEBase extends TileEntity {
    
    private final String IDENT_COVER      = "cover";
    private final String IDENT_DYE        = "dye";
    private final String IDENT_OVERLAY    = "overlay";
    private final String IDENT_ITEMSTACKS = "itemstacks";
    private final String IDENT_METADATA   = "metadata";
    private final String IDENT_OWNER      = "owner";
    private final String IDENT_PATTERN    = "pattern";
    
    public ItemStack[] cover   = new ItemStack[7];
    public ItemStack[] dye     = new ItemStack[7];
    public ItemStack[] overlay = new ItemStack[7];
    public byte[] pattern      = new byte[7];
    
    /** Holds specific block information like facing, states, etc. */
    public short metadata;
    
    /** Holds name of player that created tile entity. */
    private String owner = "";
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        NBTTagList itemstack_list = nbt.getTagList(IDENT_ITEMSTACKS, 10);
        
        cover   = new ItemStack[7];
        dye     = new ItemStack[7];
        overlay = new ItemStack[7];
        
        for (int idx = 0; idx < itemstack_list.tagCount(); ++idx)
        {
            NBTTagCompound nbt1 = itemstack_list.getCompoundTagAt(idx);
            
            if (itemstack_list.getCompoundTagAt(idx).hasKey(IDENT_COVER)) {
                cover[nbt1.getByte(IDENT_COVER) & 255] = ItemStack.loadItemStackFromNBT(nbt1);
            } else if (itemstack_list.getCompoundTagAt(idx).hasKey(IDENT_DYE)) {
                dye[nbt1.getByte(IDENT_DYE) & 255] = ItemStack.loadItemStackFromNBT(nbt1);
            } else if (itemstack_list.getCompoundTagAt(idx).hasKey(IDENT_OVERLAY)) {
                overlay[nbt1.getByte(IDENT_OVERLAY) & 255] = ItemStack.loadItemStackFromNBT(nbt1);
            }
        }

        pattern  = nbt.getByteArray(IDENT_PATTERN);
        metadata = nbt.getShort(IDENT_METADATA);
        owner    = nbt.getString(IDENT_OWNER);
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
                nbt1.setByte(IDENT_COVER, side);
                cover[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
            if (dye[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(IDENT_DYE, side);
                dye[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
            if (overlay[side] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte(IDENT_OVERLAY, side);
                overlay[side].writeToNBT(nbt1);
                itemstack_list.appendTag(nbt1);
            }
        }
        
        nbt.setTag(IDENT_ITEMSTACKS, itemstack_list);
        
        nbt.setByteArray(IDENT_PATTERN, pattern);
        nbt.setShort(IDENT_METADATA, metadata);
        nbt.setString(IDENT_OWNER, owner);
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
    public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z)
    {
        return oldBlock != newBlock;
    }
    
    /**
     * Returns true if entityPlayer is owner of tile entity.
     */
    public boolean isOwner(EntityPlayer entityPlayer)
    {
        return owner.equals(entityPlayer.getDisplayName()) || owner.equals("");
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
