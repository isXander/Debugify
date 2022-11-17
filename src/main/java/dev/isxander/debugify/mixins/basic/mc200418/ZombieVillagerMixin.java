package dev.isxander.debugify.mixins.basic.mc200418;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@BugFix(id = "MC-200418", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(ZombieVillager.class)
public class ZombieVillagerMixin {
    @Inject(method = "finishConversion", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    @SuppressWarnings("InvalidInjectorMethodSignature")
    private void dismountIfJockey(ServerLevel world, CallbackInfo ci, Villager villager) {
        if (villager.isPassenger() && villager.getVehicle() instanceof Chicken && villager.isBaby()) {
            villager.removeVehicle();
        }
    }
}
