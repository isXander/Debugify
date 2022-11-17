package dev.isxander.debugify.mixins.basic.mc224729;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;

@BugFix(id = "MC-224729", category = FixCategory.BASIC, env = BugFix.Env.SERVER, modConflicts = "chunksavingfix")
@Mixin(ChunkMap.class)
public class ChunkMapMixin {
    @ModifyArg(method = "saveAllChunks", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;", ordinal = 0))
    private Predicate<ChunkHolder> alwaysAccessibleFlushSave(Predicate<ChunkHolder> predicate) {
        return (chunkHolder) -> true;
    }

    @ModifyArg(method = "saveAllChunks", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;", ordinal = 1))
    private Predicate<ChunkAccess> saveProtoChunks(Predicate<ChunkAccess> predicate) {
        return (c) -> predicate.test(c) || c instanceof ProtoChunk;
    }

    @ModifyExpressionValue(method = "saveChunkIfNeeded", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkHolder;wasAccessibleSinceLastSave()Z"))
    private boolean alwaysAccessibleChunkSave(boolean accessible) {
        return true;
    }
}
