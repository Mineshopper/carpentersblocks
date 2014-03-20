package carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;

import carpentersblocks.util.ModLogger;
import carpentersblocks.util.registry.FeatureRegistry;

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
        for (String name : FeatureRegistry.overlay_list) {
            
            String itemName = name.substring(0, name.indexOf(":"));
            String overlayType = name.substring(name.indexOf(":") + 1);

            Overlay overlay = Overlay.NONE;
            
            if (overlayType.equals("grass")) {
                overlay = Overlay.GRASS;
            } else if (overlayType.equals("snow")) {
                overlay = Overlay.SNOW;
            } else if (overlayType.equals("web")) {
                overlay = Overlay.WEB;
            } else if (overlayType.equals("vine")) {
                overlay = Overlay.VINE;
            } else if (overlayType.equals("hay")) {
                overlay = Overlay.HAY;
            } else if (overlayType.equals("mycelium")) {
                overlay = Overlay.MYCELIUM;
            }
            
            overlayMap.put(itemName, overlay);

        }
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
