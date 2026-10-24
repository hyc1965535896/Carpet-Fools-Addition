package dev.funnyport;

// 文本 API：1.19.2 起 mojmap 提供 Component.literal 工厂；更老版本用 TextComponent
//? if >=1.19.2 {
import net.minecraft.network.chat.Component;
//?} else {
import net.minecraft.network.chat.TextComponent;
//?}

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

/**
 * 跨版本文本与消息工具：统一 literal 构造、私聊、广播入口。
 */
public final class FunnyportText {
    private FunnyportText() {}

    public static MutableComponent literal(String text) {
        //? if >=1.19.2 {
        return Component.literal(text);
        //?} else {
        return new TextComponent(text);
        //?}
    }

    public static void sendToPlayer(ServerPlayer player, MutableComponent msg) {
        // 1.19.2 起 ServerPlayer#sendSystemMessage
        //? if >=1.19.2 {
        player.sendSystemMessage(msg);
        //?} else {
        player.sendMessage(msg, player.getUUID());
        //?}
    }

    public static void broadcast(MinecraftServer server, MutableComponent msg) {
        // 1.19.2 起 PlayerList#broadcastSystemMessage
        //? if >=1.19.2 {
        server.getPlayerList().broadcastSystemMessage(msg, false);
        //?} else {
        server.getPlayerList().broadcastMessage(msg, ChatType.SYSTEM, new UUID(0L, 0L));
        //?}
    }
}
