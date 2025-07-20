package dev.isxander.debugify.client.mixins.basic.mc57057;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.GuardianAttackSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Guardian;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-57057", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Guardian laser attack sound ignores distance")
@Mixin(GuardianAttackSoundInstance.class)
public abstract class GuardianAttackSoundInstanceMixin extends AbstractTickableSoundInstance {
    protected GuardianAttackSoundInstanceMixin(SoundEvent soundEvent, SoundSource soundSource, RandomSource randomSource) {
        super(soundEvent, soundSource, randomSource);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void fixAttenuation(Guardian guardian, CallbackInfo ci) {
        this.attenuation = SoundInstance.Attenuation.LINEAR;
    }
}
