package cc.woverflow.debugify.mixins.basic.client.mc4490;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
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
