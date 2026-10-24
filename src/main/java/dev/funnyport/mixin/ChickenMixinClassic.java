package dev.funnyport.mixin;

import dev.funnyport.FunnyportLoot;
import dev.funnyport.FunnyportSettings;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * “鸡下全部物品”（&lt;1.21.2）：旧式 spawnAtLocation(ItemLike) 掉蛋调用。
 * require = 0：调用点结构对不上时静默跳过。
 */
//? if <1.21.2 {
@Mixin(net.minecraft.world.entity.animal.Chicken.class)
public abstract class ChickenMixinClassic {
    @Redirect(method = "aiStep",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"),
            require = 0)
    private ItemEntity funnyport$chickenLoot(net.minecraft.world.entity.animal.Chicken chicken, ItemLike item) {
        return chicken.spawnAtLocation(FunnyportSettings.chickenLoot ? FunnyportLoot.randomStack() : new ItemStack(item.asItem()));
    }
}
//?}
