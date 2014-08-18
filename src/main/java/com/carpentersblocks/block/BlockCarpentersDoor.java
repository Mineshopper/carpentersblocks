package com.carpentersblocks.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.data.Hinge;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import com.carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersDoor extends BlockHinged {

    @SuppressWarnings("SameParameterValue")
    public BlockCarpentersDoor(Material material)
    {
        super(material);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        IconRegistry.icon_door_screen_tall         = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_screen_tall"        );
        IconRegistry.icon_door_glass_tall_top      = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_glass_tall_top"     );
        IconRegistry.icon_door_glass_tall_bottom   = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_glass_tall_bottom"  );
        IconRegistry.icon_door_glass_top           = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_glass_top"          );
        IconRegistry.icon_door_french_glass_top    = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_french_glass_top"   );
        IconRegistry.icon_door_french_glass_bottom = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "door/door_french_glass_bottom");
    }

    @Override
    /**
     * Alters hinge type and redstone behavior.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        if (!entityPlayer.isSneaking()) {

            int type = Hinge.getType(TE);

            if (++type > 5) {
                type = 0;
            }

            setHingeType(TE, type);
            super.onHammerRightClick(TE, entityPlayer);
            return true;

        }

        return super.onHammerRightClick(TE, entityPlayer);
    }

    @Override
    /**
     * Returns the ID of the items to drop on destruction.
     */
    public Item getItemDropped(int metadata, Random random, int par3)
    {
        return ItemRegistry.itemCarpentersDoor;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public Item getItem(World world, int x, int y, int z)
    {
        return ItemRegistry.itemCarpentersDoor;
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersDoorRenderID;
    }

}
