package dev.funnyport;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 1.19.3 起 BuiltInRegistries 才存在；更老版本用 Registry 的静态字段
//? if >=1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
import net.minecraft.core.Registry;
//?}

/**
 * “全部物品”随机战利品池：遍历物品注册表取任意非空气物品。
 * 用自己的 java.util.Random，规避各版本 RandomSource 差异。
 */
public final class FunnyportLoot {
    private static final Random RANDOM = new Random();
    private static List<Item> POOL;

    private FunnyportLoot() {}

    public static ItemStack randomStack() {
        if (POOL == null) {
            List<Item> items = new ArrayList<>();
            Iterable<Item> registry;
            //? if >=1.19.3 {
            registry = BuiltInRegistries.ITEM;
            //?} else {
            registry = Registry.ITEM;
            //?}
            for (Item item : registry) {
                if (item != Items.AIR) items.add(item);
            }
            POOL = items;
        }
        return new ItemStack(POOL.get(RANDOM.nextInt(POOL.size())));
    }
}
