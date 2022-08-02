package dev.isxander.debugify.client.mixins.basic.mc4490;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@BugFix(id = "MC-4490", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(FishingBobberEntityRenderer.class)
public class FishingBobberEntityRendererMixin {
    @ModifyConstant(method = "render", constant = @Constant(floatValue = -0.1875F))
    private float renderSneakOffset(float constant) {
        return -0.2875F;
    }
}
