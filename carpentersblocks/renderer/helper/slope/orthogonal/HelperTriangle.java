package carpentersblocks.renderer.helper.slope.orthogonal;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import carpentersblocks.renderer.helper.RenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperTriangle extends RenderHelper {

    /**
     * Renders a half triangle on the North left face of the block.
     */
    public static void renderFaceZNegXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMin, zMin, uBL, floatingHeight ? icon.getMaxV() : vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR,                                   vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMin, uTR, floatingHeight ? icon.getMaxV() : vTR, TOP_RIGHT   );
    }

    /**
     * Renders a half triangle on the North right face of the block.
     */
    public static void renderFaceZNegXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMin, uTL, floatingHeight ? icon.getMaxV() : vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBL,                                   vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR, floatingHeight ? icon.getMaxV() : vBR, BOTTOM_RIGHT);
    }

    /**
     * Renders a half triangle on the South left face of the block.
     */
    public static void renderFaceZPosXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        prepareRender(renderBlocks, SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMin, zMax, uBL, floatingHeight ? icon.getMaxV() : vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBR,                                   vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMax, uTR, floatingHeight ? icon.getMaxV() : vTR, TOP_RIGHT   );
    }

    /**
     * Renders a half triangle on the South right face of the block.
     */
    public static void renderFaceZPosXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        prepareRender(renderBlocks, SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMin, zMax, uBL,                                   vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBR, floatingHeight ? icon.getMaxV() : vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTL, floatingHeight ? icon.getMaxV() : vTL, TOP_LEFT    );
    }

    /**
     * Renders a half triangle on the West left face of the block.
     */
    public static void renderFaceXNegZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        prepareRender(renderBlocks, WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMin, zMin, uBL, floatingHeight ? icon.getMaxV() : vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMax, uBR,                                   vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR, floatingHeight ? icon.getMaxV() : vTR, TOP_RIGHT   );
    }

    /**
     * Renders a half triangle on the West right face of the block.
     */
    public static void renderFaceXNegZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        prepareRender(renderBlocks, WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMin, uTL, floatingHeight ? icon.getMaxV() : vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBL,                                   vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMax, uBR, floatingHeight ? icon.getMaxV() : vBR, BOTTOM_RIGHT);
    }

    /**
     * Renders a half triangle on the East left face of the block.
     */
    public static void renderFaceXPosZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        prepareRender(renderBlocks, EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMin, zMax, uBL, floatingHeight ? icon.getMaxV() : vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBR,                                   vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMin, uTR, floatingHeight ? icon.getMaxV() : vTR, TOP_RIGHT   );
    }

    /**
     * Renders a half triangle on the East right face of the block.
     */
    public static void renderFaceXPosZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        prepareRender(renderBlocks, EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMax, uTL, floatingHeight ? icon.getMaxV() : vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBL,                                   vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBR, floatingHeight ? icon.getMaxV() : vBR, BOTTOM_RIGHT);
    }

}
