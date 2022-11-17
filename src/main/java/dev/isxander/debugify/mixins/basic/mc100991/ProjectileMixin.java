package dev.isxander.debugify.mixins.basic.mc100991;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@BugFix(id = "MC-100991", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Projectile.class)
public abstract class ProjectileMixin {
    @Shadow @Nullable
    public abstract Entity getOwner();
}
