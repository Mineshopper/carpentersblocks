package carpentersblocks.data;

import net.minecraft.item.ItemStack;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class FlowerPot {

    /**
     * 16-bit data components:
     *
     * [00000000]  [00000000]
     * Unused      Design
     */

    /**
     * Returns design.
     */
    public static int getDesign(TEBase TE)
    {
        return BlockProperties.getData(TE);
    }

    /**
     * Sets design.
     */
    public static void setDesign(TEBase TE, int design)
    {
        if (design > 0 && BlockProperties.hasCover(TE, 6)) {
            BlockProperties.setCover(TE, 6, 0, (ItemStack)null);
        }

        BlockProperties.setData(TE, design);
    }

}
