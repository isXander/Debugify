package dev.isxander.debugify.client.mixins.basic.mc231097;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-231097", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Shadow @Final protected Minecraft minecraft;

    @Shadow public abstract boolean isUsingItem();

    @Inject(method = "drop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;removeFromSelected(Z)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
    private void onDropItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        if (isUsingItem()) {
            minecraft.gameMode.releaseUsingItem((LocalPlayer) (Object) this);
        }
    }
}
