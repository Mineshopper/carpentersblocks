package com.carpentersblocks.block;

import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
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

public class BlockCarpentersDaylightSensor extends BlockSided {

    private static DaylightSensor data = new DaylightSensor();

    public BlockCarpentersDaylightSensor(Material material)
    {
        super(material, data);
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
        int polarity = data.getPolarity(TE) == data.POLARITY_POSITIVE ? data.POLARITY_NEGATIVE : data.POLARITY_POSITIVE;

        data.setPolarity(TE, polarity);

        if (polarity == data.POLARITY_POSITIVE) {
            ChatHandler.sendMessageToPlayer("message.polarity_pos.name", entityPlayer);
        } else {
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
        int sensitivity = data.setNextSensitivity(TE);

        if (sensitivity == data.SENSITIVITY_SLEEP) {
            ChatHandler.sendMessageToPlayer("message.sensitivity_sleep.name", entityPlayer);
        } else if (sensitivity == data.SENSITIVITY_MONSTERS){
            ChatHandler.sendMessageToPlayer("message.sensitivity_monsters.name", entityPlayer);
        } else {
            ChatHandler.sendMessageToPlayer("message.sensitivity_dynamic.name", entityPlayer);
        }

        return true;
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            switch (data.getDirection(TE)) {
                case UP:
                    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
                    break;
                case NORTH:
                    setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
                    break;
                case SOUTH:
                    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
                    break;
                case WEST:
                    setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;
                case EAST:
                    setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
                    break;
                default: {}
            }
        }
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
            return data.getRedstoneOutput(TE);
        }

        return 0;
    }

    /**
     * Calculates and saves the current light level at this space.
     *
     * @param  world the {@link World}
     * @param  x the x coordinate
     * @param  y the y coordinate
     * @param  z the z coordinate
     * @return nothing
     */
    public void updateLightLevel(World world, int x, int y, int z)
    {
        if (!world.provider.hasNoSky) {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {

                int temp = data.getLightLevel(TE);
                float angle = world.getCelestialAngleRadians(1.0F);

                int lightValue = world.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) - world.skylightSubtracted;
                int sensitivity = data.getSensitivity(TE);

                if (sensitivity == data.SENSITIVITY_DYNAMIC) {

                    if (angle <= 1.67D || angle >= 4.62) {

                        /* Adjust strength based on sun exposure. */

                        switch (data.getDirection(TE)) {
                            case UP:
                                lightValue = getCelestialRedstoneOutput(world, x, y, z, lightValue, angle);
                                break;
                            case NORTH:
                            case SOUTH:
                                lightValue *= 0.6F;
                                break;
                            case EAST:
                                lightValue = getCelestialRedstoneOutput(world, x, y, z, lightValue, (float) (angle + Math.PI / 2));
                                break;
                            case WEST:
                                lightValue = getCelestialRedstoneOutput(world, x, y, z, lightValue, (float) (angle - Math.PI / 2));
                                break;
                            default: {}
                        }

                    } else {
                        lightValue = 0;
                    }

                } else if (sensitivity == data.SENSITIVITY_MONSTERS && world.isThundering()) {

                    /* Override light value to trigger monster-spawn light threshold during thunderstorms. */

                    lightValue = 7;

                }

                if (temp != lightValue) {
                    data.setLightLevel(TE, lightValue);
                    world.notifyBlocksOfNeighborChange(x, y, z, this);
                }

            }

        }
    }

    /**
     * Gets redstone output strength based on the angle of the sun.
     * <p>
     * This is copied from {@link BlockDaylightDetector#func_149957_e BlockDaylightDetector}
     * with a parameterized skylight and angle added.
     *
     * @param  world the {@link World}
     * @param  x the x coordinate
     * @param  y the y coordinate
     * @param  z the z coordinate
     * @param  skylight the saved light value
     * @param  angle the angle of the sun in radians
     * @return the output strength from 0 to 15
     */
    public int getCelestialRedstoneOutput(World world, int x, int y, int z, int skylight, float angle)
    {
        if (!world.provider.hasNoSky) {

            if (angle < (float) Math.PI) {
                angle += (0.0F - angle) * 0.2F;
            } else {
                angle += (((float) Math.PI * 2F) - angle) * 0.2F;
            }

            skylight = Math.round(skylight * MathHelper.cos(angle));

            if (skylight < 0) {
                skylight = 0;
            } else if (skylight > 15) {
                skylight = 15;
            }

        }

        return skylight;
    }

    /**
     * Whether block can be attached to specified side of another block.
     *
     * @param  side the side
     * @return whether side is supported
     */
    @Override
    public boolean canAttachToSide(int side)
    {
        return side != 0;
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
