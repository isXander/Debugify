package dev.isxander.debugify.client.mixins.basic.mc46766;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-46766", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Nullable
    public MultiPlayerGameMode gameMode;

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void onHandleBlockBreaking(boolean bl, CallbackInfo ci) {
        if (gameMode.getPlayerMode() == GameType.SPECTATOR)
            ci.cancel();
    }
}
