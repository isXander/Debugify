package dev.isxander.debugify.mixins.basic.mc248223;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@BugFix(id = "MC-248223", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Sounds of large/medium amethyst buds are switched")
@Mixin(AmethystClusterBlock.class)
public class AmethystClusterBlockMixin extends Block {
    public AmethystClusterBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull SoundType getSoundType(BlockState blockState) {
        if (blockState.getBlock() == Blocks.LARGE_AMETHYST_BUD) return SoundType.LARGE_AMETHYST_BUD;
        else if (blockState.getBlock() == Blocks.MEDIUM_AMETHYST_BUD) return SoundType.MEDIUM_AMETHYST_BUD;
        else return super.getSoundType(blockState);
    }
}
