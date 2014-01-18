package carpentersblocks.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockDeterminantRender extends BlockHandlerBase {

    protected float REDUCED_LIGHTNESS_OFFSET = -0.05F;

    /**
     * Returns whether non-cover blocks should render.
     * These will always be rendered on the opaque pass.
     */
    protected boolean shouldRenderOpaque()
    {
        return renderAlphaOverride || renderPass == 0;
    }

}
