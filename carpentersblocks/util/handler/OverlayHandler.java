package carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class OverlayHandler {

	/*
	 * Overlay definitions
	 */
	public final static byte       NO_OVERLAY = 0;
	public final static byte    OVERLAY_GRASS = 1;
	public final static byte     OVERLAY_SNOW = 2;
	public final static byte      OVERLAY_WEB = 3;
	public final static byte     OVERLAY_VINE = 4;
	public final static byte 	  OVERLAY_HAY =	5;
	public final static byte OVERLAY_MYCELIUM = 6;

	public static Map overlayMap;

	/**
	 * Initializes overlays.
	 */
	public static void init()
	{
		overlayMap = new HashMap();
		overlayMap.put(0, 	0							);
		overlayMap.put(1, 	Item.seeds.itemID			);
		overlayMap.put(2, 	Item.snowball.itemID		);
		overlayMap.put(3, 	Item.silk.itemID			);
		overlayMap.put(4, 	Block.vine.blockID			);
		overlayMap.put(5, 	Item.wheat.itemID			);
		overlayMap.put(6, 	Block.mushroomBrown.blockID	);
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
				if (mEntry.getValue().equals(itemStack.itemID)) {
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
		return new ItemStack((Integer)overlayMap.get(overlay), 1, 0);
	}

}
