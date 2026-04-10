package dev.isxander.debugify.client.mixins.basic.mc188359;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-188359", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Burp sound does not play after drinking or after eating cake")
@Mixin(Consumable.class)
public class ConsumableMixin {
    @Shadow
    @Final
    private Holder<SoundEvent> sound;

    @Inject(
            method = "onConsume",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"
            )
    )
    private void addBurpSound(Level level, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (this.sound.is(SoundEvents.GENERIC_DRINK) && user.isAlwaysTicking()) {
            level.playLocalSound(user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, Mth.randomBetween(user.getRandom(), 0.9F, 1.0F), false);
        }
    }
}
