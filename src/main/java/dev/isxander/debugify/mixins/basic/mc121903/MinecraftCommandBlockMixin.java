package dev.isxander.debugify.mixins.basic.mc121903;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-121903", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Command block minecarts do not save execution cooldown to NBT")
@Mixin(MinecartCommandBlock.class)
public class MinecraftCommandBlockMixin {
    @Shadow private int lastActivated;

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void readNbt(ValueInput nbt, CallbackInfo ci) {
        nbt.getInt("LastExecuted").ifPresent(lastExecuted -> {
            lastActivated = lastExecuted;
        });
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void writeNbt(ValueOutput nbt, CallbackInfo ci) {
        nbt.putInt("LastExecuted", lastActivated);
    }
}
