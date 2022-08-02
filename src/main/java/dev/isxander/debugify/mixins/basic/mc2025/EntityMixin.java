package dev.isxander.debugify.mixins.basic.mc2025;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-2025", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract Box getBoundingBox();

    @Shadow protected abstract NbtList toNbtList(double... values);

    @Shadow public abstract void setBoundingBox(Box boundingBox);

    @Inject(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putFloat(Ljava/lang/String;F)V", ordinal = 0))
    private void writeAABB(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        Box aabb = this.getBoundingBox();
        nbt.put("AABB", toNbtList(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ));
    }

    @Inject(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;refreshPosition()V", shift = At.Shift.BY, by = 2))
    private void readAABB(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("AABB")) {
            NbtList aabbNbt = nbt.getList("AABB", NbtElement.DOUBLE_TYPE);
            this.setBoundingBox(new Box(
                    aabbNbt.getDouble(0),
                    aabbNbt.getDouble(1),
                    aabbNbt.getDouble(2),
                    aabbNbt.getDouble(3),
                    aabbNbt.getDouble(4),
                    aabbNbt.getDouble(5)
            ));
        }
    }
}
