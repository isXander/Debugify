package dev.isxander.debugify.mixins.basic.mc139041;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.FishingRodItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-139041", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "The sounds of fishing bobbers aren't controlled by the \"Players\" sound slider")
@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
    @ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), index = 5)
    private SoundSource modifyFishingBobberSounds(SoundSource soundSource) {
        return SoundSource.PLAYERS;
    }
}
