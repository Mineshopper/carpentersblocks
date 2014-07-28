package carpentersblocks.renderer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Safe;
import carpentersblocks.renderer.helper.LightingHelper;
import carpentersblocks.tileentity.TECarpentersSafe;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersSafe extends BlockHandlerBase {

    private ForgeDirection dir;
    private boolean isOpen;
    private boolean isLocked;
    private ItemStack metal;
    private final static ItemStack ice = new ItemStack(Block.ice);
    private final static ItemStack gold = new ItemStack(Block.blockGold);
    private final static ItemStack iron = new ItemStack(Block.blockIron);

    public static class Component {

        public double xMin, yMin, zMin, xMax, yMax, zMax;

        public Component(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax)
        {
            this.xMin = xMin;
            this.yMin = yMin;
            this.zMin = zMin;
            this.xMax = xMax;
            this.yMax = yMax;
            this.zMax = zMax;
        }

    }

    private final static List<Component> coverList;
    static {
        coverList = new ArrayList<Component>();
        coverList.add(new Component(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D));
        coverList.add(new Component(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
        coverList.add(new Component(0.0625D, 0.0D, 0.0625D, 0.3125D, 0.0625D, 1.0D));
        coverList.add(new Component(0.375D, 0.0D, 0.0625D, 0.9375D, 0.0625D, 1.0D));
        coverList.add(new Component(0.0625D, 0.9375D, 0.0625D, 0.3125D, 1.0D, 1.0D));
        coverList.add(new Component(0.375D, 0.9375D, 0.0625D, 0.9375D, 1.0D, 1.0D));
        coverList.add(new Component(0.0625D, 0.0D, 0.0D, 0.3125D, 1.0D, 0.0625D));
        coverList.add(new Component(0.375D, 0.0D, 0.0D, 0.9375D, 1.0D, 0.0625D));
        coverList.add(new Component(0.3125D, 0.0D, 0.0D, 0.375D, 1.0D, 1.0D));
        coverList.add(new Component(0.375D, 0.625D, 0.0625D, 0.9375D, 0.6875D, 0.875D));
        coverList.add(new Component(0.375D, 0.3125D, 0.0625D, 0.9375D, 0.375D, 0.875D));
    }

    private final static List<Component> panelList;
    static {
        panelList = new ArrayList<Component>();
        panelList.add(new Component(0.125D, 0.875D, 0.9375D, 0.25D, 0.9375D, 1.0D));
        panelList.add(new Component(0.0625D, 0.0625D, 0.9375D, 0.125D, 0.9375D, 1.0D));
        panelList.add(new Component(0.25D, 0.0625D, 0.9375D, 0.3125D, 0.9375D, 1.0D));
        panelList.add(new Component(0.125D, 0.6875D, 0.9375D, 0.25D, 0.75D, 1.0D));
        panelList.add(new Component(0.125D, 0.0625D, 0.9375D, 0.25D, 0.125D, 1.0D));
        panelList.add(new Component(0.0625D, 0.0625D, 0.875D, 0.3125D, 0.9375D, 0.9375D));
    }

    private final int LOCKED_ACTIVE     = 0xff0000;
    private final int LOCKED_INACTIVE   = 0x7e3636;
    private final int UNLOCKED_ACTIVE   = 0x00ff00;
    private final int UNLOCKED_INACTIVE = 0x367e36;
    private final int CAPACITY_ACTIVE   = 0x0000ff;
    private final int CAPACITY_INACTIVE = 0x383884;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        /* Cover components */

        for (Component comp : coverList) {
            renderBlocks.setRenderBounds(comp.xMin, comp.yMin, comp.zMin, comp.xMax, comp.yMax, comp.zMax);
            rotateBounds(renderBlocks, ForgeDirection.WEST);
            super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        }

        /* Panel components */

        for (Component comp : panelList) {
            renderBlocks.setRenderBounds(comp.xMin, comp.yMin, comp.zMin, comp.xMax, comp.yMax, comp.zMax);
            rotateBounds(renderBlocks, ForgeDirection.WEST);
            super.renderInventoryBlock(Block.blockIron, metadata, modelID, renderBlocks);
        }

        /* Handle */

        renderBlocks.setRenderBounds(0.8125D, 0.375D, 0.9375D, 0.875D, 0.625D, 1.0D);
        rotateBounds(renderBlocks, ForgeDirection.WEST);
        super.renderInventoryBlock(Block.blockIron, metadata, modelID, renderBlocks);

        /* Sliding door */

        renderBlocks.setRenderBounds(0.375D, 0.0625D, 0.875F, 0.9375D, 0.9375D, 0.9375D);
        rotateBounds(renderBlocks, ForgeDirection.WEST);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

        /* Red light */

        renderBlocks.setRenderBounds(0.125D, 0.75D, 0.9375D, 0.25D, 0.8125D, 1.0D);
        rotateBounds(renderBlocks, ForgeDirection.WEST);
        super.renderInventoryBlock(Block.obsidian, metadata, modelID, renderBlocks);

        /* Green light */

        renderBlocks.setRenderBounds(0.125D, 0.8125D, 0.9375D, 0.25D, 0.875D, 1.0D);
        rotateBounds(renderBlocks, ForgeDirection.WEST);
        super.renderInventoryBlock(Block.obsidian, metadata, modelID, renderBlocks);

        /* Capacity strip */

        renderBlocks.setRenderBounds(0.125D, 0.125D, 0.9375D, 0.25D, 0.6875D, 1.0D);
        rotateBounds(renderBlocks, ForgeDirection.WEST);
        super.renderInventoryBlock(Block.obsidian, metadata, modelID, renderBlocks);
    }

    @Override
    /**
     * Renders safe.
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;

        ItemStack itemStack = getCoverForRendering();
        dir = Safe.getFacing(TE);
        isOpen = Safe.getState(TE) == Safe.STATE_OPEN;
        isLocked = Safe.isLocked(TE);
        metal = ((TECarpentersSafe)TE).hasUpgrade() ? gold : iron;

        /* Render cover components */

        for (Component comp : coverList) {
            renderBlockWithRotation(itemStack, x, y, z, comp.xMin, comp.yMin, comp.zMin, comp.xMax, comp.yMax, comp.zMax, dir);
        }

        renderBlockWithRotation(itemStack, x, y, z, 0.375D, 0.0625D, 0.875F, isOpen ? 0.5625D : 0.9375D, 0.9375D, 0.9375D, dir); // Render sliding door

        suppressDyeColor = true;
        suppressOverlay = true;
        suppressChiselDesign = true;

        /* Render panel components */

        for (Component comp : panelList) {
            renderBlockWithRotation(metal, x, y, z, comp.xMin, comp.yMin, comp.zMin, comp.xMax, comp.yMax, comp.zMax, dir); // Render panel
        }

        renderBlockWithRotation(metal, x, y, z, isOpen ? 0.4375D : 0.8125D, 0.375D, 0.9375D, isOpen ? 0.5D : 0.875D, 0.625D, 1.0D, dir); // Render handle

        if (renderPass == PASS_ALPHA) {

            disableAO = true;
            setIconOverride(6, IconRegistry.icon_safe_light);

            renderPartLockLight(x, y, z);
            renderPartCapacityLight(x, y, z);

            clearIconOverride(6);
            disableAO = false;

            suppressDyeColor = false;
            suppressOverlay = false;
            suppressChiselDesign = false;

        }

        renderBlocks.renderAllFaces = false;
    }

    private void renderPartCapacityLight(int x, int y, int z)
    {
        double yMin = 0.125D;
        double yMax = 0.1875D;

        /* Determine capacity level */

        TECarpentersSafe TE_safe = (TECarpentersSafe) TE;
        int numSlotsFilled = 0;

        for (int slot = 0; slot < TE_safe.getSizeInventory(); ++slot) {
            if (TE_safe.getStackInSlot(slot) != null) {
                ++numSlotsFilled;
            }
        }

        int capacity = numSlotsFilled / (3 * TE_safe.getSizeInventory() / 27);

        /* Draw capacity light strip */

        for (int box = 0; box < 9; ++box)
        {
            if (box + 1 <= capacity) {
                lightingHelper.setLightnessOverride(1.0F);
                lightingHelper.setBrightnessOverride(LightingHelper.MAX_BRIGHTNESS);
                lightingHelper.setColorOverride(CAPACITY_ACTIVE);
            } else {
                lightingHelper.setColorOverride(CAPACITY_INACTIVE);
            }

            renderBlockWithRotation(ice, x, y, z, 0.125D, yMin, 0.9375D, 0.25D, yMax, 1.0D, dir);

            lightingHelper.clearColorOverride();
            lightingHelper.clearBrightnessOverride();
            lightingHelper.clearLightnessOverride();

            yMin += 0.0625D;
            yMax += 0.0625D;
        }
    }

    private void renderPartLockLight(int x, int y, int z)
    {
        if (isLocked) {
            lightingHelper.setColorOverride(UNLOCKED_INACTIVE);
        } else {
            lightingHelper.setLightnessOverride(1.0F);
            lightingHelper.setBrightnessOverride(LightingHelper.MAX_BRIGHTNESS);
            lightingHelper.setColorOverride(UNLOCKED_ACTIVE);
        }

        renderBlockWithRotation(ice, x, y, z, 0.125D, 0.8125D, 0.9375D, 0.25D, 0.875D, 1.0D, dir);

        lightingHelper.clearColorOverride();
        lightingHelper.clearBrightnessOverride();
        lightingHelper.clearLightnessOverride();

        if (isLocked) {
            lightingHelper.setLightnessOverride(1.0F);
            lightingHelper.setBrightnessOverride(LightingHelper.MAX_BRIGHTNESS);
            lightingHelper.setColorOverride(LOCKED_ACTIVE);
        } else {
            lightingHelper.setColorOverride(LOCKED_INACTIVE);
        }

        renderBlockWithRotation(ice, x, y, z, 0.125D, 0.75D, 0.9375D, 0.25D, 0.8125D, 1.0D, dir);

        lightingHelper.clearColorOverride();
        lightingHelper.clearBrightnessOverride();
        lightingHelper.clearLightnessOverride();
    }

}
