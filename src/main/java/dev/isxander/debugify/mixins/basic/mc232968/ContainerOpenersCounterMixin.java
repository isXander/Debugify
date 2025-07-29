package dev.isxander.debugify.mixins.basic.mc232968;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@BugFix(id = "MC-232968", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Spectators can prevent the closing animation of a chest/barrel when viewing it at the same time as a non-spectator")
@Mixin(ContainerOpenersCounter.class)
public abstract class ContainerOpenersCounterMixin {
    @Shadow
    protected abstract boolean isOwnContainer(Player player);

    @Definition(id = "isOwnContainer", method = "Lnet/minecraft/world/level/block/entity/ContainerOpenersCounter;isOwnContainer(Lnet/minecraft/world/entity/player/Player;)Z")
    @Expression("?::isOwnContainer")
    @ModifyExpressionValue(method = "getPlayersWithContainerOpen", at = @At("MIXINEXTRAS:EXPRESSION"))
    private Predicate<Player> fixSpectatorContainerOpen(Predicate<Player> original) {
        return player -> isOwnContainer(player) && !player.isSpectator();
    }
}
