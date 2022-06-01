package dev.isxander.debugify.mixins.basic.client.mc148149;

import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Util.class)
public interface UtilAccessor {
    @Accessor("LOGGER")
    static Logger getLogger() {
        throw new AssertionError();
    }
}
