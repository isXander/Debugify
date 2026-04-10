package dev.isxander.debugify.mixins.basic.mc267125;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@BugFix(id = "MC-267125", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Command suggestions for reloadable content are not affected by /reload")
@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow
    @Final
    private List<ServerPlayer> players;

    @Shadow
    public abstract void sendPlayerPermissionLevel(ServerPlayer player);

    @Inject(method = "reloadResources", at = @At("RETURN"))
    private void sendCommandSuggestions(CallbackInfo ci) {
        for (ServerPlayer player : this.players) {
            this.sendPlayerPermissionLevel(player);
        }
    }
}
