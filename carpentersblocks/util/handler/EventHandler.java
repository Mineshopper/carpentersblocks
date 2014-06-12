package carpentersblocks.util.handler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import carpentersblocks.api.ICarpentersChisel;
import carpentersblocks.api.ICarpentersHammer;
import carpentersblocks.block.BlockCoverable;
import carpentersblocks.renderer.helper.ParticleHelper;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
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

    /** This is an offset used for blockIcon. */
    public final static int BLOCKICON_BASE_ID = 1000;

    @ForgeSubscribe
    /**
     * Used to store side clicked and also forces onBlockActivated
     * event when entityPlayer is sneaking and activates block with the
     * Carpenter's Hammer.
     */
    public void playerInteractEvent(PlayerInteractEvent event)
    {
        int blockID = event.entity.worldObj.getBlockId(event.x, event.y, event.z);

        if (blockID > 0 && Block.blocksList[blockID] instanceof BlockCoverable)
        {
            BlockCoverable block = (BlockCoverable) Block.blocksList[blockID];

            eventFace = event.face;
            eventEntityPlayer = event.entityPlayer;

            ItemStack itemStack = eventEntityPlayer.getHeldItem();

            MovingObjectPosition object = getMovingObjectPositionFromPlayer(eventEntityPlayer.worldObj, eventEntityPlayer, false);

            if (object != null)
            {
                Vec3 vec = object.hitVec;
                hitX = (float)vec.xCoord - event.x;
                hitY = (float)vec.yCoord - event.y;
                hitZ = (float)vec.zCoord - event.z;
            }

            boolean toolEquipped = itemStack != null && (itemStack.getItem() instanceof ICarpentersHammer || itemStack.getItem() instanceof ICarpentersChisel);

            if (event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {

                /*
                 * Creative mode won't call onBlockClicked() because it will try to destroy the block.
                 * We'll invoke it here when a Carpenter's tool is being held.
                 */
                
                if (toolEquipped && eventEntityPlayer.capabilities.isCreativeMode) {
                    block.onBlockClicked(eventEntityPlayer.worldObj, event.x, event.y, event.z, eventEntityPlayer);
                }

            } else {

                /*
                 * onBlockActivated() isn't called if the player is sneaking.
                 * We'll invoke it here.  It's not able to adjust the player's
                 * inventory for operations such as decrementing an itemstack,
                 * so we're limiting it to tool actions only.
                 */
                
                if (eventEntityPlayer.isSneaking()) {
                    block.onBlockActivated(eventEntityPlayer.worldObj, event.x, event.y, event.z, eventEntityPlayer, eventFace, 1.0F, 1.0F, 1.0F);
                }

            }
        }
    }

    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    /**
     * Grabs mouse scroll events for slope selection.
     */
    public void mouseEvent(MouseEvent event)
    {
        EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;

        if (entityPlayer.isSneaking()) {

            ItemStack itemStack = entityPlayer.getHeldItem();

            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && Block.blocksList[itemStack.itemID].equals(BlockRegistry.blockCarpentersSlope)) {

                if (event.dwheel == 120) {
                    entityPlayer.inventory.currentItem = ++entityPlayer.inventory.currentItem;
                } else if (event.dwheel == -120) {
                    entityPlayer.inventory.currentItem = --entityPlayer.inventory.currentItem;
                } else {
                    return;
                }

                PacketHandler.damageSlopeInSlot(entityPlayer.inventory.currentItem, event.dwheel == 120);

            }

        }
    }

    /**
     * Copied from Item.class.
     * Returns the MovingObjectPosition of block hit by player.
     */
    private MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityPlayer entityPlayer, boolean right_click_boat_held)
    {
        float f = 1.0F;
        float f1 = entityPlayer.prevRotationPitch + (entityPlayer.rotationPitch - entityPlayer.prevRotationPitch) * f;
        float f2 = entityPlayer.prevRotationYaw + (entityPlayer.rotationYaw - entityPlayer.prevRotationYaw) * f;
        double d0 = entityPlayer.prevPosX + (entityPlayer.posX - entityPlayer.prevPosX) * f;
        double d1 = entityPlayer.prevPosY + (entityPlayer.posY - entityPlayer.prevPosY) * f + (world.isRemote ? entityPlayer.getEyeHeight() - entityPlayer.getDefaultEyeHeight() : entityPlayer.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
        double d2 = entityPlayer.prevPosZ + (entityPlayer.posZ - entityPlayer.prevPosZ) * f;
        Vec3 vec3 = world.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        if (entityPlayer instanceof EntityPlayerMP)
        {
            d3 = ((EntityPlayerMP)entityPlayer).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
        return world.rayTraceBlocks_do_do(vec3, vec31, right_click_boat_held, !right_click_boat_held);
    }

    @ForgeSubscribe
    public void livingUpdateEvent(LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.entityLiving;
        World world = entity.worldObj;

        int x = MathHelper.floor_double(entity.posX);
        int y = MathHelper.floor_double(entity.posY - 0.20000000298023224D - entity.yOffset);
        int z = MathHelper.floor_double(entity.posZ);

        int blockId = world.getBlockId(x, y, z);

        if (blockId > 0 && Block.blocksList[blockId] instanceof BlockCoverable) {

            TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);
            int effectiveSide = BlockProperties.hasCover(TE, 1) ? 1 : 6;
            Block block = BlockProperties.getCoverBlock(TE, effectiveSide);

            /* Spawn sprint particles client-side. */

            if (world.isRemote && entity.isSprinting() && !entity.isInWater()) {
                int metadata = block instanceof BlockCoverable ? BLOCKICON_BASE_ID : BlockProperties.getCoverMetadata(TE, effectiveSide);
                ParticleHelper.spawnTileParticleAt(world, entity, OverlayHandler.getBlockFromOverlay(TE, effectiveSide, block).blockID, metadata);
            }

            /* Adjust block slipperiness according to cover. */

            TE.getBlockType().slipperiness = !BlockProperties.hasCover(TE, effectiveSide) ? Block.wood.slipperiness : OverlayHandler.getBlockFromOverlay(TE, effectiveSide, block).slipperiness;

        }
    }

    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void SoundEvent(PlaySoundEvent event)
    {
        if (event != null && event.name != null)
        {
            if (event.name.contains("carpentersblock"))
            {
                if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
                {
                    World world = FMLClientHandler.instance().getClient().theWorld;
                    int x = MathHelper.floor_float(event.x);
                    int y = MathHelper.floor_float(event.y);
                    int z = MathHelper.floor_float(event.z);
                    int blockID = world.getBlockId(x, y, z);

                    if (blockID > 0 && Block.blocksList[blockID] instanceof BlockCoverable) {

                        Block block = BlockProperties.getCoverBlock((TEBase)world.getBlockTileEntity(x, y, z), 6);

                        if (block instanceof BlockCoverable) {
                            event.result = event.manager.soundPoolSounds.getRandomSoundFromSoundPool(event.name.startsWith("dig.") ? Block.soundWoodFootstep.getBreakSound() : event.name.startsWith("place.") ? Block.soundWoodFootstep.getPlaceSound() : Block.soundWoodFootstep.getStepSound());
                        } else {
                            event.result = event.manager.soundPoolSounds.getRandomSoundFromSoundPool(event.name.startsWith("dig.") ? block.stepSound.getBreakSound() : event.name.startsWith("place.") ? block.stepSound.getPlaceSound() : block.stepSound.getStepSound());
                        }

                    } else {

                        event.result = event.manager.soundPoolSounds.getRandomSoundFromSoundPool(Block.soundWoodFootstep.getBreakSound());

                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void StepSoundInterrupt(PlaySoundAtEntityEvent event)
    {
        if (event != null && event.name != null)
        {
            if (event.name.startsWith("step.carpentersblock"))
            {
                int x = MathHelper.floor_double(event.entity.posX);
                int y = MathHelper.floor_double(event.entity.posY - 0.20000000298023224D - event.entity.yOffset);
                int z = MathHelper.floor_double(event.entity.posZ);

                TileEntity TE = event.entity.worldObj.getBlockTileEntity(x, y, z);

                if (TE != null && TE instanceof TEBase)
                {
                    Block block = BlockProperties.getCoverBlock((TEBase) TE, 6);

                    if (block instanceof BlockCoverable) {
                        event.name = Block.soundWoodFootstep.getStepSound();
                    } else if (block.stepSound != null) {
                        event.name = block.stepSound.getStepSound();
                    }
                }
            }
        }
    }

}
