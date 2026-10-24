package dev.funnyport.mixin;

import dev.funnyport.FunnyportSettings;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 15w14a（爱与抱抱）移植（子集）：怪物不再把玩家设为目标。
 * 只拦 setTarget(@Nullable LivingEntity) 这一个入口。
 */
@Mixin(Mob.class)
public abstract class MobMixin {
    @Inject(method = "setTarget(Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At("HEAD"), cancellable = true, require = 0)
    private void funnyport$loveNotWar(LivingEntity target, CallbackInfo ci) {
        if (FunnyportSettings.loveAndHugs && target instanceof Player && ((Object) this) instanceof Monster) {
            ci.cancel();
        }
    }
}
