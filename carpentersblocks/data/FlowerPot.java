package carpentersblocks.data;

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
        return BlockProperties.getMetadata(TE);
    }
    
    /**
     * Sets design.
     */
    public static void setDesign(TEBase TE, int design)
    {
        BlockProperties.setMetadata(TE, design);
    }
    
}
