package dev.funnyport;

// 全版本统一使用 legacy 的 carpet.settings.Rule（desc 内联）：
// 规则解析进 carpet 主管理器（CarpetServer.settingsManager，即 legacy 管理器），
// legacy 管理器直接取注解 desc 不查翻译表，26.2 服务端实测可用（carpet 自己的规则同款）。
// 全部规则默认关闭（false），按需在 /carpet 里开启。
import carpet.settings.Rule;

/**
 * 规则定义（全部挂在 funnyport 分类下，/carpet 里管理）。
 */
public class FunnyportSettings {
    @Rule(desc = "Port of 20w14infinite: enables /funnywarp to the built-in funny dimensions", category = {"funnyport"})
    public static boolean funnyWarp = false;

    @Rule(desc = "Port of 20w14infinite book portals: writing a funny word on a page of a book & quill and right-clicking teleports you", category = {"funnyport"})
    public static boolean funnyBookPortals = false;

    @Rule(desc = "Port of 22w13oneBlockAtATime: sneak + right-click throws the block you are holding", category = {"funnyport"})
    public static boolean throwableBlocks = false;

    @Rule(desc = "Port of 22w13oneBlockAtATime: sneak + left-click with empty hands picks the targeted block into your hand (true = even unbreakable blocks like bedrock)", category = {"funnyport"})
    public static boolean blockGrabbing = false;

    @Rule(desc = "Port of Minecraft 2.0: any food item counts as breeding food for animals", category = {"funnyport"})
    public static boolean feedingFrenzy = false;

    @Rule(desc = "Port of Minecraft 2.0 redstone bugs: placing redstone dust has a chance to spawn a 'redstone bug' silverfish", category = {"funnyport"})
    public static boolean redstoneBugs = false;

    @Rule(desc = "Port of 15w14a Love and Hugs: monsters stop targeting players", category = {"funnyport"})
    public static boolean loveAndHugs = false;

    @Rule(desc = "Port of 23w13a_or_b: enables /funnyvote chat-button voting", category = {"funnyport"})
    public static boolean funnyVote = false;

    @Rule(desc = "Port of 24w14potato: eating a poisonous potato triggers random potato doom", category = {"funnyport"})
    public static boolean potatoOfDoom = false;

    @Rule(desc = "Chickens lay a random item from the whole item registry instead of eggs", category = {"funnyport"})
    public static boolean chickenLoot = false;

    @Rule(desc = "Fishing catches a random item from the whole item registry instead of fish", category = {"funnyport"})
    public static boolean fishingLoot = false;
}
