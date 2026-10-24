package dev.funnyport;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

import java.util.Map;

/**
 * Carpet 扩展入口。规则解析进 carpet 主管理器（CarpetServer.settingsManager，
 * 即 legacy 管理器）：规则在 /carpet 下管理，分类 funnyport 显示为 Carpet Fools Addition。
 * 中文显示：/carpet language zh_cn（描述走 canHasTranslations 的 carpet.rule.* 键）。
 */
public class FunnyportExtension implements CarpetExtension {
    public static final FunnyportExtension INSTANCE = new FunnyportExtension();

    public static void load() {
        CarpetServer.manageExtension(INSTANCE);
    }

    @Override
    public void onGameStarted() {
        // 挂进 carpet 主管理器（legacy 管理器，接受带 desc 的 legacy 注解）
        CarpetServer.settingsManager.parseSettingsClass(FunnyportSettings.class);
    }

    //? if >=1.20.5 {
    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, net.minecraft.commands.CommandBuildContext buildContext) {
        FunnyWarpCommand.register(dispatcher);
        FunnyVoteCommand.register(dispatcher);
    }
    //?} else {
    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        FunnyWarpCommand.register(dispatcher);
        FunnyVoteCommand.register(dispatcher);
    }
    //?}

    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        Funnyport.LOGGER.info("[funnyport] funny dimensions online: {}", FunnyportDimensions.availableOn(server));
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return FunnyportTranslations.get(lang);
    }
}
