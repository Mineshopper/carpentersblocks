package carpentersblocks.util.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.FeatureRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OverlayHandler {

    public enum Overlay {
        NONE(new ItemStack(Blocks.air)),
        GRASS(new ItemStack(Blocks.grass)),
        SNOW(new ItemStack(Blocks.snow)),
        WEB(new ItemStack(Blocks.web)),
        VINE(new ItemStack(Blocks.vine)),
        HAY(new ItemStack(Blocks.hay_block)),
        MYCELIUM(new ItemStack(Blocks.mycelium));

        private ItemStack itemStack;

        private Overlay(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public ItemStack getItemStack() {
            return this.itemStack;
        }
    }

    public static Map overlayMap = new HashMap();

    /**
     * Initializes overlay definitions from configuration file.
     */
    public static void init()
    {
        for (String name : FeatureRegistry.overlayItems) {

            String itemName = name.substring(0, name.indexOf(":"));

            if (!overlayMap.containsKey(itemName)) {

                String overlayType = name.substring(name.indexOf(":") + 1).toLowerCase();

                if (overlayType.equals("grass")) {
                    overlayMap.put(itemName, Overlay.GRASS);
                } else if (overlayType.equals("snow")) {
                    overlayMap.put(itemName, Overlay.SNOW);
                } else if (overlayType.equals("web")) {
                    overlayMap.put(itemName, Overlay.WEB);
                } else if (overlayType.equals("vine")) {
                    overlayMap.put(itemName, Overlay.VINE);
                } else if (overlayType.equals("hay")) {
                    overlayMap.put(itemName, Overlay.HAY);
                } else if (overlayType.equals("mycelium")) {
                    overlayMap.put(itemName, Overlay.MYCELIUM);
                }

            }

        }
    }

    /**
     * Returns true if overlay covers a majority or all of side.
     */
    public static boolean coversFullSide(Overlay overlay, int side)
    {
        switch (overlay) {
            case GRASS:
            case SNOW:
            case HAY:
            case MYCELIUM:
                return side == 1;
            default: {}
        }

        return true;
    }

    /**
     * Returns overlay from qualified ItemStack.
     */
    public static Overlay getOverlayType(ItemStack itemStack)
    {
        Object object = overlayMap.get(itemStack.getDisplayName());
        return object == null ? Overlay.NONE : (Overlay) object;
    }

    @SideOnly(Side.CLIENT)
    /**
     * Returns icon for overlay side.
     *
     * Returns null if there is no icon to return.
     */
    public static IIcon getOverlayIcon(Overlay overlay, int side)
    {
        Block block = BlockProperties.toBlock(overlay.getItemStack());

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
