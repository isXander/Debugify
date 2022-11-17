package dev.isxander.debugify.mixins.basic.mc93018;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-93018", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Animal.class)
public class AnimalMixin {
    /**
     * Only make wolves love player if they are tamed when fed
     */
    @WrapWithCondition(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;setInLove(Lnet/minecraft/world/entity/player/Player;)V"))
    private boolean loveCondition(Animal animal, Player player) {
        if (animal instanceof Wolf wolf) {
            return wolf.isTame();
        }

        return true;
    }
}
