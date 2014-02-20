package carpentersblocks.data;

import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class Ladder {
    
    public final static byte FACING_ON_X  = 0;
    public final static byte FACING_ON_Z  = 1;
    public final static byte FACING_NORTH = 2;
    public final static byte FACING_SOUTH = 3;
    public final static byte FACING_WEST  = 4;
    public final static byte FACING_EAST  = 5;
    
    /**
     * Returns true if ladder is not connected to side of a block.
     */
    public static boolean isFreestanding(TEBase TE)
    {
        return BlockProperties.getMetadata(TE) < FACING_NORTH;
    }
    
}
