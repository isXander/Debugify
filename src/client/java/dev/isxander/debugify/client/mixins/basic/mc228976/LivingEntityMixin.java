package dev.isxander.debugify.client.mixins.basic.mc228976;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

@BugFix(id = "MC-228976", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = {"entitycollisionfpsfix", "lithium"})
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Redirect(method = "pushEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private List<Entity> searchEntities(Level instance, @Nullable Entity except, AABB box, Predicate<? super Entity> predicate) {
        if (!level.isClientSide || (((Entity) (Object) this) instanceof Player player && player.isLocalPlayer())) {
            return instance.getEntities(except, box, predicate);
        } else {
            return (List<Entity>) (Object) instance.getEntitiesOfClass(Player.class, box, predicate);
        }
    }
}
