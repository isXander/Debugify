package dev.isxander.debugify.mixins.basic.mc215530;

import com.mojang.authlib.GameProfile;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-215530", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "The freezing effect isn't immediately removed upon switching into spectator mode")
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    @Inject(method = "setGameMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;stopRiding()V"))
    private void onChangeToSpectator(GameType gameMode, CallbackInfoReturnable<Boolean> cir) {
        setTicksFrozen(0);
    }
}
