package carpentersblocks.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TEBase extends TileEntity {

    public short[] cover  = new short[7];
    public byte[] pattern = new byte[7];
    public byte[] color   = new byte[7];
    public byte[] overlay = new byte[7];

    /** Holds information like direction, block type, etc. */
    public short data;

    /** Holds name of player that created tile entity. */
    private String owner = "";

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
    
    private boolean isVanilla = getClass().getName().startsWith("net.minecraft.tileentity");
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
