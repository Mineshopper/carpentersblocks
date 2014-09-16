package com.carpentersblocks.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.data.PressurePlate;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.handler.ChatHandler;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersPressurePlate extends BlockSided {

    private static PressurePlate data = new PressurePlate();

    /** Whether full bounds should be used for collision purposes. */
    private boolean fullBounds = false;

    public BlockCarpentersPressurePlate(Material material)
    {
        super(material, data);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns a base icon that doesn't rely on blockIcon, which
     * is set prior to texture stitch events.
     */
    public IIcon getIcon()
    {
        return IconRegistry.icon_uncovered_full;
    }

    @Override
    /**
     * Alters polarity.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int polarity = data.getPolarity(TE) == data.POLARITY_POSITIVE ? data.POLARITY_NEGATIVE : data.POLARITY_POSITIVE;

        data.setPolarity(TE, polarity);
        TE.getWorldObj().notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord - 1, TE.zCoord, this);

        if (polarity == data.POLARITY_POSITIVE) {
            ChatHandler.sendMessageToPlayer("message.polarity_pos.name", entityPlayer);
        } else {
            ChatHandler.sendMessageToPlayer("message.polarity_neg.name", entityPlayer);
        }

        return true;
    }

    @Override
    /**
     * Alters trigger behavior.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int trigger = data.getTriggerEntity(TE);

        if (++trigger > 3) {
            trigger = 0;
        }

        data.setTriggerEntity(TE, trigger);

        if (trigger == data.TRIGGER_PLAYER) {
            ChatHandler.sendMessageToPlayer("message.trigger_player.name", entityPlayer);
        } else if (trigger == data.TRIGGER_MONSTER) {
            ChatHandler.sendMessageToPlayer("message.trigger_monster.name", entityPlayer);
        } else if (trigger == data.TRIGGER_ANIMAL) {
            ChatHandler.sendMessageToPlayer("message.trigger_animal.name", entityPlayer);
        } else {
            ChatHandler.sendMessageToPlayer("message.trigger_all.name", entityPlayer);
        }

        return true;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            float depth = fullBounds | !isDepressed(TE) ? 0.0625F : 0.03125F;
            ForgeDirection side = data.getDirection(TE);

            switch (side) {
                case DOWN:
                    setBlockBounds(0.0625F, 1.0F - depth, 0.0625F, 0.9375F, 1.0F, 0.9375F);
                    break;
                case UP:
                    setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, depth, 0.9375F);
                    break;
                case NORTH:
                    setBlockBounds(0.0625F, 0.0625F, 1.0F - depth, 0.9375F, 0.9375F, 1.0F);
                    break;
                case SOUTH:
                    setBlockBounds(0.0625F, 0.0625F, 0.0F, 0.9375F, 0.9375F, depth);
                    break;
                case WEST:
                    setBlockBounds(1.0F - depth, 0.0625F, 0.0625F, 1.0F, 0.9375F, 0.9375F);
                    break;
                case EAST:
                    setBlockBounds(0.0F, 0.0625F, 0.0625F, depth, 0.9375F, 0.9375F);
                    break;
                default: {}
            }

        }
    }

    @Override
    /**
     * How many world ticks before ticking
     */
    public int tickRate(World world)
    {
        return 20;
    }

    @Override
    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (!world.isRemote) {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {

                boolean depressed = false;

                if (isDepressed(TE)) {
                    depressed = hasTriggerInBounds(TE);
                }

                if (depressed) {
                    world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
                } else {
                    toggleOff(TE, world, x, y, z);
                }

            }

        }
    }

    @Override
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (!world.isRemote) {
            TEBase TE = getTileEntity(world, x, y, z);
            if (TE != null) {
                if (hasTriggerInBounds(TE)) {
                    toggleOn(TE, world, x, y, z);
                }
            }
        }
    }

    /**
     * Returns whether sensitive area contains an entity that can
     * trigger a state change.
     *
     * @param  TE the {@link TEBase}
     * @return whether sensitive area contains valid {@link Entity}
     */
    private boolean hasTriggerInBounds(TEBase TE)
    {
        fullBounds = true;
        List entityList = TE.getWorldObj().getEntitiesWithinAABB(Entity.class, getSensitiveAABB(TE.xCoord, TE.yCoord, TE.zCoord));
        fullBounds = false;

        if (!entityList.isEmpty()) {
            for (int idx = 0; idx < entityList.size(); ++idx) {
                if (canEntityTrigger(TE, (Entity)entityList.get(idx))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Gets the area of bounding box that is sensitive to changes.
     *
     * @param  x the x coordinate
     * @param  y the y coordinate
     * @param  z the z coordinate
     * @return the {@link AxisAlignedBB}
     */
    private AxisAlignedBB getSensitiveAABB(int x, int y, int z)
    {
        World world = Minecraft.getMinecraft().theWorld;

        if (world != null) {
            setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, x, y, z);
        }

        return AxisAlignedBB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);
    }

    /**
     * Activates pressure plate.
     */
    private void toggleOn(TEBase TE, World world, int x, int y, int z)
    {
        data.setState(TE, data.STATE_ON, true);
        notifyNeighborOfUpdate(world, x, y, z);
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    }

    /**
     * Deactivates pressure plate.
     */
    private void toggleOff(TEBase TE, World world, int x, int y, int z)
    {
        data.setState(TE, data.STATE_OFF, true);
        notifyNeighborOfUpdate(world, x, y, z);
    }

    /**
     * Returns whether pressure plate is in depressed state
     */
    private boolean isDepressed(TEBase TE)
    {
        return data.getState(TE) == data.STATE_ON;
    }

    @Override
    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Returns power level (0 or 15)
     */
    @Override
    public int getPowerOutput(TEBase TE)
    {
        int polarity = data.getPolarity(TE);

        if (isDepressed(TE)) {
            return polarity == data.POLARITY_POSITIVE ? 15 : 0;
        } else {
            return polarity == data.POLARITY_NEGATIVE ? 15 : 0;
        }
    }

    /**
     * Returns whether pressure plate should trigger based on entity colliding with it.
     */
    private boolean canEntityTrigger(TEBase TE, Entity entity)
    {
        if (entity == null) {
            return false;
        }

        int trigger = data.getTriggerEntity(TE);

        if (trigger == data.TRIGGER_PLAYER) {
            return entity instanceof EntityPlayer;
        } else if (trigger == data.TRIGGER_MONSTER) {
            return entity.isCreatureType(EnumCreatureType.monster, false);
        } else if (trigger == data.TRIGGER_ANIMAL) {
            return entity.isCreatureType(EnumCreatureType.creature, false);
        } else {
            return true;
        }
    }

    @Override
    /**
     * Ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        TEBase TE = getSimpleTileEntity(world, x, y, z);

        if (TE != null) {
            if (isDepressed(TE)) {
                notifyNeighborOfUpdate(world, x, y, z);
            }
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersPressurePlateRenderID;
    }

}
