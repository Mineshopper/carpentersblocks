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

    public final static byte NO_OVERLAY       = 0;
    public final static byte OVERLAY_GRASS    = 1;
    public final static byte OVERLAY_SNOW     = 2;
    public final static byte OVERLAY_WEB      = 3;
    public final static byte OVERLAY_VINE     = 4;
    public final static byte OVERLAY_HAY      = 5;
    public final static byte OVERLAY_MYCELIUM = 6;
    
    public static Map overlayMap;
    
    /**
     * Initializes overlays.
     */
    public static void init()
    {
        overlayMap = new HashMap();
        overlayMap.put(0, 0                                         );
        overlayMap.put(1, Item.getIdFromItem(Items.wheat_seeds)     );
        overlayMap.put(2, Item.getIdFromItem(Items.snowball)        );
        overlayMap.put(3, Item.getIdFromItem(Items.string)          );
        overlayMap.put(4, Block.getIdFromBlock(Blocks.vine)         );
        overlayMap.put(5, Item.getIdFromItem(Items.wheat)           );
        overlayMap.put(6, Block.getIdFromBlock(Blocks.brown_mushroom));
    }
    
    /**
     * Returns overlay key value of item or block in ItemStack.
     */
    public static int getKey(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            Iterator iterator = overlayMap.entrySet().iterator();
            
            while (iterator.hasNext())
            {
                Map.Entry mEntry = (Map.Entry) iterator.next();
                if (mEntry.getValue().equals(Item.getIdFromItem(itemStack.getItem()))) {
                    return (Integer) mEntry.getKey();
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Returns item or block of overlay wrapped in an ItemStack.
     */
    public static ItemStack getItemStack(int overlay)
    {
        return new ItemStack(Item.getItemById((Integer) overlayMap.get(overlay)), 1, 0);
    }
    
    /**
     * Returns block with considerations for the overlay and side of block
     * being interacted with.
     */
    public static Block getBlockFromOverlay(TEBase TE, int coverSide, Block block)
    {
        if (BlockProperties.hasOverlay(TE, coverSide))
        {
            switch (BlockProperties.getOverlay(TE, coverSide)) {
                case OverlayHandler.OVERLAY_GRASS:
                    block = Blocks.grass;
                    break;
                case OverlayHandler.OVERLAY_HAY:
                    block = Blocks.hay_block;
                    break;
                case OverlayHandler.OVERLAY_MYCELIUM:
                    block = Blocks.mycelium;
                    break;
                case OverlayHandler.OVERLAY_SNOW:
                    block = Blocks.snow;
                    break;
                case OverlayHandler.OVERLAY_VINE:
                    block = Blocks.vine;
                    break;
                case OverlayHandler.OVERLAY_WEB:
                    block = Blocks.web;
                    break;
            }
        }
        
        return block;
    }
    
}
