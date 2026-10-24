package dev.funnyport.mixin;

import dev.funnyport.FunnyportPotato;
import dev.funnyport.FunnyportSettings;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 24w14potato 移植（子集）：≥1.21.2 的钩子——Item#finishUsingItem（eat 被消耗品组件取代）。
 * require = 0：钩子语义变化时静默跳过。
 */
@Mixin(Item.class)
public abstract class ItemMixin {
    //? if >=1.21.2 {
    @Inject(method = "finishUsingItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN"), require = 0)
    private void funnyport$potatoDoomFinish(ItemStack stack, Level level, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        FunnyportPotato.trigger(level, stack, user);
    }
    //?}
}
