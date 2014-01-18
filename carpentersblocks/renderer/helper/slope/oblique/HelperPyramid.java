package carpentersblocks.renderer.helper.slope.oblique;

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
public class HelperPyramid extends RenderHelper {

    /**
     * Renders the given texture to the negative North slope of the block.
     */
    public static void renderFaceYNegZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        double uBM  = uBR  - (uBR  -  uBL) / 2;
        double xMid = xMax - (xMax - xMin) / 2;

        setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST );
        setupVertex(renderBlocks, xMid, yMin, zMax, uBM, vBR, TOP_CENTER);
        setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST );
    }

    /**
     * Renders the given texture to the negative South slope of the block.
     */
    public static void renderFaceYNegZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, SOUTH, x, y, z, icon);

        double uBM  = uBR  - (uBR  -  uBL) / 2;
        double xMid = xMax - (xMax - xMin) / 2;

        setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST    );
        setupVertex(renderBlocks, xMid, yMin, zMin, uBM, vBR, BOTTOM_CENTER);
        setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST    );
    }

    /**
     * Renders the given texture to the negative West slope of the block.
     */
    public static void renderFaceYNegXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, WEST, x, y, z, icon);

        double uBM  = uBR  - (uBR  -  uBL) / 2;
        double zMid = zMax - (zMax - zMin) / 2;

        setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST  );
        setupVertex(renderBlocks, xMax, yMin, zMid, uBM, vBR, LEFT_CENTER);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST  );
    }

    /**
     * Renders the given texture to the negative East slope of the block.
     */
    public static void renderFaceYNegXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, EAST, x, y, z, icon);

        double uBM  = uBR  - (uBR  -  uBL) / 2;
        double zMid = zMax - (zMax - zMin) / 2;

        setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST   );
        setupVertex(renderBlocks, xMin, yMin, zMid, uBM, vBR, RIGHT_CENTER);
        setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST   );
    }

    /**
     * Renders the given texture to the positive North slope of the block.
     */
    public static void renderFaceYPosZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        double uTM  = uTR  - (uTR  -  uTL) / 2;
        double xMid = xMax - (xMax - xMin) / 2;

        setupVertex(renderBlocks, xMid, yMax, zMax, uTM, vTL, TOP_CENTER  );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST   );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST   );
    }

    /**
     * Renders the given texture to the positive South slope of the block.
     */
    public static void renderFaceYPosZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, SOUTH, x, y, z, icon);

        double uTM  = uTR  - (uTR  -  uTL) / 2;
        double xMid = xMax - (xMax - xMin) / 2;

        setupVertex(renderBlocks, xMid, yMax, zMin, uTM, vTL, BOTTOM_CENTER);
        setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST    );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST    );
    }

    /**
     * Renders the given texture to the positive West slope of the block.
     */
    public static void renderFaceYPosXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, WEST, x, y, z, icon);

        double uTM  = uTR  - (uTR  -  uTL) / 2;
        double zMid = zMax - (zMax - zMin) / 2;

        setupVertex(renderBlocks, xMax, yMax, zMid, uTM, vTL, LEFT_CENTER );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST   );
        setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST   );
    }

    /**
     * Renders the given texture to the positive East slope of the block.
     */
    public static void renderFaceYPosXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, EAST, x, y, z, icon);

        double uTM  = uTR  - (uTR  -  uTL) / 2;
        double zMid = zMax - (zMax - zMin) / 2;

        setupVertex(renderBlocks, xMin, yMax, zMid, uTM, vTR, RIGHT_CENTER);
        setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST   );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST   );
    }

}
