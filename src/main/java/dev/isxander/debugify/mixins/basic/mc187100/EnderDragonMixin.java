package dev.isxander.debugify.mixins.basic.mc187100;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-187100", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "End crystals try to heal dying Ender dragons")
@Mixin(EnderDragon.class)
public class EnderDragonMixin {
    @Shadow
    public int dragonDeathTime;

    @Shadow
    @Nullable
    public EndCrystal nearestCrystal;

    @WrapMethod(method = "checkCrystals")
    private void dontFindNearestCrystalIfDying(Operation<Void> original) {
        if (this.dragonDeathTime > 0) {
            this.nearestCrystal = null;
        } else {
            original.call();
        }
    }

    @Inject(method = "tickDeath", at = @At("HEAD"))
    private void clearNearestCrystalOnDeath(CallbackInfo ci) {
        this.nearestCrystal = null;
    }
}
