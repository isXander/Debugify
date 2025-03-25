package dev.isxander.debugify.mixins.basic.mc206922;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-206922", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Entity.class)
public class EntityMixin {
    @Shadow public int tickCount;

    @WrapMethod(method = "thunderHit")
    protected void bypassStruckByLightning(ServerLevel world, LightningBolt lightning, Operation<Void> operation) {
        operation.call(world, lightning);
    }
}
