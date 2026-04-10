package dev.isxander.debugify.mixins.basic.mc224729;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@BugFix(id = "MC-224729", category = FixCategory.BASIC, env = BugFix.Env.SERVER, modConflicts = {"chunksavingfix", "moonrise"}, description = "Partially generated chunks are not saved in some situations")
@Mixin(ChunkMap.class)
public class ChunkMapMixin {
    @Definition(id = "filter", method = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;")
    @Definition(id = "wasAccessibleSinceLastSave", method = "Lnet/minecraft/server/level/ChunkHolder;wasAccessibleSinceLastSave()Z")
    @Expression("?.filter(::wasAccessibleSinceLastSave)")
    @ModifyArg(method = "saveAllChunks", at = @At("MIXINEXTRAS:EXPRESSION"))
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
