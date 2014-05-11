package carpentersblocks.data;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class FlowerPot {

    /**
     * 16-bit data components:
     *
     * [000]  [0]      [0000] [00000000]
     * Unused Enriched Angle  Design
     */

    public final static byte COLOR_NATURAL  = 0;
    public final static byte COLOR_ENRICHED = 1;

    /**
     * Returns enrichment.
     */
    public static boolean isEnriched(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x1000;
        return temp > 0;
    }

    /**
     * Sets enrichment.
     */
    public static void setEnrichment(TEBase TE, boolean enriched)
    {
        if (isEnriched(TE) && !enriched) {
            BlockProperties.ejectEntity(TE, new ItemStack(Items.dye, 1, 15));
        }

        int temp = BlockProperties.getMetadata(TE) & 0xefff;
        temp |= (enriched ? 1 : 0) << 12;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns angle as value from 0 to 15.
     */
    public static int getAngle(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xf00;
        return temp >> 8;
    }

    /**
     * Sets angle as value from 0 to 15.
     */
    public static void setAngle(TEBase TE, int angle)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xf0ff;
        temp |= angle << 8;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns design.
     */
    public static int getDesign(TEBase TE)
    {
        return BlockProperties.getMetadata(TE) & 0xff;
    }

    /**
     * Sets design.
     */
    public static void setDesign(TEBase TE, int design)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xff00;
        temp |= design;

        if (design > 0 && BlockProperties.hasCover(TE, 6)) {
            BlockProperties.setCover(TE, 6, (ItemStack)null);
        }

        BlockProperties.setMetadata(TE, temp);
    }

}
