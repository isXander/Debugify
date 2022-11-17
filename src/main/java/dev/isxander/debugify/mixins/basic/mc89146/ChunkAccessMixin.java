package dev.isxander.debugify.mixins.basic.mc89146;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.LinkedHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;

@BugFix(id = "MC-89146", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(ChunkAccess.class)
public class ChunkAccessMixin {
    @ModifyExpressionValue(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;", ordinal = 3))
    private HashMap<BlockPos, BlockEntity> wrapLinkedHashMap(HashMap<BlockPos, BlockEntity> hashMap) {
        return new LinkedHashMap<>(hashMap);
    }
}
