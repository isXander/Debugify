package dev.isxander.debugify.client.mixins.basic.mc165305;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.client.renderer.RenderPipelines.BEACON_BEAM_SNIPPET;

@BugFix(id = "MC-165306", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Beacon beams are transparent from the inside")
@Mixin(RenderPipelines.class)
public abstract class RenderPipelinesMixin {
    @Shadow
    public static RenderPipeline register(RenderPipeline pipeline) {
        return null;
    }

    @Mutable
    @Shadow
    @Final
    public static final RenderPipeline BEACON_BEAM_OPAQUE = register(
            RenderPipeline.builder(BEACON_BEAM_SNIPPET).withLocation("pipeline/beacon_beam_opaque").withCull(false).build()
    );
}
