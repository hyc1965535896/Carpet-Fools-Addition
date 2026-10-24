package dev.funnyport;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;

/**
 * 20w14infinite 的 /warp 移植：/funnywarp <dimension> / /funnywarp list
 */
public final class FunnyWarpCommand {
    private FunnyWarpCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("funnywarp")
            // 1.21.11 起权限检查走 PermissionSet
            //? if >=1.21.11 {
            .requires(src -> src.permissions().hasPermission(net.minecraft.server.permissions.Permissions.COMMANDS_MODERATOR))
            //?} else {
            .requires(src -> src.hasPermission(2))
            //?}
            .then(Commands.literal("list").executes(ctx -> {
                doList(ctx.getSource());
                return 1;
            }))
            .then(Commands.argument("dimension", StringArgumentType.word())
                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(FunnyportDimensions.NAMES, builder))
                .executes(ctx -> {
                    warp(ctx.getSource(), getString(ctx, "dimension"));
                    return 1;
                })));
    }

    private static void warp(CommandSourceStack source, String name) {
        if (!FunnyportSettings.funnyWarp) {
            source.sendFailure(FunnyportText.literal("funnyWarp 规则未开启（/funnyport set funnyWarp true）"));
            return;
        }
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(FunnyportText.literal("只有玩家能执行 /funnywarp"));
            return;
        }
        if (!FunnyportDimensions.NAMES.contains(name)) {
            source.sendFailure(FunnyportText.literal("未知维度：" + name + "，可用：" + FunnyportDimensions.NAMES));
            return;
        }
        if (!FunnyportDimensions.teleport(source.getServer(), player, name)) {
            source.sendFailure(FunnyportText.literal("维度 funnyport:" + name + " 没注册成功，检查日志里 worldgen 报错"));
        }
    }

    private static void doList(CommandSourceStack source) {
        MutableComponent msg = FunnyportText.literal("§a搞笑维度：§e" + String.join("§7, §e", FunnyportDimensions.NAMES)
                + "§7（也可直接 /execute in funnyport:<名字>）");
        // 1.20 起 sendSuccess 换成 Supplier
        //? if >=1.20 {
        source.sendSuccess(() -> msg, false);
        //?} else {
        source.sendSuccess(msg, false);
        //?}
    }
}
