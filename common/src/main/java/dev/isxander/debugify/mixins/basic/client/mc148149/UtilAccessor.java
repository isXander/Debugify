package dev.isxander.debugify.mixins.basic.client.mc148149;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@BugFix(id = "MC-148149", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, fabricConflicts = "fastopenlinksandfolders")
@Mixin(Util.class)
public interface UtilAccessor {
    @Accessor("LOGGER")
    static Logger getLogger() {
        throw new AssertionError();
    }
}
