package carpentersblocks.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.FlowerPot;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersFlowerPot;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.flowerpot.FlowerPotDesignHandler;
import carpentersblocks.util.flowerpot.FlowerPotHandler;
import carpentersblocks.util.flowerpot.FlowerPotHandler.Profile;
import carpentersblocks.util.flowerpot.FlowerPotProperties;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersFlowerPot extends BlockCoverable {
    
    public BlockCarpentersFlowerPot(Material material)
    {
        super(material);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        for (int numIcon = 0; numIcon < FlowerPotDesignHandler.maxNum; ++numIcon) {
            if (FlowerPotDesignHandler.hasDesign[numIcon]) {
                IconRegistry.icon_flower_pot_design[numIcon] = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "flowerpot/design/design_" + numIcon);
            }
        }
        
        IconRegistry.icon_flower_pot       = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "flowerpot/flower_pot");
        IconRegistry.icon_flower_pot_glass = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "flowerpot/flower_pot_glass");
        
        super.registerBlockIcons(iconRegister);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Returns the icon on the side given the block metadata.
     */
    public IIcon getIcon(int side, int metadata)
    {
        /*
         * This doesn't work perfectly, but it's necessary to render
         * the pot as an Item in the inventory.  Block destruction will
         * spawn cover and block icons as a result.
         */
        if (side == 1 && metadata == 0) {
            return IconRegistry.icon_flower_pot;
        } else {
            return super.getIcon(side, metadata);
        }
    }
    
    @Override
    /**
     * Sneak-click removes plant and/or soil.
     */
    protected boolean preOnBlockClicked(TEBase TE, World world, int x, int y, int z, EntityPlayer entityPlayer)
    {        
        if (entityPlayer.isSneaking()) {
            
            if (EventHandler.hitY > 0.375F) {
                
                if (FlowerPot.isEnriched(TE)) {
                    FlowerPot.setEnrichment(TE, false);
                    return true;
                }
                
                if (FlowerPotProperties.hasPlant(TE)) {
                    return FlowerPotProperties.setPlant(TE, (ItemStack)null);
                }
                
            } else if (FlowerPotProperties.hasSoil(TE)) {
                
                if (EventHandler.eventFace == 1 && EventHandler.hitX > 0.375F && EventHandler.hitX < 0.625F && EventHandler.hitZ > 0.375F && EventHandler.hitZ < 0.625F) {
                    return FlowerPotProperties.setSoil(TE, (ItemStack)null);
                }
                
            }
            
        }
        
        return false;
    }
    
    @Override
    /**
     * Cycle backward through bed designs.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int design = FlowerPotDesignHandler.getPrev(FlowerPot.getDesign(TE));
        FlowerPot.setDesign(TE, design);
        
        return true;
    }
    
    @Override
    /**
     * Cycle forward through designs or set to no design.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        if (entityPlayer.isSneaking()) {
            FlowerPot.setDesign(TE, 0);
        } else {
            int design = FlowerPotDesignHandler.getNext(FlowerPot.getDesign(TE));
            FlowerPot.setDesign(TE, design);
        }
        
        return true;
    }
    
    @Override
    /**
     * Everything contained in this will run before default onBlockActivated events take place,
     * but after the player has been verified to have permission to edit block.
     */
    protected void preOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, List<Boolean> altered, List<Boolean> decInv)
    {
        ItemStack itemStack = entityPlayer.getHeldItem();

        World world = TE.getWorldObj();
        
        int x = TE.xCoord;
        int y = TE.yCoord;
        int z = TE.zCoord;
        
        if (itemStack != null) {
            
            boolean hasCover = BlockProperties.hasCover(TE, 6);
            boolean hasOverlay = BlockProperties.hasOverlay(TE, 6);
            boolean soilAreaClicked = side == 1 && hitX > 0.375F && hitX < 0.625F && hitZ > 0.375F && hitZ < 0.625F;
            
            if (FlowerPotProperties.hasSoil(TE)) {
                
                /*
                 * Leaf blocks can be plants or covers.  We need to differentiate
                 * it based on where the block is clicked, and whether it already
                 * has a cover.
                 */
                if (!soilAreaClicked) {
                    if (!hasCover && BlockProperties.isCover(itemStack) || !hasOverlay && BlockProperties.isOverlay(itemStack)) {
                        return;
                    }
                }
                
                if (!FlowerPotProperties.hasPlant(TE)) {
                    
                    if (FlowerPotProperties.isPlant(itemStack)) {

                        int angle = MathHelper.floor_double((entityPlayer.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 15;

                        FlowerPot.setAngle(TE, angle);
                        FlowerPotProperties.setPlant(TE, itemStack);
                        altered.add(decInv.add(true));
                    }
                    
                } else {

                    if (itemStack.getItem().equals(Items.dye) && itemStack.getItemDamage() == 15) {

                        if (!FlowerPot.isEnriched(TE) && FlowerPotProperties.isPlantColorable(TE)) {
                            
                            FlowerPot.setEnrichment(TE, true);
                            altered.add(decInv.add(true));

                            /* Play fertilize sound. */
                            world.playAuxSFX(2005, x, y, z, 0);
                            
                            /* Spawn fertilize effect. */
                            ItemDye.func_150918_a(world, x, y, z, 15);                            
                            
                        }

                    }
                    
                }
                
            } else {
                
                if (FlowerPotProperties.isSoil(itemStack)) {
                    
                    if (hasCover || soilAreaClicked) {
                        FlowerPotProperties.setSoil(TE, itemStack);
                        altered.add(decInv.add(true));
                    }
                    
                }
                
            }
            
        }
    }
    
    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (!world.isRemote && TE != null) {
            
            if (!canPlaceBlockOnSide(world, x, y, z, 1)) {
                dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
            }
            
            /* Eject double tall plant if obstructed. */
            
            if (FlowerPotProperties.hasPlant(TE)) {
                
                Profile profile = FlowerPotHandler.getPlantProfile(TE);
                
                if (profile.equals(Profile.DOUBLEPLANT) || profile.equals(Profile.THIN_DOUBLEPLANT)) {
                    if (world.getBlock(x, y + 1, z).isSideSolid(world, x, y + 1, z, ForgeDirection.DOWN)) {
                        FlowerPotProperties.setPlant(TE, (ItemStack)null);
                    }
                }
                
            }
            
        }
        
        super.onNeighborBlockChange(world, x, y, z, block);
    }
    
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        Block block_YN = world.getBlock(x, y - 1, z);
        
        if (block_YN != null) {
            return block_YN.isSideSolid(world, x, y - 1, z, ForgeDirection.UP) || block_YN.canPlaceTorchOnTop(world, x, y - 1, z);
        } else {
            return false;
        }
    }
    
    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TECarpentersFlowerPot TE = (TECarpentersFlowerPot) world.getTileEntity(x, y, z);
        
        if (FlowerPotProperties.hasPlant(TE)) {
            
            switch (FlowerPotHandler.getPlantProfile(TE)) {
                case CACTUS:
                case LEAVES:
                    setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.99F, 0.6875F);
                    break;
                default:
                    setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.75F, 0.6875F);
            }
            
        } else {
            
            setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.375F, 0.6875F);
            
        }
    }
    
    @Override
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (TE != null && TE instanceof TECarpentersFlowerPot) {
            
            AxisAlignedBB axisAlignedBB = AxisAlignedBB.getAABBPool().getAABB(x + 0.3125F, y, z + 0.3125F, x + 0.6875F, y + 0.375F, z + 0.6875F);
            
            if (FlowerPotProperties.hasPlant(TE)) {
                
                switch (FlowerPotHandler.getPlantProfile(TE)) {
                    case CACTUS:
                    case LEAVES:
                        axisAlignedBB = AxisAlignedBB.getAABBPool().getAABB(x + 0.3125F, y, z + 0.3125F, x + 0.6875F, y + 0.99F, z + 0.6875F);
                        break;
                    default: {}
                }
                
            }
            
            return axisAlignedBB;
            
        }
        
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
    
    @Override
    /**
     * Returns light value based on plant and soil in pot.
     */
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (TE != null && TE instanceof TECarpentersFlowerPot) {
            
            int coverLight = super.getLightValue(world, x, y, z);
            int potLight = getLightValue();
            
            if (FlowerPotProperties.hasSoil(TE)) {
                
                int soil_lightValue = BlockProperties.toBlock(FlowerPotProperties.getSoil(TE)).getLightValue();
                
                if (soil_lightValue > potLight) {
                    potLight = soil_lightValue;
                }
                
            }
            
            if (FlowerPotProperties.hasPlant(TE)) {
                
                int plant_lightValue = FlowerPotProperties.toBlock(FlowerPotProperties.getPlant(TE)).getLightValue();
                
                if (plant_lightValue > potLight) {
                    potLight = plant_lightValue;
                }
                
            }
            
            return coverLight > potLight ? coverLight : potLight;
            
        }
                
        return getLightValue();
    }
    
    @Override
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (TE != null && TE instanceof TECarpentersFlowerPot) {
            
            if (FlowerPotProperties.hasPlant(TE)) {
                
                ItemStack itemStack = FlowerPotProperties.getPlant(TE);
                
                BlockProperties.setHostMetadata(TE, itemStack.getItemDamage());
                FlowerPotProperties.toBlock(itemStack).onEntityCollidedWithBlock(world, x, y, z, entity);
                BlockProperties.resetHostMetadata(TE);
                
            }
            
        }
        
        super.onEntityCollidedWithBlock(world, x, y, z, entity);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns true only if block is flowerPot
     */
    public boolean isFlowerPot()
    {
        return true;
    }
    
    @Override
    protected boolean canCoverBase(TEBase TE, World world, int x, int y, int z)
    {
        return !FlowerPotProperties.hasDesign(TE);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (TE != null && TE instanceof TECarpentersFlowerPot) {
            
            /*
             * Metadata at coordinates are for the base cover only.
             * We need to set it for appropriate attributes in order
             * to get accurate results.
             */
            
            if (FlowerPotProperties.hasPlant(TE)) {
                
                ItemStack itemStack = FlowerPotProperties.getPlant(TE);
                
                BlockProperties.setHostMetadata(TE, itemStack.getItemDamage());
                FlowerPotProperties.toBlock(itemStack).randomDisplayTick(world, x, y, z, random);
                BlockProperties.resetHostMetadata(TE);
                
            }
            
            if (FlowerPotProperties.hasSoil(TE)) {
                
                ItemStack itemStack = FlowerPotProperties.getSoil(TE);
                
                BlockProperties.setHostMetadata(TE, itemStack.getItemDamage());
                BlockProperties.toBlock(itemStack).randomDisplayTick(world, x, y, z, random);
                BlockProperties.resetHostMetadata(TE);
                
            }
            
        }
        
        super.randomDisplayTick(world, x, y, z, random);
    }

    @Override
    /**
     * Ejects contained items into the world, and notifies neighbors of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (TE != null && TE instanceof TECarpentersFlowerPot) {
            if (FlowerPot.isEnriched(TE)) {
                FlowerPot.setEnrichment(TE, false);
            }
            if (FlowerPotProperties.hasPlant(TE)) {
                FlowerPotProperties.setPlant(TE, (ItemStack)null);
            }
            if (FlowerPotProperties.hasSoil(TE)) {
                FlowerPotProperties.setSoil(TE, (ItemStack)null);
            }
        }
        
        super.breakBlock(world, x, y, z, block, metadata);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TECarpentersFlowerPot();
    }
    
    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersFlowerPotRenderID;
    }
    
}
