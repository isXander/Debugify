package dev.isxander.debugify.error;

import dev.isxander.debugify.fixes.BugFixData;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public record MixinErrorEntry(BugFixData fix, IMixinInfo mixinInfo, Throwable throwable, ErrorStage errorStage) {
}
