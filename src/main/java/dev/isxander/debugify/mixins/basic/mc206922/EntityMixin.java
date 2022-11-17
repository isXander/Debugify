package dev.isxander.debugify.mixins.basic.mc206922;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-206922", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Entity.class)
public class EntityMixin {
    @Shadow public int tickCount;

    @Inject(method = "thunderHit", at = @At("HEAD"), cancellable = true)
    protected void struckByLightningHead(ServerLevel world, LightningBolt lightning, CallbackInfo ci) {}
}
