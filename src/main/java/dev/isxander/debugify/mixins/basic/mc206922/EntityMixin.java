package dev.isxander.debugify.mixins.basic.mc206922;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@BugFix(id = "MC-206922", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Items dropped by entities that are killed by lightning instantly disappear")
@Mixin(Entity.class)
public class EntityMixin {
    @Shadow public int tickCount;

    @WrapMethod(method = "thunderHit")
    protected void bypassStruckByLightning(ServerLevel level, LightningBolt lightningBolt, Operation<Void> operation) {
        operation.call(level, lightningBolt);
    }
}
