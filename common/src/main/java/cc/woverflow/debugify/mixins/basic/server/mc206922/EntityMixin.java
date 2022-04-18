package cc.woverflow.debugify.mixins.basic.server.mc206922;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-206922", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Entity.class)
public class EntityMixin {
    @Shadow public int age;

    @Inject(method = "onStruckByLightning", at = @At("HEAD"), cancellable = true)
    protected void struckByLightningHead(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {}
}
