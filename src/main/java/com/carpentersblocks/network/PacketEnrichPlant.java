package com.carpentersblocks.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.EntityLivingUtil;

public class PacketEnrichPlant extends TilePacket {

    private int hexColor;

    public PacketEnrichPlant() {}

    /**
     * For the server to examine plant color, since it's a client-side only property.
     */
    public PacketEnrichPlant(int x, int y, int z, int hexColor)
    {
        super(x, y, z);
        this.hexColor = hexColor;
    }

    @Override
    public void processData(EntityPlayer entityPlayer, ByteBufInputStream bbis) throws IOException
    {
        super.processData(entityPlayer, bbis);
        World world = entityPlayer.worldObj;
        hexColor = bbis.readInt();

        TEBase TE = (TEBase) world.getTileEntity(x, y, z);

        if (TE != null) {
            if (hexColor != 16777215 && !TE.hasAttribute(TE.ATTR_FERTILIZER)) {
                TE.addAttribute(TE.ATTR_FERTILIZER, new ItemStack(Items.dye, 1, 15));
                EntityLivingUtil.decrementCurrentSlot(entityPlayer);
            }
        }
    }

    @Override
    public void appendData(ByteBuf buffer) throws IOException
    {
        super.appendData(buffer);
        buffer.writeInt(hexColor);
    }

}
