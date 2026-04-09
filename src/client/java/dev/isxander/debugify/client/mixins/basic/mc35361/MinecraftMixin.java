package dev.isxander.debugify.client.mixins.basic.mc35361;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.tutorial.Tutorial;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-35361", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Inventory opening is detected while in Nether Portal")
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Nullable
    public LocalPlayer player;

    @WrapWithCondition(
            method = "handleKeybinds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/tutorial/Tutorial;onOpenInventory()V"
            )
    )
    private boolean addNetherPortalCheck(Tutorial instance) {
        return this.player.portalProcess == null || !this.player.portalProcess.isInsidePortalThisTick();
    }
}
