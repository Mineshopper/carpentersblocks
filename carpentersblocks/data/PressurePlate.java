package carpentersblocks.data;

import net.minecraft.block.material.Material;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class PressurePlate {

    /**
     * 16-bit data components:
     *
     * [000000000]  [00]     [0]       [0]    [000]
     * Unused       Trigger  Polarity  State  Type
     */

    public final static byte POLARITY_POSITIVE = 0;
    public final static byte POLARITY_NEGATIVE = 1;

    public final static byte STATE_OFF = 0;
    public final static byte STATE_ON  = 1;

    public final static byte TRIGGER_PLAYER  = 0;
    public final static byte TRIGGER_MONSTER = 1;
    public final static byte TRIGGER_ANIMAL  = 2;
    public final static byte TRIGGER_ALL     = 3;

    /**
     * Returns type.
     */
    public static int getType(TEBase TE)
    {
        return BlockProperties.getData(TE) & 0x7;
    }

    /**
     * Sets type.
     */
    public static void setType(TEBase TE, int type)
    {
        int temp = BlockProperties.getData(TE) & 0xfff8;
        temp |= type;

        BlockProperties.setData(TE, temp);
    }

    /**
     * Returns state.
     */
    public static int getState(TEBase TE)
    {
        int temp = BlockProperties.getData(TE) & 0x8;
        return temp >> 3;
    }

    /**
     * Sets state.
     */
    public static void setState(TEBase TE, int state, boolean playSound)
    {
        int temp = BlockProperties.getData(TE) & 0xfff7;
        temp |= state << 3;

        if (
                !TE.worldObj.isRemote &&
                BlockProperties.getCoverBlock(TE, 6).blockMaterial != Material.cloth &&
                playSound &&
                getState(TE) != state
                ) {
            TE.worldObj.playSoundEffect(TE.xCoord + 0.5D, TE.yCoord + 0.1D, TE.zCoord + 0.5D, "random.click", 0.3F, getState(TE) == STATE_ON ? 0.5F : 0.6F);
        }

        BlockProperties.setData(TE, temp);
    }

    /**
     * Returns polarity.
     */
    public static int getPolarity(TEBase TE)
    {
        int temp = BlockProperties.getData(TE) & 0x10;
        return temp >> 4;
    }

    /**
     * Sets polarity.
     */
    public static void setPolarity(TEBase TE, int polarity)
    {
        int temp = BlockProperties.getData(TE) & 0xffef;
        temp |= polarity << 4;

        BlockProperties.setData(TE, temp);
    }

    /**
     * Returns trigger entity.
     */
    public static int getTriggerEntity(TEBase TE)
    {
        int temp = BlockProperties.getData(TE) & 0x60;
        return temp >> 5;
    }

    /**
     * Sets trigger entity.
     */
    public static void setTriggerEntity(TEBase TE, int trigger)
    {
        int temp = BlockProperties.getData(TE) & 0xff9f;
        temp |= trigger << 5;

        BlockProperties.setData(TE, temp);
    }

}
