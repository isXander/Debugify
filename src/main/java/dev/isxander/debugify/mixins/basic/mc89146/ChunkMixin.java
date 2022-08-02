package dev.isxander.debugify.mixins.basic.mc89146;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.LinkedHashMap;

@BugFix(id = "MC-89146", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Chunk.class)
public class ChunkMixin {
    @ModifyExpressionValue(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;", ordinal = 3))
    private HashMap<BlockPos, BlockEntity> wrapLinkedHashMap(HashMap<BlockPos, BlockEntity> hashMap) {
        return new LinkedHashMap<>(hashMap);
    }
}
