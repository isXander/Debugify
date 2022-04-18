package cc.woverflow.debugify.mixins.basic.server.mc100991;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@BugFix(id = "MC-100991", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin {
    @Shadow @Nullable public abstract Entity getOwner();
}
