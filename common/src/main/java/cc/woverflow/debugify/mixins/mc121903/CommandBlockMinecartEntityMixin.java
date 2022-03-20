package cc.woverflow.debugify.mixins.mc121903;

import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandBlockMinecartEntity.class)
public class CommandBlockMinecartEntityMixin {
    @Shadow private int lastExecuted;

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readNbt(NbtCompound nbt, CallbackInfo ci) {
        lastExecuted = nbt.getInt("LastExecuted");
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("LastExecuted", lastExecuted);
    }
}
