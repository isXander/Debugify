package dev.isxander.debugify.mixins.basic.mc153010;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-153010", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "doMobLoot gamerule doesn't prevent foxes from dropping their items")
@Mixin(Fox.class)
public class FoxMixin {
    // gate dropping death loot with gamerule check
    @ModifyExpressionValue(method = "dropAllDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private boolean preventLootDropIfGameruleIsFalse(boolean isEmpty, ServerLevel level) {
        return isEmpty || !level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
    }
}
