package dev.isxander.debugify.mixins.basic.client.mc228976;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-228976", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, fabricConflicts = "entitycollisionfpsfix")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickCramming", at = @At("HEAD"), cancellable = true)
    private void preventCrammingTick(CallbackInfo ci) {
        if (world.isClient) {
            ci.cancel();
        }
    }
}
