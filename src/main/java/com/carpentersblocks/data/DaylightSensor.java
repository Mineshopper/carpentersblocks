package com.carpentersblocks.data;

import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;

public class DaylightSensor implements ISided {

    /**
     * 16-bit data components:
     *
     * [000000] [000] [00]        [0]      [0000]
     * Unused   Dir   Sensitivity Polarity LightLevel
     */

    public final byte POLARITY_POSITIVE = 0;
    public final byte POLARITY_NEGATIVE = 1;

    public final byte SENSITIVITY_SLEEP    = 0;
    public final byte SENSITIVITY_MONSTERS = 1;
    public final byte SENSITIVITY_DYNAMIC  = 2;

    /**
     * Returns direction.
     */
    @Override
    public ForgeDirection getDirection(TEBase TE)
    {
        int side = (BlockProperties.getMetadata(TE) & 0x380) >> 7;
        return ForgeDirection.getOrientation(side);
    }

    /**
     * Sets direction.
     */
    @Override
    public void setDirection(TEBase TE, ForgeDirection dir)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfc7f;
        temp |= dir.ordinal() << 7;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns light level.
     */
    public int getLightLevel(TEBase TE)
    {
        return BlockProperties.getMetadata(TE) & 0xf;
    }

    /**
     * Sets light level.
     */
    public void setLightLevel(TEBase TE, int lightLevel)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfff0;
        temp |= lightLevel;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns polarity.
     */
    public int getPolarity(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x10;
        return temp >> 4;
    }

    /**
     * Sets polarity.
     */
    public void setPolarity(TEBase TE, int state)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xffef;
        temp |= state << 4;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns sensitivity.
     */
    public int getSensitivity(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x60;
        return temp >> 5;
    }

    /**
     * Sets sensitivity.
     */
    private void setSensitivity(TEBase TE, int sensitivity)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xff9f;
        temp |= sensitivity << 5;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Sets sensor to next sensitivity level.
     * Returns new sensitivity.
     */
    public int setNextSensitivity(TEBase TE)
    {
        int sensitivity = getSensitivity(TE);

        if (++sensitivity > 2) {
            sensitivity = 0;
        }

        setSensitivity(TE, sensitivity);

        return sensitivity;
    }

    /**
     * Returns the operational state of the daylight sensor.
     *
     * @param  TE the {@link TEBase}
     * @return true if sensor is outputting redstone current
     */
    public boolean isActive(TEBase TE)
    {
        return getRedstoneOutput(TE) > 0;
    }

    /**
     * Gets redstone output based on sensor polarity, sensitivity, and light level.
     *
     * @param  TE the {@link TEBase}
     * @return redstone output between 0 and 15
     */
    public int getRedstoneOutput(TEBase TE)
    {
        boolean posPolarity = getPolarity(TE) == POLARITY_POSITIVE;
        int output = 0;
        int lightLevel = getLightLevel(TE);
        int sensitivity = getSensitivity(TE);

        if (sensitivity == SENSITIVITY_SLEEP) {
            boolean active = posPolarity ? lightLevel > 11 : lightLevel <= 11;
            if (active) {
                output = 15;
            }
        } else if (sensitivity == SENSITIVITY_MONSTERS) {
            boolean active = posPolarity ? lightLevel > 7 : lightLevel <= 7;
            if (active) {
                output = 15;
            }
        } else {
            output = posPolarity ? lightLevel : 15 - lightLevel;
        }

        return output;
    }

}