package cc.woverflow.debugify.mixins.gameplay.client.mc12829;

import cc.woverflow.debugify.Debugify;
import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
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
        return isSpectator || (abilities.flying && Debugify.isGameplayFixesEnabled());
    }
}
