package com.carpentersblocks.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class PacketActivateBlock extends TilePacket {

    private int side;

    public PacketActivateBlock() {}

    public PacketActivateBlock(int x, int y, int z, int side)
    {
        super(x, y, z);
        this.side = side;
    }

    @Override
    public void processData(EntityPlayer entityPlayer, DataInputStream bbis) throws IOException
    {
        super.processData(entityPlayer, bbis);

        ItemStack itemStack = entityPlayer.getHeldItem();
        side = bbis.readInt();

        Block block = Block.blocksList[entityPlayer.worldObj.getBlockId(x, y, z)];

        if (block != null) {

            boolean result = block.onBlockActivated(entityPlayer.worldObj, x, y, z, entityPlayer, side, 1.0F, 1.0F, 1.0F);

            if (!result) {
                if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                    itemStack.tryPlaceItemIntoWorld(entityPlayer, entityPlayer.worldObj, x, y, z, side, 1.0F, 1.0F, 1.0F);
                    if (!entityPlayer.capabilities.isCreativeMode && --itemStack.stackSize <= 0) {
                        entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, (ItemStack)null);
                    }
                }
            }

        }
    }

    @Override
    public void appendData(DataOutputStream outputStream) throws IOException
    {
        super.appendData(outputStream);
        outputStream.writeInt(side);
    }

}
