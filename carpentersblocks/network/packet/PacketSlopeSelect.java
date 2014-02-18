package carpentersblocks.network.packet;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class PacketSlopeSelect extends C17PacketCustomPayload {
    
    boolean nextSlope;
    
    public PacketSlopeSelect(boolean nextSlope)
    {
        this.nextSlope = nextSlope;
    }
    
    @Override
    public void readPacketData(PacketBuffer var1) throws IOException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void writePacketData(PacketBuffer var1) throws IOException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void processPacket(INetHandler var1)
    {
        
    }
    
}