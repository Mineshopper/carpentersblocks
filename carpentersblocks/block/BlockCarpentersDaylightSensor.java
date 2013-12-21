package carpentersblocks.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.DaylightSensor;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersDaylightSensor;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersDaylightSensor extends BlockBase {

	public BlockCarpentersDaylightSensor(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersDaylightSensor");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
	}

	@SideOnly(Side.CLIENT)
	@Override
	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister iconRegister)
	{
		IconRegistry.icon_daylight_sensor_glass_top = iconRegister.registerIcon("carpentersblocks:daylightsensor/daylight_sensor_glass_top");

		blockIcon = IconRegistry.icon_solid;
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
			entityPlayer.addChatMessage("message.activation_day.name");
			break;
		case DaylightSensor.POLARITY_NEGATIVE:
			entityPlayer.addChatMessage("message.activation_night.name");
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
		TEBase TE = (TEBase)world.getBlockTileEntity(x, y, z);

		int type = DaylightSensor.getType(TE);

		boolean isDay = type > 2;
		boolean outputDay = DaylightSensor.getPolarity(TE) == DaylightSensor.POLARITY_POSITIVE;

		return isDay ? outputDay ? type : 0 : !outputDay ? 15 : 0;
	}

	public void updateLightLevel(World world, int x, int y, int z)
	{
		if (!world.provider.hasNoSky)
		{
			TEBase TE = (TEBase)world.getBlockTileEntity(x, y, z);

			int old_lightValue = DaylightSensor.getType(TE);

			int lightValue = world.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) - world.skylightSubtracted;
			float lightAngle = world.getCelestialAngleRadians(1.0F);

			if (lightAngle < (float)Math.PI) {
				lightAngle += (0.0F - lightAngle) * 0.2F;
			} else {
				lightAngle += ((float)Math.PI * 2F - lightAngle) * 0.2F;
			}

			lightValue = Math.round(lightValue * MathHelper.cos(lightAngle));

			if (lightValue < 0) {
				lightValue = 0;
			} else if (lightValue > 15) {
				lightValue = 15;
			}

			if (old_lightValue != lightValue)
			{
				DaylightSensor.setType(TE, lightValue);
				world.notifyBlocksOfNeighborChange(x, y, z, blockID);
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
	public TileEntity createNewTileEntity(World world)
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
