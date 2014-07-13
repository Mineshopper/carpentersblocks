package carpentersblocks.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

public class TilePacket implements ICarpentersPacket {

    protected int x;
    protected int y;
    protected int z;

    public TilePacket() {}

    /**
     * Creates a packet that passes x, y, z coordinates.
     */
    public TilePacket(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void processData(EntityPlayer entityPlayer, DataInputStream bbis) throws IOException
    {
        x = bbis.readInt();
        y = bbis.readInt();
        z = bbis.readInt();
    }

    @Override
    public void appendData(DataOutputStream outputStream) throws IOException
    {
        outputStream.writeInt(x);
        outputStream.writeInt(y);
        outputStream.writeInt(z);
    }

}
