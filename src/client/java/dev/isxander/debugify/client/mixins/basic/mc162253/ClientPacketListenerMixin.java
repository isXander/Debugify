package dev.isxander.debugify.client.mixins.basic.mc162253;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.lighting.LevelLightEngine;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-162253", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = {"forgetmechunk", "phosphor", "starlight"})
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    /**
     * Mojank decided to do some random light update for no reason
     * tell them no.
     */
    @WrapWithCondition(method = "m_ozctwjwc", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/lighting/LevelLightEngine;updateSectionStatus(Lnet/minecraft/core/SectionPos;Z)V"))
    private boolean shouldDoUselessLightUpdate(LevelLightEngine instance, SectionPos pos, boolean notReady) {
        return false;
    }
}
