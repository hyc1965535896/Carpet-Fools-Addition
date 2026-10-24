package dev.funnyport;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * 土豆天罚效果表（24w14potato 移植，子集）。
 *
 * 注意：本类必须在 dev.funnyport 主包，不能放 dev.funnyport.mixin——
 * mixin 包是受保护命名空间，业务类放进去会被 Mixin 子系统拒绝加载
 * （IllegalClassLoadError，26.2 服务端实测）。0.2.5 修复。
 */
public final class FunnyportPotato {
    private FunnyportPotato() {}

    public static void trigger(Level level, ItemStack stack, LivingEntity eater) {
        // 防炸盾：任何异常只记日志，绝不带崩服务端
        try {
            doTrigger(level, stack, eater);
        } catch (Throwable t) {
            Funnyport.LOGGER.warn("[funnyport] potato doom effect failed", t);
        }
    }

    private static void doTrigger(Level level, ItemStack stack, LivingEntity eater) {
        if (!FunnyportSettings.potatoOfDoom || level.isClientSide()) return;
        if (stack.getItem() != Items.POISONOUS_POTATO) return;
        if (!(eater instanceof ServerPlayer)) return;
        ServerPlayer player = (ServerPlayer) eater;

        int roll = level.getRandom().nextInt(7);
        switch (roll) {
            case 0:
                player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 0));
                FunnyportText.sendToPlayer(player, FunnyportText.literal("§5土豆把你往上拽……"));
                break;
            case 1:
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 160, 2));
                FunnyportText.sendToPlayer(player, FunnyportText.literal("§5毒发如约而至。"));
                break;
            case 2:
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 1));
                FunnyportText.sendToPlayer(player, FunnyportText.literal("§5你比刚才更饿了。"));
                break;
            case 3:
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));
                FunnyportText.sendToPlayer(player, FunnyportText.literal("§5土豆挡住了你的眼睛。"));
                break;
            case 4:
                //? if >=1.21.5 {
                player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 300, 1));
                //?} else {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 1));
                //?}
                FunnyportText.sendToPlayer(player, FunnyportText.literal("§5土豆之怒灌注全身！"));
                break;
            case 5:
                for (int i = 0; i < 3; i++) {
                    ItemEntity drop = new ItemEntity(level, player.getX(), player.getY() + 1.0, player.getZ(),
                            new ItemStack(Items.POISONOUS_POTATO));
                    drop.setDeltaMovement((level.getRandom().nextDouble() - 0.5) * 0.5, 0.4,
                            (level.getRandom().nextDouble() - 0.5) * 0.5);
                    level.addFreshEntity(drop);
                }
                FunnyportText.sendToPlayer(player, FunnyportText.literal("§5土豆生生不息。"));
                break;
            default:
                FunnyportText.sendToPlayer(player, FunnyportText.literal("§7无事发生……这次。"));
                break;
        }
    }
}
