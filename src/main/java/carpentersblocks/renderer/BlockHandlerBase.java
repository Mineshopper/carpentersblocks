package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import carpentersblocks.block.BlockCoverable;
import carpentersblocks.renderer.helper.FancyFluidsHelper;
import carpentersblocks.renderer.helper.LightingHelper;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.VertexHelper;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.DesignHandler;
import carpentersblocks.util.handler.DyeHandler;
import carpentersblocks.util.handler.OptifineHandler;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.handler.OverlayHandler.Overlay;
import carpentersblocks.util.registry.FeatureRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerBase implements ISimpleBlockRenderingHandler {

    protected static final int DOWN  = 0;
    protected static final int UP    = 1;
    protected static final int NORTH = 2;
    protected static final int SOUTH = 3;
    protected static final int WEST  = 4;
    protected static final int EAST  = 5;

    public Tessellator    tessellator = Tessellator.instance;
    public RenderBlocks   renderBlocks;
    public LightingHelper lightingHelper;
    public Block          srcBlock;
    public TEBase         TE;
    public boolean        suppressOverlay;
    public boolean        suppressChiselDesign;
    public boolean        suppressDyeColor;
    public boolean        disableAO;
    public boolean        hasDyeOverride;
    public int            dyeOverride;
    public boolean[]      hasIconOverride   = new boolean[6];
    public IIcon[]        iconOverride      = new IIcon[6];

    /** 0-5 are side covers, with 6 being the block itself. */
    public int            coverRendering    = 6;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();

        if (block instanceof BlockCoverable) {

            IIcon icon = renderBlocks.getIconSafe(((BlockCoverable)block).getIcon());
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);

        } else {

            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderBlocks.getIconSafe(block.getIcon(0, metadata)));
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderBlocks.getIconSafe(block.getIcon(1, metadata)));
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderBlocks.getIconSafe(block.getIcon(2, metadata)));
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderBlocks.getIconSafe(block.getIcon(3, metadata)));
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderBlocks.getIconSafe(block.getIcon(4, metadata)));
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderBlocks.getIconSafe(block.getIcon(5, metadata)));

        }

        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(90.0F, 0.0F, -1.0F, 0.0F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelID, RenderBlocks renderBlocks)
    {
        VertexHelper.vertexCount = 0;

        TileEntity TE_default = blockAccess.getTileEntity(x, y, z);

        /*
         * A catch-all for bad render calls.  Could happen if tile entities aren't
         * properly loaded when chunks are created, or with multi-block entities
         * like the door or bed when created or destroyed, when TE does not yet exist
         * or has already been removed.
         */
        if (TE_default != null && TE_default instanceof TEBase) {

            TE = (TEBase) TE_default;
            srcBlock = block;
            this.renderBlocks = renderBlocks;
            this.lightingHelper = new LightingHelper(renderBlocks);

            renderCarpentersBlock(x, y, z);
            renderSideBlocks(x, y, z);

            /* Will render a fluid block in this space if valid. */

            if (FeatureRegistry.enableFancyFluids) {

                int renderPass = MinecraftForgeClient.getRenderPass();

                if (renderPass >= 0 && Minecraft.isFancyGraphicsEnabled() && BlockProperties.hasCover(TE, 6)) {
                    FancyFluidsHelper.render(this, x, y, z, renderPass);
                }

            }

        }

        return VertexHelper.vertexCount > 0;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return 0;
    }

    /**
     * Rotates renderBounds if direction does not equal SOUTH.
     */
    protected void rotateBounds(RenderBlocks renderBlocks, ForgeDirection dir)
    {
        switch (dir) {
            case NORTH:
                renderBlocks.setRenderBounds(
                        1.0D - renderBlocks.renderMaxX,
                        renderBlocks.renderMinY,
                        1.0D - renderBlocks.renderMaxZ,
                        1.0D - renderBlocks.renderMinX,
                        renderBlocks.renderMaxY,
                        1.0D - renderBlocks.renderMinZ
                        );
                break;
            case EAST:
                renderBlocks.setRenderBounds(
                        renderBlocks.renderMinZ,
                        renderBlocks.renderMinY,
                        1.0D - renderBlocks.renderMaxX,
                        renderBlocks.renderMaxZ,
                        renderBlocks.renderMaxY,
                        1.0D - renderBlocks.renderMinX
                        );
                break;
            case WEST:
                renderBlocks.setRenderBounds(
                        1.0D - renderBlocks.renderMaxZ,
                        renderBlocks.renderMinY,
                        renderBlocks.renderMinX,
                        1.0D - renderBlocks.renderMinZ,
                        renderBlocks.renderMaxY,
                        renderBlocks.renderMaxX
                        );
                break;
            default: {}
        }
    }

    protected ItemStack getCoverForRendering()
    {
        return BlockProperties.getCoverForRendering(TE, coverRendering);
    }

    /**
     * Sets directional block side rotation in RenderBlocks.
     */
    protected void setDirectionalRotation(int side)
    {
        int metadata = getCoverForRendering().getItemDamage();
        int dir = metadata & 12;

        switch (side)
        {
            case DOWN:
                if (metadata == 3 || dir == 4) {
                    renderBlocks.uvRotateBottom = 1;
                }
                break;
            case UP:
                if (metadata == 3 || dir == 4) {
                    renderBlocks.uvRotateTop = 1;
                }
                break;
            case NORTH:
                if (metadata == 3 || dir == 4) {
                    renderBlocks.uvRotateNorth = 1;
                }
                break;
            case SOUTH:
                if (metadata == 3 || dir == 4) {
                    renderBlocks.uvRotateSouth = 1;
                }
                break;
            case WEST:
                if (metadata == 3 || dir == 8) {
                    renderBlocks.uvRotateWest = 1;
                }
                break;
            case EAST:
                if (metadata == 3 || dir == 8) {
                    renderBlocks.uvRotateEast = 1;
                }
                break;
        }
    }

    /**
     * Resets side rotation in RenderBlocks.
     */
    protected void clearRotation(int side)
    {
        switch (side)
        {
            case DOWN:
                renderBlocks.uvRotateBottom = 0;
                break;
            case UP:
                renderBlocks.uvRotateTop = 0;
                break;
            case NORTH:
                renderBlocks.uvRotateNorth = 0;
                break;
            case SOUTH:
                renderBlocks.uvRotateSouth = 0;
                break;
            case WEST:
                renderBlocks.uvRotateWest = 0;
                break;
            case EAST:
                renderBlocks.uvRotateEast = 0;
                break;
        }
    }

    /**
     * Returns texture rotation for side.
     */
    protected int getRotation(int side)
    {
        switch (side)
        {
            case DOWN:
                return renderBlocks.uvRotateBottom;
            case UP:
                return renderBlocks.uvRotateTop;
            case NORTH:
                return renderBlocks.uvRotateNorth;
            case SOUTH:
                return renderBlocks.uvRotateSouth;
            case WEST:
                return renderBlocks.uvRotateWest;
            default:
                return renderBlocks.uvRotateEast;
        }
    }

    /**
     * Sets texture rotation for side.
     */
    protected void setRotation(int side, int rotation)
    {
        switch (side)
        {
            case DOWN:
                renderBlocks.uvRotateBottom = rotation;
                break;
            case UP:
                renderBlocks.uvRotateTop = rotation;
                break;
            case NORTH:
                renderBlocks.uvRotateNorth = rotation;
                break;
            case SOUTH:
                renderBlocks.uvRotateSouth = rotation;
                break;
            case WEST:
                renderBlocks.uvRotateWest = rotation;
                break;
            default:
                renderBlocks.uvRotateEast = rotation;
                break;
        }
    }

    /**
     * Sets dye override.
     */
    protected void setDyeOverride(int dye)
    {
        hasDyeOverride = true;
        dyeOverride = dye;
    }

    /**
     * Clears dye override.
     */
    protected void clearDyeOverride()
    {
        hasDyeOverride = false;
    }

    /**
     * Sets icon override.
     * Using side 6 overrides all sides.
     * RenderBlocks' icon override will override this one
     * when breaking animation is played.
     */
    protected void setIconOverride(int side, IIcon icon)
    {
        if (side == 6) {
            for (int count = 0; count < 6; ++count) {
                hasIconOverride[count] = true;
                iconOverride[count] = icon;
            }
        } else {
            hasIconOverride[side] = true;
            iconOverride[side] = icon;
        }
    }

    /**
     * Clears icon override.
     */
    protected void clearIconOverride(int side)
    {
        if (side == 6) {
            for (int count = 0; count < 6; ++count) {
                hasIconOverride[count] = false;
            }
        } else {
            hasIconOverride[side] = false;
        }
    }

    /**
     * Sets up side cover rendering bounds.
     * Will return block location where side cover should be rendered.
     */
    protected int[] getSideCoverRenderBounds(int x, int y, int z, int side)
    {
        double offset = 1.0D / 16.0D;

        /*
         * Make snow match vanilla snow depth for continuity.
         */
        if (side == UP)
        {
            Block block = BlockProperties.toBlock(getCoverForRendering());
            if (block.equals(Blocks.snow) || block.equals(Blocks.snow_layer)) {
                offset = 1.0D / 8.0D;
            }
        }

        /*
         * Adjust bounds of side cover to accommodate
         * partial rendered blocks (slabs, stair steps)
         */
        switch (side) {
            case DOWN:
                if (renderBlocks.renderMinY > 0.0D) {
                    renderBlocks.renderMaxY = renderBlocks.renderMinY;
                    renderBlocks.renderMinY -= offset;
                } else {
                    renderBlocks.renderMaxY = 1.0D;
                    renderBlocks.renderMinY = renderBlocks.renderMaxY - offset;
                    y -= 1;
                }
                break;
            case UP:
                if (renderBlocks.renderMaxY < 1.0D) {
                    renderBlocks.renderMinY = renderBlocks.renderMaxY;
                    renderBlocks.renderMaxY += offset;
                } else {
                    renderBlocks.renderMaxY = offset;
                    renderBlocks.renderMinY = 0.0D;
                    y += 1;
                }
                break;
            case NORTH:
                if (renderBlocks.renderMinZ > 0.0D) {
                    renderBlocks.renderMaxZ = renderBlocks.renderMinZ;
                    renderBlocks.renderMinZ -= offset;
                } else {
                    renderBlocks.renderMaxZ = 1.0D;
                    renderBlocks.renderMinZ = renderBlocks.renderMaxZ - offset;
                    z -= 1;
                }
                break;
            case SOUTH:
                if (renderBlocks.renderMaxZ < 1.0D) {
                    renderBlocks.renderMinZ = renderBlocks.renderMaxZ;
                    renderBlocks.renderMaxZ += offset;
                } else {
                    renderBlocks.renderMaxZ = offset;
                    renderBlocks.renderMinZ = 0.0D;
                    z += 1;
                }
                break;
            case WEST:
                if (renderBlocks.renderMinX > 0.0D) {
                    renderBlocks.renderMaxX = renderBlocks.renderMinX;
                    renderBlocks.renderMinX -= offset;
                } else {
                    renderBlocks.renderMaxX = 1.0D;
                    renderBlocks.renderMinX = renderBlocks.renderMaxX - offset;
                    x -= 1;
                }
                break;
            case EAST:
                if (renderBlocks.renderMaxX < 1.0D) {
                    renderBlocks.renderMinX = renderBlocks.renderMaxX;
                    renderBlocks.renderMaxX += offset;
                } else {
                    renderBlocks.renderMaxX = offset;
                    renderBlocks.renderMinX = 0.0D;
                    x += 1;
                }
                break;
        }

        int[] coords = { x, y, z };
        return coords;
    }

    /**
     * Renders side covers.
     */
    protected void renderSideBlocks(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;

        srcBlock.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);

        for (int side = 0; side < 6; ++side)
        {
            if (BlockProperties.hasCover(TE, side))
            {
                coverRendering = side;
                int[] renderOffset = getSideCoverRenderBounds(x, y, z, side);
                renderBlock(getCoverForRendering(), renderOffset[0], renderOffset[1], renderOffset[2]);
                renderBlocks.setRenderBoundsFromBlock(srcBlock);
            }
        }

        renderBlocks.renderAllFaces = false;
        coverRendering = 6;
    }

    /**
     * Override to provide custom icons.
     */
    protected IIcon getUniqueIcon(ItemStack itemStack, int side, IIcon icon)
    {
        return icon;
    }

    /**
     * Returns icon for face.
     */
    protected IIcon getIcon(ItemStack itemStack, int side)
    {
        BlockProperties.prepareItemStackForRendering(itemStack);
        IIcon icon = renderBlocks.getIconSafe(getUniqueIcon(itemStack, side, BlockProperties.toBlock(itemStack).getIcon(side, itemStack.getItemDamage())));

        if (hasIconOverride[side]) {
            icon = renderBlocks.getIconSafe(iconOverride[side]);
        }

        return icon;
    }

    /**
     * Renders multiple textures to side.
     */
    protected void renderMultiTexturedSide(ItemStack itemStack, int x, int y, int z, int side, IIcon icon)
    {
        Block block = BlockProperties.toBlock(itemStack);

        // TODO: Revisit render passes when alpha rendering bug is fixed.

        /* Render side */

        int tempRotation = getRotation(side);
        if (BlockProperties.blockRotates(itemStack)) {
            setDirectionalRotation(side);
        }
        setColorAndRender(itemStack, x, y, z, side, icon);
        setRotation(side, tempRotation);

        /* Render BlockGrass side overlay here, if needed. */

        if (block.equals(Blocks.grass) && side > 0 && !isPositiveFace(side)) {
            if (Minecraft.isFancyGraphicsEnabled()) {
                setColorAndRender(new ItemStack(Blocks.grass), x, y, z, side, BlockGrass.getIconSideOverlay());
            } else {
                setColorAndRender(new ItemStack(Blocks.dirt), x, y, z, side, IconRegistry.icon_overlay_fast_grass_side);
            }
        }

        /* Render decorations. */

        boolean temp_dye_state = suppressDyeColor;
        suppressDyeColor = true;

        if (!suppressChiselDesign && BlockProperties.hasChiselDesign(TE, coverRendering)) {
            renderChiselDesign(x, y, z, side);
        }

        if (!suppressOverlay && BlockProperties.hasOverlay(TE, coverRendering)) {
            renderOverlay(block, x, y, z, side);
        }

        suppressDyeColor = temp_dye_state;
    }

    /**
     * Sets side icon, draws any attributes needed, and hands to appropriate render method.
     * This will bypass typical draw behavior if breaking animation is being drawn.
     */
    protected void delegateSideRender(ItemStack itemStack, int x, int y, int z, int side)
    {
        /*
         * A texture override in the context of this mod indicates the breaking
         * animation is being drawn. If this is the case, draw side without
         * decorations. Can also check icon name for beginsWith("destroy_stage_").
         */
        if (renderBlocks.hasOverrideBlockTexture()) {
            setColorAndRender(itemStack, x, y, z, side, renderBlocks.overrideBlockTexture);
        } else {
            renderMultiTexturedSide(itemStack, x, y, z, side, getIcon(itemStack, side));
        }
    }

    /**
     * Renders overlay on side.
     */
    protected void renderOverlay(Block block, int x, int y, int z, int side)
    {
        side = isPositiveFace(side) ? 1 : side;
        Overlay overlay = OverlayHandler.getOverlayType(BlockProperties.getOverlay(TE, coverRendering));

        IIcon icon = OverlayHandler.getOverlayIcon(overlay, side);

        if (icon != null) {
            setColorAndRender(overlay.getItemStack(), x, y, z, side, icon);
        }
    }

    /**
     * Renders chisel design on side.
     */
    protected void renderChiselDesign(int x, int y, int z, int side)
    {
        String design = BlockProperties.getChiselDesign(TE, coverRendering);
        IIcon icon = renderBlocks.getIconSafe(IconRegistry.icon_design_chisel.get(DesignHandler.listChisel.indexOf(design)));
        setColorAndRender(new ItemStack(Blocks.glass), x, y, z, side, icon);
    }

    /**
     * Sets color, lightness, and brightness in {@link LightingHelper} and
     * renders side.
     *
     * @param itemStack  the cover ItemStack
     * @param block  the block inside the ItemStack
     * @param x  the x coordinate
     * @param y  the y coordinate
     * @param z  the z coordinate
     * @param side  the side currently being worked on
     * @param icon  the icon for the side
     * @return nothing
     */
    public void setColorAndRender(ItemStack itemStack, int x, int y, int z, int side, IIcon icon)
    {
        float[] rgb = getBlockRGB(BlockProperties.toBlock(itemStack), itemStack.getItemDamage(), x, y, z, side, icon);

        if (!suppressDyeColor && (BlockProperties.hasDye(TE, coverRendering) || hasDyeOverride)) {
            rgb = hasDyeOverride ? LightingHelper.getRGB(dyeOverride) : LightingHelper.getRGB(DyeHandler.getColor(BlockProperties.getDye(TE, coverRendering)));
        }

        lightingHelper.setupColor(x, y, z, side, rgb, icon);
        render(x, y, z, side, icon);
    }

    /**
     * Returns float array with RGB values for block.  Color is most
     * commonly different for {@link Blocks#grass}
     * <p>
     * If using our custom render helpers, be sure to use {@link #applyAnaglyph(float[])}.
     *
     * @param itemStack  the cover {@link ItemStack}
     * @param block  the {@link Block} inside the {@link ItemStack}
     * @param x  the x coordinate
     * @param y  the y coordinate
     * @param z  the z coordinate
     * @return a float array with rgb values
     */
    public float[] getBlockRGB(Block block, int metadata, int x, int y, int z, int side, IIcon icon)
    {
        BlockProperties.setHostMetadata(TE, metadata);
        float rgb[] = LightingHelper.getRGB(OptifineHandler.enableOptifineIntegration ? OptifineHandler.getColorMultiplier(block, renderBlocks.blockAccess, x, y, z) : block.colorMultiplier(renderBlocks.blockAccess, x, y, z));
        BlockProperties.resetHostMetadata(TE);

        if (block.equals(Blocks.grass) && !isPositiveFace(side) && !icon.equals(BlockGrass.getIconSideOverlay())) {
            rgb[0] = rgb[1] = rgb[2] = 1.0F;
        }

        return rgb;
    }

    /**
     * Returns whether side is considered a top face.
     *
     * @param TE  the {@link TEBase}
     * @param block  the {@link Block}
     * @param side  the side
     * @param icon  the {@link IIcon}
     * @return true if positive y face
     */
    protected boolean isPositiveFace(int side)
    {
        return side == 1;
    }

    /**
     * Renders a side.
     *
     * @param x  the x coordinate
     * @param y  the y coordinate
     * @param z  the z coordinate
     * @param side  the side currently being worked on
     * @param icon  the icon for the side
     * @return nothing
     */
    protected void render(int x, int y, int z, int side, IIcon icon)
    {
        switch (side) {
            case DOWN:
                RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
                break;
            case UP:
                RenderHelper.renderFaceYPos(renderBlocks, x, y, z, icon);
                break;
            case NORTH:
                RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
                break;
            case SOUTH:
                RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
                break;
            case WEST:
                RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
                break;
            case EAST:
                RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
                break;
        }
    }

    /**
     * Blocks override this in order to render everything they need.
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        renderBlock(getCoverForRendering(), x, y, z);
    }

    /**
     * Sets renderBlocks enableAO state to true depending on
     * rendering environment and block requirements.
     */
    public boolean getEnableAO(ItemStack itemStack)
    {
        Block block = BlockProperties.toBlock(itemStack);
        return Minecraft.isAmbientOcclusionEnabled() && !disableAO && block.getLightValue() == 0;
    }

    /**
     * Renders block.
     * Coordinates may change since side covers render here.
     */
    protected void renderBlock(ItemStack itemStack, int x, int y, int z)
    {
        if (BlockProperties.toBlock(itemStack) == null) {
            return;
        }

        renderBlocks.enableAO = getEnableAO(itemStack);

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, DOWN) || renderBlocks.renderMinY > 0.0D)
        {
            lightingHelper.setupLightingYNeg(itemStack, x, y, z);
            delegateSideRender(itemStack, x, y, z, DOWN);
        }

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, UP) || renderBlocks.renderMaxY < 1.0D)
        {
            lightingHelper.setupLightingYPos(itemStack, x, y, z);
            delegateSideRender(itemStack, x, y, z, UP);
        }

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, NORTH) || renderBlocks.renderMinZ > 0.0D)
        {
            lightingHelper.setupLightingZNeg(itemStack, x, y, z);
            delegateSideRender(itemStack, x, y, z, NORTH);
        }

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, SOUTH) || renderBlocks.renderMaxZ < 1.0D)
        {
            lightingHelper.setupLightingZPos(itemStack, x, y, z);
            delegateSideRender(itemStack, x, y, z, SOUTH);
        }

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, WEST) || renderBlocks.renderMinX > 0.0D)
        {
            lightingHelper.setupLightingXNeg(itemStack, x, y, z);
            delegateSideRender(itemStack, x, y, z, WEST);
        }

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, EAST) || renderBlocks.renderMaxX < 1.0D)
        {
            lightingHelper.setupLightingXPos(itemStack, x, y, z);
            delegateSideRender(itemStack, x, y, z, EAST);
        }

        renderBlocks.enableAO = false;
    }

}
