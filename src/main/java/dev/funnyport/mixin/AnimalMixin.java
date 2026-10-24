package dev.funnyport.mixin;

import dev.funnyport.FunnyportSettings;
// Animal 全版本都在 entity.animal 包（1.21.11 只有鸡等子类挪包）
import net.minecraft.world.entity.animal.Animal;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Minecraft 2.0 移植：feedingFrenzy 开启时，任何非空物品都算繁殖食物。
 *
 * 注意：Animal#isFood 在 1.20.5+ 是 abstract（1.16.5 是具体方法），@Inject 方法本体在
 * abstract 方法上会 NPE 崩服（26.2 实测）。因此改为重定向 mobInteract 里对 isFood 的
 * 调用点——调用点全版本都在具体方法里。只覆盖 Animal#mobInteract 这条喂食入口，
 * 子类自带的特殊进食逻辑（如熊猫吃竹子）不受影响。
 * require = 0：调用点结构变化时静默跳过，不炸服。
 */
@Mixin(Animal.class)
public abstract class AnimalMixin {
    @Redirect(method = "mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/Animal;isFood(Lnet/minecraft/world/item/ItemStack;)Z"),
            require = 0)
    private boolean funnyport$feedingFrenzy(Animal animal, ItemStack stack) {
        if (FunnyportSettings.feedingFrenzy && !stack.isEmpty()) {
            return true;
        }
        return animal.isFood(stack);
    }
}
