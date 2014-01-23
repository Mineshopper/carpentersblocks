package carpentersblocks.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockDeterminantRender extends BlockHandlerBase {

    protected float REDUCED_LIGHTNESS_OFFSET = -0.05F;

    /**
     * Returns whether opaque objects should render.
     */
    protected boolean shouldRenderOpaque()
    {
        return renderAlphaOverride || renderPass == PASS_OPAQUE;
    }

    /**
     * Returns whether alpha objects should render.
     */
    protected boolean shouldRenderAlpha()
    {
        return renderPass == PASS_ALPHA;
    }

}
