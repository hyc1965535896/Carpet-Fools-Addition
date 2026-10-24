package dev.funnyport;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * 事件侧的移植：
 *  - onUseBlock：Minecraft 2.0 的“红石虫”——放红石粉时有几率刷出银鱼虫
 *  - onAttackBlock：22w13oneBlockAtATime 的“捡方块”——空手潜行左键把方块整个拿进手
 *
 * UseBlockCallback / AttackBlockCallback 在全版本都返回 InteractionResult，无版本分叉。
 */
public final class FunnyportEvents {
    private FunnyportEvents() {}

    public static InteractionResult onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
        if (!FunnyportSettings.redstoneBugs || level.isClientSide() || !(player instanceof ServerPlayer)) {
            return InteractionResult.PASS;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == Items.REDSTONE && level.getRandom().nextFloat() < 0.15f) {
            spawnRedstoneBug((ServerLevel) level, hit.getLocation());
        }
        return InteractionResult.PASS;
    }

    private static void spawnRedstoneBug(ServerLevel level, Vec3 pos) {
        // 26.1 起 EntityType 的静态常量取消，改走内置注册表
        //? if >=26.1 {
        @SuppressWarnings("unchecked")
        EntityType<Silverfish> type = (EntityType<Silverfish>)
                net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE
                        .getValue(net.minecraft.resources.Identifier.withDefaultNamespace("silverfish"));
        //?} else {
        EntityType<Silverfish> type = EntityType.SILVERFISH;
        //?}
        Silverfish bug = new Silverfish(type, level);
        bug.setCustomName(FunnyportText.literal("红石虫"));
        bug.setCustomNameVisible(true);
        bug.setPos(pos.x, pos.y, pos.z);
        bug.setPersistenceRequired();
        level.addFreshEntity(bug);
        level.playSound(null, bug.blockPosition(), SoundEvents.SILVERFISH_AMBIENT, SoundSource.HOSTILE, 0.8f, 1.0f);
    }

    public static InteractionResult onAttackBlock(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction) {
        if (!FunnyportSettings.blockGrabbing || level.isClientSide() || !(player instanceof ServerPlayer)) {
            return InteractionResult.PASS;
        }
        if (!player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty()) {
            return InteractionResult.PASS;
        }
        if (!player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        BlockState state = level.getBlockState(pos);
        // true = 拿起所有方块，包括基岩等硬度 -1 的不可破坏方块
        if (state.isAir()) {
            return InteractionResult.PASS;
        }
        ItemStack give = new ItemStack(state.getBlock().asItem());
        if (give.isEmpty()) {
            return InteractionResult.PASS;
        }
        // destroyBlock 的 4 参形态全版本通用
        level.destroyBlock(pos, false, player, 512);
        player.setItemInHand(hand, give);
        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.4f, 0.8f);
        return InteractionResult.SUCCESS;
    }
}
