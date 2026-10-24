package dev.funnyport;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FunnyPort —— 把 Minecraft 历代愚人节快照的内容移植到常规版本的 Carpet 扩展。
 *
 * 覆盖：20w14infinite、22w13oneBlockAtATime、Minecraft 2.0、15w14a、23w13a_or_b、24w14potato。
 */
public class Funnyport implements ModInitializer {
    public static final String MODID = "funnyport";
    public static final String VERSION = "1.0";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Override
    public void onInitialize() {
        FunnyportExtension.load();
        UseItemCallback.EVENT.register(FunnyportThrow::onUseItem);
        UseBlockCallback.EVENT.register(FunnyportEvents::onUseBlock);
        AttackBlockCallback.EVENT.register(FunnyportEvents::onAttackBlock);
        LOGGER.info("[funnyport] {} loaded: April Fools ports for regular versions", VERSION);
    }
}
