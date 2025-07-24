package dev.isxander.debugify.client.mixins.basic.mc165305;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-165306", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Beacon beams are transparent from the inside")
@Mixin(RenderPipelines.class)
public class RenderPipelinesMixin {
    @Definition(id = "BEACON_BEAM_OPAQUE", field = "Lnet/minecraft/client/renderer/RenderPipelines;BEACON_BEAM_OPAQUE:Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    @Definition(id = "build", method = "Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;build()Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    @Definition(id = "register", method = "Lnet/minecraft/client/renderer/RenderPipelines;register(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    @Expression("BEACON_BEAM_OPAQUE = register(@(?.build()))")
    @WrapOperation(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static RenderPipeline disableCull(RenderPipeline.Builder builder, Operation<RenderPipeline> original) {
        return original.call(builder.withCull(false));
    }
}
