package dev.funnyport.mixin;

import dev.funnyport.FunnyportLoot;
import dev.funnyport.FunnyportSettings;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// 1.20 起 loot 上下文换成 LootParams 且返回 fastutil 表；更老版本是 LootContext + java.util.List
//? if >=1.20 {
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.storage.loot.LootParams;
//?} else {
import java.util.List;
import net.minecraft.world.level.storage.loot.LootContext;
//?}

/**
 * “钓鱼钓出全部物品”：收杆结算时，把战利品整体替换为注册表里的随机物品。
 * require = 0：结构对不上时静默跳过。
 */
@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    //? if >=1.20 {
    @Redirect(method = "retrieve(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"),
            require = 0)
    private ObjectArrayList<ItemStack> funnyport$fishingLoot(LootTable table, LootParams params) {
        ObjectArrayList<ItemStack> loot = table.getRandomItems(params);
        if (FunnyportSettings.fishingLoot) {
            loot.clear();
            loot.add(FunnyportLoot.randomStack());
        }
        return loot;
    }
    //?} else {
    @Redirect(method = "retrieve(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/List;"),
            require = 0)
    private List<ItemStack> funnyport$fishingLoot(LootTable table, LootContext context) {
        List<ItemStack> loot = table.getRandomItems(context);
        if (FunnyportSettings.fishingLoot) {
            loot.clear();
            loot.add(FunnyportLoot.randomStack());
        }
        return loot;
    }
    //?}
}
