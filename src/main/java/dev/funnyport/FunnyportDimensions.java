package dev.funnyport;

//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.WritableBookContent;
//?} else {
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
//?}

import net.minecraft.core.BlockPos;
// Registries 类是 1.19.3 加进 mojmap 的；更老版本用 Registry.DIMENSION_REGISTRY
//? if >=1.19.3 {
import net.minecraft.core.registries.Registries;
//?} else {
import net.minecraft.core.Registry;
//?}

// 1.21 系列把 ResourceLocation 收成私有构造 + 工厂；1.21.11 干脆改名 Identifier
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
import net.minecraft.resources.ResourceLocation;
//?}

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
// ChunkStatus 在 1.20.5 挪进了 .status 子包
//? if >=1.20.5 {
import net.minecraft.world.level.chunk.status.ChunkStatus;
//?} else {
import net.minecraft.world.level.chunk.ChunkStatus;
//?}

// 1.21.2 起 teleportTo 要带 Relative 集合
//? if >=1.21.2 {
import net.minecraft.world.entity.Relative;
//?}

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * 20w14infinite 移植核心：固定“搞笑维度”清单、书页词语匹配、跨维度传送。
 * 维度本体由 jar 内置数据包注册：data/funnyport/dimension/*.json
 */
public final class FunnyportDimensions {
    public static final List<String> NAMES = Collections.unmodifiableList(Arrays.asList(
            "boxes", "creeper", "void", "islands", "floodgate", "unicorns", "dark", "ocean"));

    private FunnyportDimensions() {}

    public static ResourceKey<Level> key(String name) {
        //? if >=1.21.11 {
        Identifier id = Identifier.fromNamespaceAndPath(Funnyport.MODID, name);
        //?} else {
        //? if >=1.21 {
        net.minecraft.resources.ResourceLocation id = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Funnyport.MODID, name);
        //?} else {
        net.minecraft.resources.ResourceLocation id = new net.minecraft.resources.ResourceLocation(Funnyport.MODID, name);
        //?}
        //?}
        //? if >=1.19.3 {
        return ResourceKey.create(Registries.DIMENSION, id);
        //?} else {
        return ResourceKey.create(Registry.DIMENSION_REGISTRY, id);
        //?}
    }

    /** 服务器启动后检查哪些搞笑维度真的注册出来了（数据包加载失败时会缺）。 */
    public static List<String> availableOn(MinecraftServer server) {
        List<String> found = new ArrayList<String>();
        for (String name : NAMES) {
            if (server.getLevel(key(name)) != null) found.add(name);
        }
        return found;
    }

    public static boolean teleport(MinecraftServer server, ServerPlayer player, String name) {
        if (server == null) return false;
        ServerLevel target = server.getLevel(key(name));
        if (target == null) return false;

        if ("void".equals(name)) {
            // 虚空世界没有落脚点，先铺一块黑曜石平台（和原版末地平台同款待遇）
            buildPlatform(target);
            //? if >=1.21.2 {
            player.teleportTo(target, 0.5, 65.0, 0.5, java.util.Collections.<Relative>emptySet(),
                    yaw(player), pitch(player), false);
            //?} else {
            player.teleportTo(target, 0.5, 65.0, 0.5, yaw(player), pitch(player));
            //?}
        } else {
            // 原地切维度，落到脚下的最高方块上
            int x = (int) Math.floor(player.getX());
            int z = (int) Math.floor(player.getZ());
            target.getChunk(x >> 4, z >> 4, ChunkStatus.FULL, true);
            int top = target.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            //? if >=1.21.2 {
            player.teleportTo(target, player.getX(), top + 1.0, player.getZ(), java.util.Collections.<Relative>emptySet(),
                    yaw(player), pitch(player), false);
            //?} else {
            player.teleportTo(target, player.getX(), top + 1.0, player.getZ(), yaw(player), pitch(player));
            //?}
        }
        FunnyportText.sendToPlayer(player, FunnyportText.literal("§aCarpet Fools §7» §f已进入愚人节维度 §e" + name));
        return true;
    }

    // 1.16.5 的 mojmap 里朝向是公开字段 yRot/xRot，1.17 起才有 getYRot()/getXRot()
    private static float yaw(ServerPlayer player) {
        //? if >=1.17 {
        return player.getYRot();
        //?} else {
        return player.yRot;
        //?}
    }

    private static float pitch(ServerPlayer player) {
        //? if >=1.17 {
        return player.getXRot();
        //?} else {
        return player.xRot;
        //?}
    }

    private static void buildPlatform(ServerLevel level) {        BlockPos center = new BlockPos(0, 64, 0);
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                BlockPos pos = center.offset(dx, 0, dz);
                if (level.getBlockState(pos).isAir()) {
                    level.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3);
                }
            }
        }
    }

    public static Optional<String> matchFunnyWord(String text) {
        String word = normalize(text);
        if (word.isEmpty()) return Optional.empty();
        return NAMES.contains(word) ? Optional.of(word) : Optional.empty();
    }

    /** 扫书页：书与笔的任意一页写了“搞笑词”就算命中（原版 20w14∞ 的玩法雏形）。 */
    public static Optional<String> matchFunnyWord(ItemStack stack) {
        //? if >=1.20.5 {
        WritableBookContent content = stack.get(DataComponents.WRITABLE_BOOK_CONTENT);
        if (content == null) return Optional.empty();
        List<String> pages = content.getPages(false).toList();
        for (String page : pages) {
            Optional<String> match = matchFunnyWord(page);
            if (match.isPresent()) return match;
        }
        return Optional.empty();
        //?} else {
        CompoundTag tag = stack.getTag();
        // 1.16.5 的 nbt.Tag 接口还没有 TAG_LIST/TAG_STRING 常量，直接用类型 id（9=LIST，8=STRING）
        if (tag == null || !tag.contains("pages", 9)) return Optional.empty();
        ListTag pages = tag.getList("pages", 8);
        for (int i = 0; i < pages.size(); i++) {
            Optional<String> match = matchFunnyWord(pages.getString(i));
            if (match.isPresent()) return match;
        }
        return Optional.empty();
        //?}
    }

    private static String normalize(String text) {
        if (text == null) return "";
        return text.replaceAll("§.", "").trim().toLowerCase(Locale.ROOT);
    }
}
