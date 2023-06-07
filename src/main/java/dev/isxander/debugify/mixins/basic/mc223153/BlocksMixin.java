package dev.isxander.debugify.mixins.basic.mc223153;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@BugFix(id = "MC-223153", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Blocks.class)
public class BlocksMixin {
    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 0), slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=raw_copper_block")))
    private static BlockBehaviour.Properties addCopperSound(BlockBehaviour.Properties settings) {
        return settings.sound(SoundType.COPPER);
    }
}
