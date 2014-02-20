package carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class OverlayHandler {
    
    public enum Overlay {
        NONE,
        GRASS,
        SNOW,
        WEB,
        VINE,
        HAY,
        MYCELIUM
    }

    public static Map overlayMap = new HashMap();
    
    /**
     * Initializes overlays.
     */
    public static void init()
    {
        overlayMap.put(Items.wheat_seeds.getUnlocalizedName()    , Overlay.GRASS   );
        overlayMap.put(Items.snowball.getUnlocalizedName()       , Overlay.SNOW    );
        overlayMap.put(Items.string.getUnlocalizedName()         , Overlay.WEB     );
        overlayMap.put(Blocks.vine.getUnlocalizedName()          , Overlay.VINE    );
        overlayMap.put(Items.wheat.getUnlocalizedName()          , Overlay.HAY     );
        overlayMap.put(Blocks.brown_mushroom.getUnlocalizedName(), Overlay.MYCELIUM);
        overlayMap.put(Blocks.red_mushroom.getUnlocalizedName()  , Overlay.MYCELIUM);
    }
    
    /**
     * Returns overlay from ItemStack.
     */
    public static Overlay getOverlay(ItemStack itemStack)
    {
        if (itemStack != null) {
            return (Overlay) overlayMap.get(itemStack.getUnlocalizedName());
        }
        
        return Overlay.NONE;
    }
    
    /**
     * Returns block with considerations for the overlay and side of block
     * being interacted with.
     * 
     * Used for interaction only; never for drops.
     */
    public static Block getHostBlockFromOverlay(TEBase TE, int side, Block block)
    {
        if (BlockProperties.hasOverlay(TE, side))
        {
            switch (getOverlay(BlockProperties.getOverlay(TE, side))) {
                case GRASS:
                    return Blocks.grass;
                case SNOW:
                    return Blocks.snow;
                case WEB:
                    return Blocks.web;
                case VINE:
                    return Blocks.vine;
                case HAY:
                    return Blocks.hay_block;
                case MYCELIUM:
                    return Blocks.mycelium;
                default:
                    break;
            }     
        }
        
        return block;
    }
    
}
