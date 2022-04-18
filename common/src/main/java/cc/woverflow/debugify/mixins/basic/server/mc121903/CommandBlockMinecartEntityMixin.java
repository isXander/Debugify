package cc.woverflow.debugify.mixins.basic.server.mc121903;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-121903", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(CommandBlockMinecartEntity.class)
public class CommandBlockMinecartEntityMixin {
    @Shadow private int lastExecuted;

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("LastExecuted"))
            lastExecuted = nbt.getInt("LastExecuted");
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("LastExecuted", lastExecuted);
    }
}
