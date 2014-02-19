package carpentersblocks.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TEBase extends TileEntity {
    
    public ItemStack[] cover = new ItemStack[7];
    public byte[] pattern  = new byte[7];
    public byte[] color    = new byte[7];
    public byte[] overlay  = new byte[7];
    
    /** Holds specific block information like facing, states, etc. */
    public short data;
    
    /** Holds name of player that created tile entity. */
    private String owner = "";
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        //cover = new ItemStack[cover.length];
        NBTTagList list = nbt.getTagList("covers", 10);

        for (int idx = 0; idx < list.tagCount(); ++idx)
        {
            NBTTagCompound nbt1 = list.getCompoundTagAt(idx);
            cover[nbt1.getByte("cover") & 255] = ItemStack.loadItemStackFromNBT(nbt1);
        }

        pattern  = nbt.getByteArray("pattern");
        color    = nbt.getByteArray("color");
        overlay  = nbt.getByteArray("overlay");
        data     = nbt.getShort("data");
        owner    = nbt.getString("owner");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        NBTTagList coverList = new NBTTagList();
        
        for (int side = 0; side < cover.length; ++side)
        {
            if (cover[side] != null)
            {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte("cover", (byte) side);
                cover[side].writeToNBT(nbt1);
                coverList.appendTag(nbt1);
            }
        }
        
        nbt.setTag("covers", coverList);
        nbt.setByteArray("pattern", pattern);
        nbt.setByteArray("color", color);
        nbt.setByteArray("overlay", overlay);
        nbt.setShort("data", data);
        nbt.setString("owner", owner);
    }
    
    @Override
    /**
     * Overridden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
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
            Minecraft.getMinecraft().renderGlobal.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
            worldObj.func_147451_t(xCoord, yCoord, zCoord);
        }
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
