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
    public boolean needsSync;

    /**
     * Since the update interval is set to infinity, the position will never get updated.
     * To fix, we mark velocity as dirty via needsSync to force an update.
     *
     * We inject precisely here as the target is only called when `!firstTick && isServerLevel`,
     * since this is where the issue occurs.
     */
    @Inject(method = "setPosRaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isRemoved()Z"))
    private void forceClientUpdate(CallbackInfo ci) {
        if (this.getType().updateInterval() == Integer.MAX_VALUE) {
            this.needsSync = true;
        }
    }
}
