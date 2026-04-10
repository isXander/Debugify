package dev.isxander.debugify.mixins.basic.mc223153;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-223153", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Block of Raw Copper uses stone sounds instead of copper sounds")
@Mixin(Blocks.class)
public class BlocksMixin {

    @Definition(id = "register", method = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;")
    @Expression("register('raw_copper_block', ?)")
    @ModifyArg(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static BlockBehaviour.Properties addCopperSound(BlockBehaviour.Properties settings) {
        return settings.sound(SoundType.COPPER);
    }
}
