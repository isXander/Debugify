package dev.isxander.debugify.mixins.basic.mc224729;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@BugFix(id = "MC-224729", category = FixCategory.BASIC, env = BugFix.Env.SERVER, modConflicts = "chunksavingfix")
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

    @ModifyExpressionValue(method = "save(Lnet/minecraft/server/world/ChunkHolder;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ChunkHolder;isAccessible()Z"))
    private boolean alwaysAccessibleChunkSave(boolean accessible) {
        return true;
    }
}
