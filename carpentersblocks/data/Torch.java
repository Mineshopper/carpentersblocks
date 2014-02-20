package carpentersblocks.data;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class Torch {
    
    /**
     * 16-bit data components:
     *
     * [0000000000]  [0]    [00]   [000]
     * Unused        Ready  State  Facing
     */
    
    public enum State
    {
        LIT,
        SMOLDERING,
        UNLIT
    }
    
    /**
     * Returns facing.
     */
    public static ForgeDirection getFacing(TEBase TE)
    {
        return ForgeDirection.getOrientation(BlockProperties.getMetadata(TE) & 0x7);
    }
    
    /**
     * Sets facing.
     */
    public static void setFacing(TEBase TE, int side)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfff8;
        temp |= side;
        
        BlockProperties.setMetadata(TE, temp);
    }
    
    /**
     * Returns state.
     */
    public static State getState(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x18;
        int val = temp >> 3;
        
        return val == State.LIT.ordinal() ? State.LIT : val == State.SMOLDERING.ordinal() ? State.SMOLDERING : State.UNLIT;
    }
    
    /**
     * Sets state.
     */
    public static void setState(TEBase TE, State state)
    {
        if (state.ordinal() > getState(TE).ordinal()) {
            double[] headCoords = getHeadCoordinates(TE);
            World world = TE.getWorldObj();
            world.playSoundEffect(headCoords[0], headCoords[1], headCoords[2], "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        }
        
        int temp = BlockProperties.getMetadata(TE) & 0xffe7;
        temp |= state.ordinal() << 3;
        
        BlockProperties.setMetadata(TE, temp);
    }
    
    /**
     * Returns whether block is capable of handling logic functions.
     * This is implemented because for buttons and levers the SERVER
     * lags behind the client and will cause the block to pop of walls
     * before it has a chance to set the correct facing.
     */
    public static boolean isReady(TEBase TE)
    {
        return (BlockProperties.getMetadata(TE) & 0x20) > 0;
    }
    
    /**
     * Sets block as ready.
     */
    public static void setReady(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xffdf;
        temp |= 1 << 5;
        
        BlockProperties.setMetadata(TE, temp);
    }
    
    /**
     * Returns location where particles and sounds originate.
     */
    public static double[] getHeadCoordinates(TEBase TE)
    {
        double[] coords;
        
        double xOffset = TE.xCoord + 0.5F;
        double yOffset = TE.yCoord + 0.7F;
        double zOffset = TE.zCoord + 0.5F;
        double offset1 = 0.2199999988079071D;
        double offset2 = 0.27000001072883606D;
        
        ForgeDirection facing = getFacing(TE);
        
        switch (facing) {
            case NORTH:
                coords = new double[] { xOffset, yOffset + offset1, zOffset + offset2 };
                break;
            case SOUTH:
                coords = new double[] { xOffset, yOffset + offset1, zOffset - offset2 };
                break;
            case WEST:
                coords = new double[] { xOffset + offset2, yOffset + offset1, zOffset };
                break;
            case EAST:
                coords = new double[] { xOffset - offset2, yOffset + offset1, zOffset };
                break;
            default:
                coords = new double[] { xOffset, yOffset, zOffset };
        }
        
        return coords;
    }
    
}
