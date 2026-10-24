package dev.funnyport;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 规则描述翻译。carpet 26.x 按 <modid>.rule.&lt;规则名&gt;.desc 查表；
 * 同时提供 carpet.rule.* 旧格式键，兼容 &lt;1.20.5 的 carpet 汉化显示。
 */
public final class FunnyportTranslations {
    private static final Map<String, String> EN = buildEn();
    private static final Map<String, String> ZH = buildZh();

    private FunnyportTranslations() {}

    private static Map<String, String> buildEn() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("funnyport.category.funnyport", "Carpet Fools Addition");
        map.put("carpet.category.funnyport", "Carpet Fools Addition");
        put(map, "funnyWarp", "Port of 20w14infinite: enables /funnywarp to the built-in funny dimensions");
        put(map, "funnyBookPortals", "Port of 20w14infinite book portals: writing a funny word (e.g. 'creeper') on any page of a book & quill and right-clicking teleports you to that dimension");
        put(map, "throwableBlocks", "Port of 22w13oneBlockAtATime: sneak + right-click throws the block you are holding");
        put(map, "blockGrabbing", "Port of 22w13oneBlockAtATime: sneak + left-click with empty hands picks the targeted block into your hand (true = even unbreakable blocks like bedrock)");
        put(map, "feedingFrenzy", "Port of Minecraft 2.0: any food item counts as breeding food for animals");
        put(map, "redstoneBugs", "Port of Minecraft 2.0 redstone bugs: placing redstone dust has a chance to spawn a 'redstone bug' silverfish");
        put(map, "loveAndHugs", "Port of 15w14a Love and Hugs: monsters stop targeting players");
        put(map, "funnyVote", "Port of 23w13a_or_b: enables /funnyvote chat-button voting");
        put(map, "potatoOfDoom", "Port of 24w14potato: eating a poisonous potato triggers random potato doom");
        put(map, "chickenLoot", "Chickens lay a random item from the whole item registry instead of eggs");
        put(map, "fishingLoot", "Fishing catches a random item from the whole item registry instead of fish");
        return Collections.unmodifiableMap(map);
    }

    private static Map<String, String> buildZh() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("funnyport.category.funnyport", "Carpet Fools Addition");
        map.put("carpet.category.funnyport", "Carpet Fools Addition");
        put(map, "funnyWarp", "移植自 20w14infinite：开启 /funnywarp，传送到内置搞笑维度");
        put(map, "funnyBookPortals", "移植自 20w14infinite 书本传送门：书与笔任意一页写下搞笑词（如 creeper）后右键，直接传进对应维度");
        put(map, "throwableBlocks", "移植自 22w13oneBlockAtATime：潜行 + 右键，把手中的方块抛出去");
        put(map, "blockGrabbing", "移植自 22w13oneBlockAtATime：空手潜行左键，把方块整个拿进手里（开启后连基岩等不可破坏方块也能拿）");
        put(map, "feedingFrenzy", "移植自 Minecraft 2.0：任意食物都能当繁殖食物喂动物");
        put(map, "redstoneBugs", "移植自 Minecraft 2.0 红石虫：放红石粉时有几率刷出一条叫“红石虫”的银鱼虫");
        put(map, "loveAndHugs", "移植自 15w14a 爱与抱抱：怪物不再攻击玩家");
        put(map, "funnyVote", "移植自 23w13a_or_b 投票更新：开启 /funnyvote 聊天栏按钮投票");
        put(map, "potatoOfDoom", "移植自 24w14potato：吃下毒土豆会触发随机土豆天罚");
        put(map, "chickenLoot", "鸡下蛋改为掉落注册表里的随机物品");
        put(map, "fishingLoot", "钓鱼钓上来的是注册表里的随机物品");
        putName(map, "funnyWarp", "愚人节传送");
        putName(map, "funnyBookPortals", "书本传送门");
        putName(map, "throwableBlocks", "抛掷方块");
        putName(map, "blockGrabbing", "徒手捡方块");
        putName(map, "feedingFrenzy", "万物可喂");
        putName(map, "redstoneBugs", "红石虫");
        putName(map, "loveAndHugs", "爱与抱抱");
        putName(map, "funnyVote", "愚人节投票");
        putName(map, "potatoOfDoom", "土豆天罚");
        putName(map, "chickenLoot", "鸡下全部物品");
        putName(map, "fishingLoot", "钓鱼钓全部物品");
        return Collections.unmodifiableMap(map);
    }

    /** 规则名汉化键：&lt;ns&gt;.rule.&lt;名&gt;.name（显示为“中文名 (原标识符)”）。 */
    private static void putName(Map<String, String> map, String rule, String zhName) {
        map.put("funnyport.rule." + rule + ".name", zhName);
        map.put("carpet.rule." + rule + ".name", zhName);
    }

    /** 双键写入：funnyport.rule.*（carpet 26.x）+ carpet.rule.*（旧版 carpet 汉化显示）。 */
    private static void put(Map<String, String> map, String rule, String desc) {
        map.put("funnyport.rule." + rule + ".desc", desc);
        map.put("carpet.rule." + rule + ".desc", desc);
    }

    public static Map<String, String> get(String lang) {
        if (lang == null) return EN;
        return lang.toLowerCase(Locale.ROOT).startsWith("zh") ? ZH : EN;
    }
}
