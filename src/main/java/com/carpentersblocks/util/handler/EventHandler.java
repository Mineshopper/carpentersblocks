package com.carpentersblocks.util.handler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.api.ICarpentersChisel;
import com.carpentersblocks.api.ICarpentersHammer;
import com.carpentersblocks.block.BlockCoverable;
import com.carpentersblocks.network.PacketActivateBlock;
import com.carpentersblocks.network.PacketSlopeSelect;
import com.carpentersblocks.renderer.helper.ParticleHelper;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.handler.OverlayHandler.Overlay;
import com.carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EventHandler {

    public static float hitX;
    public static float hitY;
    public static float hitZ;

    /** Stores face for onBlockClicked(). */
    public static int eventFace;

    /** Stores entity that hit block. */
    public static EntityPlayer eventEntityPlayer;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    /**
     * Check render settings on GUI open/close event.
     */
    public void onGuiOpenEvent(GuiOpenEvent event)
    {
        if (event.gui == null) {
            if (ShadersHandler.enableShadersModCoreIntegration) {
                ShadersHandler.update();
            }
        }
    }

    @SubscribeEvent
    /**
     * Used to store side clicked and also forces onBlockActivated
     * event when entityPlayer is sneaking and activates block with the
     * Carpenter's Hammer.
     */
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        Block block = event.entity.worldObj.getBlock(event.x, event.y, event.z);

        if (block instanceof BlockCoverable) {

            eventFace = event.face;
            eventEntityPlayer = event.entityPlayer;

            ItemStack itemStack = eventEntityPlayer.getHeldItem();

            MovingObjectPosition object = getMovingObjectPositionFromPlayer(eventEntityPlayer.worldObj, eventEntityPlayer);

            if (object != null) {
                hitX = (float)object.hitVec.xCoord - event.x;
                hitY = (float)object.hitVec.yCoord - event.y;
                hitZ = (float)object.hitVec.zCoord - event.z;
            } else {
                hitX = hitY = hitZ = 1.0F;
            }

            switch (event.action) {
                case LEFT_CLICK_BLOCK:

                    boolean toolEquipped = itemStack != null && (itemStack.getItem() instanceof ICarpentersHammer || itemStack.getItem() instanceof ICarpentersChisel);

                    /*
                     * Creative mode doesn't normally invoke onBlockClicked(), but rather it tries
                     * to destroy the block.
                     *
                     * We'll invoke it here when a Carpenter's tool is being held.
                     */

                    if (toolEquipped && eventEntityPlayer.capabilities.isCreativeMode) {
                        block.onBlockClicked(eventEntityPlayer.worldObj, event.x, event.y, event.z, eventEntityPlayer);
                    }

                    break;
                case RIGHT_CLICK_BLOCK:

                    /*
                     * To enable full functionality with the hammer, we need to override pretty
                     * much everything that happens on sneak right-click.
                     *
                     * onBlockActivated() isn't called if the player is sneaking, so do it here.
                     *
                     * The server will receive the packet and attempt to alter the Carpenter's
                     * block.  If nothing changes, vanilla behavior will resume - the Item(Block)
                     * in the ItemStack (if applicable) will be created adjacent to block.
                     */

                    if (eventEntityPlayer.isSneaking()) {
                        if (!(itemStack != null && itemStack.getItem() instanceof ItemBlock && !BlockProperties.isOverlay(itemStack))) {
                            event.setCanceled(true); // Normally prevents server event, but sometimes it doesn't, so check below
                            if (event.entity.worldObj.isRemote) {
                                PacketHandler.sendPacketToServer(new PacketActivateBlock(event.x, event.y, event.z, event.face));
                            }
                        }
                    }

                    break;
                default: {}
            }

        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    /**
     * Grabs mouse scroll events for slope selection.
     */
    public void onMouseEvent(MouseEvent event)
    {
        // We only want to process wheel events
        if (event.button < 0)
        {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;

            if (entityPlayer.isSneaking()) {
                ItemStack itemStack = entityPlayer.getHeldItem();
                if (itemStack != null && itemStack.getItem() instanceof ItemBlock && BlockProperties.toBlock(itemStack).equals(BlockRegistry.blockCarpentersSlope)) {
                    if (event.dwheel != 0) {
                        PacketHandler.sendPacketToServer(new PacketSlopeSelect(entityPlayer.inventory.currentItem, event.dwheel > 0));
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

    /**
     * Returns the MovingObjectPosition of block hit by player.
     * Adapted from protected method of same name in Item.class.
     */
    private MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityPlayer entityPlayer)
    {
        double xPos = entityPlayer.prevPosX + (entityPlayer.posX - entityPlayer.prevPosX);
        double yPos = entityPlayer.prevPosY + (entityPlayer.posY - entityPlayer.prevPosY) + (world.isRemote ? entityPlayer.getEyeHeight() - entityPlayer.getDefaultEyeHeight() : entityPlayer.getEyeHeight());
        double zPos = entityPlayer.prevPosZ + (entityPlayer.posZ - entityPlayer.prevPosZ);

        float pitch = entityPlayer.prevRotationPitch + (entityPlayer.rotationPitch - entityPlayer.prevRotationPitch);
        float yaw = entityPlayer.prevRotationYaw + (entityPlayer.rotationYaw - entityPlayer.prevRotationYaw);

        float commonComp = -MathHelper.cos(-pitch * 0.017453292F);

        float xComp = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI) * commonComp;
        float yComp = MathHelper.sin(-pitch * 0.017453292F);
        float zComp = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI) * commonComp;
        double reachDist = 5.0D;

        if (entityPlayer instanceof EntityPlayerMP) {
            reachDist = ((EntityPlayerMP)entityPlayer).theItemInWorldManager.getBlockReachDistance();
        }

        Vec3 vec1 = Vec3.createVectorHelper(xPos, yPos, zPos);
        Vec3 vec2 = vec1.addVector(xComp * reachDist, yComp * reachDist, zComp * reachDist);

        return world.rayTraceBlocks(vec1, vec2);
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingUpdateEvent event)
    {
        Entity entity = event.entityLiving;
        World world = entity.worldObj;

        /*
         * The purpose of the function is to manifest sprint particles
         * and adjust slipperiness when entity is moving on block, so check
         * that the conditions are met first.
         */
        if (!isMovingOnGround(entity))
        {
            return;
        }

        TEBase TE = getTileEntityAtFeet(entity);
        if (TE != null) {

            ItemStack itemStack = getSurfaceItemStack(TE);

            /* Spawn sprint particles client-side. */

            if (world.isRemote && entity.isSprinting() && !entity.isInWater()) {
                ParticleHelper.spawnTileParticleAt(entity, itemStack);
            }

            /* Adjust block slipperiness according to cover. */

            Block block = BlockProperties.toBlock(itemStack);
            if (block instanceof BlockCoverable) {
                TE.getBlockType().slipperiness = Blocks.dirt.slipperiness;
            } else {
                TE.getBlockType().slipperiness = block.slipperiness;
            }

        }
    }

    /**
     * {@link onPlaySoundEvent} is used differently for singleplayer
     * and multiplayer sound events. This will try to locate the {@link TEBase}
     * that best represents the origin of the sound.
     * <p>
     * In singleplayer, this is normally the origin since it's used mainly
     * for placement and destruction sounds.
     * <p>
     * In multiplayer, foreign players produce this event for step sounds,
     * requiring a y offset of -1 to approximate the origin.
     *
     * @param  world the {@link World}
     * @param  x the x coordinate
     * @param  y the y coordinate
     * @param  z the z coordinate
     * @return an approximate {@link TEBase} used for producing a sound
     */
    private TEBase getApproximateSoundOrigin(World world, int x, int y, int z)
    {
        // Try origin first
        TileEntity TE = world.getTileEntity(x, y, z);
        if (TE != null && TE instanceof TEBase)
        {
            return (TEBase) TE;
        }
        else
        {
            // Try y-offset -1
            TileEntity TE_YN = world.getTileEntity(x, y - 1, z);
            if (TE_YN != null && TE_YN instanceof TEBase)
            {
                return (TEBase) TE_YN;
            }
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlaySoundEvent(PlaySoundEvent17 event)
    {
        if (event != null && event.name != null && event.name.contains(CarpentersBlocks.MODID))
        {
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
            {
                World world = FMLClientHandler.instance().getClient().theWorld;
                int x = MathHelper.floor_double(event.sound.getXPosF());
                int y = MathHelper.floor_double(event.sound.getYPosF());
                int z = MathHelper.floor_double(event.sound.getZPosF());

                // We'll set a default block type to be safe
                Block block = Blocks.planks;

                // Grab approximate origin, and gather accurate block type
                TEBase TE = getApproximateSoundOrigin(world, x, y, z);
                if (TE != null && TE.hasAttribute(TE.ATTR_COVER[6])) {
                    block = BlockProperties.toBlock(BlockProperties.getCoverSafe(TE, 6));
                }

                if (event.name.startsWith("step.")) {
                    event.result = new PositionedSoundRecord(new ResourceLocation(block.stepSound.getStepResourcePath()), block.stepSound.getVolume() * 0.15F, block.stepSound.getPitch(), x + 0.5F, y + 0.5F, z + 0.5F);
                } else { // "dig." usually
                    event.result = new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F, x + 0.5F, y + 0.5F, z + 0.5F);
                }
            }
        }
    }

    /**
     * Override sounds when walking on block.
     *
     * @param event
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlaySoundAtEntityEvent(PlaySoundAtEntityEvent event)
    {
        if (event != null && event.name != null && event.name.contains(CarpentersBlocks.MODID))
        {
            Entity entity = event.entity;

            /*
             * The function to my knowledge is only used for playing walking sounds
             * at entity, so we'll check for the conditions first.
             */
            if (!isMovingOnGround(entity))
            {
                return;
            }

            TEBase TE = getTileEntityAtFeet(entity);
            if (TE != null) {

                // Give SoundType a valid resource by default
                event.name = Blocks.planks.stepSound.getStepResourcePath();

                // Gather accurate SoundType based on block properties
                Block block = BlockProperties.toBlock(getSurfaceItemStack(TE));
                if (!(block instanceof BlockCoverable))
                {
                    event.name = block.stepSound.getStepResourcePath();
                }

            }
        }
    }

    /**
     * Gets the {@link TEBase} object at player's feet, if one exists.
     * <p>
     * It is safer to gather the tile entity reference than a block reference.
     *
     * @param entity
     * @return
     */
    private TEBase getTileEntityAtFeet(Entity entity)
    {
        int x = MathHelper.floor_double(entity.posX);
        int y = MathHelper.floor_double(entity.posY - 0.20000000298023224D - entity.yOffset);
        int z = MathHelper.floor_double(entity.posZ);

        TileEntity tileEntity = entity.worldObj.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TEBase)
        {
            return (TEBase) tileEntity;
        }
        else
        {
            return null;
        }
    }

    /**
     * Gets an {@link ItemStack} that best represents the surface
     * of a Carpenter's Block.
     * <p>
     * The top side cover and any overlays are taken into consideration.
     *
     * @param TE
     * @return
     */
    private ItemStack getSurfaceItemStack(TEBase TE)
    {
        // Check for top side cover
        int effectiveSide = TE.hasAttribute(TE.ATTR_COVER[1]) ? 1 : 6;
        ItemStack itemStack = BlockProperties.getCover(TE, effectiveSide);

        // Check for overlay on cover
        if (TE.hasAttribute(TE.ATTR_OVERLAY[effectiveSide])) {
            Overlay overlay = OverlayHandler.getOverlayType(TE.getAttribute(TE.ATTR_OVERLAY[effectiveSide]));
            if (OverlayHandler.coversFullSide(overlay, 1)) {
                itemStack = overlay.getItemStack();
            }
        }

        return itemStack;
    }

    /**
     * Determines if the player is moving in the x, z directions on
     * solid ground.
     *
     * @param entity
     * @return
     */
    private boolean isMovingOnGround(Entity entity)
    {
        return entity.onGround && (entity.motionX != 0 || entity.motionZ != 0);
    }

}
