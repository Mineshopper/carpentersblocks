package com.carpentersblocks.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.data.DaylightSensor;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.tileentity.TECarpentersDaylightSensor;
import com.carpentersblocks.util.handler.ChatHandler;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersDaylightSensor extends BlockCoverable {

    @SuppressWarnings("SameParameterValue")
    public BlockCarpentersDaylightSensor(Material material)
    {
        super(material);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        IconRegistry.icon_daylight_sensor_glass_top = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "daylightsensor/daylight_sensor_glass_top");
    }

    @Override
    /**
     * Alters polarity.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int polarity = DaylightSensor.getPolarity(TE) == DaylightSensor.POLARITY_POSITIVE ? DaylightSensor.POLARITY_NEGATIVE : DaylightSensor.POLARITY_POSITIVE;

        DaylightSensor.setPolarity(TE, polarity);

        switch (polarity) {
            case DaylightSensor.POLARITY_POSITIVE:
                ChatHandler.sendMessageToPlayer("message.polarity_pos.name", entityPlayer);
                break;
            case DaylightSensor.POLARITY_NEGATIVE:
                ChatHandler.sendMessageToPlayer("message.polarity_neg.name", entityPlayer);
        }

        return true;
    }

    @Override
    /**
     * Alters polarity.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int sensitivity = DaylightSensor.setNextSensitivity(TE);

        if (sensitivity == DaylightSensor.SENSITIVITY_SLEEP) {
            ChatHandler.sendMessageToPlayer("message.sensitivity_sleep.name", entityPlayer);
        } else {
            ChatHandler.sendMessageToPlayer("message.sensitivity_monsters.name", entityPlayer);
        }

        return true;
    }

    @Override
    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            return DaylightSensor.isActive(TE) ? 15 : 0;
        }

        return 0;
    }

    public void updateLightLevel(World world, int x, int y, int z)
    {
        if (!world.provider.hasNoSky)
        {
            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {

                int temp_lightValue = DaylightSensor.getLightLevel(TE);
                int lightValue = world.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) - world.skylightSubtracted;

                if (world.isThundering()) {
                    lightValue = 7;
                }

                if (temp_lightValue != lightValue) {
                    DaylightSensor.setLightLevel(TE, lightValue);
                    world.notifyBlocksOfNeighborChange(x, y, z, this);
                }

            }
        }
    }

    @Override
    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TECarpentersDaylightSensor();
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersDaylightSensorRenderID;
    }

}
