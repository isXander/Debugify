package dev.isxander.debugify.mixins.basic.mc177381;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.commands.LocateCommand;
import org.spongepowered.asm.mixin.Mixin;

@BugFix(id = "MC-177381", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Game does not count the distance properly if you locate a structure from more than 46340 blocks away")
@Mixin(LocateCommand.class)
public class LocateCommandMixin {
    @WrapMethod(method = "dist")
    private static float distanceHypot(int x1, int z1, int x2, int z2, Operation<Float> original) {
        return (float) Math.hypot(x2 - x1, z2 - z1);
    }
}
