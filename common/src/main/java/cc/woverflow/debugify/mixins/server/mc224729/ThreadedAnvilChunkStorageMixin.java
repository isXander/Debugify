package cc.woverflow.debugify.mixins.server.mc224729;

import cc.woverflow.debugify.fixes.BugFix;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@BugFix(id = "MC-224729", env = BugFix.Env.SERVER, fabricConflicts = "chunksavingfix")
@Mixin(ThreadedAnvilChunkStorage.class)
public class ThreadedAnvilChunkStorageMixin {
    @ModifyArg(method = "save(Z)V", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;", ordinal = 0))
    private Predicate<ChunkHolder> alwaysAccessibleFlushSave(Predicate<ChunkHolder> predicate) {
        return (chunkHolder) -> true;
    }

    @ModifyArg(method = "save(Z)V", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;", ordinal = 1))
    private Predicate<Chunk> saveProtoChunks(Predicate<Chunk> predicate) {
        return (c) -> predicate.test(c) || c instanceof ProtoChunk;
    }

    @ModifyExpressionValue(method = "saveChunkIfNeeded", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ChunkHolder;isAccessible()Z"))
    private boolean alwaysAccessibleChunkSave(boolean accessible) {
        return true;
    }
}
