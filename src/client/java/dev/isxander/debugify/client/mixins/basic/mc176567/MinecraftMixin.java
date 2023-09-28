package dev.isxander.debugify.client.mixins.basic.mc176567;

import com.mojang.blaze3d.platform.Window;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@BugFix(id = "MC-176567", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "smoothmenu")
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    @Final
    private Window window;

    /**
     * @author
     * @reason
     */
    @Overwrite
    private int getFramerateLimit() {
        return window.getFramerateLimit();
    }
}
