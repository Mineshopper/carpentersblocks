package carpentersblocks.tileentity;

import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import carpentersblocks.data.FlowerPot;

public class TECarpentersFlowerPot extends TEBase {

    private final String TAG_PLANT_ITEMSTACKS = "pot_property";
    public final String TAG_SOIL  = "soil";
    public final String TAG_PLANT = "plant";

    public ItemStack soil;
    public ItemStack plant;

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        NBTTagList list = nbt.getTagList(TAG_PLANT_ITEMSTACKS, 10);

        soil = null;
        plant = null;

        for (int idx = 0; idx < list.tagCount(); ++idx)
        {
            if ((list.getCompoundTagAt(idx)).hasKey(TAG_SOIL)) {
                soil = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(idx));
            } else if ((list.getCompoundTagAt(idx)).hasKey(TAG_PLANT)) {
                plant = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(idx));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        NBTTagList list = new NBTTagList();

        if (soil != null) {
            NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setByte(TAG_SOIL, (byte) 0);
            soil.writeToNBT(nbt1);
            list.appendTag(nbt1);
        }
        if (plant != null) {
            NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setByte(TAG_PLANT, (byte) 0);
            plant.writeToNBT(nbt1);
            list.appendTag(nbt1);
        }

        nbt.setTag(TAG_PLANT_ITEMSTACKS, list);
    }

    @Override
    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        World world = getWorldObj();

        if (world.isRemote) {

            boolean wasEnriched = FlowerPot.isEnriched(this);

            super.onDataPacket(net, pkt);

            /*
             * The server doesn't send particle spawn packets, so it
             * has to be handled client-side.
             *
             * This spawns the fertilization effect as seen when growing
             * saplings or crops with bonemeal.
             */

            if (!wasEnriched && FlowerPot.isEnriched(this)) {
                ItemDye.func_150918_a(world, xCoord, yCoord, zCoord, 15);
            }

        }
    }

}
