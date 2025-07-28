package dev.isxander.debugify.client.mixins.basic.mc129556;

import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-129556", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "GUI logic is included in \"root.tick.textures\"")
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;", ordinal = 0))
    private void switchProfilerToGui(CallbackInfo ci, @Local ProfilerFiller profilerFiller) {
        profilerFiller.popPush("gui");
    }
}
