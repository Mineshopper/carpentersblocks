package com.carpentersblocks.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import com.carpentersblocks.block.BlockCarpentersSlope;

public class PacketSlopeSelect implements ICarpentersPacket {

    private boolean incDamage = false;

    public PacketSlopeSelect() {}

    public PacketSlopeSelect(boolean incDamage)
    {
        this.incDamage = incDamage;
    }

    @Override
    public void processData(EntityPlayer entityPlayer, ByteBufInputStream bbis) throws IOException
    {
        ItemStack itemStack = entityPlayer.getHeldItem();

        if (itemStack != null) {

            boolean incDmg = bbis.readBoolean();
            int maxDmg = BlockCarpentersSlope.slopeType.length - 1;
            int itemDmg = itemStack.getItemDamage();
            itemDmg += incDmg ? 1 : -1;

            if (itemDmg > maxDmg) {
                itemDmg = 0;
            } else if (itemDmg < 0) {
                itemDmg = maxDmg;
            }

            itemStack.setItemDamage(itemDmg);

        }
    }

    @Override
    public void appendData(ByteBuf buffer) throws IOException
    {
        buffer.writeBoolean(incDamage);
    }

}
