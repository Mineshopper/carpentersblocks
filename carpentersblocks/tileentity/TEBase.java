package carpentersblocks.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TEBase extends TileEntity {

	public short[] cover = new short[7];
	public byte[] pattern = new byte[7];
	public byte[] color = new byte[7];
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
		readFromNBT(pkt.customParam1);

		if (worldObj.isRemote) {
			Minecraft.getMinecraft().renderGlobal.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
			worldObj.updateAllLightTypes(xCoord, yCoord, zCoord);
		}
	}

	/**
	 * Returns true if entityPlayer is owner of tile entity.
	 */
	public boolean isOwner(EntityLiving entityLiving)
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

}