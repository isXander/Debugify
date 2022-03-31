package cc.woverflow.debugify.mixins.server.mc200418;

import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ZombieVillagerEntity.class)
public class ZombieVillagerEntityMixin {
    @Inject(method = "finishConversion", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void dismountIfJockey(ServerWorld world, CallbackInfo ci, VillagerEntity villager) {
        if (villager.hasVehicle()) {
            villager.dismountVehicle();
        }
    }
}
