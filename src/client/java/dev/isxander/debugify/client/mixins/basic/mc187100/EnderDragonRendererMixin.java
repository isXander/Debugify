package dev.isxander.debugify.client.mixins.basic.mc187100;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-187100", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "End crystals try to heal dying Ender dragons")
@Mixin(EnderDragonRenderer.class)
public class EnderDragonRendererMixin {
    @ModifyExpressionValue(method = "extractRenderState(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;F)V", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;nearestCrystal:Lnet/minecraft/world/entity/boss/enderdragon/EndCrystal;"))
    private EndCrystal checkDragonDeath(EndCrystal original, EnderDragon enderDragon, EnderDragonRenderState enderDragonRenderState) {
        return enderDragonRenderState.deathTime > 0 ? null : original;
    }
}
