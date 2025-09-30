package dev.isxander.debugify.mixins.basic.mc210802;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkMap.class)
public interface ChunkMapAccessor {
    @Invoker("anyPlayerCloseEnoughForSpawning")
    boolean getAnyPlayerCloseEnoughForSpawning(ChunkPos chunkPos);
}
