package dev.isxander.debugify.client.mixins.basic.mc90683;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-90683", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @WrapWithCondition(method = "onEntityPassengersSet", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;)V"))
    private boolean shouldWarn(Logger logger, String warning) {
        return false;
    }
}
