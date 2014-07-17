package carpentersblocks.block;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Bed;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.ChatHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersBed extends BlockCoverable {

    public BlockCarpentersBed(int blockID, Material material)
    {
        super(blockID, material);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister)
    {
        IconRegistry.icon_bed_pillow = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "bed/bed_pillow");
    }

    @Override
    /**
     * Determines if this block is classified as a Bed, Allowing
     * players to sleep in it, though the block has to specifically
     * perform the sleeping functionality in it's activated event.
     */
    public boolean isBed(World world, int x, int y, int z, EntityLivingBase player)
    {
        return true;
    }

    @Override
    /**
     * Cycle backward through bed designs.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        BlockProperties.setPrevDesign(TE);
        BlockProperties.setDesign(Bed.getOppositeTE(TE), BlockProperties.getDesign(TE));

        return true;
    }

    @Override
    /**
     * Cycle forward through bed designs.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        if (entityPlayer.isSneaking()) {
            BlockProperties.clearDesign(TE);
            BlockProperties.clearDesign(Bed.getOppositeTE(TE));
        } else {
            BlockProperties.setNextDesign(TE);
            BlockProperties.setDesign(Bed.getOppositeTE(TE), BlockProperties.getDesign(TE));
        }

        return true;
    }

    @Override
    /**
     * Called upon block activation (right click on the block.)
     */
    protected void postOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, ActionResult actionResult)
    {
        actionResult.setAltered();
        World world = TE.getWorldObj();

        int x = TE.xCoord;
        int y = TE.yCoord;
        int z = TE.zCoord;

        if (!Bed.isHeadOfBed(TE)) {

            TEBase TE_opp = Bed.getOppositeTE(TE);

            if (TE_opp != null) {
                x = TE_opp.xCoord;
                z = TE_opp.zCoord;
            } else {
                return;
            }

        }

        if (world.provider.canRespawnHere() && world.getBiomeGenForCoords(x, z) != BiomeGenBase.hell) {

            if (Bed.isOccupied(TE)) {

                EntityPlayer entityPlayer1 = null;
                Iterator iterator = world.playerEntities.iterator();

                while (iterator.hasNext()) {

                    EntityPlayer entityPlayer2 = (EntityPlayer)iterator.next();

                    if (entityPlayer2.isPlayerSleeping()) {

                        ChunkCoordinates chunkCoordinates = entityPlayer2.playerLocation;

                        if (chunkCoordinates.posX == x && chunkCoordinates.posY == y && chunkCoordinates.posZ == z) {
                            entityPlayer1 = entityPlayer2;
                        }

                    }

                }

                if (entityPlayer1 != null) {

                    ChatHandler.sendMessageToPlayer("tile.bed.occupied", entityPlayer, false);
                    return;

                }

                setBedOccupied(world, x, y, z, entityPlayer, false);

            }

            EnumStatus enumstatus = entityPlayer.sleepInBedAt(x, y, z);

            if (enumstatus == EnumStatus.OK) {

                setBedOccupied(world, x, y, z, entityPlayer, true);

            } else {

                if (enumstatus == EnumStatus.NOT_POSSIBLE_NOW) {
                    ChatHandler.sendMessageToPlayer("tile.bed.noSleep", entityPlayer, false);
                } else if (enumstatus == EnumStatus.NOT_SAFE) {
                    ChatHandler.sendMessageToPlayer("tile.bed.notSafe", entityPlayer, false);
                }

            }

        } else {

            world.setBlockToAir(x, y, z);
            world.newExplosion((Entity)null, x + 0.5F, y + 0.5F, z + 0.5F, 5.0F, true, true);

        }
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        if (!world.isRemote) {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {
                if (Bed.getOppositeTE(TE) == null) {
                    world.setBlockToAir(x, y, z);
                }
            }

        }

        super.onNeighborBlockChange(world, x, y, z, blockID);
    }

    @Override
    /**
     * Returns the items to drop on destruction.
     */
    public int idDropped(int par1, Random random, int par2)
    {
        return ItemRegistry.itemCarpentersBed.itemID;
    }

    @Override
    /**
     * Called when a user either starts or stops sleeping in the bed.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param player The player or camera entity, null in some cases.
     * @param occupied True if we are occupying the bed, or false if they are stopping use of the bed
     */
    public void setBedOccupied(World world, int x, int y, int z, EntityPlayer entityPlayer, boolean isOccupied)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null && !TE.getWorldObj().isRemote) {

            Bed.setOccupied(TE, isOccupied);

            TEBase TE_opp = Bed.getOppositeTE(TE);

            if (TE_opp != null) {
                Bed.setOccupied(TE_opp, isOccupied);
            }

        }
    }

    @Override
    /**
     * Returns the direction of the block. Same values that
     * are returned by BlockDirectional
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return Bed direction
     */
    public int getBedDirection(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        switch (Bed.getDirection(TE))
        {
            case NORTH:
                return 0;
            case SOUTH:
                return 2;
            case WEST:
                return 3;
            default:
                return 1;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    public int idPicked(World world, int x, int y, int z)
    {
        return ItemRegistry.itemCarpentersBed.itemID;
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersBedRenderID;
    }

}
