package com.carpentersblocks.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import com.carpentersblocks.util.EntityLivingUtil;

public class PacketActivateBlock extends TilePacket {

    private int side;

    public PacketActivateBlock() {}

    public PacketActivateBlock(int x, int y, int z, int side)
    {
        super(x, y, z);
        this.side = side;
    }

    @Override
    public void processData(EntityPlayer entityPlayer, ByteBufInputStream bbis) throws IOException
    {
        super.processData(entityPlayer, bbis);

        ItemStack itemStack = entityPlayer.getHeldItem();
        side = bbis.readInt();

        boolean result = entityPlayer.worldObj.getBlock(x, y, z).onBlockActivated(entityPlayer.worldObj, x, y, z, entityPlayer, side, 1.0F, 1.0F, 1.0F);

        if (!result) {
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                itemStack.tryPlaceItemIntoWorld(entityPlayer, entityPlayer.worldObj, x, y, z, side, 1.0F, 1.0F, 1.0F);
                EntityLivingUtil.decrementCurrentSlot(entityPlayer);
            }
        }
    }

    @Override
    public void appendData(ByteBuf buffer) throws IOException
    {
        super.appendData(buffer);
        buffer.writeInt(side);
    }

}
