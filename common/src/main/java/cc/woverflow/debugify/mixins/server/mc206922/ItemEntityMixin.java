package cc.woverflow.debugify.mixins.server.mc206922;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin {
    @Override
    protected void struckByLightningHead(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
        if (age <= 8) {
            ci.cancel();
        }
    }
}
