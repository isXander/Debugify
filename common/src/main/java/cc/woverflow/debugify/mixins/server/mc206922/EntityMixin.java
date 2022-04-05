package cc.woverflow.debugify.mixins.server.mc206922;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow public int age;

    @Inject(method = "onStruckByLightning", at = @At("HEAD"), cancellable = true)
    protected void struckByLightningHead(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {}
}
