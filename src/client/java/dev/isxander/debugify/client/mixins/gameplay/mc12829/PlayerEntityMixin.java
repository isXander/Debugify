package dev.isxander.debugify.client.mixins.gameplay.mc12829;

import dev.isxander.debugify.client.DebugifyClient;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@BugFix(id = "MC-12829", category = FixCategory.GAMEPLAY, env = BugFix.Env.CLIENT)
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntityMixin {
    @Shadow @Final private PlayerAbilities abilities;

    @Override
    protected boolean isNotClimbing(boolean isSpectator) {
        return isSpectator || (abilities.flying && DebugifyClient.isGameplayFixesEnabled());
    }
}
