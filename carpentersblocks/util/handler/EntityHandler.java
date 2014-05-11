package carpentersblocks.util.handler;

import carpentersblocks.CarpentersBlocks;
import carpentersblocks.entity.item.EntityCarpentersTile;
import carpentersblocks.renderer.entity.RenderCarpentersTile;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityHandler {

    public final static int ID_TILE = 0;

    /**
     * Registers entities.
     */
    public static void registerEntities()
    {
        EntityRegistry.registerModEntity(EntityCarpentersTile.class, "CarpentersTile", ID_TILE, CarpentersBlocks.instance, 64, 999, false);
    }

    @SideOnly(Side.CLIENT)
    /**
     * Initializes entity renderers.
     */
    public static void registerEntityRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityCarpentersTile.class, new RenderCarpentersTile());
    }

}
