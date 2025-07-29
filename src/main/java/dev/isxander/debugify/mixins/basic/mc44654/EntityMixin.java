package dev.isxander.debugify.mixins.basic.mc44654;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-44654", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Some entities don't update position to the client when teleported")
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract EntityType<?> getType();

    @Shadow
    public boolean hasImpulse;

    @Inject(method = "setPosRaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isRemoved()Z", shift = At.Shift.AFTER))
    private void fixUpdateInterval(double x, double y, double z, CallbackInfo ci) {
        if (this.getType().updateInterval() == Integer.MAX_VALUE) {
            this.hasImpulse = true;
        }
    }
}
