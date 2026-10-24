package dev.funnyport.mixin;

import dev.funnyport.FunnyportPotato;
import dev.funnyport.FunnyportSettings;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 24w14potato 移植（子集）：<1.21.2 的钩子——LivingEntity#eat。
 * require = 0：1.21.2 起 eat 被消耗品组件取代，缺失时静默跳过。
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    //? if <1.21.2 {
    @Inject(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN"), require = 0)
    private void funnyport$potatoDoomEat(Level level, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        FunnyportPotato.trigger(level, stack, (LivingEntity) (Object) this);
    }
    //?}
}
