package dev.funnyport.mixin;

import dev.funnyport.FunnyportLoot;
import dev.funnyport.FunnyportSettings;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BiConsumer;

/**
 * “鸡下全部物品”（1.21.2-1.21.10）：gift loot table 机制，声明类在 animal.Chicken。
 * require = 0：调用点结构对不上时静默跳过。
 */
//? if >=1.21.2 && <1.21.11 {
@Mixin(net.minecraft.world.entity.animal.Chicken.class)
public abstract class ChickenMixinLegacy {
    @Redirect(method = "aiStep",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/Chicken;dropFromGiftLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Ljava/util/function/BiConsumer;)Z"),
            require = 0)
    private boolean funnyport$chickenLoot(net.minecraft.world.entity.animal.Chicken chicken, ServerLevel level, ResourceKey<LootTable> key, BiConsumer consumer) {
        if (FunnyportSettings.chickenLoot) {
            // 规则开：自己掉随机物品（consumer 的参数顺序各版本存疑，不依赖它）
            java.util.function.BiConsumer spawn = (lvl, stack) -> {
                net.minecraft.world.entity.item.ItemEntity drop = new net.minecraft.world.entity.item.ItemEntity(
                        level, chicken.getX(), chicken.getY() + 0.5, chicken.getZ(), (ItemStack) stack);
                level.addFreshEntity(drop);
            };
            chicken.playSound(net.minecraft.sounds.SoundEvents.CHICKEN_EGG, 1.0f, 1.0f);
            spawn.accept(level, FunnyportLoot.randomStack());
            return true;
        }
        return chicken.dropFromGiftLootTable(level, key, consumer);
    }
}
//?}
