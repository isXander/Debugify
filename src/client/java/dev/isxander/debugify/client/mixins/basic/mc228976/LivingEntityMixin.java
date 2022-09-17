package dev.isxander.debugify.client.mixins.basic.mc228976;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

@BugFix(id = "MC-228976", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = {"entitycollisionfpsfix", "lithium"})
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(method = "tickCramming", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private List<Entity> searchEntities(World instance, @Nullable Entity except, Box box, Predicate<? super Entity> predicate) {
        if (!world.isClient || (((Entity) (Object) this) instanceof PlayerEntity player && player.isMainPlayer())) {
            return instance.getOtherEntities(except, box, predicate);
        } else {
            return (List<Entity>) (Object) instance.getEntitiesByClass(PlayerEntity.class, box, predicate);
        }
    }
}
