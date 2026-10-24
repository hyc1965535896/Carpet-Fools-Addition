package dev.funnyport;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;

/**
 * 23w13a_or_b（Vote Update）移植（子集）：/funnyvote 聊天栏按钮投票。
 * ask 发起、cast 投票、end 开票。
 */
public final class FunnyVoteCommand {
    private FunnyVoteCommand() {}

    private static class VoteState {
        final String question;
        final List<String> options;
        final Map<UUID, Integer> votes = new HashMap<UUID, Integer>();

        VoteState(String question, List<String> options) {
            this.question = question;
            this.options = options;
        }
    }

    private static VoteState current = null;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("funnyvote")
            .then(Commands.literal("ask")
                // 1.21.11 起权限检查走 PermissionSet
                //? if >=1.21.11 {
                .requires(src -> src.permissions().hasPermission(net.minecraft.server.permissions.Permissions.COMMANDS_MODERATOR))
                //?} else {
                .requires(src -> src.hasPermission(2))
                //?}
                .then(Commands.argument("question", StringArgumentType.string())
                    .then(Commands.argument("option1", StringArgumentType.string())
                        .then(Commands.argument("option2", StringArgumentType.string())
                            .executes(ctx -> {
                                return ask(ctx.getSource(), getString(ctx, "question"),
                                        Arrays.asList(getString(ctx, "option1"), getString(ctx, "option2")));
                            })
                            .then(Commands.argument("option3", StringArgumentType.string())
                                .executes(ctx -> {
                                    return ask(ctx.getSource(), getString(ctx, "question"),
                                            Arrays.asList(getString(ctx, "option1"), getString(ctx, "option2"),
                                                    getString(ctx, "option3")));
                                })
                            )
                        )
                    )
                )
            )
            .then(Commands.literal("cast")
                .then(Commands.argument("index", IntegerArgumentType.integer(1))
                    .executes(ctx -> {
                        return cast(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "index"));
                    })
                )
            )
            .then(Commands.literal("end")
                .executes(ctx -> {
                    return end(ctx.getSource());
                })
            )
        );
    }

    private static int ask(CommandSourceStack source, String question, List<String> options) {
        if (current != null) {
            source.sendFailure(FunnyportText.literal("上一场投票还没结束，先 /funnyvote end"));
            return 0;
        }
        current = new VoteState(question, new ArrayList<String>(options));

        MutableComponent msg = FunnyportText.literal("§6[愚人节投票] §e" + question).withStyle(ChatFormatting.BOLD);
        for (int i = 0; i < options.size(); i++) {
            msg.append(FunnyportText.literal("\n"));
            msg.append(optionButton(i + 1));
            msg.append(FunnyportText.literal(" " + options.get(i)));
        }
        msg.append(FunnyportText.literal("\n§7点击按钮或输入 /funnyvote cast <编号>"));
        FunnyportText.broadcast(source.getServer(), msg);
        return 1;
    }

    private static int cast(CommandSourceStack source, int index) {
        if (!FunnyportSettings.funnyVote) {
            source.sendFailure(FunnyportText.literal("funnyVote 规则未开启"));
            return 0;
        }
        if (current == null) {
            source.sendFailure(FunnyportText.literal("当前没有进行中的投票"));
            return 0;
        }
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(FunnyportText.literal("只有玩家能投票"));
            return 0;
        }
        if (index > current.options.size()) {
            source.sendFailure(FunnyportText.literal("没有 " + index + " 号选项"));
            return 0;
        }
        if (current.votes.containsKey(player.getUUID())) {
            source.sendFailure(FunnyportText.literal("你已经投过了，一人一票"));
            return 0;
        }
        current.votes.put(player.getUUID(), index);
        FunnyportText.broadcast(source.getServer(),
                FunnyportText.literal("§7" + player.getName().getString() + " §f投给了 §e" + current.options.get(index - 1)));
        return 1;
    }

    private static int end(CommandSourceStack source) {
        if (current == null) {
            source.sendFailure(FunnyportText.literal("当前没有进行中的投票"));
            return 0;
        }
        MutableComponent msg = FunnyportText.literal("§6[愚人节投票] §e开票：" + current.question);
        int[] tally = new int[current.options.size()];
        for (Integer choice : current.votes.values()) {
            tally[choice - 1]++;
        }
        int best = 0;
        for (int i = 0; i < tally.length; i++) {
            if (tally[i] > tally[best]) best = i;
        }
        for (int i = 0; i < current.options.size(); i++) {
            String mark = (tally[best] > 0 && i == best) ? "§a★ " : "§7  ";
            msg.append(FunnyportText.literal("\n" + mark + current.options.get(i) + " §f- §e" + tally[i] + " 票"));
        }
        msg.append(FunnyportText.literal("\n§7共 " + current.votes.size() + " 人参与，感谢大家理性的声音"));
        FunnyportText.broadcast(source.getServer(), msg);
        current = null;
        return 1;
    }

    private static MutableComponent optionButton(int index) {
        MutableComponent button = FunnyportText.literal("[§n投 " + index + "§r]");
        // 1.21.5 把 ClickEvent 改成了密封记录体系
        //? if >=1.21.5 {
        button = button.withStyle(style -> style.withClickEvent(new ClickEvent.RunCommand("/funnyvote cast " + index)));
        //?} else {
        button = button.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/funnyvote cast " + index)));
        //?}
        return button;
    }
}
