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
     * Returns whether flower pot has a design.
     */
    public final static boolean hasDesign(TEBase TE)
    {
        return FlowerPot.getDesign(TE) > 0;
    }
    
    /**
     * Returns soil ItemStack.
     */
    public final static ItemStack getSoil(TEBase TE)
    {
        return ((TECarpentersFlowerPot)TE).soil;
    }
    
    /**
     * Returns plant ItemStack.
     */
    public final static ItemStack getPlant(TEBase TE)
    {
        return ((TECarpentersFlowerPot)TE).plant;
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
            BlockProperties.ejectEntity(TE, ((TECarpentersFlowerPot)TE).soil);
        }

        ((TECarpentersFlowerPot)TE).soil = itemStack;
        
        World world = TE.getWorldObj();
        Block block = itemStack == null ? null : Block.getBlockFromItem(itemStack.getItem());
        
        world.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, block);
        world.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
        
        return true;
    }
    
    /**
     * Sets plant block.
     */
    public static boolean setPlant(TEBase TE, ItemStack itemStack)
    {
        if (hasPlant(TE)) {
            BlockProperties.ejectEntity(TE, ((TECarpentersFlowerPot)TE).plant);
        }
        
        if (itemStack != null) {
            System.out.println("DEBUG: plant name = " + itemStack.getUnlocalizedName());
        }

        ((TECarpentersFlowerPot)TE).plant = itemStack;
        
        World world = TE.getWorldObj();
        Block block = itemStack == null ? null : Block.getBlockFromItem(itemStack.getItem());
        
        world.notifyBlocksOfNeighborChange(TE.xCoord, TE.yCoord, TE.zCoord, block);
        world.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
        
        return true;
    }
    
}
