package dev.isxander.debugify.mixins.basic.mc158900;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.UserBanList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-158900", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "\"bad packet id 26\" upon connecting after tempban expire")
@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Shadow
    @Final
    private UserBanList bans;

    @ModifyExpressionValue(method = "canPlayerLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/UserBanList;isBanned(Lcom/mojang/authlib/GameProfile;)Z"))
    private boolean npeCheck(boolean original, @Local(argsOnly = true) GameProfile gameProfile) {
        return this.bans.get(gameProfile) != null && original;
    }
}
