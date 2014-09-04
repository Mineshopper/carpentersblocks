package com.carpentersblocks.util.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ChatHandler {

    /**
     * Sends a chat message to a player.
     */
    public static void sendMessageToPlayer(String string, EntityPlayer entityPlayer)
    {
        sendMessageToPlayer(string, entityPlayer, true);
    }

    /**
     * Sends a chat message to a player with style override.
     */
    public static void sendMessageToPlayer(String string, EntityPlayer entityPlayer, boolean styled)
    {
        ChatComponentTranslation chat = new ChatComponentTranslation(string, new Object[0]);

        if (styled) {
            chat.getChatStyle().setColor(EnumChatFormatting.GOLD);
        }

        entityPlayer.addChatMessage(chat);
    }

    /**
     * Gets full unlocalized name for ItemStack (e.g. tile.dirt.name)
     *
     * @param  itemStack the ItemStack
     * @return the full unlocalized string
     */
    public static String getUnlocalizedNameEfficiently(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getItem() != null) {
            return itemStack.getItem().getUnlocalizedNameInefficiently(itemStack) + ".name";
        }

        return "";
    }

    /**
     * Gets default language display name for ItemStack (using en_US locale).
     *
     * @param  itemStack the ItemStack
     * @return the display name
     */
    public static String getDefaultTranslation(ItemStack itemStack)
    {
        // Translate using FML's language registry
        String name = LanguageRegistry.instance().getStringLocalization(getUnlocalizedNameEfficiently(itemStack), "en_US");
        if (name.equals("")) {
            // Translate using Minecraft's language registry
            name = StatCollector.translateToFallback(getUnlocalizedNameEfficiently(itemStack));
        }

        return name;
    }

}
