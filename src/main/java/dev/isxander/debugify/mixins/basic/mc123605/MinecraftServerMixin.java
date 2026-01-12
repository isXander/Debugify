package dev.isxander.debugify.mixins.basic.mc123605;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-123605", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Debug world still sets clear weather time instead of deactivating gamerule doWeatherCycle")
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @WrapOperation(method = "setupDebugLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/ServerLevelData;setClearWeatherTime(I)V"))
    private void disableDebugWorldWeather(ServerLevelData instance, int i, Operation<Void> original) {
        instance.getGameRules().set(GameRules.ADVANCE_WEATHER, false, (MinecraftServer) (Object) this);
    }
}
