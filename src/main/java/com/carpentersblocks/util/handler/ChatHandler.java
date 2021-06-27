package com.carpentersblocks.util.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ChatHandler {

    /**
     * Sends a chat message to a player.
     */
    public static void sendMessageToPlayer(String string, PlayerEntity playerEntity) {
        sendMessageToPlayer(string, playerEntity, true);
    }

    /**
     * Sends a chat message to a player with style override.
     */
    public static void sendMessageToPlayer(String string, PlayerEntity playerEntity, boolean styled) {
        TranslationTextComponent chat = new TranslationTextComponent(string, new Object[0]);
        if (styled) {
            chat.getStyle().withColor(Color.fromLegacyFormat(TextFormatting.GOLD));
        }
        playerEntity.sendMessage(chat, Util.NIL_UUID);
    }

}
