package dev.isxander.debugify.mixins.basic.mc245394;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-245394", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "The sounds of raid horns blaring aren't controlled by the correct sound slider")
@Mixin(Raid.class)
public class RaidMixin {
    @ModifyArg(method = "playSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundSoundPacket;<init>(Lnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;DDDFFJ)V"), index = 1)
    private SoundSource modifyRaidSoundSource(SoundSource soundSource) {
        return SoundSource.HOSTILE;
    }
}
