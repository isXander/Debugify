package dev.isxander.debugify.mixins.basic.mc219981;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-219981", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "If zombies spawned with max health over 20 (leader zombie bonus), they will have 20 health instead of their max health")
@Mixin(Zombie.class)
public class ZombieMixin extends Monster {
    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "handleAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Zombie;getAttribute(Lnet/minecraft/core/Holder;)Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;", ordinal = 3))
    private void getZombieDamage(float f, CallbackInfo ci, @Share("zombieDamage") LocalFloatRef zombieDamageRef) {
        zombieDamageRef.set(this.getMaxHealth() - this.getHealth());
    }

    @Inject(method = "handleAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Zombie;setCanBreakDoors(Z)V"))
    private void setZombieHealth(float f, CallbackInfo ci, @Share("zombieDamage") LocalFloatRef zombieDamageRef) {
        this.setHealth(this.getMaxHealth() - zombieDamageRef.get());
    }
}
