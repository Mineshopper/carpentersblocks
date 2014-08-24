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
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
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
     * Check render settings on render event.
     */
    public void onPreRenderWorldEvent(RenderWorldEvent.Pre event)
    {
        if (ShadersHandler.enableShadersModCoreIntegration) {
            ShadersHandler.updateLightness();
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
                            event.setCanceled(true);
                            PacketHandler.sendPacketToServer(new PacketActivateBlock(event.x, event.y, event.z, event.face));
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
        EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;

        if (entityPlayer.isSneaking()) {
            ItemStack itemStack = entityPlayer.getHeldItem();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && BlockProperties.toBlock(itemStack).equals(BlockRegistry.blockCarpentersSlope)) {
                if (event.dwheel != 0) {
                    PacketHandler.sendPacketToServer(new PacketSlopeSelect(event.dwheel > 0));
                }
                event.setCanceled(true);
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

        int x = MathHelper.floor_double(entity.posX);
        int y = MathHelper.floor_double(entity.posY - 0.20000000298023224D - entity.yOffset);
        int z = MathHelper.floor_double(entity.posZ);

        if (world.getBlock(x, y, z) instanceof BlockCoverable) {

            TEBase TE = (TEBase) world.getTileEntity(x, y, z);
            int effectiveSide = BlockProperties.hasCover(TE, 1) ? 1 : 6;
            ItemStack itemStack = BlockProperties.getCover(TE, effectiveSide);

            if (BlockProperties.hasOverlay(TE, effectiveSide)) {
                Overlay overlay = OverlayHandler.getOverlayType(BlockProperties.getOverlay(TE, effectiveSide));
                if (OverlayHandler.coversFullSide(overlay, 1)) {
                    itemStack = overlay.getItemStack();
                }
            }

            /* Spawn sprint particles client-side. */

            if (world.isRemote && entity.isSprinting() && !entity.isInWater()) {
                ParticleHelper.spawnTileParticleAt(entity, itemStack);
            }

            /* Adjust block slipperiness according to cover. */

            if (BlockProperties.toBlock(itemStack) instanceof BlockCoverable) {
                TE.getBlockType().slipperiness = Blocks.dirt.slipperiness;
            } else {
                TE.getBlockType().slipperiness = BlockProperties.toBlock(itemStack).slipperiness;
            }

        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlaySoundEvent(PlaySoundEvent17 event)
    {
        if (event != null && event.name != null && event.name.contains(CarpentersBlocks.MODID)) {

            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {

                World world = FMLClientHandler.instance().getClient().theWorld;
                int x = MathHelper.floor_float(event.sound.getXPosF());
                int y = MathHelper.floor_float(event.sound.getYPosF());
                int z = MathHelper.floor_float(event.sound.getZPosF());

                Block block = world.getBlock(x, y, z);

                TileEntity TE = world.getTileEntity(x, y, z);

                if (TE != null && TE instanceof TEBase) {
                    block = BlockProperties.toBlock(BlockProperties.getCover((TEBase) TE, 6));
                }

                if (block instanceof BlockCoverable) {
                    block = Blocks.planks;
                }

                event.result = new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F, x + 0.5F, y + 0.5F, z + 0.5F);

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
        if (event != null && event.name != null && event.name.contains(CarpentersBlocks.MODID)) {

            int x = MathHelper.floor_double(event.entity.posX);
            int y = MathHelper.floor_double(event.entity.posY - 0.20000000298023224D - event.entity.yOffset);
            int z = MathHelper.floor_double(event.entity.posZ);

            Block block = event.entity.worldObj.getBlock(x, y, z);
            String prefix = event.name.substring(0, event.name.indexOf(".") + 1);

            if (block instanceof BlockCoverable) {

                block = BlockProperties.toBlock(BlockProperties.getCover((TEBase) event.entity.worldObj.getTileEntity(x, y, z), 6));

                if (block instanceof BlockCoverable) {
                    event.name = prefix + Blocks.planks.stepSound.soundName;
                } else if (block.stepSound != null) {
                    event.name = prefix + block.stepSound.soundName;
                }

            } else {

                event.name = prefix + Blocks.planks.stepSound.soundName;

            }
        }
    }

}
