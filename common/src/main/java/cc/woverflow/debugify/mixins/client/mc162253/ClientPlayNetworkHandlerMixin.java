package cc.woverflow.debugify.mixins.client.mc162253;

import cc.woverflow.debugify.fixes.BugFix;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(conflict = @Condition("forgetmechunk"))
@BugFix(id = "MC-162253", env = BugFix.Env.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    /**
     * Mojank decided to do some random light update for no reason
     * tell them no.
     */
    @WrapWithCondition(method = "method_38546", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/light/LightingProvider;setSectionStatus(Lnet/minecraft/util/math/ChunkSectionPos;Z)V"))
    private boolean shouldDoUselessLightUpdate(LightingProvider instance, ChunkSectionPos pos, boolean notReady) {
        return false;
    }
}
