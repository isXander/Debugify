package dev.isxander.debugify.mixins.basic.mc206922;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-206922", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin {
    @Override
    protected void struckByLightningHead(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
        if (age <= 8) {
            ci.cancel();
        }
    }
}
