package dev.isxander.debugify.mixins.basic.mc278019;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-278019", category = FixCategory.BASIC, env = BugFix.Env.SERVER, modConflicts = "exodus1427", description = "Superflat worlds have their sea level at Y=-63")
@Mixin(FlatLevelSource.class)
public class FlatLevelSourceMixin {
    @ModifyReturnValue(method = "getSeaLevel", at = @At("RETURN"))
    private int modifySuperflatSeaLevel(int original) {
        return 63;
    }
}
