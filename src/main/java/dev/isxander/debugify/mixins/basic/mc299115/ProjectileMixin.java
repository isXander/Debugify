package dev.isxander.debugify.mixins.basic.mc299115;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-299115", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Arrow loses owner tag when deflected whilst owner is offline.")
@Mixin(Projectile.class)
public class ProjectileMixin {
    @Shadow
    @Nullable
    protected EntityReference<Entity> owner;

    /**
     * This deflect method takes the `Entity` owner, not an `EntityReference<Entity>`.
     * This means during deflection, the owner is resolved from its reference.
     * Because the owner is no longer online, it is nullified.
     */
    @WrapWithCondition(method = "deflect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;setOwner(Lnet/minecraft/world/entity/Entity;)V"))
    private boolean preventNullifyingOwner(Projectile instance, Entity owner) {
        // only allow setting the owner if:
        // 1. the new owner is not null
        // 2. the current owner reference is null (which means it never had a reference to any owner)
        return this.owner == null || owner != null;
    }
}
