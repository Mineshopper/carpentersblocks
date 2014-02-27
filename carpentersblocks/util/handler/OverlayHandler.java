package carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

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
        
}
