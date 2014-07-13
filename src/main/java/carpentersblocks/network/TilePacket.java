package carpentersblocks.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

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
    public void processData(EntityPlayer entityPlayer, ByteBufInputStream bbis) throws IOException
    {
        x = bbis.readInt();
        y = bbis.readInt();
        z = bbis.readInt();
    }

    @Override
    public void appendData(ByteBuf buffer) throws IOException
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

}
