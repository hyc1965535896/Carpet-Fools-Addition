package dev.funnyport;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
// fabric-api 的 UseItemCallback：1.20.5-1.21.1 返回 InteractionResultHolder，其余版本返回 InteractionResult
import net.minecraft.world.item.ItemStack;
//? if >=1.21.2 {
import net.minecraft.world.InteractionResult;
//?} else {
import net.minecraft.world.InteractionResultHolder;
//?}

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * 22w13oneBlockAtATime 移植（子集）：潜行 + 右键，把手中方块整个抛出去。
 */
public final class FunnyportThrow {
    private FunnyportThrow() {}

    //? if >=1.21.2 {
    public static InteractionResult onUseItem(Player player, Level level, InteractionHand hand) {
    //?} else {
    public static InteractionResultHolder<ItemStack> onUseItem(Player player, Level level, InteractionHand hand) {
    //?}
        ItemStack stack = player.getItemInHand(hand);
        // 只在服务端处理，避免两端不一致
        if (!FunnyportSettings.throwableBlocks || level.isClientSide() || !(player instanceof ServerPlayer)
                || !player.isShiftKeyDown() || stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) {
            //? if >=1.21.2 {
            return InteractionResult.PASS;
            //?} else {
            return InteractionResultHolder.pass(stack);
            //?}
        }

        ItemStack thrown = stack.split(1);
        Vec3 look = player.getLookAngle();
        ItemEntity entity = new ItemEntity(level, player.getX(), player.getEyeY() - 0.3, player.getZ(), thrown);
        entity.setPickUpDelay(20);
        entity.setDeltaMovement(look.x, look.y + 0.3, look.z);
        level.addFreshEntity(entity);

        level.playSound(null, player.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.6f, 0.4f);
        //? if >=26.3 {
        player.swing(hand, net.minecraft.world.item.component.SwingAnimation.DEFAULT, true);
        //?} else {
        player.swing(hand);
        //?}
        //? if >=1.21.2 {
        return InteractionResult.SUCCESS;
        //?} else {
        return InteractionResultHolder.success(stack);
        //?}
    }
}
