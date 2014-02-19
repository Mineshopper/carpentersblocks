package carpentersblocks.util.flowerpot;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import carpentersblocks.data.FlowerPot;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersFlowerPot;
import carpentersblocks.util.BlockProperties;

public class FlowerPotProperties {
    
    /**
     * Ejects an item at given coordinates.
     */
    public static void ejectEntity(TEBase TE, ItemStack itemStack)
    {
        BlockProperties.ejectEntity(TE, FlowerPotHandler.getFilteredItem(itemStack));
    }
    
    /**
     * Returns whether flower pot has a design.
     */
    public final static boolean hasDesign(TEBase TE)
    {
        return FlowerPot.getDesign(TE) > 0;
    }
    
    /**
     * Returns soil block metadata.
     */
    public static int getSoilMetadata(TEBase TE)
    {
        return ((TECarpentersFlowerPot)TE).soil.getItemDamage();
    }
    
    /**
     * Returns plant block metadata.
     */
    public static int getPlantMetadata(TEBase TE)
    {
        return ((TECarpentersFlowerPot)TE).plant.getItemDamage();
    }
    
    /**
     * Returns soil block.
     */
    public final static Block getSoil(TEBase TE)
    {
        return Block.getBlockFromItem(((TECarpentersFlowerPot)TE).soil.getItem());
    }
    
    /**
     * Returns plant block.
     */
    public final static Block getPlant(TEBase TE)
    {
        return Block.getBlockFromItem(((TECarpentersFlowerPot)TE).plant.getItem());
    }
    
    /**
     * Returns whether pot has soil.
     */
    public final static boolean hasSoil(TEBase TE)
    {
        ItemStack itemStack = ((TECarpentersFlowerPot)TE).soil;
        
        return itemStack != null && isSoil(itemStack);
    }
    
    /**
     * Returns whether pot has plant.
     */
    public static boolean hasPlant(TEBase TE)
    {
        ItemStack itemStack = ((TECarpentersFlowerPot)TE).plant;
        
        return itemStack != null && isPlant(itemStack);
    }
    
    /**
     * Returns whether ItemStack contains soil.
     */
    public static boolean isSoil(ItemStack itemStack)
    {
        Block block = Block.getBlockFromItem(itemStack.getItem());
        
        if (itemStack.getItem() instanceof ItemBlock && !block.hasTileEntity(itemStack.getItemDamage())) {
            Material material = block.getMaterial();
            return material.equals(Material.grass) || material.equals(Material.ground) || material.equals(Material.sand);
        } else {
            return false;
        }
    }
    
    /**
     * Returns whether ItemStack contains a plant.
     */
    public static boolean isPlant(ItemStack itemStack)
    {
        itemStack = FlowerPotHandler.getEquivalentBlock(itemStack);
        
        Block block = Block.getBlockFromItem(itemStack.getItem());
        
        if (itemStack.getItem() instanceof ItemBlock && !block.hasTileEntity(itemStack.getItemDamage())) {
            return block instanceof IPlantable || block instanceof IShearable;
        } else {
            return false;
        }
    }
    
    /**
     * Sets soil block.
     */
    public static boolean setSoil(TEBase TE, ItemStack itemStack)
    {
        if (hasSoil(TE)) {
            ejectEntity(TE, new ItemStack(getSoil(TE), 1, getSoilMetadata(TE)));
        }

        ((TECarpentersFlowerPot)TE).soil = itemStack;
        
        World world = TE.getWorldObj();
        
        world.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, Block.getBlockFromItem(itemStack.getItem()));
        world.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
        
        return true;
    }
    
    /**
     * Sets plant block.
     */
    public static boolean setPlant(TEBase TE, ItemStack itemStack)
    {
        if (hasPlant(TE)) {
            ejectEntity(TE, new ItemStack(getPlant(TE), 1, getPlantMetadata(TE)));
        }
        
        itemStack = FlowerPotHandler.getEquivalentBlock(itemStack);

        ((TECarpentersFlowerPot)TE).plant = itemStack;
        
        World world = TE.getWorldObj();
        
        world.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, Block.getBlockFromItem(itemStack.getItem()));
        world.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
        
        return true;
    }
    
}
