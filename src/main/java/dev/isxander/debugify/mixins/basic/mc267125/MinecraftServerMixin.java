package dev.isxander.debugify.mixins.basic.mc267125;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@BugFix(id = "MC-267125", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Command suggestions for reloadable content are not affected by /reload")
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow
    public abstract PlayerList getPlayerList();

    @Inject(method = "method_29440", at = @At("TAIL"))
    private void fixReloading(Collection collection, MinecraftServer.ReloadableResources reloadableResources, CallbackInfo ci) {
        this.getPlayerList().getPlayers().forEach(this.getPlayerList()::sendPlayerPermissionLevel);
    }
}
