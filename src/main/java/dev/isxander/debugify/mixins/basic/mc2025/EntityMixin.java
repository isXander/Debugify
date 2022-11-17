package dev.isxander.debugify.mixins.basic.mc2025;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-2025", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract AABB getBoundingBox();

    @Shadow protected abstract ListTag newDoubleList(double... values);

    @Shadow public abstract void setBoundingBox(AABB boundingBox);

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putFloat(Ljava/lang/String;F)V", ordinal = 0))
    private void writeAABB(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        AABB aabb = this.getBoundingBox();
        nbt.put("AABB", newDoubleList(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ));
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;reapplyPosition()V", shift = At.Shift.BY, by = 2))
    private void readAABB(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("AABB")) {
            ListTag aabbNbt = nbt.getList("AABB", Tag.TAG_DOUBLE);
            this.setBoundingBox(new AABB(
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
