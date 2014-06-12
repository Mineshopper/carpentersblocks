package carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.FeatureRegistry;
import carpentersblocks.util.registry.IconRegistry;

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
        for (String name : FeatureRegistry.overlayItems) {

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

            if (!overlay.equals(Overlay.NONE) && !overlayMap.containsKey(itemName)) {
                overlayMap.put(itemName, overlay);
            }

        }
    }

    /**
     * Returns overlay from ItemStack.
     */
    public static Overlay getOverlay(ItemStack itemStack)
    {
        if (itemStack != null) {
            return (Overlay) overlayMap.get(itemStack.getDisplayName());
        }

        return Overlay.NONE;
    }

    /**
     * Returns ItemStack representative of overlay block type.
     * Will return block or cover if no overlay is present.
     *
     * Use this when determining side particles to render.
     */
    public static ItemStack getOverlay(TEBase TE, int cover)
    {
        return getOverlaySideSensitive(TE, cover, -1);
    }

    /**
     * Returns ItemStack representative of overlay block type.
     * Will return block or cover if no overlay is present.
     *
     * Use this when determining side particles to render.
     */
    public static ItemStack getOverlaySideSensitive(TEBase TE, int cover, int side)
    {
        ItemStack itemStack = BlockProperties.getCover(TE, cover);

        boolean returnOverlay = Math.abs(side) == 1;

        switch (getOverlay(BlockProperties.getOverlay(TE, cover))) {
            case GRASS:
                if (returnOverlay) {
                    return new ItemStack(Blocks.grass);
                }
                break;
            case SNOW:
                if (returnOverlay) {
                    return new ItemStack(Blocks.snow);
                }
                break;
            case WEB:
                return new ItemStack(Blocks.web);
            case VINE:
                return new ItemStack(Blocks.vine);
            case HAY:
                if (returnOverlay) {
                    return new ItemStack(Blocks.hay_block);
                }
                break;
            case MYCELIUM:
                if (returnOverlay) {
                    return new ItemStack(Blocks.mycelium);
                }
                break;
            default: {}
        }

        return itemStack;
    }

    /**
     * Returns icon for overlay.
     */
    public static IIcon getOverlayIcon(TEBase TE, int cover, int side)
    {
        Block block = BlockProperties.toBlock(OverlayHandler.getOverlay(TE, cover));

        Overlay overlay = OverlayHandler.getOverlay(BlockProperties.getOverlay(TE, cover));

        switch (overlay) {
            case GRASS:
            case SNOW:
            case HAY:
            case MYCELIUM:
                switch (side) {
                    case 0:
                        return null;
                    case 1:
                        return block.getBlockTextureFromSide(1);
                    default:
                        switch (overlay) {
                            case GRASS:
                                return RenderBlocks.fancyGrass ? BlockGrass.getIconSideOverlay() : IconRegistry.icon_overlay_fast_grass_side;
                            case SNOW:
                                return IconRegistry.icon_overlay_snow_side;
                            case HAY:
                                return IconRegistry.icon_overlay_hay_side;
                            case MYCELIUM:
                                return IconRegistry.icon_overlay_mycelium_side;
                            default:
                                return null;
                        }
                }
            case WEB:
            case VINE:
                return block.getBlockTextureFromSide(side);
            default: {
                return null;
            }
        }
    }

}
