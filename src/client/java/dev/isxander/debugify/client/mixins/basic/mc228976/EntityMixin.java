package dev.isxander.debugify.client.mixins.basic.mc228976;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-228976", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = {"entitycollisionfpsfix", "lithium"})
@Mixin(Entity.class)
public class EntityMixin {
    @Shadow public Level level;

    @Inject(method = "isInWall", at = @At("HEAD"), cancellable = true)
    private void preventWallCheck(CallbackInfoReturnable<Boolean> cir) {
        if (level.isClientSide)
            cir.setReturnValue(false);
    }
}
