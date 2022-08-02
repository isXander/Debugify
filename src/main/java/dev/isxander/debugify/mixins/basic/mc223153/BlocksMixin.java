package dev.isxander.debugify.mixins.basic.mc223153;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.sound.BlockSoundGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@BugFix(id = "MC-223153", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Blocks.class)
public class BlocksMixin {
    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;of(Lnet/minecraft/block/Material;Lnet/minecraft/block/MapColor;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 0), slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=raw_copper_block")))
    private static AbstractBlock.Settings addCopperSound(AbstractBlock.Settings settings) {
        return settings.sounds(BlockSoundGroup.COPPER);
    }
}
