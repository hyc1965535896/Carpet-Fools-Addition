package dev.funnyport.mixin;

import dev.funnyport.FunnyportDimensions;
import dev.funnyport.FunnyportSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
// 1.21.2 起 vanilla 把 Item#use 的返回值统一成了 InteractionResult
//? if >=1.21.2 {
import net.minecraft.world.InteractionResult;
//?} else {
import net.minecraft.world.InteractionResultHolder;
//?}

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 20w14infinite 书本传送门：在书与笔里写下搞笑词后右键。
 * 注入在 HEAD，命中时直接拦截，原版“打开编辑界面/发包”的逻辑都不会再跑。
 */
@Mixin(WritableBookItem.class)
public abstract class WritableBookItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true, require = 0)
    private void funnyport$bookPortal(Level level, Player player, InteractionHand hand,
                                       //? if >=1.21.2 {
                                       CallbackInfoReturnable<InteractionResult> cir) {
        //?} else {
                                       CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        //?}
        if (!FunnyportSettings.funnyBookPortals) return;

        ItemStack stack = player.getItemInHand(hand);
        String word = FunnyportDimensions.matchFunnyWord(stack).orElse(null);
        if (word == null) return;

        if (level.isClientSide()) {
            // 客户端：吞掉返回值，别让编辑界面弹出来
            //? if >=1.21.2 {
            cir.setReturnValue(InteractionResult.SUCCESS);
            //?} else {
            cir.setReturnValue(InteractionResultHolder.success(stack));
            //?}
            return;
        }
        if (player instanceof ServerPlayer) {
            MinecraftServer server = level.getServer();
            if (FunnyportDimensions.teleport(server, (ServerPlayer) player, word)) {
                //? if >=1.21.2 {
                cir.setReturnValue(InteractionResult.SUCCESS);
                //?} else {
                cir.setReturnValue(InteractionResultHolder.success(stack));
                //?}
            }
        }
    }
}
