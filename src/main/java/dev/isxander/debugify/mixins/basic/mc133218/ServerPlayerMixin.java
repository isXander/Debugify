package dev.isxander.debugify.mixins.basic.mc133218;

import com.mojang.authlib.GameProfile;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-133218", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Usable items continue to be used on the death screen after dying when the \"keepInventory\" gamerule is set to \"true\"")
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/scores/Scoreboard;forAllObjectives(Lnet/minecraft/world/scores/criteria/ObjectiveCriteria;Lnet/minecraft/world/scores/ScoreHolder;Ljava/util/function/Consumer;)V"))
    private void stopUsingItemOnDeath(DamageSource damageSource, CallbackInfo ci) {
        this.stopUsingItem();
    }
}
