package carpentersblocks.data;

import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class DaylightSensor {

    /**
     * 16-bit data components:
     *
     * [000000000]  [00]         [0]       [0000]
     * Unused       Sensitivity  Polarity  LightLevel
     */

    public final static byte POLARITY_POSITIVE = 0;
    public final static byte POLARITY_NEGATIVE = 1;

    public final static byte SENSITIVITY_SLEEP    = 0;
    public final static byte SENSITIVITY_MONSTERS = 1;

    /**
     * Returns light level.
     */
    public static int getLightLevel(TEBase TE)
    {
        return BlockProperties.getData(TE) & 0xf;
    }

    /**
     * Sets light level.
     */
    public static void setLightLevel(TEBase TE, int lightLevel)
    {
        int temp = BlockProperties.getData(TE) & 0xfff0;
        temp |= lightLevel;

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
    public static void setPolarity(TEBase TE, int state)
    {
        int temp = BlockProperties.getData(TE) & 0xffef;
        temp |= state << 4;

        BlockProperties.setData(TE, temp);
    }

    /**
     * Returns sensitivity.
     */
    public static int getSensitivity(TEBase TE)
    {
        int temp = BlockProperties.getData(TE) & 0x60;
        return temp >> 5;
    }

    /**
     * Sets sensitivity.
     */
    private static void setSensitivity(TEBase TE, int sensitivity)
    {
        int temp = BlockProperties.getData(TE) & 0xff9f;
        temp |= sensitivity << 5;

        BlockProperties.setData(TE, temp);
    }

    /**
     * Sets sensor to next sensitivity level.
     * Returns new sensitivity.
     */
    public static int setNextSensitivity(TEBase TE)
    {
        int sensitivity = getSensitivity(TE);

        if (++sensitivity > 1) {
            sensitivity = 0;
        }

        setSensitivity(TE, sensitivity);

        return sensitivity;
    }

    /**
     * Returns whether daylight sensor is in active state.
     */
    public static boolean isActive(TEBase TE)
    {
        boolean posPolarity = getPolarity(TE) == POLARITY_POSITIVE;
        boolean isActive = false;

        int lightLevel = getLightLevel(TE);

        if (getSensitivity(TE) == SENSITIVITY_SLEEP) {
            isActive = lightLevel > 11;
        } else {
            isActive = lightLevel > 7;
        }

        return posPolarity ? isActive : !isActive;
    }

}
