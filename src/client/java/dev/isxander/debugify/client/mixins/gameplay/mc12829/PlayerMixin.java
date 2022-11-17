package dev.isxander.debugify.client.mixins.gameplay.mc12829;

import dev.isxander.debugify.client.DebugifyClient;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@BugFix(id = "MC-12829", category = FixCategory.GAMEPLAY, env = BugFix.Env.CLIENT)
@Mixin(Player.class)
public class PlayerMixin extends LivingEntityMixin {
    @Shadow @Final private Abilities abilities;

    @Override
    protected boolean isNotClimbing(boolean isSpectator) {
        return isSpectator || (abilities.flying && DebugifyClient.isGameplayFixesEnabled());
    }
}
