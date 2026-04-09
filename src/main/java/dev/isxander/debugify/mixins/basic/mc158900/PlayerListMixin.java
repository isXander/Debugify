package dev.isxander.debugify.mixins.basic.mc158900;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.UserBanList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-158900", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "\"bad packet id 26\" disconnect after first-login after a temporary ban expires")
@Mixin(PlayerList.class)
public class PlayerListMixin {
    @WrapOperation(
            method = "canPlayerLogin",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/UserBanList;isBanned(Lnet/minecraft/server/players/NameAndId;)Z"
            )
    )
    private boolean removeExpiredBeforeCheck(UserBanList instance, NameAndId user, Operation<Boolean> original) {
        instance.get(user); // get will remove expired bans
        return original.call(instance, user);
    }

}
