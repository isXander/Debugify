package dev.isxander.debugify.mixins.basic.mc2025;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.debugify.helpers.mc2025.AABBSerialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@BugFix(id = "MC-2025", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract AABB getBoundingBox();

    @Shadow public abstract void setBoundingBox(AABB boundingBox);

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", ordinal = 0))
    private void writeAABB(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        AABB aabb = this.getBoundingBox();

        nbt.store("AABB", AABBSerialization.CODEC, aabb);
    }

    @Inject(
            method = "load",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"
            )
    )
    private void readAABB(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("AABB")) {
            AABB aabb = nbt.read("AABB", AABBSerialization.CODEC).orElseThrow();
            this.setBoundingBox(aabb);
        }
    }
}
