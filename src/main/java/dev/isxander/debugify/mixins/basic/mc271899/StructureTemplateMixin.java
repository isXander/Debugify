package dev.isxander.debugify.mixins.basic.mc271899;

import com.google.common.collect.Maps;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@BugFix(id = "MC-271899", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "StructureTemplate Palette's caches are not thread safe")
@Mixin(StructureTemplate.Palette.class)
public class StructureTemplateMixin {
    @Mutable
    @Shadow
    @Final
    private Map<Block, List<StructureTemplate.StructureBlockInfo>> cache;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void newHashMap(List list, CallbackInfo ci) {
        this.cache = Maps.newConcurrentMap();
    }
}
