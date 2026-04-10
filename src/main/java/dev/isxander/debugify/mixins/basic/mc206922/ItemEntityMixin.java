package dev.isxander.debugify.mixins.basic.mc206922;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;

@BugFix(id = "MC-206922", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Items dropped by entities that are killed by lightning instantly disappear")
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin {

    @Override
    protected void bypassStruckByLightning(ServerLevel world, LightningBolt lightning, Operation<Void> operation) {
        if (tickCount > 8) {
            super.bypassStruckByLightning(world, lightning, operation);
        }
    }
}
